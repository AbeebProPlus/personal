package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.institute.Institute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituteRepository extends JpaRepository<Institute, String> {

    boolean existsByInstituteId(String id);

    Institute findByInstituteName(String instituteName);



    boolean existsByInstituteName(String instituteName);

    Institute findByInstituteId(String id);
}
