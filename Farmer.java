package in.agricred.farmer;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="farmers")
public class Farmer {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Integer age;
  private String village;
  private String taluka;
  private String district;
  private String state = "Maharashtra";
  private Double acres;
  private Double irrigatedAcres;
  private String soilType;
  private Boolean landOwned;
  private String primaryCrop;
  private Double annualIncome;
  private Double annualExpenses;
  private Double existingDebt;
  private Boolean insuranceActive = false;
  private Instant createdAt = Instant.now();

  public Farmer() {}
  public Long getId(){return id;} public void setId(Long v){id=v;}
  public String getName(){return name;} public void setName(String v){name=v;}
  public Integer getAge(){return age;} public void setAge(Integer v){age=v;}
  public String getVillage(){return village;} public void setVillage(String v){village=v;}
  public String getTaluka(){return taluka;} public void setTaluka(String v){taluka=v;}
  public String getDistrict(){return district;} public void setDistrict(String v){district=v;}
  public String getState(){return state;} public void setState(String v){state=v;}
  public Double getAcres(){return acres;} public void setAcres(Double v){acres=v;}
  public Double getIrrigatedAcres(){return irrigatedAcres;} public void setIrrigatedAcres(Double v){irrigatedAcres=v;}
  public String getSoilType(){return soilType;} public void setSoilType(String v){soilType=v;}
  public Boolean getLandOwned(){return landOwned;} public void setLandOwned(Boolean v){landOwned=v;}
  public String getPrimaryCrop(){return primaryCrop;} public void setPrimaryCrop(String v){primaryCrop=v;}
  public Double getAnnualIncome(){return annualIncome;} public void setAnnualIncome(Double v){annualIncome=v;}
  public Double getAnnualExpenses(){return annualExpenses;} public void setAnnualExpenses(Double v){annualExpenses=v;}
  public Double getExistingDebt(){return existingDebt;} public void setExistingDebt(Double v){existingDebt=v;}
  public Boolean getInsuranceActive(){return insuranceActive;} public void setInsuranceActive(Boolean v){insuranceActive=v;}
  public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
}
