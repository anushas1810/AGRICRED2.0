package in.agricred.solution;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solutions")
public class SolutionController {
  private final Map<String,List<SolutionAction>> records = new ConcurrentHashMap<>();

  @PostMapping("/{loanId}/execute")
  public Map<String,Object> execute(@PathVariable String loanId, @RequestBody(required=false) ExecuteRequest request) {
    List<SolutionAction> actions = List.of(
      action("SUBSIDY_MATCH","Government scheme matching","Review scheme eligibility and provide application links."),
      action("INSURANCE_GUIDANCE","Crop insurance guidance","Show enrollment window, documents and official portal guidance."),
      action("CROP_PLAN","Crop-plan diversification","Offer a locally reviewed diversification plan."),
      action("LOAN_RESTRUCTURE","Loan restructuring review","Refer the case to an authorized lending officer for affordability review."),
      action("MARKET_LINKAGE","Market linkage","Compare nearby regulated mandi prices and transport considerations."),
      action("EARLY_WARNING","Early-warning protection","Enable weather and repayment reminders after the farmer opts in.")
    );
    records.put(loanId, actions);
    return Map.of("loanId",loanId,"farmerId",request == null ? "" : Objects.toString(request.farmerId(), ""),
      "status","RECOMMENDATIONS_CREATED","actions",actions,
      "note","No government enrollment, insurance purchase, or loan change is performed automatically.");
  }

  @GetMapping("/{loanId}")
  public Map<String,Object> get(@PathVariable String loanId) {
    return Map.of("loanId",loanId,"actions",records.getOrDefault(loanId,List.of()));
  }
  private SolutionAction action(String type,String title,String description) {
    return new SolutionAction(UUID.randomUUID().toString(),type,title,description,"RECOMMENDED",Instant.now().toString());
  }
  public record ExecuteRequest(String farmerId) {}
  public record SolutionAction(String id,String type,String title,String description,String status,String createdAt) {}
}
