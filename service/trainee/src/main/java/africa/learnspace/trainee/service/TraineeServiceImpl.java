package africa.learnspace.trainee.service;


import africa.learnspace.common.services.LoanCalculator;
import africa.learnspace.loan.manager.*;
import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.program.Program;
import africa.learnspace.loan.models.institute.InstituteEmployee;
import africa.learnspace.loan.models.trainee.*;
import africa.learnspace.trainee.dto.*;
import africa.learnspace.notification.controller.NotificationController;
import africa.learnspace.notification.data.request.SendEmailRequest;
import africa.learnspace.trainee.dto.request.*;
import africa.learnspace.trainee.LoanRequestDto;
import africa.learnspace.trainee.exceptions.TraineeException;

import africa.learnspace.trainee.util.*;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.Role;
import africa.learnspace.user.model.User;
import africa.learnspace.usermanagement.iam.TokenGenerator;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import africa.learnspace.usermanagement.services.KeyCloakUserService;
import africa.learnspace.trainee.util.ApiResponse;
import africa.learnspace.trainee.util.GenerateResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;

import java.util.Set;

import static africa.learnspace.loan.models.institute.InstituteStatus.ACTIVE;
import static africa.learnspace.trainee.util.ConstantUtils.*;
import static africa.learnspace.user.model.Role.TRAINEE;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    private final CohortManager cohortManager;
    private final ProgramManager programManager;
    private final InstituteManager instituteManager;
    private final TraineeManager traineeManager;
    private final TraineeLoanDetailManager traineeLoanDetailManager;

    private final PaymentManager paymentManager;

    private final BankDetailManager bankDetailManager;
    private final LoanRequestManager loanRequestManager;
    private final ModelMapper mapper;
    private final InstituteEmployeeManager instituteEmployeeManager;
    private final UserManager userManager;
    private final KeyCloakUserService keyCloakUserService;
    private final TraineeMapper traineeMapper;

    private final TokenGenerator tokenGenerator;
    private final LoanCalculator loanCalculator;
    private final NotificationController notificationController;
    @Value("${baseUrl:http://localhost:3000}")
    private String baseUrl;

    @Override
    public Page<Trainee> viewAllTraineesInACohort(ViewAllTraineeRequest viewAllTraineeRequest, Pageable pageable) {
        validateInstituteId(viewAllTraineeRequest.getInstituteId());
        validateProgramId(viewAllTraineeRequest.getProgramId());
        validateCohortId(viewAllTraineeRequest.getCohortId());
        Institute institute = findInstituteById(viewAllTraineeRequest.getInstituteId());
        Program program = findProgramInTrainingInstitute(viewAllTraineeRequest.getProgramId(), institute);
        Cohort cohort = findCohortInProgram(viewAllTraineeRequest.getCohortId(), program);
        return traineeManager.findAllByCohort(cohort, pageable);
    }

    private Institute findInstituteById(String id) {
        return instituteManager.findInstituteById(id)
                .orElseThrow(() -> new TraineeException(INSTITUTE_DOES_NOT_EXIST));
    }

    private Program findProgramInTrainingInstitute(String programId, Institute institute) {
        return programManager.findProgramByProgramIdAndInstitute(programId, institute).orElseThrow(
                () -> new TraineeException(INSTITUTE_DOES_NOT_OFFER_THIS_PROGRAM)
        );
    }

    private Cohort findCohortInProgram(String cohortId, Program program) {
        return cohortManager.findByCohortIdAndInstituteProgram(cohortId, program).orElseThrow(
                () -> new TraineeException(COHORT_IS_NOT_PART_OF_PROGRAM));
    }

    private void validateInstituteId(String instituteId) {
        if (isBlankOrEmptyOrNull(instituteId))
            throw new TraineeException("Institute id " + NAME_ERROR);
    }

    private void validateProgramId(String programId) {
        if (isBlankOrEmptyOrNull(programId))
            throw new TraineeException("Program id " + NAME_ERROR);
    }

    private void validateCohortId(String cohortId) {
        if (isBlankOrEmptyOrNull(cohortId))
            throw new TraineeException("Cohort id " + NAME_ERROR);
    }

    @Override
    @Transactional
    public ApiResponse viewTraineeDetails(TraineeDetailsRequest traineeDetailsRequest) {
        Institute institute = findInstituteById(traineeDetailsRequest.getInstituteId());
        Program program = findProgramInTrainingInstitute(traineeDetailsRequest.getProgramId(), institute);
        Cohort cohort = findCohortInProgram(traineeDetailsRequest.getCohortId(), program);
        Trainee trainee = traineeManager.findTraineeInCohort(traineeDetailsRequest.getTraineeId(), cohort)
                .orElseThrow(() -> new TraineeException(TRAINEE_DOES_NOT_EXIST));
        TraineeDto traineeDto = createTraineeDto(trainee);
        return GenerateResponse.okResponse(traineeDto);
    }

    private TraineeDto createTraineeDto(Trainee trainee) {
        if (trainee == null || trainee.getUser() == null) throw new TraineeException("User does not exist");
        UserDto user = mapper.map(trainee.getUser(), UserDto.class);
        TraineeDto traineeDto = traineeMapper.traineeToTraineeDto(user, trainee, getTraineeLoanDetail(trainee),
                getPaymentHistory(trainee), getTraineeBankDetails(trainee));
        traineeDto.setTuitionAmount(trainee.getCohort().getTuition());
        return traineeDto;
    }

    private List<PaymentDto> getPaymentHistory(Trainee trainee) {
        List<Payment> payments = paymentManager.findTraineePayments(trainee);
        return traineeMapper.paymentsToPaymentDto(payments);
    }

    private TraineeLoanDetailDto getTraineeLoanDetail(Trainee trainee) {
        TraineeLoanDetail traineeLoanDetail= traineeLoanDetailManager.findLoanDetailByTrainee(trainee);
        return traineeMapper.traineeLoanDetailToTraineeLoanDetailDto(traineeLoanDetail);
    }

    private List<BankDetailDto> getTraineeBankDetails(Trainee trainee) {
        List<BankDetail> bankDetails = bankDetailManager.getTraineeBankDetails(trainee);
        return traineeMapper.bankDetailsDtoToBankDetails(bankDetails);
    }

    @Override
    public ApiResponse referTrainee (ReferTraineeRequest referTraineeRequest, String email){
        InstituteEmployee instituteEmployee = instituteEmployeeManager.findByUserEmail(email, referTraineeRequest.getInstituteId())
                .orElseThrow(() -> new TraineeException("You are not an employee of this institute"));
        if (!Objects.equals(instituteEmployee.getUser().getEmail(), email)) {
            throw new TraineeException("You are not an employee of this institute");
        }
        createLoanRequestForTrainee(referTraineeRequest);
        return GenerateResponse.okResponse("Trainee referred successfully");
    }
    private void createLoanRequestForTrainee (ReferTraineeRequest referTraineeRequest){
        validateTraineeId(referTraineeRequest.getTraineeId());
        Trainee trainee = findTrainee(referTraineeRequest.getTraineeId());
        LoanRequest loanRequest = mapper.map(referTraineeRequest, LoanRequest.class);
        loanRequest.setTrainee(trainee);
        loanRequestManager.createLoanRequest(loanRequest);
    }
    private void validateTraineeId (String traineeId){
        if (isBlankOrEmptyOrNull(traineeId))
            throw new TraineeException("Trainee id " + NAME_ERROR);
    }
    private boolean isBlankOrEmptyOrNull (String name){
        return StringUtils.isBlank(name);
    }
    Trainee findTrainee (String traineeId){
        return traineeManager.findTraineeById(traineeId)
                .orElseThrow(() -> new TraineeException(TRAINEE_DOES_NOT_EXIST));
    }
    @Override
    @Transactional
    public ApiResponse editTraineeDetails(EditTraineeRequest editTraineeRequest, String email) {
        InstituteEmployee instituteEmployee = instituteEmployeeManager.findByUserEmail(email, editTraineeRequest.getInstituteId())
                .orElseThrow(() -> new TraineeException("UNAUTHORIZED"));
        if (!Objects.equals(instituteEmployee.getUser().getEmail(), email)) {
            throw new TraineeException("UNAUTHORIZED");
        }
        if (userManager.userExistsByEmail(editTraineeRequest.getEmail()))
            throw new TraineeException(USER_EXISTS);
        if (!isValidMailAddress(editTraineeRequest.getEmail()))
            throw new TraineeException(PROVIDE_VALID_EMAIL);
        if (isBlankOrEmptyOrNull(editTraineeRequest.getFirstName()))
            throw new TraineeException("First name " + NAME_ERROR);
        if (isBlankOrEmptyOrNull(editTraineeRequest.getLastName())) {
            throw new TraineeException("Last name " + NAME_ERROR);
        }
        Trainee trainee = traineeManager.findTraineeById(editTraineeRequest.getTraineeId())
                .orElseThrow(() -> new TraineeException(TRAINEE_DOES_NOT_EXIST));
        User user = trainee.getUser();
        mapper.map(editTraineeRequest, user);
        userManager.saveUser(user);
        traineeManager.createTrainee(trainee);
        return GenerateResponse.okResponse("Trainee details updated successfully");
    }
    public ApiResponse viewLoanReferral(String traineeId) {
        validateTraineeId(traineeId);
        Trainee trainee = findTrainee(traineeId);
        if (findTrainee(trainee) == null) throw new TraineeException(TRAINEE_WAS_NOT_REFERRED);
        LoanRequest loanRequest = loanRequestManager.findLoanRequestByTrainee(trainee);
        LoanRequestDto loanRequestDto = mapper.map(loanRequest, LoanRequestDto.class);
        return GenerateResponse.okResponse(loanRequestDto);
    }

    @Override
    @Transactional
    public ApiResponse removeTrainee (RemoveTraineeRequest request, String email){
        InstituteEmployee instituteEmployee = instituteEmployeeManager.findByUserEmail(email, request.getInstituteId())
                .orElseThrow(() -> new TraineeException("You are not an admin of this institute"));
        if (!Objects.equals(instituteEmployee.getUser().getEmail(), email)) {
            throw new TraineeException("You are not an admin of this institute");
        }
        if (!instituteManager.existsByInstituteId(request.getInstituteId()))
            throw new TraineeException(INSTITUTE_DOES_NOT_EXIST);
        Trainee trainee = traineeManager.findTraineeById(request.getTraineeId())
                .orElseThrow(() -> new TraineeException(TRAINEE_DOES_NOT_EXIST));
        if (loanRequestManager.existsByTrainee(trainee))
            throw new TraineeException(TRAINEE_HAS_LOAN_REQUEST);
        traineeManager.deleteTrainee(request.getTraineeId());
        return GenerateResponse.okResponse("Trainee removed successfully");
    }

    @Override
    public ApiResponse inviteTrainee (InviteTraineeDto inviteTraineeDto, String adminEmail){
        InstituteEmployee instituteEmployee = instituteEmployeeManager.findByUserEmail(adminEmail, inviteTraineeDto.getInstituteId())
                .orElseThrow(() -> new TraineeException("You are not an admin of this institute"));
        if (!Objects.equals(instituteEmployee.getUser().getEmail(), adminEmail)) {
            throw new TraineeException("You are not an admin of this institute");
        }
        User foundUser = userManager.findByEmail(inviteTraineeDto.getTraineeEmail());
        if (foundUser == null)
            throw new TraineeException(USER_DOES_NOT_EXIST);
        String token = tokenGenerator.generateToken(foundUser.getEmail());
        String link = baseUrl + "/create-password?token=" + token;
        Context context = new Context();
        context.setVariable("token", link);
        context.setVariable("firstName", foundUser.getFirstName());
        context.setVariable("currentYear", LocalDate.now().getYear());
        SendEmailRequest emailRequest = new SendEmailRequest();
        emailRequest.setSubject("Password Reset Instructions");
        emailRequest.setTo(foundUser.getEmail());
        emailRequest.setHtmlContent("password_reset");
        emailRequest.setContext(context);
        notificationController.sendEmail(emailRequest);
        return GenerateResponse.okResponse("Invite has been sent successfully");
    }

    @Override
    public ApiResponse addTraineeToCohort (AddTraineeRequest addTraineeRequest, String email){
        InstituteEmployee employee = instituteEmployeeManager.findByUserEmail(email, addTraineeRequest.getInstituteId())
                .orElseThrow(() -> new TraineeException("UNAUTHORIZED"));
        if (!Objects.equals(employee.getUser().getEmail(), email)) {
            throw new TraineeException("UNAUTHORIZED");
        }
        Institute institute = instituteManager.findInstituteById(addTraineeRequest.getInstituteId())
                .orElseThrow(() -> new TraineeException(INSTITUTE_DOES_NOT_EXIST));
        if (institute.getStatus() != ACTIVE) throw new TraineeException(INSTITUTE_IS_NOT_ACTIVE);
        validateTraineeDetails(addTraineeRequest);
        if (userManager.userExistsByEmail(addTraineeRequest.getEmailAddress()))
            throw new TraineeException(USER_EXISTS);
        Program program = findProgramInTrainingInstitute(addTraineeRequest.getProgramId(), institute);
        findCohortInProgram(addTraineeRequest.getCohortId(), program);
        Trainee trainee = createTraineeAccount(addTraineeRequest.getEmailAddress(), addTraineeRequest.getFirstName(), addTraineeRequest.getLastName(),
                addTraineeRequest.getCohortId());
        addTraineeToCohort(addTraineeRequest.getCohortId(), trainee);
        return GenerateResponse.okResponse(trainee);
    }
    private void addUserTraineeToKeyCloak (User user){
        UserRegistrationRequest userRegistrationRequest = mapper.map(user, UserRegistrationRequest.class);
        userRegistrationRequest.setRole(String.valueOf(TRAINEE));
        keyCloakUserService.createUser(userRegistrationRequest);
    }
    private void validateTraineeDetails (AddTraineeRequest addTraineeRequest){
        if (isBlankOrEmptyOrNull(addTraineeRequest.getFirstName()))
            throw new TraineeException("First name " + NAME_ERROR);
        if (isBlankOrEmptyOrNull(addTraineeRequest.getLastName()))
            throw new TraineeException("Last name " + NAME_ERROR);
        if (isBlankOrEmptyOrNull(addTraineeRequest.getEmailAddress()))
            throw new TraineeException("Email " + NAME_ERROR);
        if (!isValidMailAddress(addTraineeRequest.getEmailAddress()))
            throw new TraineeException(PROVIDE_VALID_EMAIL);
        validateInstituteId(addTraineeRequest.getInstituteId());
        validateCohortId(addTraineeRequest.getCohortId());
        validateProgramId(addTraineeRequest.getProgramId());
    }
    private boolean isValidMailAddress (String email){
        return email.matches("^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+\\.[A-Za-z]+$");
    }
    private Trainee createTraineeAccount (String traineeEmail, String traineeFirstName, String
            traineeLastName, String cohortId){
        if (traineeExistsInCohort(traineeEmail)) throw new TraineeException(TRAINEE_EXISTS_IN_COHORT);
        Cohort foundCohort = cohortManager.findById(cohortId)
                .orElseThrow(() -> new TraineeException(COHORT_IS_NOT_PART_OF_PROGRAM));
        User user = addUserTraineeToUserRepo(traineeEmail, traineeFirstName, traineeLastName);
        Trainee trainee = Trainee.builder().user(user).cohort(foundCohort).build();
        addUserTraineeToKeyCloak(user);
        traineeManager.createTrainee(trainee);
        return trainee;
    }
    private boolean traineeExistsInCohort (String traineeEmail){
        return traineeManager.trainingExistsInCohort(traineeEmail);
    }
    private void addTraineeToCohort (String cohortId, Trainee trainee){
        Cohort foundCohort = cohortManager.findById(cohortId).
                orElseThrow(() -> new TraineeException(COHORT_IS_NOT_PART_OF_PROGRAM));
        trainee.setCohort(foundCohort);
        cohortManager.save(foundCohort);
    }
    private User addUserTraineeToUserRepo (String traineeEmail, String traineeFirstName,
                                           String traineeLastName){
        User user = new User();
        user.setEmail(traineeEmail);
        user.setFirstName(traineeFirstName);
        user.setLastName(traineeLastName);
        user.setCreatedAt(LocalDateTime.now());
        Set<Role> roles = new HashSet<>();
        roles.add(TRAINEE);
        user.setRoles(roles);
        userManager.saveUser(user);
        return user;
    }
    private LoanRequest findTrainee(Trainee trainee){
        return loanRequestManager.findLoanRequestByTrainee(trainee);
    }
    @Override
    public LoanPerformanceDto viewTraineeLoanPerformance(ViewLoanPerformanceDto loanPerformanceDto) {
        Institute institute = instituteManager.findByInstituteId(loanPerformanceDto.getInstituteId());
        Program program = findProgramInTrainingInstitute(loanPerformanceDto.getProgramId(), institute);
        if (!cohortIsPartOfProgram(loanPerformanceDto.getCohortId(), program))
            throw new TraineeException(COHORT_IS_NOT_PART_OF_PROGRAM);
        Trainee trainee = traineeManager.findTraineeById(loanPerformanceDto.getTraineeId())
                .orElseThrow(() -> new TraineeException(TRAINEE_DOES_NOT_EXIST));
        List<PaymentDto> payments = getPaymentHistory(trainee);
        TraineeLoanDetailDto traineeLoanDetail = updateTraineeLoanDetail(trainee);
        return LoanPerformanceDto.builder()
                .instituteName(institute.getInstituteName()).traineeLoanDetail(traineeLoanDetail)
                .paymentHistory(payments).build();
    }
    private boolean cohortIsPartOfProgram(String cohortId, Program program){
        Cohort cohort = cohortManager.findByCohortIdAndInstituteProgram(cohortId, program)
                .orElse(null);
        return cohort != null;
    }
    private TraineeLoanDetailDto updateTraineeLoanDetail(Trainee trainee) {
        TraineeLoanDetail loanDetail = traineeLoanDetailManager.findLoanDetailByTrainee(trainee);
        if (loanDetail == null) throw new TraineeException("TRAINEE HAS NO LOAN DETAILS");
        loanDetail.setAmountDisbursed(loanDetail.getAmountDisbursed());
        loanDetail.setTotalOutstanding(totalOutstanding(loanDetail));
        loanDetail.setTotalAmountRepaid(totalAmountRepaid(loanDetail));
        loanDetail.setRepaymentPercentage(getRepaymentPercentage(loanDetail));
        loanDetail.setDebtPercentage(getDebtPercentage(loanDetail));
        loanDetail.setInterestIncurred(interestIncurred());
        loanDetail.setMonthlyExpectedRepayment(BigDecimal.ZERO);
        loanDetail.setLastActualRepayment(BigDecimal.ZERO);
        traineeLoanDetailManager.createTraineeDetail(loanDetail);
        TraineeLoanDetailDto traineeLoanDetailDto = traineeMapper.traineeLoanDetailToTraineeLoanDetailDto(loanDetail);
        return traineeLoanDetailDto;
    }
    private double getRepaymentPercentage(TraineeLoanDetail loanDetail) {
        return loanCalculator.repaymentPercentage(loanDetail.getTotalAmountRepaid(), loanDetail.getAmountDisbursed());
    }

    private double getDebtPercentage(TraineeLoanDetail loanDetail) {
        return loanCalculator.debtPercentage(loanDetail.getTotalOutstanding(), loanDetail.getAmountDisbursed());
    }

    private BigDecimal totalOutstanding(TraineeLoanDetail loanDetail) {
        return loanCalculator.totalOutstanding(loanDetail.getAmountDisbursed(), loanDetail.getTotalAmountRepaid());
    }
    private BigDecimal totalAmountRepaid(TraineeLoanDetail loanDetail) {
        return loanCalculator.totalAmountRepaid(loanDetail.getTotalAmountRepaid());
    }
    private BigDecimal interestIncurred(){
        return loanCalculator.interestIncurred();
    }
}