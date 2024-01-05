package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.trainee.Trainee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, String> {
    @Query("SELECT t FROM Trainee t WHERE t.cohort = ?1")
    Page<Trainee> findAllByCohort(Cohort cohort, Pageable pageable);

    Optional<Trainee> findByTraineeId(String id);
    Optional<Trainee> findByTraineeIdAndCohort(String traineeId, Cohort cohort);
    boolean existsByUserEmail(String traineeEmail);
    void deleteByTraineeId(String traineeId);
    List<Trainee> findAllByCohort_CohortId(String cohortId);
}