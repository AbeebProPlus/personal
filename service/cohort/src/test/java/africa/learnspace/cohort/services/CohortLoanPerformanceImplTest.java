package africa.learnspace.cohort.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import africa.learnspace.cohort.data.response.CohortLoanPerformanceResponse;
import africa.learnspace.cohort.mappers.CohortMapper;
import africa.learnspace.common.exceptions.ResourceNotFoundException;
import africa.learnspace.loan.manager.CohortLoanDetailManager;
import africa.learnspace.loan.manager.CohortManager;
import africa.learnspace.loan.manager.TraineeLoanDetailManager;
import africa.learnspace.loan.manager.TraineeManager;
import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.cohort.CohortLoanDetail;
import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.loan.models.trainee.TraineeLoanDetail;
import africa.learnspace.common.services.LoanCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CohortLoanPerformanceImplTest {

    @Mock
    private TraineeManager traineeManager;
    @Mock
    private TraineeLoanDetailManager traineeLoanDetailManager;
    @Mock
    private CohortLoanDetailManager cohortLoanDetailManager;
    @Mock
    private CohortManager cohortManager;
    @Mock
    private CohortMapper cohortMapper;

    @Mock
    private LoanCalculator loanCalculator;

    private CohortLoanPerformanceImpl cohortLoanPerformance;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cohortLoanPerformance = new CohortLoanPerformanceImpl(traineeManager, traineeLoanDetailManager, cohortLoanDetailManager, cohortManager, cohortMapper, loanCalculator);
    }

    @Test
    public void testViewCohortLoanPerformance() {
        // Arrange
        String cohortId = "123";
        Cohort cohort = new Cohort();
        when(cohortManager.getCohort(cohortId)).thenReturn(Optional.of(cohort));

        List<Trainee> traineesInCohort = new ArrayList<>();
        when(traineeManager.getAllTraineesByCohortId(cohortId)).thenReturn(traineesInCohort);

        List<TraineeLoanDetail> traineeLoanDetails = new ArrayList<>();
        when(traineeLoanDetailManager.findByTraineeId(anyString())).thenAnswer(invocation -> {
            String traineeId = invocation.getArgument(0);
            TraineeLoanDetail traineeLoanDetail = new TraineeLoanDetail();
            traineeLoanDetail.setTraineeLoanDetailId(traineeId);
            traineeLoanDetails.add(traineeLoanDetail);
            return Optional.of(traineeLoanDetail);
        });

        CohortLoanDetail cohortLoanDetail = new CohortLoanDetail();
        when(cohortMapper.detailsToResponse(any())).thenReturn(new CohortLoanPerformanceResponse());
        when(cohortLoanDetailManager.save(any())).thenReturn(cohortLoanDetail);

        // Act
        CohortLoanPerformanceResponse response = cohortLoanPerformance.viewCohortLoanPerformance(cohortId);

        // Assert
        assertNotNull(response);
        assertEquals(cohort.getName(), response.getCohortName());
        //
    }

    @Test
    public void testViewCohortLoanPerformanceCohortNotFound() {
        // Arrange
        String cohortId = "456";
        when(cohortManager.getCohort(cohortId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cohortLoanPerformance.viewCohortLoanPerformance(cohortId);
        });
    }
}
