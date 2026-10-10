package in.agricred.auth;

import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @PostMapping("/login")
  public Map<String,Object> login(@RequestBody LoginRequest request) {
    if (request.email() == null || request.password() == null || request.email().isBlank() || request.password().isBlank()) {
      throw new IllegalArgumentException("Email and password are required");
    }
    // Local demo only: replace with BCrypt + JWT/OIDC before production.
    boolean officer = request.email().equalsIgnoreCase("officer@agricred.in") && request.password().equals("demo123");
    boolean farmer = request.email().equalsIgnoreCase("ramesh@farmer.in") && request.password().equals("demo123");
    if (!officer && !farmer) throw new IllegalArgumentException("Invalid demo credentials");
    return Map.of("token", "LOCAL-DEMO-TOKEN", "name", officer ? "Loan Officer" : "Ramesh Kumar",
      "role", officer ? "OFFICER" : "FARMER", "demoOnly", true);
  }
  @GetMapping("/me")
  public Map<String,Object> me() {
    return Map.of("authenticated", false, "message", "Configure JWT/OIDC to enable protected identity lookup.");
  }
  public record LoginRequest(String email, String password) {}
}
