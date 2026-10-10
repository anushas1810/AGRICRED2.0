package in.agricred.loan;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
  private final LoanRepository repository;
  public LoanController(LoanRepository repository) { this.repository = repository; }

  @GetMapping
  public List<LoanApplication> all(@RequestParam(required=false) String status) {
    return status == null || status.isBlank() ? repository.findAll() : repository.findByStatusIgnoreCase(status);
  }

  @GetMapping("/{id}")
  public LoanApplication get(@PathVariable Long id) {
    return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public LoanApplication create(@RequestBody LoanApplication input) {
    input.setId(null);
    if (input.getStatus() == null || input.getStatus().isBlank()) input.setStatus("SUBMITTED");
    if (input.getEmi() == null && input.getAmount() != null && input.getTenureMonths() != null && input.getTenureMonths() > 0) {
      input.setEmi(input.getAmount() / input.getTenureMonths());
    }
    return repository.save(input);
  }

  @PutMapping("/{id}/status")
  public LoanApplication status(@PathVariable Long id, @RequestBody StatusRequest request) {
    LoanApplication loan = get(id);
    if (!List.of("DRAFT","SUBMITTED","UNDER_REVIEW","APPROVED","REJECTED").contains(request.status())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported loan status");
    }
    loan.setStatus(request.status());
    return repository.save(loan);
  }
  public record StatusRequest(String status) {}
}
