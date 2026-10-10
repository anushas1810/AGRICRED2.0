package in.agricred.loan;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="loan_applications")
public class LoanApplication {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  private Long farmerId;
  private Double amount;
  private Integer tenureMonths;
  private Double emi;
  private String season;
  private String status = "SUBMITTED";
  private Instant createdAt = Instant.now();
  public LoanApplication() {}
  public Long getId(){return id;} public void setId(Long v){id=v;}
  public Long getFarmerId(){return farmerId;} public void setFarmerId(Long v){farmerId=v;}
  public Double getAmount(){return amount;} public void setAmount(Double v){amount=v;}
  public Integer getTenureMonths(){return tenureMonths;} public void setTenureMonths(Integer v){tenureMonths=v;}
  public Double getEmi(){return emi;} public void setEmi(Double v){emi=v;}
  public String getSeason(){return season;} public void setSeason(String v){season=v;}
  public String getStatus(){return status;} public void setStatus(String v){status=v;}
  public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
}
