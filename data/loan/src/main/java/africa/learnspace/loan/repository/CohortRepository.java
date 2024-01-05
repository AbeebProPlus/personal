package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.program.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface CohortRepository extends JpaRepository<Cohort, String> {
    Page<Cohort> findAllByProgram_ProgramId(String programId, Pageable pageable);
    Optional<Cohort> findByCohortIdAndProgram(String cohortId, Program program);
}
