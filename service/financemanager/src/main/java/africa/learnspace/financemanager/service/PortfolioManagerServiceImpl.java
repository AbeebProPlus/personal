package africa.learnspace.financemanager.service;

import africa.learnspace.financemanager.data.dto.request.PortfolioManagerRequest;
import africa.learnspace.financemanager.data.dto.response.ApiResponse;
import africa.learnspace.financemanager.data.dto.response.InstituteResponse;
import africa.learnspace.common.responses.PaginatedResponse;
import africa.learnspace.financemanager.exception.PortfolioManagerException;
import africa.learnspace.loan.manager.InstituteEmployeeManager;
import africa.learnspace.loan.manager.InstituteManager;
import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.loan.models.institute.InstituteEmployee;
import africa.learnspace.loan.models.institute.InstituteStatus;
import africa.learnspace.notification.controller.NotificationController;
import africa.learnspace.notification.data.request.EmailRequest;
import africa.learnspace.notification.data.request.SendEmailRequest;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import africa.learnspace.usermanagement.iam.TokenGenerator;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import africa.learnspace.usermanagement.services.KeyCloakUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioManagerServiceImpl implements PortfolioManagerService {
    private final InstituteManager instituteManager;
    private final InstituteEmployeeManager instituteEmployeeManager;


    private final NotificationController controller;

    private final TokenGenerator tokenGenerator;

    @Value("${baseUrl:http://localhost:3000}")
    private String baseUrl;

    private final UserManager userManager;
    private final KeyCloakUserService keyCloakUserService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public String inviteTrainingInstitute(PortfolioManagerRequest portfolioManagerRequest) {
        String id = portfolioManagerRequest.getInstituteAdminEmail();
        if (id == null || id.isEmpty()) {
            throw new PortfolioManagerException("Admin email cannot be empty.");
        }
        if (!id.contains("@")) {
            throw new PortfolioManagerException("Invalid admin email format. Please provide a valid email.");
        }
        boolean existsByName = instituteManager.existsByInstituteName(portfolioManagerRequest.getInstituteName());
        if (existsByName) {
            throw new PortfolioManagerException("Institute Name Already Exists");
        }
        boolean existsByEmail = instituteEmployeeManager.existsByAdminById(id);
        if (existsByEmail) {
            throw new PortfolioManagerException("Training Institute Admin Email Already Exists");
        }

        Institute newInstitute = mapAndSaveInstitute(portfolioManagerRequest);
        User adminUser = mapAndSaveToUserDatabase(portfolioManagerRequest);
        InstituteEmployee instituteEmployee = InstituteEmployee.builder().institute(newInstitute).user(adminUser).isAdmin(true).build();
        instituteEmployeeManager.save(instituteEmployee);
        createTrainingInstituteAccount(adminUser, newInstitute.getInstituteName());
        return "User created successfully";
    }



    private Institute mapAndSaveInstitute(PortfolioManagerRequest portfolioManagerRequest) {
        Institute institute = new Institute();
        modelMapper.map(portfolioManagerRequest, institute);
        institute.setStatus(InstituteStatus.INACTIVE);
        return instituteManager.save(institute);
    }

    private User mapAndSaveToUserDatabase(PortfolioManagerRequest request){
        User user = new User();
        user.setEmail(request.getInstituteAdminEmail());
        user.setLastName(request.getInstituteAdminLastName());
        user.setFirstName(request.getInstituteAdminFirstName());
        return userManager.saveUser(user);

    }
    private void createTrainingInstituteAccount(User user, String instituteName) {
        UserRegistrationRequest request = new UserRegistrationRequest();
        String token = tokenGenerator.generateToken(user.getEmail());
        String link = baseUrl +"/create-password?token=" + token;
        log.info(token);
        modelMapper.map(user, request);
        request.setRole("INSTITUTE_ADMIN");
        request.setInstituteName(instituteName);
        keyCloakUserService.createClient(instituteName);
        keyCloakUserService.createUser(request);
        SendEmailRequest emailRequest = getSendEmailRequest(request, link);
        controller.sendEmail(emailRequest);
    }

    private SendEmailRequest getSendEmailRequest(UserRegistrationRequest request, String link) {

        log.info("Controller Called");

        Context context = controller.getNameAndLinkContext(link, request.getFirstName());
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setContext(context);
        emailRequest.setSubject("Invitation to LearnSpace");
        emailRequest.setEmail(request.getEmail());
        emailRequest.setTemplate("institute-invitation");
        emailRequest.setFirstName(request.getFirstName());
        log.info(" Email: {}" ,controller.getSendEmailRequest(emailRequest));
        return controller.getSendEmailRequest(emailRequest);
    }

    @Override
    public PaginatedResponse<InstituteResponse> viewAllTrainingInstitutes(PageRequest pageRequest) {
        Page<Institute> institute = instituteManager.viewAllTrainingInstitute(pageRequest);
        if(institute == null){
            throw new PortfolioManagerException("No Institute found.");
        }
        List<Institute> instituteList = institute.getContent();
        log.info("response {}", instituteList);
        List<InstituteResponse> instituteResponses = instituteList.stream()
                .map(instituted -> modelMapper.map(instituted, InstituteResponse.class))
                .toList();
        return PaginatedResponse.<InstituteResponse>builder()
                .content(instituteResponses)
                .pageNo(institute.getNumber())
                .pageSize(institute.getSize())
                .last(institute.isLast())
                .build();

    }

    @Override
    public InstituteResponse viewTrainingInstitutes(String id) {
        log.info("Hello motherFucker: {}", id);

        Institute institute = instituteManager.findByInstituteId(id);
        if (institute == null) {
            throw new PortfolioManagerException("Institute not found with ID: " + id);
        }
        return modelMapper.map(institute, InstituteResponse.class);
    }

    @Override
    public ApiResponse deactivateTrainingInstitute(String institute_Id) {
        Institute institute = instituteManager.findByInstituteId(institute_Id);
        if(institute == null){
            throw new PortfolioManagerException("Institute not found with ID: " + institute_Id);
        }
        institute.setStatus(InstituteStatus.DEACTIVATE);
        instituteManager.save(institute);
        return ApiResponse.builder()
                .message("Training Institute Deactivated Successfully")
                .isSuccessful(true)
                .status(HttpStatus.OK)
                .build();
    }

}