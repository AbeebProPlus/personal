package africa.learnspace.investor.service;

import africa.learnspace.cohort.exceptions.InvalidInputException;
import africa.learnspace.common.exceptions.ResourceNotFoundException;
import africa.learnspace.investment.manager.InvestorManager;
import africa.learnspace.investment.model.Investor;
import africa.learnspace.investor.data.request.EditInvestorRequest;
import africa.learnspace.investor.data.response.EditInvestorResponse;
import africa.learnspace.investor.dto.request.AddInvestorRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import africa.learnspace.investor.dto.response.ApiResponse;
import africa.learnspace.investor.mappers.InvestorMapper;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {InvestorServiceImpl.class})
@ExtendWith(SpringExtension.class)
class InvestorServiceImplTest {
    @MockBean
    private InvestorManager investorManager;

    @MockBean
    private InvestorMapper investorMapper;

    @Autowired
    private InvestorServiceImpl investorService;

    @MockBean
    private UserManager userManager;

    @Test
    void testEditInvestorDetails_Success() {
        // Create sample data
        EditInvestorRequest editRequest = new EditInvestorRequest();
        editRequest.setEmail("email@email.com");
        editRequest.setFirstName("jide");

        Investor mockedInvestor = new Investor();
        mockedInvestor.setInvestorId("1");

        // Mocking a User
        User mockedUser = new User();
        mockedUser.setUserId("2");

        // Set the User in the Investor
        mockedInvestor.setUser(mockedUser);

        // Mock behavior of investorManager and userManager
        when(investorManager.findInvestorUserById(any())).thenReturn(Optional.of(mockedInvestor));
        when(userManager.findById(any())).thenReturn(Optional.of(mockedUser));

        // Create a new instance of EditInvestorResponse and set the message
        EditInvestorResponse mockedResponse = EditInvestorResponse.builder().firstName(editRequest.getFirstName())
                .email(editRequest.getEmail()).build();

        // Mock behavior of investorMapper
        when(investorMapper.investorToInvestorResponse(any())).thenReturn(mockedResponse);

        // Test the editInvestorDetails method
        EditInvestorResponse response = investorService.editInvestorDetails("1", editRequest);

        // Verify the interactions and assertions
        verify(investorManager).findInvestorUserById("1");
        verify(userManager).findById("2");
        verify(investorMapper).editInvestorFromEditRequest(editRequest, mockedUser);
        verify(userManager).saveUser(mockedUser);


    }

    @Test
    void testEditInvestorDetails_InvalidInputException() {
        // Test for InvalidInputException when EditInvestorRequest is null
        assertThrows(InvalidInputException.class, () -> investorService.editInvestorDetails("",null));
    }

    @Test
    void testEditInvestorDetails_ResourceNotFoundException() {
        // Test for ResourceNotFoundException when Investor is not found
        EditInvestorRequest editRequest = new EditInvestorRequest();
        editRequest.setLastName("Ola");
        editRequest.setFirstName("Jide");
        editRequest.setEmail("name@email.com");

        // Mocking behavior to simulate Investor not found
        when(investorManager.findInvestorUserById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> investorService.editInvestorDetails("1", editRequest));
    }

    @Test
    void testAddInvestor() {
        when(investorManager.findInvestorByName(Mockito.<String>any())).thenReturn("Investor By Name");

        AddInvestorRequest addInvestorRequest = new AddInvestorRequest();
        addInvestorRequest.setAmountInvested(new BigDecimal("2.3"));
        addInvestorRequest.setEmail("jane.doe@example.org");
        addInvestorRequest.setFirstName("Jane");
        addInvestorRequest.setLastName("Doe");
        addInvestorRequest.setPhoneNumber("6625550144");
        assertThrows(RuntimeException.class,
                () -> investorService.addInvestor("jane.doe@example.org", addInvestorRequest));
        verify(investorManager).findInvestorByName(Mockito.<String>any());
    }


