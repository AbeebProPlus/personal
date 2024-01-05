package africa.learnspace.cohort.services;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CohortLoanPerformanceImpl implements CohortLoanPerformance {

    private final TraineeManager traineeManager;
    private final TraineeLoanDetailManager traineeLoanDetailManager;
    private final CohortLoanDetailManager cohortLoanDetailManager;
    private final CohortManager cohortManager;
    private final CohortMapper cohortMapper;
    private final LoanCalculator loanCalculator;

    @Override
    public CohortLoanPerformanceResponse viewCohortLoanPerformance(String cohortId) {
        Cohort cohort = cohortManager.getCohort(cohortId).orElseThrow(()-> new ResourceNotFoundException("Cohort not found"));

        List<Trainee> traineesInCohort = traineeManager.getAllTraineesByCohortId(cohortId);

        List<TraineeLoanDetail> traineeLoanDetails = traineesInCohort.stream()
                        .map(trainee -> traineeLoanDetailManager.findByTraineeId(trainee.getTraineeId()).orElseThrow())
                        .toList();

        CohortLoanPerformanceResponse mapped = cohortMapper.detailsToResponse(cohortLoanDetail(traineeLoanDetails));
        mapped.setCohortName(cohort.getName());

        return mapped;

    }

    private CohortLoanDetail cohortLoanDetail(List<TraineeLoanDetail> traineeLoanDetails){
        BigDecimal totalAmountRepaid = loanCalculator.summation(traineeLoanDetails, TraineeLoanDetail::getTotalAmountRepaid);
        BigDecimal totalOutstanding = loanCalculator.summation(traineeLoanDetails, TraineeLoanDetail::getTotalOutstanding);
        BigDecimal monthlyExpectedRepayment = loanCalculator.summation(traineeLoanDetails, TraineeLoanDetail::getMonthlyExpectedRepayment);
        BigDecimal lastActualRepayment = loanCalculator.summation(traineeLoanDetails, TraineeLoanDetail::getLastActualRepayment);
        BigDecimal amount = loanCalculator.summation(traineeLoanDetails, TraineeLoanDetail::getAmountDisbursed);
        double debtPercentage = debtPercentage(traineeLoanDetails);
        double repaymentPercentage = repaymentPercentage(traineeLoanDetails);
        BigDecimal interestIncurred = loanCalculator.interestIncurred();

        CohortLoanDetail cohortLoanDetail = CohortLoanDetail.builder()
                .totalAmountDisbursed(amount)
                .totalAmountRepaid(totalAmountRepaid)
                .totalOutstanding(totalOutstanding)
                .debtPercentage(debtPercentage)
                .repaymentPercentage(repaymentPercentage)
                .monthlyExpected(monthlyExpectedRepayment)
                .lastMonthActual(lastActualRepayment)
                .totalInterestIncurred(interestIncurred)
                .build();

        cohortLoanDetailManager.save(cohortLoanDetail);

        return cohortLoanDetail;
    }

    private double debtPercentage(List<TraineeLoanDetail> traineeLoanDetails) {
        return traineeLoanDetails.stream()
                .mapToDouble(detail -> loanCalculator.debtPercentage(detail.getTotalOutstanding(), detail.getAmountDisbursed()))
                .sum();
    }

    private double repaymentPercentage(List<TraineeLoanDetail> traineeLoanDetails) {
        return traineeLoanDetails.stream()
                .mapToDouble(detail -> loanCalculator.repaymentPercentage(detail.getTotalAmountRepaid(), detail.getAmountDisbursed()))
                .sum();
    }

}
