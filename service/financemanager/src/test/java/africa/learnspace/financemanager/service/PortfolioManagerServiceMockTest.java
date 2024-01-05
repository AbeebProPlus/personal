package africa.learnspace.financemanager.service;

import africa.learnspace.financemanager.data.dto.request.PortfolioManagerRequest;
import africa.learnspace.financemanager.data.dto.response.ApiResponse;
import africa.learnspace.financemanager.data.dto.response.InstituteResponse;
import africa.learnspace.common.responses.PaginatedResponse;
import africa.learnspace.financemanager.exception.PortfolioManagerException;
import africa.learnspace.loan.manager.InstituteManager;
import africa.learnspace.loan.models.institute.Institute;
import africa.learnspace.notification.controller.NotificationController;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.usermanagement.iam.TokenGenerator;
import africa.learnspace.usermanagement.services.KeyCloakServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import africa.learnspace.loan.manager.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PortfolioManagerServiceMockTest {
    @InjectMocks
    private PortfolioManagerServiceImpl portfolioManagerService;
    @Mock
    private KeyCloakServiceImpl keyCloakUserService;
    @Mock
    private InstituteManager instituteManager;

    @Mock

    private InstituteEmployeeManager instituteEmployeeManager;
    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private  NotificationController controller;
    @Mock
    private UserManager userManager;
    @Mock
    private ModelMapper modelMapper;

    private PortfolioManagerRequest request;

    @BeforeEach
    void setUp() {
        request = new PortfolioManagerRequest();
        request.setInstituteName("Semicolon");
        request.setInstituteAdminFirstName("Patience");
        request.setInstituteAdminLastName("Tommy");
        request.setInstituteAdminEmail("Patience@example.com");
    }




    @Test
    void inviteTrainingInstitute_InstituteNameAlreadyExists_Failure() {
        when(instituteManager.existsByInstituteName(request.getInstituteName())).thenReturn(true);
        assertThrows(PortfolioManagerException.class,
                () -> portfolioManagerService.inviteTrainingInstitute(request),
                "Institute Name Already Exists"
        );
    }

    @Test
    void inviteTrainingInstitute_AdminEmailAlreadyExists_Failure() {
        when(instituteEmployeeManager.existsByAdminById(request.getInstituteAdminEmail())).thenReturn(true);

        assertThrows(PortfolioManagerException.class,
                () -> portfolioManagerService.inviteTrainingInstitute(request),
                "Training Institute Admin Email Already Exists"
        );
    }


    @Test
    void portfolioManagerCanViewAllTrainingInstitute() {
        List<Institute> sampleInstitute = new ArrayList<>();
        Page<Institute> samplePage = new PageImpl<>(sampleInstitute);
        PageRequest pageRequest = PageRequest.of(0,5);
        PortfolioManagerException exception = assertThrows(PortfolioManagerException.class, ()->{
            PaginatedResponse<InstituteResponse> response = portfolioManagerService.viewAllTrainingInstitutes(pageRequest);
        });

        assertEquals("No Institute found.", exception.getMessage());
    }



    @Test
    void viewTrainingInstitutes_InstituteNotFound_ThrowsEntityNotFoundException() {
        String instituteId = "nonexistentId";
        when(instituteManager.findByInstituteId(instituteId)).thenReturn(null);

        assertThrows(PortfolioManagerException.class,
                () -> portfolioManagerService.viewTrainingInstitutes(instituteId)
        );
    }

    @Test
    void deActivateTrainingInstitute_InstituteFound_ReturnsSuccessResponse() {
        String instituteId = "123";
        Institute mockInstitute = new Institute();
        mockInstitute.setInstituteId(instituteId);
        when(instituteManager.findByInstituteId(instituteId)).thenReturn(mockInstitute);

        ApiResponse response = portfolioManagerService.deactivateTrainingInstitute(instituteId);

        assertNotNull(response);
        assertEquals("Training Institute Deactivated Successfully", response.getMessage());
        assertTrue(response.getIsSuccessful());
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void deActivateTrainingInstitute_InstituteNotFound_ThrowsEntityNotFoundException() {
        String instituteId = "nonexistentId";
        when(instituteManager.findByInstituteId(instituteId)).thenReturn(null);

        PortfolioManagerException exception = assertThrows(PortfolioManagerException.class,
                () -> portfolioManagerService.deactivateTrainingInstitute(instituteId)
        );
        assertEquals("Institute not found with ID: " + instituteId, exception.getMessage());
    }
}