    @Test
    void testAddInvestor2() {
        User user = new User();
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setDisabled(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setPhoneNumber("6625550144");
        user.setRoles(new HashSet<>());
        user.setUserId("42");
        user.setVerified(true);

        Investor investor = new Investor();
        investor.setAmountInvested(new BigDecimal("2.3"));
        investor.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        investor.setInvestorId("42");
        investor.setUser(user);
        when(investorManager.createInvestor(Mockito.<Investor>any())).thenReturn(investor);
        when(investorManager.findInvestorByName(Mockito.<String>any())).thenReturn(null);

        User user2 = new User();
        user2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setDisabled(true);
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setPhoneNumber("6625550144");
        user2.setRoles(new HashSet<>());
        user2.setUserId("42");
        user2.setVerified(true);

        User user3 = new User();
        user3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user3.setDisabled(true);
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setLastName("Doe");
        user3.setPhoneNumber("6625550144");
        user3.setRoles(new HashSet<>());
        user3.setUserId("42");
        user3.setVerified(true);

        User user4 = new User();
        user4.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user4.setDisabled(true);
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setLastName("Doe");
        user4.setPhoneNumber("6625550144");
        user4.setRoles(new HashSet<>());
        user4.setUserId("42");
        user4.setVerified(true);
        Optional<User> ofResult = Optional.of(user4);
        when(userManager.findById(Mockito.<String>any())).thenReturn(ofResult);
        when(userManager.findByEmail(Mockito.<String>any())).thenReturn(user2);
        when(userManager.saveUser(Mockito.<User>any())).thenReturn(user3);

        User user5 = new User();
        user5.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user5.setDisabled(true);
        user5.setEmail("jane.doe@example.org");
        user5.setFirstName("Jane");
        user5.setLastName("Doe");
        user5.setPhoneNumber("6625550144");
        user5.setRoles(new HashSet<>());
        user5.setUserId("42");
        user5.setVerified(true);
        when(investorMapper.mapInvestorUserObject(Mockito.<AddInvestorRequest>any())).thenReturn(user5);

        AddInvestorRequest addInvestorRequest = new AddInvestorRequest();
        addInvestorRequest.setAmountInvested(new BigDecimal("2.3"));
        addInvestorRequest.setEmail("jane.doe@example.org");
        addInvestorRequest.setFirstName("Jane");
        addInvestorRequest.setLastName("Doe");
        addInvestorRequest.setPhoneNumber("6625550144");
        ApiResponse actualAddInvestorResult = investorService.addInvestor("jane.doe@example.org", addInvestorRequest);
        verify(investorManager).createInvestor(Mockito.<Investor>any());
        verify(investorManager).findInvestorByName(Mockito.<String>any());
        verify(investorMapper).mapInvestorUserObject(Mockito.<AddInvestorRequest>any());
        verify(userManager).findByEmail(Mockito.<String>any());
        verify(userManager).findById(Mockito.<String>any());
        verify(userManager).saveUser(Mockito.<User>any());
        assertEquals("Investor Add Successfully", actualAddInvestorResult.getData());
        assertTrue(actualAddInvestorResult.isSuccessful());
    }


    @Test
    void testAddInvestor3() {
        when(investorManager.findInvestorByName(Mockito.<String>any())).thenReturn(null);

        User user = new User();
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setDisabled(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setPhoneNumber("6625550144");
        user.setRoles(new HashSet<>());
        user.setUserId("42");
        user.setVerified(true);
        when(userManager.findByEmail(Mockito.<String>any())).thenReturn(user);
        when(investorMapper.mapInvestorUserObject(Mockito.<AddInvestorRequest>any()))
                .thenThrow(new RuntimeException("Investor Add Successfully"));

        AddInvestorRequest addInvestorRequest = new AddInvestorRequest();
        addInvestorRequest.setAmountInvested(new BigDecimal("2.3"));
        addInvestorRequest.setEmail("jane.doe@example.org");
        addInvestorRequest.setFirstName("Jane");
        addInvestorRequest.setLastName("Doe");
        addInvestorRequest.setPhoneNumber("6625550144");
        assertThrows(RuntimeException.class,
                () -> investorService.addInvestor("jane.doe@example.org", addInvestorRequest));
        verify(investorManager).findInvestorByName(Mockito.<String>any());
        verify(investorMapper).mapInvestorUserObject(Mockito.<AddInvestorRequest>any());
        verify(userManager).findByEmail(Mockito.<String>any());
    }


    @Test
    void testAddInvestor4() {
        when(investorManager.findInvestorByName(Mockito.<String>any())).thenReturn(null);

        User user = new User();
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setDisabled(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setPhoneNumber("6625550144");
        user.setRoles(new HashSet<>());
        user.setUserId("42");
        user.setVerified(true);

        User user2 = new User();
        user2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setDisabled(true);
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setPhoneNumber("6625550144");
        user2.setRoles(new HashSet<>());
        user2.setUserId("42");
        user2.setVerified(true);
        Optional<User> emptyResult = Optional.empty();
        when(userManager.findById(Mockito.<String>any())).thenReturn(emptyResult);
        when(userManager.findByEmail(Mockito.<String>any())).thenReturn(user);
        when(userManager.saveUser(Mockito.<User>any())).thenReturn(user2);

        User user3 = new User();
        user3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user3.setDisabled(true);
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setLastName("Doe");
        user3.setPhoneNumber("6625550144");
        user3.setRoles(new HashSet<>());
        user3.setUserId("42");
        user3.setVerified(true);
        when(investorMapper.mapInvestorUserObject(Mockito.<AddInvestorRequest>any())).thenReturn(user3);

        AddInvestorRequest addInvestorRequest = new AddInvestorRequest();
        addInvestorRequest.setAmountInvested(new BigDecimal("2.3"));
        addInvestorRequest.setEmail("jane.doe@example.org");
        addInvestorRequest.setFirstName("Jane");
        addInvestorRequest.setLastName("Doe");
        addInvestorRequest.setPhoneNumber("6625550144");
        assertThrows(ResourceNotFoundException.class,
                () -> investorService.addInvestor("jane.doe@example.org", addInvestorRequest));
        verify(investorManager).findInvestorByName(Mockito.<String>any());
        verify(investorMapper).mapInvestorUserObject(Mockito.<AddInvestorRequest>any());
        verify(userManager).findByEmail(Mockito.<String>any());
        verify(userManager).findById(Mockito.<String>any());
        verify(userManager).saveUser(Mockito.<User>any());
    }
}
