package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.institute.InstituteEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstituteEmployeeRepository extends JpaRepository<InstituteEmployee, String> {
    Optional<InstituteEmployee> findByUser_EmailAndInstitute_InstituteId(String email, String instituteId);
    Optional<InstituteEmployee> findByUser_UserIdAndInstitute_InstituteIdAndIsAdminIsTrue(String userId, String instituteId);
    boolean existsById(String id);

}
