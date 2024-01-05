package africa.learnspace.trainee.service;

import africa.learnspace.common.services.LoanCalculator;
import africa.learnspace.loan.manager.*;
import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.institute.InstituteEmployee;
import africa.learnspace.loan.models.program.Program;
import africa.learnspace.loan.models.trainee.*;
import africa.learnspace.trainee.dto.LoanPerformanceDto;

import africa.learnspace.trainee.dto.request.*;
import africa.learnspace.notification.controller.NotificationController;
import africa.learnspace.trainee.dto.request.TraineeDetailsRequest;


import africa.learnspace.trainee.dto.request.InviteTraineeDto;
import africa.learnspace.trainee.exceptions.TraineeException;
import africa.learnspace.trainee.util.ApiResponse;
import africa.learnspace.trainee.util.GenerateResponse;
import africa.learnspace.trainee.util.TraineeMapper;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import africa.learnspace.usermanagement.iam.TokenGenerator;

import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import africa.learnspace.usermanagement.services.KeyCloakUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

import static africa.learnspace.loan.models.institute.InstituteStatus.ACTIVE;
import static africa.learnspace.loan.models.institute.InstituteStatus.INACTIVE;
import static africa.learnspace.trainee.util.ConstantUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {
    @Mock
    private UserManager userManager;
    @Mock
    private CohortManager cohortManager;
    @Mock
    private ProgramManager programManager;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private InstituteManager instituteManager;
    @Mock
    private BankDetailManager bankDetailManager;
    @Mock
    private ModelMapper mapper;
    @Mock
    private LoanCalculator loanCalculator;
    @Mock
    private TraineeLoanDetailManager traineeLoanDetailManager;
    @Mock
    private PaymentManager paymentManager;
    @Mock
    private TraineeManager traineeManager;

    @Mock
    private KeyCloakUserService keyCloakUserService;

    @Mock
    private LoanRequestManager loanRequestManager;
    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private InstituteEmployeeManager instituteEmployeeManager;
    @Mock
    private NotificationController notificationController;
    @InjectMocks
    private TraineeServiceImpl traineeService;


    @Test
    void testViewAllTraineesInACohort() {
        ViewAllTraineeRequest viewAllTraineeRequest = new ViewAllTraineeRequest();
        viewAllTraineeRequest.setInstituteId("instituteId");
        viewAllTraineeRequest.setProgramId("programId");
        viewAllTraineeRequest.setCohortId("cohortId");

        Institute institute = new Institute();
        Program program = new Program();
        Cohort cohort = new Cohort();
        Pageable pageable = Pageable.unpaged();
        Page<Trainee> traineePage = Page.empty();

        when(instituteManager.findInstituteById(viewAllTraineeRequest.getInstituteId())).thenReturn(Optional.of(institute));
        when(programManager.findProgramByProgramIdAndInstitute(viewAllTraineeRequest.getProgramId(), institute))
                .thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(viewAllTraineeRequest.getCohortId(), program))
                .thenReturn(Optional.of(cohort));
        when(traineeManager.findAllByCohort(cohort, pageable)).thenReturn(traineePage);

        Page<Trainee> result = traineeService.viewAllTraineesInACohort(viewAllTraineeRequest, pageable);

        assertNotNull(result);
        assertEquals(traineePage, result);
        verify(instituteManager, times(1)).findInstituteById(viewAllTraineeRequest.getInstituteId());
        verify(programManager, times(1)).findProgramByProgramIdAndInstitute(viewAllTraineeRequest.getProgramId(), institute);
        verify(cohortManager, times(1)).findByCohortIdAndInstituteProgram(viewAllTraineeRequest.getCohortId(), program);
        verify(traineeManager, times(1)).findAllByCohort(cohort, pageable);

    }
    @Test
    void testViewAllTraineesInACohort_InvalidInstitute() {
        ViewAllTraineeRequest viewAllTraineeRequest = new ViewAllTraineeRequest();
        viewAllTraineeRequest.setInstituteId("invalid_institute");
        viewAllTraineeRequest.setProgramId("456");
        viewAllTraineeRequest.setCohortId("789");

        when(instituteManager.findInstituteById("invalid_institute")).thenReturn(Optional.empty());

        assertThrows(TraineeException.class,
                () -> traineeService.viewAllTraineesInACohort(viewAllTraineeRequest, Pageable.unpaged()));

        verify(instituteManager, times(1)).findInstituteById("invalid_institute");
        verify(programManager, never()).findProgramByProgramIdAndInstitute(anyString(), any());
        verify(cohortManager, never()).findByCohortIdAndInstituteProgram(anyString(), any());
        verify(traineeManager, never()).findAllByCohort(any(), any());
    }
    @Test
    void testViewAllTraineesInACohort_InvalidProgram() {
        ViewAllTraineeRequest viewAllTraineeRequest = new ViewAllTraineeRequest();
        viewAllTraineeRequest.setInstituteId("123");
        viewAllTraineeRequest.setProgramId("invalid_program");
        viewAllTraineeRequest.setCohortId("789");

        when(instituteManager.findInstituteById("123")).thenReturn(Optional.of(new Institute()));

        when(programManager.findProgramByProgramIdAndInstitute("invalid_program", new Institute()))
                .thenReturn(Optional.empty());

        assertThrows(TraineeException.class,
                () -> traineeService.viewAllTraineesInACohort(viewAllTraineeRequest, Pageable.unpaged()));

        verify(instituteManager, times(1)).findInstituteById("123");
        verify(cohortManager, never()).findByCohortIdAndInstituteProgram(anyString(), any());
        verify(traineeManager, never()).findAllByCohort(any(), any());
    }

    @Test
    void testViewAllTraineesInACohort_InvalidCohort() {
        ViewAllTraineeRequest viewAllTraineeRequest = new ViewAllTraineeRequest();
        viewAllTraineeRequest.setInstituteId("123");
        viewAllTraineeRequest.setProgramId("456");
        viewAllTraineeRequest.setCohortId("invalid_cohort");
        when(instituteManager.findInstituteById("123")).thenReturn(Optional.of(new Institute()));

        when(programManager.findProgramByProgramIdAndInstitute("456", new Institute()))
                .thenReturn(Optional.of(new Program()));

        when(cohortManager.findByCohortIdAndInstituteProgram("invalid_cohort", new Program()))
                .thenReturn(Optional.empty());

        assertThrows(TraineeException.class,
                () -> traineeService.viewAllTraineesInACohort(viewAllTraineeRequest, Pageable.unpaged()));
    }

    @Test
    void removeTrainee_SuccessfulRemoval() {
        String email = "admin@example.com";
        String instituteId = "institute123";
        String traineeId = "trainee123";
        RemoveTraineeRequest request = new RemoveTraineeRequest();
        request.setTraineeId(traineeId);
        request.setInstituteId(instituteId);
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        User user = new User();
        user.setEmail(email);
        instituteEmployee.setUser(user);
        Trainee trainee = new Trainee();
        when(instituteEmployeeManager.findByUserEmail(eq(email), eq(instituteId))).thenReturn(Optional.of(instituteEmployee));
        when(instituteManager.existsByInstituteId(eq(instituteId))).thenReturn(true);
        when(traineeManager.findTraineeById(eq(traineeId))).thenReturn(Optional.of(trainee));
        when(loanRequestManager.existsByTrainee(eq(trainee))).thenReturn(false);
        ApiResponse response = traineeService.removeTrainee(request, email);

        assertEquals(GenerateResponse.okResponse("Trainee removed successfully"), response);
        verify(traineeManager, times(1)).deleteTrainee(eq(traineeId));
    }

    @Test
    void removeTrainee_NotAdminException() {
        String email = "nonadmin@example.com";
        String instituteId = "institute123";
        RemoveTraineeRequest request = new RemoveTraineeRequest();
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(new User());

        when(instituteEmployeeManager.findByUserEmail(eq(email), eq(instituteId))).thenReturn(Optional.of(instituteEmployee));

        assertThrows(TraineeException.class, () -> traineeService.removeTrainee(request, email));
        verify(traineeManager, never()).deleteTrainee(anyString());
    }

    @Test
    void removeTrainee_InstituteNotExistException() {
        String email = "admin@example.com";
        String instituteId = "institute123";
        RemoveTraineeRequest request = new RemoveTraineeRequest();

        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(new User());

        when(instituteEmployeeManager.findByUserEmail(eq(email), eq(instituteId))).thenReturn(Optional.of(instituteEmployee));
        when(instituteManager.existsByInstituteId(eq(instituteId))).thenReturn(false);
        assertThrows(TraineeException.class, () -> traineeService.removeTrainee(request, email),
                INSTITUTE_DOES_NOT_EXIST);
        verify(traineeManager, never()).deleteTrainee(anyString());
    }

    @Test
    void removeTrainee_TraineeNotExistException() {
        String email = "admin@example.com";
        String instituteId = "institute123";
        String traineeId = "trainee123";
        RemoveTraineeRequest request = new RemoveTraineeRequest();
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(new User());

        when(instituteEmployeeManager.findByUserEmail(eq(email), eq(instituteId))).thenReturn(Optional.of(instituteEmployee));
        when(instituteManager.existsByInstituteId(eq(instituteId))).thenReturn(true);
        when(traineeManager.findTraineeById(eq(traineeId))).thenReturn(Optional.empty());
        assertThrows(TraineeException.class, () -> traineeService.removeTrainee(request, email),
                TRAINEE_DOES_NOT_EXIST);
        verify(traineeManager, never()).deleteTrainee(anyString());
    }

    @Test
    void removeTrainee_TraineeHasLoanRequestException() {
        String email = "admin@example.com";
        String instituteId = "institute123";
        String traineeId = "trainee123";
        RemoveTraineeRequest request = new RemoveTraineeRequest();
        request.setTraineeId(traineeId);
        request.setInstituteId(instituteId);
        Institute institute = new Institute();
        institute.setInstituteId(instituteId);
        institute.setStatus(INACTIVE);
        User user = new User();
        user.setEmail(email);
        InstituteEmployee employee = new InstituteEmployee();
        employee.setUser(user);
        employee.setInstitute(institute);
        Program program = new Program();
        program.setProgramId("123");
        program.setInstitute(institute);
        Cohort cohort = new Cohort();
        cohort.setCohortId("1234");
        cohort.setProgram(program);
        Trainee trainee = new Trainee();
        trainee.setTraineeId(traineeId);
        trainee.setCohort(cohort);

        when(instituteEmployeeManager.findByUserEmail(email, institute.getInstituteId()))
                .thenReturn(Optional.of(employee));

        when(instituteManager.existsByInstituteId(eq(instituteId))).thenReturn(true);
        when(traineeManager.findTraineeById(traineeId)).thenReturn(Optional.of(trainee));
        when(loanRequestManager.existsByTrainee(eq(trainee))).thenReturn(true);
        TraineeException exception = assertThrows(TraineeException.class, () -> traineeService.removeTrainee(request, email));
        assertEquals("Trainee with loan request cannot be deleted", exception.getMessage());
    }

    @Test
    void viewTraineeDetails_InstituteNotFound() {
        TraineeDetailsRequest request = new TraineeDetailsRequest();
        request.setInstituteId("institute123");
        request.setProgramId("program123");
        request.setCohortId("cohort123");
        request.setTraineeId("trainee123");
        Cohort cohort = new Cohort();
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);

        when(traineeManager.findTraineeInCohort(eq(request.getTraineeId()), eq(cohort))).thenReturn(Optional.empty());

        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.viewTraineeDetails(request));

        assertEquals("Training institute does not exist", exception.getMessage());

    }
    @Test
    void viewTraineeDetails_TraineeNotFoundInCohort() {
        TraineeDetailsRequest request = new TraineeDetailsRequest();
        request.setInstituteId("institute123");
        request.setProgramId("program123");
        request.setCohortId("cohort123");
        request.setTraineeId("trainee123");
        Cohort cohort = new Cohort();
        Institute institute = new Institute();
        institute.setInstituteId("institute123");
        Program program = new Program();
        cohort.setProgram(program);
        program.setInstitute(institute);
        when(traineeManager.findTraineeInCohort(eq(request.getTraineeId()), eq(cohort))).thenReturn(Optional.empty());
        when(instituteManager.findInstituteById(eq(request.getInstituteId()))).thenReturn(Optional.of(institute));
        when(programManager.findProgramByProgramIdAndInstitute(eq(request.getProgramId()), eq(institute)))
                .thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(eq(request.getCohortId()), eq(program)))
                .thenReturn(Optional.of(cohort));
        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.viewTraineeDetails(request));

        assertEquals(TRAINEE_DOES_NOT_EXIST, exception.getMessage());
    }
    @Test
    public void testViewTraineeDetails_CohortNotInProgram() {
        TraineeDetailsRequest request = new TraineeDetailsRequest();
        request.setInstituteId("institute123");
        request.setProgramId("program123");
        request.setCohortId("cohort123");
        request.setTraineeId("trainee123");

        Institute institute = new Institute();
        Program program = new Program();

        when(instituteManager.findInstituteById(any())).thenReturn(java.util.Optional.of(institute));
        when(programManager.findProgramByProgramIdAndInstitute(any(), any())).thenReturn(java.util.Optional.of(program));
        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.viewTraineeDetails(request));
        assertEquals(COHORT_IS_NOT_PART_OF_PROGRAM, exception.getMessage());
    }
    @Test
    void testReferTraineeAsAdmin() {
        ReferTraineeRequest referTraineeRequest = new ReferTraineeRequest();
        referTraineeRequest.setInstituteId("1");
        referTraineeRequest.setTraineeId("123");

        InstituteEmployee instituteEmployee = new InstituteEmployee();
        User user2 = new User();
        user2.setEmail("admin@example.com");
        instituteEmployee.setUser(user2);

        when(instituteEmployeeManager.findByUserEmail("admin@example.com", "1"))
                .thenReturn(Optional.of(instituteEmployee));

        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("trainee@example.com");
        trainee.setUser(user);
        when(traineeManager.findTraineeById("123")).thenReturn(Optional.of(trainee));

        LoanRequest loanRequest = new LoanRequest();
        when(mapper.map(eq(referTraineeRequest), eq(LoanRequest.class))).thenReturn(loanRequest);

        ApiResponse response = traineeService.referTrainee(referTraineeRequest, "admin@example.com");

        assertEquals("Trainee referred successfully", response.getData());

        verify(loanRequestManager, times(1)).createLoanRequest(loanRequest);
    }
    @Test
    void testReferTraineeAsNonAdmin() {
        ReferTraineeRequest referTraineeRequest = new ReferTraineeRequest();
        referTraineeRequest.setInstituteId("1");

        when(instituteEmployeeManager.findByUserEmail("11@fkff.com", "11")).thenReturn(Optional.of(new InstituteEmployee()));
        TraineeException exception  = assertThrows(TraineeException.class, () -> {
            traineeService.referTrainee(referTraineeRequest, "nonadmin@example.com");
        });
        assertEquals("You are not an employee of this institute", exception.getMessage());

        verify(loanRequestManager, never()).createLoanRequest(any(LoanRequest.class));
    }


    @Test
    public void testInviteTraineeUserNotFound () {
        InviteTraineeDto inviteTraineeDto = new InviteTraineeDto();
        inviteTraineeDto.setTraineeEmail("dddd");
        inviteTraineeDto.setInstituteId("1");

        when(instituteEmployeeManager.findByUserEmail("!", "ddld")).
                thenReturn(Optional.of(new InstituteEmployee()));

        when(userManager.findByEmail("nonexistent@example.com")).thenReturn(null);

        when(tokenGenerator.generateToken("nonexistent@example.com")).thenReturn("test-token");

        doNothing().when(notificationController).sendEmail(any());

        assertThrows(TraineeException.class, () -> {
            traineeService.inviteTrainee(inviteTraineeDto, "admin@example.com");
        });
    }
    @Test
    void addTraineeToCohort_Unauthorized () {
        String email = "nonadmin@example.com";
        AddTraineeRequest addTraineeRequest = new AddTraineeRequest();
        addTraineeRequest.setInstituteId("institute123");
        addTraineeRequest.setProgramId("program123");
        addTraineeRequest.setCohortId("cohort123");
        addTraineeRequest.setFirstName("John");
        addTraineeRequest.setLastName("Doe");
        addTraineeRequest.setEmailAddress("john.doe@example.com");
        when(instituteEmployeeManager.findByUserEmail(eq(email), eq(addTraineeRequest.getInstituteId())))
                .thenReturn(Optional.empty());
        assertThrows(TraineeException.class, () -> traineeService.addTraineeToCohort(addTraineeRequest, email));
        verifyNoInteractions(instituteManager, cohortManager, programManager, userManager, traineeManager, keyCloakUserService);
    }

    @Test
    void testAddTraineeToCohort_InActiveInstitute(){
        AddTraineeRequest request = new AddTraineeRequest();
        request.setFirstName("Habeeb");
        request.setLastName("Ahmad");
        request.setEmailAddress("abeebahmad@gmail.com");
        request.setCohortId("1234");
        request.setProgramId("123");
        request.setInstituteId("245");
        Institute institute = new Institute();
        institute.setInstituteId("245");
        institute.setStatus(INACTIVE);
        User user = new User();
        user.setEmail("admin@gmail.com");
        InstituteEmployee employee = new InstituteEmployee();
        employee.setUser(user);
        employee.setInstitute(institute);
        Program program = new Program();
        program.setProgramId("123");
        program.setInstitute(institute);
        Cohort cohort = new Cohort();
        cohort.setCohortId("1234");
        cohort.setProgram(program);
        Trainee trainee = new Trainee();
        trainee.setCohort(cohort);

        when(instituteEmployeeManager.findByUserEmail("admin@gmail.com", institute.getInstituteId()))
                .thenReturn(Optional.of(employee));
        when(instituteManager.findInstituteById(request.getInstituteId()))
                .thenReturn(Optional.of(institute));
        when(traineeManager.findTraineeByEmail("trainee@gmail.com")).thenReturn(false);
        when(programManager.findProgramByProgramIdAndInstitute(program.getProgramId(), institute))
                .thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(cohort.getCohortId(), program))
                .thenReturn(Optional.of(cohort));
        when(cohortManager.findById(cohort.getCohortId())).thenReturn(Optional.of(cohort));
        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.addTraineeToCohort(request, employee.getUser().getEmail()));
        assertEquals(INSTITUTE_IS_NOT_ACTIVE, exception.getMessage());
    }
    @Test
    void testAddTraineeToCohort_InvalidFirstName(){
        AddTraineeRequest request = new AddTraineeRequest();
        request.setFirstName("");
        request.setLastName("Ahmad");
        request.setEmailAddress("abeebahmad@gmail.com");
        request.setCohortId("1234");
        request.setProgramId("123");
        request.setInstituteId("245");
        Institute institute = new Institute();
        institute.setInstituteId("245");
        institute.setStatus(ACTIVE);
        User user = new User();
        user.setEmail("admin@gmail.com");
        InstituteEmployee employee = new InstituteEmployee();
        employee.setUser(user);
        employee.setInstitute(institute);
        Program program = new Program();
        program.setProgramId("123");
        program.setInstitute(institute);
        Cohort cohort = new Cohort();
        cohort.setCohortId("1234");
        cohort.setProgram(program);
        Trainee trainee = new Trainee();
        trainee.setCohort(cohort);

        when(instituteEmployeeManager.findByUserEmail("admin@gmail.com", institute.getInstituteId()))
                .thenReturn(Optional.of(employee));
        when(instituteManager.findInstituteById(request.getInstituteId()))
                .thenReturn(Optional.of(institute));
        when(traineeManager.findTraineeByEmail("trainee@gmail.com")).thenReturn(false);
        when(programManager.findProgramByProgramIdAndInstitute(program.getProgramId(), institute))
                .thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(cohort.getCohortId(), program))
                .thenReturn(Optional.of(cohort));
        when(cohortManager.findById(cohort.getCohortId())).thenReturn(Optional.of(cohort));
        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.addTraineeToCohort(request, employee.getUser().getEmail()));
        assertEquals("First name cannot be empty or blank", exception.getMessage());
    }

    @Test
    void testAddTraineeToCohort_InvalidLastName(){
        AddTraineeRequest request = new AddTraineeRequest();
        request.setFirstName("Habeeb");
        request.setLastName("");
        request.setEmailAddress("abeebahmad@gmail.com");
        request.setCohortId("1234");
        request.setProgramId("123");
        request.setInstituteId("245");
        Institute institute = new Institute();
        institute.setInstituteId("245");
        institute.setStatus(ACTIVE);
        User user = new User();
        user.setEmail("admin@gmail.com");
        InstituteEmployee employee = new InstituteEmployee();
        employee.setUser(user);
        employee.setInstitute(institute);
        Program program = new Program();
        program.setProgramId("123");
        program.setInstitute(institute);
        Cohort cohort = new Cohort();
        cohort.setCohortId("1234");
        cohort.setProgram(program);
        Trainee trainee = new Trainee();
        trainee.setCohort(cohort);

        when(instituteEmployeeManager.findByUserEmail("admin@gmail.com", institute.getInstituteId()))
                .thenReturn(Optional.of(employee));
        when(instituteManager.findInstituteById(request.getInstituteId()))
                .thenReturn(Optional.of(institute));
        when(traineeManager.findTraineeByEmail("trainee@gmail.com")).thenReturn(false);
        when(programManager.findProgramByProgramIdAndInstitute(program.getProgramId(), institute))
                .thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(cohort.getCohortId(), program))
                .thenReturn(Optional.of(cohort));
        when(cohortManager.findById(cohort.getCohortId())).thenReturn(Optional.of(cohort));
        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.addTraineeToCohort(request, employee.getUser().getEmail()));
        assertEquals("Last name cannot be empty or blank", exception.getMessage());
    }
    @Test
    void testAddToCohort_InvalidEmail(){
        AddTraineeRequest request = new AddTraineeRequest();
        request.setFirstName("djdjdj");
        request.setLastName("Ahmad");
        request.setEmailAddress("abeebahmad@");
        request.setCohortId("1234");
        request.setProgramId("123");
        request.setInstituteId("245");
        Institute institute = new Institute();
        institute.setInstituteId("245");
        institute.setStatus(ACTIVE);
        User user = new User();
        user.setEmail("admin@gmail.com");
        InstituteEmployee employee = new InstituteEmployee();
        employee.setUser(user);
        employee.setInstitute(institute);
        Program program = new Program();
        program.setProgramId("123");
        program.setInstitute(institute);
        Cohort cohort = new Cohort();
        cohort.setCohortId("1234");
        cohort.setProgram(program);
        Trainee trainee = new Trainee();
        trainee.setCohort(cohort);

        when(instituteEmployeeManager.findByUserEmail("admin@gmail.com", institute.getInstituteId()))
                .thenReturn(Optional.of(employee));
        when(instituteManager.findInstituteById(request.getInstituteId()))
                .thenReturn(Optional.of(institute));
        when(traineeManager.findTraineeByEmail("trainee@gmail.com")).thenReturn(false);
        when(programManager.findProgramByProgramIdAndInstitute(program.getProgramId(), institute))
                .thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(cohort.getCohortId(), program))
                .thenReturn(Optional.of(cohort));
        when(cohortManager.findById(cohort.getCohortId())).thenReturn(Optional.of(cohort));
        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.addTraineeToCohort(request, employee.getUser().getEmail()));
        assertEquals("Provide a valid email address", exception.getMessage());
    }

    @Test
    void addTraineeToCohort_ExistingTrainee () {
        String email = "admin@example.com";
        AddTraineeRequest request = new AddTraineeRequest();
        request.setFirstName("Habeeb");
        request.setLastName("Ahmad");
        request.setEmailAddress("abeebahmad@gmail.com");
        request.setCohortId("1234");
        request.setProgramId("123");
        request.setInstituteId("245");
        Institute institute = new Institute();
        institute.setInstituteId("245");
        institute.setStatus(ACTIVE);
        User user = new User();
        user.setEmail(email);
        InstituteEmployee employee = new InstituteEmployee();
        employee.setUser(user);
        employee.setInstitute(institute);
        Program program = new Program();
        program.setProgramId("123");
        program.setInstitute(institute);
        Cohort cohort = new Cohort();
        cohort.setCohortId("1234");
        cohort.setProgram(program);
        Trainee trainee = new Trainee();
        trainee.setCohort(cohort);

        when(instituteEmployeeManager.findByUserEmail(email, institute.getInstituteId()))
                .thenReturn(Optional.of(employee));
        when(instituteEmployeeManager.findByUserEmail(eq(email), eq(request.getInstituteId())))
                .thenReturn(Optional.of(employee));
        when(instituteManager.findInstituteById(eq(request.getInstituteId()))).thenReturn(Optional.of(institute));
        when(cohortManager.findById(eq(request.getCohortId()))).thenReturn(Optional.of(cohort));
        when(programManager.findProgramByProgramIdAndInstitute(eq(request.getProgramId()), eq(institute)))
                .thenReturn(Optional.of(program));
        when(userManager.userExistsByEmail(eq(request.getEmailAddress()))).thenReturn(true);
        when(traineeManager.trainingExistsInCohort(eq(request.getEmailAddress()))).thenReturn(true);

        TraineeException exception =assertThrows(TraineeException.class,
                () -> traineeService.addTraineeToCohort(request, email));
        assertEquals("This email has been used by a user", exception.getMessage());
    }
    @Test
    void testViewTraineeLoanPerformance_Success() {
        ViewLoanPerformanceDto loanPerformanceDto = createValidLoanPerformanceDto();
        Institute institute = new Institute();
        Program program = new Program();
        Cohort cohort = new Cohort();
        Trainee trainee = new Trainee();
        TraineeLoanDetail traineeLoanDetail = new TraineeLoanDetail();
        traineeLoanDetail.setTrainee(trainee);

        when(instituteManager.findByInstituteId(any())).thenReturn(institute);
        when(programManager.findProgramByProgramIdAndInstitute(any(), any())).thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(any(), any())).thenReturn(Optional.of(cohort));
        when(traineeManager.findTraineeById(any())).thenReturn(Optional.of(trainee));
        when(traineeLoanDetailManager.findLoanDetailByTrainee(trainee)).thenReturn(traineeLoanDetail);
        when(traineeManager.findTraineeById(any())).thenReturn(Optional.of(trainee));
        LoanPerformanceDto result = traineeService.viewTraineeLoanPerformance(loanPerformanceDto);

        assertNotNull(result);
        verify(instituteManager, times(1)).findByInstituteId(any());
        verify(programManager, times(1)).findProgramByProgramIdAndInstitute(any(), any());
        verify(cohortManager, times(1)).findByCohortIdAndInstituteProgram(any(), any());
        verify(traineeManager, times(1)).findTraineeById(any());
        verify(traineeLoanDetailManager, times(1)).findLoanDetailByTrainee(trainee);
    }

    @Test
    public void testViewTraineeLoanPerformanceCohortNotPartOfProgram() {
        ViewLoanPerformanceDto loanPerformanceDto = createValidLoanPerformanceDto();
        Institute institute = createInstitute();
        Program program = createProgram();
        when(instituteManager.findByInstituteId(any())).thenReturn(institute);
        when(programManager.findProgramByProgramIdAndInstitute(any(), any())).thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(any(), any())).thenReturn(Optional.of(new Cohort()));

        assertThrows(TraineeException.class, () -> traineeService.viewTraineeLoanPerformance(loanPerformanceDto));
    }

    @Test
    public void testViewTraineeLoanPerformanceInstituteDoesNotOfferProgram() {
        ViewLoanPerformanceDto loanPerformanceDto = createValidLoanPerformanceDto();
        Institute institute = createInstitute();
        when(instituteManager.findByInstituteId(any())).thenReturn(institute);
        when(programManager.findProgramByProgramIdAndInstitute(any(), any())).thenReturn(Optional.of(new Program()));

        assertThrows(TraineeException.class, () -> traineeService.viewTraineeLoanPerformance(loanPerformanceDto));
    }

    @Test
    public void testViewTraineeLoanPerformanceTraineeDoesNotExist() {
        ViewLoanPerformanceDto loanPerformanceDto = createValidLoanPerformanceDto();
        Institute institute = createInstitute();
        Program program = createProgram();
        Cohort cohort = createCohort();
        when(instituteManager.findByInstituteId("234")).thenReturn(institute);
        when(programManager.findProgramByProgramIdAndInstitute(any(), any())).thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(any(), any())).thenReturn(Optional.of(cohort));
        when(traineeManager.findTraineeById("1444")).thenReturn(Optional.empty());

        assertThrows(TraineeException.class, () -> traineeService.viewTraineeLoanPerformance(loanPerformanceDto));
    }

    @Test
    public void testViewTraineeLoanPerformanceTraineeHasNoLoanDetails() {
        ViewLoanPerformanceDto loanPerformanceDto = createValidLoanPerformanceDto();
        Institute institute = createInstitute();
        Program program = createProgram();
        Cohort cohort = createCohort();
        Trainee trainee = createTrainee();
        when(instituteManager.findByInstituteId(any())).thenReturn(institute);
        when(programManager.findProgramByProgramIdAndInstitute(any(), any())).thenReturn(Optional.of(program));
        when(cohortManager.findByCohortIdAndInstituteProgram(any(), any())).thenReturn(Optional.of(cohort));
        when(traineeManager.findTraineeById(any())).thenReturn(Optional.of(trainee));
        when(traineeLoanDetailManager.findLoanDetailByTrainee(any())).thenReturn(null);

        assertThrows(TraineeException.class, () -> traineeService.viewTraineeLoanPerformance(loanPerformanceDto));
    }
    @Test
    void testEditTraineeDetailsUnauthorizedUser() {
        String email = "non_employee@gmail.com";
        EditTraineeRequest editTraineeRequest = new EditTraineeRequest();
        editTraineeRequest.setInstituteId("123");
        editTraineeRequest.setTraineeId("456");
        editTraineeRequest.setEmail("new_email@gmail.com");
        editTraineeRequest.setFirstName("New");
        editTraineeRequest.setLastName("Name");

        when(instituteEmployeeManager.findByUserEmail(email, editTraineeRequest.getInstituteId()))
                .thenReturn(Optional.empty());

        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.editTraineeDetails(editTraineeRequest, email));
        assertEquals("UNAUTHORIZED", exception.getMessage());
    }
    @Test
    void testEditTraineeDetailsBlankFirstName() {
        String email = "admin@gmail.com";
        EditTraineeRequest editTraineeRequest = new EditTraineeRequest();
        editTraineeRequest.setInstituteId("123");
        editTraineeRequest.setTraineeId("456");
        editTraineeRequest.setEmail("new_email@gmail.com");
        editTraineeRequest.setFirstName("");
        editTraineeRequest.setLastName("Name");

        User user = new User();
        user.setEmail(email);
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(user);
        when(instituteEmployeeManager.findByUserEmail(email, editTraineeRequest.getInstituteId()))
                .thenReturn(Optional.of(instituteEmployee));

        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.editTraineeDetails(editTraineeRequest, email));
        assertEquals("First name cannot be empty or blank", exception.getMessage());
    }
    @Test
    void testEditTraineeDetailsBlankLastName() {
        String email = "admin@gmail.com";
        EditTraineeRequest editTraineeRequest = new EditTraineeRequest();
        editTraineeRequest.setInstituteId("123");
        editTraineeRequest.setTraineeId("456");
        editTraineeRequest.setEmail("new_email@gmail.com");
        editTraineeRequest.setFirstName("New");
        editTraineeRequest.setLastName("");
        User user = new User();
        user.setEmail(email);
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(user);
        when(instituteEmployeeManager.findByUserEmail(email, editTraineeRequest.getInstituteId()))
                .thenReturn(Optional.of(instituteEmployee));

       TraineeException exception =  assertThrows(TraineeException.class,
                () -> traineeService.editTraineeDetails(editTraineeRequest, email));
        assertEquals("Last name cannot be empty or blank", exception.getMessage());
    }
    @Test
    void testEditTraineeDetail_TraineeDoesNotExist() {
        String email = "admin@gmail.com";
        EditTraineeRequest editTraineeRequest = new EditTraineeRequest();
        editTraineeRequest.setInstituteId("123");
        editTraineeRequest.setTraineeId("456");
        editTraineeRequest.setEmail("new_email@gmail.com");
        editTraineeRequest.setFirstName("New");
        editTraineeRequest.setLastName("Name");
        User user = new User();
        user.setEmail(email);
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(user);
        when(instituteEmployeeManager.findByUserEmail(email, editTraineeRequest.getInstituteId()))
                .thenReturn(Optional.of(instituteEmployee));

        TraineeException exception =  assertThrows(TraineeException.class,
                () -> traineeService.editTraineeDetails(editTraineeRequest, email));
        assertEquals("Trainee does not exist", exception.getMessage());
    }
    @Test
    void testEditTraineeDetail_UserEmailExist() {
        String email = "admin@gmail.com";
        EditTraineeRequest editTraineeRequest = new EditTraineeRequest();
        editTraineeRequest.setInstituteId("123");
        editTraineeRequest.setTraineeId("456");
        editTraineeRequest.setEmail("new_email@gmail.com");
        editTraineeRequest.setFirstName("New");
        editTraineeRequest.setLastName("Name");
        User user = new User();
        user.setEmail(email);
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(user);
        when(instituteEmployeeManager.findByUserEmail(email, editTraineeRequest.getInstituteId()))
                .thenReturn(Optional.of(instituteEmployee));
        when(userManager.userExistsByEmail(editTraineeRequest.getEmail())).thenReturn(true);
        TraineeException exception =  assertThrows(TraineeException.class,
                () -> traineeService.editTraineeDetails(editTraineeRequest, email));
        assertEquals("This email has been used by a user", exception.getMessage());
    }
    @Test
    void testEditTraineeDetail_Success() {
        String email = "admin@gmail.com";
        EditTraineeRequest editTraineeRequest = new EditTraineeRequest();
        editTraineeRequest.setInstituteId("123");
        editTraineeRequest.setTraineeId("456");
        editTraineeRequest.setEmail("new_email@gmail.com");
        editTraineeRequest.setFirstName("New");
        editTraineeRequest.setLastName("Name");
        User user = new User();
        user.setEmail(email);
        Trainee trainee = new Trainee();
        trainee.setTraineeId(editTraineeRequest.getTraineeId());
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(user);
        when(instituteEmployeeManager.findByUserEmail(email, editTraineeRequest.getInstituteId()))
                .thenReturn(Optional.of(instituteEmployee));
        when(userManager.userExistsByEmail(editTraineeRequest.getEmail())).thenReturn(false);
        when(traineeManager.findTraineeById(editTraineeRequest.getTraineeId())).thenReturn(Optional.of(trainee));
        ApiResponse response = traineeService.editTraineeDetails(editTraineeRequest, email);
        assertSame(response.getData(), "Trainee details updated successfully");
    }
    @Test
    void testEditTraineeDetailsInvalidEmail() {
        String email = "admin@gmail.com";
        EditTraineeRequest editTraineeRequest = new EditTraineeRequest();
        editTraineeRequest.setInstituteId("123");
        editTraineeRequest.setTraineeId("456");
        editTraineeRequest.setEmail("invalid_email");
        editTraineeRequest.setFirstName("New");
        editTraineeRequest.setLastName("Name");
        User user = new User();
        user.setEmail(email);
        InstituteEmployee instituteEmployee = new InstituteEmployee();
        instituteEmployee.setUser(user);
        when(instituteEmployeeManager.findByUserEmail(email, editTraineeRequest.getInstituteId()))
                .thenReturn(Optional.of(instituteEmployee));
        TraineeException exception = assertThrows(TraineeException.class,
                () -> traineeService.editTraineeDetails(editTraineeRequest, email));
        assertEquals("Provide a valid email address", exception.getMessage());
    }

    private ViewLoanPerformanceDto createValidLoanPerformanceDto() {
        ViewLoanPerformanceDto dto = new ViewLoanPerformanceDto();
        dto.setInstituteId("123");
        dto.setProgramId("456");
        dto.setCohortId("789");
        dto.setTraineeId("101");
        return dto;
    }

    private Institute createInstitute() {
        Institute institute = new Institute();
        institute.setInstituteName("Test Institute");
        return institute;
    }

    private Program createProgram() {
        Program program = new Program();
        program.setProgramId("456");
        return program;
    }

    private Cohort createCohort() {
        Cohort cohort = new Cohort();
        cohort.setCohortId("789");
        return cohort;
    }

    private Trainee createTrainee() {
        Trainee trainee = new Trainee();
        trainee.setTraineeId("101");
        return trainee;
    }
}