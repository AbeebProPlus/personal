package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.program.Program;
import africa.learnspace.loan.repository.CohortRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CohortManager {

    private final CohortRepository cohortRepository;

    public CohortManager(CohortRepository cohortRepository) {
        this.cohortRepository = cohortRepository;
    }

    public Optional<Cohort> findById(String cohortId) {
        return cohortRepository.findById(cohortId);
    }

    public Optional<Cohort> findByCohortIdAndInstituteProgram(String cohortId, Program program){
        return cohortRepository.findByCohortIdAndProgram(cohortId, program);
    }
    public Cohort save(Cohort cohort) {
        return cohortRepository.save(cohort);
    }

    public void delete(Cohort cohort){
        cohortRepository.delete(cohort);
    }

    public List<Cohort> getAllCohorts (){
        return cohortRepository.findAll();
    }

    public Optional<Cohort> getCohort(String cohortId){
        return cohortRepository.findById(cohortId);
    }

    public Page<Cohort> getAllCohortsByProgramId(String programId, Pageable pageable){
        return cohortRepository.findAllByProgram_ProgramId(programId, pageable);
    }
}
