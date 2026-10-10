package in.agricred.assessment;

import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analyze")
public class AssessmentController {
  @PostMapping("/financial")
  public Map<String,Object> financial(@RequestBody RiskRequest r) {
    double monthlyIncome = r.annualIncome() / 12.0;
    double monthlyExpenses = r.annualExpenses() / 12.0;
    double surplus = monthlyIncome - monthlyExpenses;
    double monthlyDebt = r.existingDebt() / 24.0;
    double emi = r.tenureMonths() == null || r.tenureMonths() <= 0 ? 0 : r.requestedLoan() / r.tenureMonths();
    return Map.of("annualNetIncome", r.annualIncome()-r.annualExpenses(),
      "monthlySurplus", surplus, "monthlyExistingDebtEstimate", monthlyDebt,
      "requestedMonthlyPaymentEstimate", emi,
      "foirPercent", percent((monthlyDebt+emi), monthlyIncome),
      "dtiPercent", percent(r.existingDebt(), r.annualIncome()));
  }

  @PostMapping("/cashflow")
  public Map<String,Object> cashflow(@RequestBody RiskRequest r) {
    double monthlySurplus = (r.annualIncome()-r.annualExpenses())/12.0;
    double proposedEmi = r.tenureMonths() == null || r.tenureMonths() <= 0 ? 0 : r.requestedLoan()/r.tenureMonths();
    return Map.of("monthlySurplus", monthlySurplus, "proposedEmi", proposedEmi,
      "surplusAfterEmi", monthlySurplus-proposedEmi, "affordableBySimpleRule", proposedEmi <= monthlySurplus*0.4);
  }

  @PostMapping("/agri-risk")
  public Map<String,Object> agriRisk(@RequestBody RiskRequest r) {
    double drought = clamp(r.droughtRisk() == null ? 25 : r.droughtRisk(), 0, 100);
    double market = Boolean.TRUE.equals(r.priceCrashRisk()) ? 75 : 25;
    return Map.of("droughtRiskPercent", drought, "marketPriceRiskPercent", market,
      "combinedRiskBand", (drought + market)/2 >= 60 ? "HIGH" : (drought+market)/2 >= 35 ? "MODERATE" : "LOW",
      "note", "Demo estimates only; connect verified weather and mandi data before operational use.");
  }

  @PostMapping("/credit-risk")
  public Map<String,Object> creditRisk(@RequestBody RiskRequest r) {
    double monthlyIncome = r.annualIncome()/12.0;
    double monthlyExpenses = r.annualExpenses()/12.0;
    double proposedEmi = r.tenureMonths() == null || r.tenureMonths() <= 0 ? 0 : r.requestedLoan()/r.tenureMonths();
    double foir = percent(proposedEmi + r.existingDebt()/24.0, monthlyIncome);
    double dti = percent(r.existingDebt(), r.annualIncome());
    double drought = r.droughtRisk() == null ? 25 : r.droughtRisk();
    int score = 500;
    Map<String,Double> factors = new LinkedHashMap<>();
    double repayment = Math.min(Math.max(r.onTimeLoans(),0)*30,90);
    factors.put("repaymentHistory", repayment); score += repayment;
    double stability = r.incomeVariancePct() != null && r.incomeVariancePct() < 15 ? 45 : 0;
    factors.put("incomeStability", stability); score += stability;
    double land = Boolean.TRUE.equals(r.landOwned()) ? 40 : 0;
    factors.put("landOwnership", land); score += land;
    double irrigation = Boolean.TRUE.equals(r.irrigationAvailable()) ? 30 : 0;
    factors.put("irrigation", irrigation); score += irrigation;
    double diversity = Boolean.TRUE.equals(r.cropDiversification()) ? 25 : 0;
    factors.put("cropDiversification", diversity); score += diversity;
    double insurance = Boolean.TRUE.equals(r.insuranceActive()) ? 20 : 0;
    factors.put("insuranceActive", insurance); score += insurance;
    double foirPenalty = foir > 40 ? -(foir-40)*3 : 0;
    factors.put("foirPenalty", foirPenalty); score += foirPenalty;
    double dtiPenalty = dti > 30 ? -(dti-30)*2.5 : 0;
    factors.put("dtiPenalty", dtiPenalty); score += dtiPenalty;
    double droughtPenalty = drought > 30 ? -(drought-30)*2 : 0;
    factors.put("droughtPenalty", droughtPenalty); score += droughtPenalty;
    double uninsuredPenalty = Boolean.TRUE.equals(r.insuranceActive()) ? 0 : -40;
    factors.put("noInsurancePenalty", uninsuredPenalty); score += uninsuredPenalty;
    double pricePenalty = Boolean.TRUE.equals(r.priceCrashRisk()) ? -25 : 0;
    factors.put("priceCrashPenalty", pricePenalty); score += pricePenalty;
    double defaultPenalty = Boolean.TRUE.equals(r.existingDefault()) ? -100 : 0;
    factors.put("existingDefaultPenalty", defaultPenalty); score += defaultPenalty;
    score = (int)Math.round(clamp(score,0,1000));
    String category = score >= 750 ? "A" : score >= 650 ? "B" : score >= 550 ? "C" : "D";
    String risk = category.equals("A") ? "LOW" : category.equals("B") ? "MEDIUM_LOW" : category.equals("C") ? "MEDIUM_HIGH" : "HIGH";
    double defaultProbability = clamp((1000-score)/10.0, 0, 100);
    return Map.of("creditScore",score,"category",category,"riskBand",risk,
      "defaultProbabilityEstimatePercent",defaultProbability,"foirPercent",foir,"dtiPercent",dti,
      "monthlySurplus",monthlyIncome-monthlyExpenses,"recommendedMaxEmi",Math.max(0,(monthlyIncome-monthlyExpenses)*0.4),
      "factorContributions",factors,
      "disclaimer","Deterministic demonstration heuristic, not a validated lending or ML model.");
  }

  private static double percent(double numerator, double denominator) { return denominator <= 0 ? 100 : numerator/denominator*100.0; }
  private static double clamp(double v,double min,double max){return Math.max(min,Math.min(max,v));}

  public record RiskRequest(double annualIncome, double annualExpenses, double existingDebt, double requestedLoan,
    Integer tenureMonths, Integer onTimeLoans, Double incomeVariancePct, Boolean landOwned,
    Boolean irrigationAvailable, Boolean cropDiversification, Boolean insuranceActive,
    Double droughtRisk, Boolean priceCrashRisk, Boolean existingDefault) {}
}
