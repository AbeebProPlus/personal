package africa.learnspace.loan.manager;


import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.repository.InstituteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.List;


@Component
@RequiredArgsConstructor
public class InstituteManager {

    private final InstituteRepository instituteRepository;




    public Institute createInstitute(Institute institute) {
        return instituteRepository.save(institute);
    }

    public Optional<Institute> findInstituteById(String id){
        return instituteRepository.findById(id);
    }
    public boolean existsByInstituteId(String instituteId){
        return instituteRepository.existsByInstituteId(instituteId);
    }

    public Institute findById(String instituteId) {

        return instituteRepository.findById(instituteId).orElseThrow(()-> new RuntimeException("institute doesnt exist"));
    }

    public void deleteAll() {
        instituteRepository.deleteAll();
    }


    public Institute findByInstituteName(String instituteName) {
        return instituteRepository.findByInstituteName(instituteName);

    }



    public boolean existsByInstituteName(String instituteName) {
        return instituteRepository.existsByInstituteName(instituteName);
    }




    public Institute save(Institute institute){
        return instituteRepository.save(institute);
    }



    public Page<Institute> viewAllTrainingInstitute(PageRequest pageRequest) {
        return instituteRepository.findAll(pageRequest);
    }

    public Institute findByInstituteId(String instituteId){
        return instituteRepository.findByInstituteId(instituteId);

    }



}
