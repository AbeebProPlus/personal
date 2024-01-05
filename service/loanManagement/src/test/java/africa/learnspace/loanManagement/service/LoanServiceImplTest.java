package africa.learnspace.loanManagement.service;

import africa.learnspace.loan.manager.LoanOfferManager;
import africa.learnspace.loan.manager.LoanRequestManager;
import africa.learnspace.loan.manager.TraineeManager;
import africa.learnspace.loan.models.trainee.*;
import africa.learnspace.loan.utils.LoanException;
import africa.learnspace.loanManagement.data.request.CreatLoanOfferRequest;
import africa.learnspace.loanManagement.data.response.LoanRequestResponse;
import africa.learnspace.loanManagement.data.response.ViewAllLoanOfferResponse;
import africa.learnspace.loanManagement.data.response.ViewAllLoanResponse;
import africa.learnspace.loanManagement.data.response.ViewLoanOfferResponse;
import africa.learnspace.loanManagement.utils.LoanMapper;
import africa.learnspace.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LoanServiceImplTest {
    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanRequestManager loanRequestManager;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanOfferManager loanOfferManager;

    @Mock
    private TraineeManager traineeManager;
    private LoanRequest loanRequest;

    private LoanRequestResponse loanRequestResponse;

    private LoanOffer loanOffer;

    private ViewLoanOfferResponse loanOfferResponse;
    private List<LoanRequest> loanRequests;
    private Page<LoanRequest> loanRequests1;

    private PageRequest pageRequest = PageRequest.of(1,10);

    private List<ViewAllLoanResponse> loanRequestResponseList;

    private ViewAllLoanResponse viewAllLoanResponse;

    private ViewAllLoanOfferResponse viewAllLoanOfferResponse;
    private CreatLoanOfferRequest creatLoanOfferRequest;

    private Trainee trainee;

    private LoanProduct loanProduct;


    private Page<LoanOffer> loanOfferPage;


    @BeforeEach
    void setUp() {

        creatLoanOfferRequest = new CreatLoanOfferRequest();
        creatLoanOfferRequest.setLoanAmount(BigDecimal.valueOf(3450000L));
        creatLoanOfferRequest.setLoanAmountApproved(BigDecimal.valueOf(2000000L));
        creatLoanOfferRequest.setLoanTermsAndConditions("You must pay!");
        creatLoanOfferRequest.setMoratorium(5);
        creatLoanOfferRequest.setTenor(2);

        User user = new User();
        user.setVerified(true);
        user.setFirstName("Yomi");
        user.setLastName("Ola");

        trainee = new Trainee();
        trainee.setUser(user);
        trainee.setVerified(true);

         loanProduct = new LoanProduct();
        loanProduct.setTenor(creatLoanOfferRequest.getTenor());
        loanProduct.setInterestRate(creatLoanOfferRequest.getInterestRate());
        loanProduct.setLoanAmountApproved(creatLoanOfferRequest.getLoanAmountApproved());
        loanProduct.setMoratorium(creatLoanOfferRequest.getMoratorium());

        Loan loan = new Loan();

        loan.setLoanProduct(loanProduct);

        loanRequest = new LoanRequest();
        loanRequest.setLoanRequestId("12345");
        loanRequest.setLoanRequestId("12345");
        loanRequest.setStatus(LoanRequestStatus.APPROVED);
        loanRequest.setTrainee(trainee);


        loanRequestResponse = new LoanRequestResponse();
        loanRequestResponse.setEmail(loanRequest.getTrainee().getUser().getEmail());
        loanRequestResponse.setFirstName(loanRequest.getTrainee().getUser().getFirstName());

        loanOffer = new LoanOffer();
         loanOffer.setLastName(loanRequest.getTrainee().getUser().getLastName());
        loanOffer.setFirstName(loanRequest.getTrainee().getUser().getFirstName());
       loanOffer.setEmail(loanRequest.getTrainee().getUser().getEmail());
        loanOffer.setLoanOfferId("1245678");

        loanOfferResponse  = new ViewLoanOfferResponse();
        loanOfferResponse.setLastName(loanOffer.getLastName());
        loanOfferResponse.setFirstName(loanOffer.getFirstName());
        loanOfferResponse.setEmail(loanOffer.getEmail());

        viewAllLoanResponse =  new ViewAllLoanResponse();
        viewAllLoanResponse.setFirstName(loanRequest.getTrainee().getUser().getFirstName());
        viewAllLoanResponse.setLastName(loanOffer.getLastName());
        loanRequestResponseList = new ArrayList<>();
        loanRequestResponseList.add(viewAllLoanResponse);

        viewAllLoanOfferResponse = new ViewAllLoanOfferResponse();
        viewAllLoanOfferResponse.setFirstName(loanOffer.getFirstName());
        viewAllLoanOfferResponse.setLastName(loanOffer.getLastName());
        viewAllLoanOfferResponse.setEmail(loanOffer.getEmail());

        loanRequests = new ArrayList<>();
        loanRequests.add(loanRequest);
        loanRequests1 = new PageImpl<>(loanRequests);

        List<LoanOffer> loanOffers = new ArrayList<>();
        loanOffers.add(loanOffer);
        loanOfferPage = new PageImpl<>(loanOffers);


        trainee = new Trainee();
        trainee.setTraineeId("128888");
        trainee.setUser(user);

    }

    @Test
    void testThatLoanRequestCanBeView() {
        when(loanRequestManager.findById(anyString())).thenReturn(loanRequest);
        when(loanMapper.mapWithLoanRequestResponse(loanRequest)).thenReturn(loanRequestResponse);
        LoanRequestResponse loanRequestResponse =  (LoanRequestResponse) loanService.viewLoanRequest(loanRequest.getLoanRequestId()).getData();
       assertEquals(loanRequest.getTrainee().getUser().getEmail(),loanRequestResponse.getEmail());
    }
    @Test
    void testThatWhenAnInvalidIdISProvidedErrorISThrown(){
        when(loanRequestManager.findById(anyString())).thenThrow(LoanException.class);
        assertThrows(LoanException.class,() ->loanService.viewLoanRequest("invalid id"));
    }
    @Test
    void testThatWhenAEmptyStringIsProvidedErrorISThrown(){
        assertThrows(LoanException.class,() ->loanService.viewLoanRequest(""));
    }
    @Test
    void testThatWhenAnNullValueIsProvidedErrorISThrown(){
        assertThrows(LoanException.class,() ->loanService.viewLoanRequest(null));
    }


    @Test
    public void testThatALoanOfferCanBeViewed(){
        when(loanOfferManager.findById(anyString())).thenReturn(loanOffer);
        when(loanMapper.mapWithViewLoanOfferResponse(loanOffer)).thenReturn(loanOfferResponse);
        ViewLoanOfferResponse viewLoanOfferResponse = (ViewLoanOfferResponse)  loanService.viewLoanOffer(loanOffer.getLoanOfferId()).getData();
        assertNotNull(viewLoanOfferResponse);
        assertEquals(loanOffer.getFirstName(), viewLoanOfferResponse.getFirstName());
    }
    @Test
    public void testThatErrorIsThrownWhenInvalidLoanOfferIsProvided(){
        when(loanOfferManager.findById(anyString())).thenThrow(LoanException.class);
        assertThrows(LoanException.class, () -> loanService.viewLoanOffer( "Invalid id"));
    }

    @Test
    void testThatWhenAEmptyStringIsProvidedErrorIsThrownFromTheViewLoanOfferMethod(){
        assertThrows(LoanException.class,() ->loanService.viewLoanOffer(""));
    }
    @Test
    void testThatWhenNullValueIsProvidedErrorIsThrownFromTheViewLoanOfferMethod(){
        assertThrows(LoanException.class,() ->loanService.viewLoanOffer(null));
    }

    @Test
    void testThatAnEmptyListIsReturned(){
        when(loanRequestManager.findAll(any(PageRequest.class))).thenReturn(Page.empty());
        List<ViewAllLoanResponse> loanRequestResponseList = (List<ViewAllLoanResponse>) loanService.viewAllLoanRequests(0,10).getData();
        assertTrue(loanRequestResponseList.isEmpty());
    }

    @Test
    void testThatWhenViewALlLoanRequestISCalledAListOfLoanRequestResponseIsReturned(){
        when(loanRequestManager.findAll(any(PageRequest.class))).thenReturn(loanRequests1);
        when(loanMapper.mapWithViewAllLoanRequestResponse(any())).thenReturn(viewAllLoanResponse);
        List<ViewAllLoanResponse> loanRequestResponseList = (List<ViewAllLoanResponse> ) loanService.viewAllLoanRequests(0,1).getData();
        assertFalse(loanRequestResponseList.isEmpty());
        assertEquals(1, loanRequestResponseList.size());
    }

    @Test
    public void testThatLoanOfferCanBeCreated() {
        when(traineeManager.findById(anyString())).thenReturn(trainee);
        when(loanRequestManager.findLoanRequestByTrainee(trainee)).thenReturn(loanRequest);
        when(loanOfferManager.findByLoanOfferByLoanRequest(loanRequest)).thenReturn(null);
        when(loanMapper.mapWithLoanOffer(any(),any(),any())).thenReturn(loanOffer);
        when(loanMapper.mapLoanProduct(any())).thenReturn(loanProduct);
        when(loanOfferManager.creatLoanOffer(any())).thenReturn(loanOffer);
        LoanOffer loanOffer = (LoanOffer) loanService.creatLoanOffer(creatLoanOfferRequest, loanRequest.getLoanRequestId(), trainee.getTraineeId())
                .getData();
        assertNotNull(loanOffer);
        assertEquals(loanRequest.getTrainee().getUser().getFirstName(), loanOffer.getFirstName());
    }

    @Test
    public void testThatLoanOfferCanBeCreatedWhenNullValueIsProvidedForTraineeId() {
        assertThrows(LoanException.class, () -> loanService.creatLoanOffer(creatLoanOfferRequest,loanRequest.getLoanRequestId(), null));
    }
    @Test
    public void testThatLoanOfferCanBeCreatedWhenNullValueIsProvidedForLoanRequestId() {
        assertThrows(LoanException.class, () -> loanService.creatLoanOffer(creatLoanOfferRequest,null, trainee.getTraineeId()));
    }

    @Test
    public void testThatLoanOffersCanNotBeMappedToTheSameLoanRequest() {
        when(traineeManager.findById(anyString())).thenReturn(trainee);
        when(loanRequestManager.findLoanRequestByTrainee(trainee)).thenReturn(loanRequest);
        when(loanOfferManager.findByLoanOfferByLoanRequest(loanRequest)).thenReturn(loanOffer);
        loanOffer = (LoanOffer) loanService.creatLoanOffer(creatLoanOfferRequest, loanRequest.getLoanRequestId(), trainee.getTraineeId()).getData();
       assertEquals(loanOffer.getFirstName(), loanRequest.getTrainee().getUser().getFirstName());
    }


    @Test
    public void testThatLoanOfferCanNotCreatedBeWithInvalidLoanRequestIdProvided(){
        when(traineeManager.findById(anyString())).thenReturn(trainee);
        when(loanRequestManager.findLoanRequestByTrainee(trainee)).thenReturn(loanRequest);
        when(loanOfferManager.findByLoanOfferByLoanRequest(loanRequest)).thenReturn(null);
        assertThrows(LoanException.class,  () -> loanService.creatLoanOffer(creatLoanOfferRequest,"Invalid loan request id","Invalid trainee id"));
    }
    @Test
    public void testThatLoanOfferCanNotCreatedWhenLoanRequestIsNotVerified(){
        loanRequest.getTrainee().setVerified(false);
        when(traineeManager.findById(anyString())).thenReturn(trainee);
        when(loanRequestManager.findLoanRequestByTrainee(trainee)).thenReturn(loanRequest);
        when(loanOfferManager.findByLoanOfferByLoanRequest(loanRequest)).thenReturn(null);
        assertThrows(LoanException.class,  () -> loanService.creatLoanOffer(creatLoanOfferRequest,"Invalid loan request id","Invalid trainee id"));
    }
    @Test
    public void testThatLoanOfferCanNotCreatedWhenLoanRequestIsNotApproved(){
        loanRequest.setStatus(LoanRequestStatus.NEW);
        when(traineeManager.findById(anyString())).thenReturn(trainee);
        when(loanRequestManager.findLoanRequestByTrainee(trainee)).thenReturn(loanRequest);
        when(loanOfferManager.findByLoanOfferByLoanRequest(loanRequest)).thenReturn(null);
        assertThrows(LoanException.class,  () -> loanService.creatLoanOffer(creatLoanOfferRequest,"Invalid loan request id","Invalid trainee id"));
    }

    @Test
    void testThatAllLoanOfferCanBeViewed(){
        when(loanOfferManager.findAll(any(PageRequest.class))).thenReturn(loanOfferPage);
        when(loanMapper.mapWithViewAllLoanOfferResponse(any())).thenReturn(viewAllLoanOfferResponse);
        List<ViewAllLoanOfferResponse> loanOfferResponseList = (List<ViewAllLoanOfferResponse> ) loanService.viewAllLoanOffer(0,1).getData();
        assertFalse(loanOfferResponseList.isEmpty());
        assertEquals(1, loanOfferResponseList.size());
    }
    @Test
    void testThatAnEmptyListIsReturnedWhenNoLoanOfferIsCreatedButViewAllLoanOfferMethodIsCalled(){
        when(loanOfferManager.findAll(any(PageRequest.class))).thenReturn(Page.empty());
        List<ViewAllLoanOfferResponse> loanOfferResponseList = (List<ViewAllLoanOfferResponse>) loanService.viewAllLoanOffer(0,10).getData();
        assertTrue(loanOfferResponseList.isEmpty());
    }

}
