package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.institute.InstituteEmployee;
import africa.learnspace.loan.repository.InstituteEmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class InstituteEmployeeManager  {

    private final InstituteEmployeeRepository instituteEmployeeRepository;

    public void save(InstituteEmployee instituteEmployee) {
        instituteEmployeeRepository.save(instituteEmployee);
    }
    public Optional<InstituteEmployee> findByUserEmail(String email, String instituteId) {
        return instituteEmployeeRepository.findByUser_EmailAndInstitute_InstituteId(email, instituteId);
    }
    public Optional<InstituteEmployee> findByInstituteAdminById(String adminId, String instituteId) {
        return instituteEmployeeRepository.findByUser_UserIdAndInstitute_InstituteIdAndIsAdminIsTrue(adminId, instituteId);
    }

    public boolean existsByAdminById(String id) {
        return instituteEmployeeRepository.existsById(id);
    }
}
