package in.agricred.loan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LoanRepository extends JpaRepository<LoanApplication, Long> {
  List<LoanApplication> findByStatusIgnoreCase(String status);
}
