package africa.learnspace.trainee.service;


import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.trainee.dto.LoanPerformanceDto;
import africa.learnspace.trainee.dto.request.*;
import africa.learnspace.trainee.util.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TraineeService {
    ApiResponse viewTraineeDetails(TraineeDetailsRequest traineeDetailsRequest);

    Page<Trainee> viewAllTraineesInACohort(ViewAllTraineeRequest viewAllTraineeRequest, Pageable pageable);
    ApiResponse editTraineeDetails(EditTraineeRequest editTraineeRequest, String email);
    ApiResponse referTrainee(ReferTraineeRequest referTraineeRequest, String email);
    ApiResponse removeTrainee(RemoveTraineeRequest request, String email);
    ApiResponse inviteTrainee(InviteTraineeDto inviteTraineeDto, String adminEmail);
    ApiResponse addTraineeToCohort(AddTraineeRequest request, String adminEmail);
    LoanPerformanceDto viewTraineeLoanPerformance(ViewLoanPerformanceDto loanPerformanceDto);
    ApiResponse viewLoanReferral(String traineeId);
}
