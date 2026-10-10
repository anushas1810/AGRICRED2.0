package in.agricred.farmer;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {
  private final FarmerRepository repository;
  public FarmerController(FarmerRepository repository) { this.repository = repository; }

  @GetMapping
  public List<Farmer> all() { return repository.findAll(); }

  @GetMapping("/{id}")
  public Farmer get(@PathVariable Long id) {
    return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found"));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Farmer create(@RequestBody Farmer farmer) {
    farmer.setId(null);
    return repository.save(farmer);
  }

  @PutMapping("/{id}")
  public Farmer update(@PathVariable Long id, @RequestBody Farmer input) {
    Farmer farmer = get(id);
    farmer.setName(input.getName()); farmer.setAge(input.getAge());
    farmer.setVillage(input.getVillage()); farmer.setTaluka(input.getTaluka());
    farmer.setDistrict(input.getDistrict()); farmer.setState(input.getState());
    farmer.setAcres(input.getAcres()); farmer.setIrrigatedAcres(input.getIrrigatedAcres());
    farmer.setSoilType(input.getSoilType()); farmer.setLandOwned(input.getLandOwned());
    farmer.setPrimaryCrop(input.getPrimaryCrop()); farmer.setAnnualIncome(input.getAnnualIncome());
    farmer.setAnnualExpenses(input.getAnnualExpenses()); farmer.setExistingDebt(input.getExistingDebt());
    farmer.setInsuranceActive(input.getInsuranceActive());
    return repository.save(farmer);
  }
}
