package in.agricred.assessment;

import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ExternalDataController {
  @GetMapping("/external/weather")
  public Map<String,Object> weather(@RequestParam(defaultValue="Nashik") String district) {
    return Map.of("district",district,"rainfallMmLast30Days",72,"temperatureC",31.5,"droughtProbabilityPercent",25,"source","MOCK_DATA");
  }
  @GetMapping("/external/mandi-price")
  public Map<String,Object> mandi(@RequestParam(defaultValue="cotton") String crop) {
    return Map.of("crop",crop,"pricePerQuintalINR",7200,"trend","STABLE","source","MOCK_DATA");
  }
  @GetMapping("/external/schemes")
  public Map<String,Object> schemes(@RequestParam(defaultValue="Maharashtra") String state,
      @RequestParam(defaultValue="cotton") String crop) {
    return Map.of("state",state,"crop",crop,"schemes",new String[]{"PMFBY crop insurance guidance","Kisan Credit Card eligibility review","State agriculture subsidy eligibility review"},"source","MOCK_DATA","note","Verify eligibility and official scheme details before advising a farmer.");
  }
}
