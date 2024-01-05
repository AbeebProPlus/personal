package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.institute.InstituteAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituteAddressRepository extends JpaRepository<InstituteAddress, String> {
}
