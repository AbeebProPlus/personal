package africa.learnspace.web.controller.traineecontroller;


import africa.learnspace.loan.models.trainee.Trainee;

import africa.learnspace.trainee.dto.request.*;
import africa.learnspace.trainee.dto.request.InviteTraineeDto;
import africa.learnspace.trainee.service.TraineeService;
import jakarta.validation.Valid;
import africa.learnspace.trainee.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/learnspace/trainee")
public class TraineeController {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService){
        this.traineeService = traineeService;
    }

    @PostMapping("/loan-performance")
    @PreAuthorize("hasAnyRole('PORTFOLIO_MANAGER')")
    public ResponseEntity<?> viewTraineeLoanPerformance(@Valid @RequestBody ViewLoanPerformanceDto loanPerformanceDto) {
        return ResponseEntity.ok(traineeService.viewTraineeLoanPerformance(loanPerformanceDto));
    }
    @PostMapping("/reference")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN')")
    public ResponseEntity<?> referTrainee(@Valid @RequestBody ReferTraineeRequest referTraineeRequest,
                                          @AuthenticationPrincipal Jwt jwt){
        String email = jwt.getClaim("email");
        return ResponseEntity.ok(traineeService.referTrainee(referTraineeRequest, email));
    }
    @PostMapping("/invite")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN')")
    public ResponseEntity<?> inviteTrainee(@RequestBody InviteTraineeDto inviteTraineeDto,
                                           @AuthenticationPrincipal Jwt jwt){
        String email = jwt.getClaim("email");
        return ResponseEntity.ok(traineeService.inviteTrainee(inviteTraineeDto, email));
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN')")
    public ResponseEntity<?> removeTrainee(@RequestBody RemoveTraineeRequest request,
                                           @AuthenticationPrincipal Jwt jwt){
        String email = jwt.getClaim("email");
        return ResponseEntity.ok(traineeService.removeTrainee(request, email));
    }
    @PostMapping("/trainees")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'PORTFOLIO_MANAGER')")
    public ResponseEntity<Page<Trainee>> viewAllTrainees(@Valid @RequestBody ViewAllTraineeRequest viewAllTraineeRequest,
                                                         @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Trainee> trainees = traineeService.viewAllTraineesInACohort(viewAllTraineeRequest, pageable);
        return ResponseEntity.ok(trainees);
    }

    @PatchMapping("/edit")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN')")
    public ResponseEntity<?> editTrainee(@RequestBody EditTraineeRequest editTraineeRequest,
                                         @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        return ResponseEntity.ok(traineeService.editTraineeDetails(editTraineeRequest, email));
    }
    @PostMapping("/details")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'PORTFOLIO_MANAGER')")
    public ResponseEntity<?> viewTraineeDetails(@Valid @RequestBody TraineeDetailsRequest traineeDetailsRequest) {
        return ResponseEntity.ok(traineeService.viewTraineeDetails(traineeDetailsRequest));
    }
    @GetMapping("/loan-referral/{traineeId}")
    @PreAuthorize("hasAnyRole('TRAINEE')")
    public ApiResponse viewLoanReferral(@PathVariable String traineeId){
        return traineeService.viewLoanReferral(traineeId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN')")
    public ResponseEntity<?> addTraineeToCohort(@Valid @RequestBody AddTraineeRequest addTraineeRequest,
                                                @AuthenticationPrincipal Jwt jwt){
        String email = jwt.getClaim("email");
        log.info(email);
        return ResponseEntity.ok(traineeService.addTraineeToCohort(addTraineeRequest, email));
    }
}