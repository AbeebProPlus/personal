package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.institute.InstituteAddress;
import africa.learnspace.loan.repository.InstituteAddressRepository;
import org.springframework.stereotype.Component;

@Component
public class InstituteAddressManager {

    private final InstituteAddressRepository instituteAddressRepository;

    public InstituteAddressManager(InstituteAddressRepository instituteAddressRepository) {
        this.instituteAddressRepository = instituteAddressRepository;
    }

    public InstituteAddress createInstituteAddress(InstituteAddress instituteAddress) {
        return instituteAddressRepository.save(instituteAddress);
    }
}
