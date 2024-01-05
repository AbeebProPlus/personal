package africa.learnspace.investor.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import africa.learnspace.investment.manager.FundProductManager;
import africa.learnspace.investment.model.FundProduct;
import africa.learnspace.investor.dto.request.FundProductRequest;
import africa.learnspace.investor.dto.response.ApiResponse;
import africa.learnspace.common.exceptions.ResourceNotFoundException;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import africa.learnspace.investor.dto.response.*;
import africa.learnspace.common.responses.PaginatedResponse;
import org.modelmapper.ModelMapper;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FundProductServiceImpl.class})
@ExtendWith(SpringExtension.class)
class FundProductServiceImplTest {
    @MockBean
    private FundProductManager fundProductManager;

    @Autowired
    private FundProductServiceImpl fundProductServiceImpl;

    @MockBean
    private UserManager userManager;

    private User user;
    @MockBean

    private ModelMapper modelMapper;

    private FundProduct fundProduct;
    private User user2;
    private FundProductRequest fundProductRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setDisabled(true);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setRoles(new HashSet<>());
        user.setUserId("42");
        user.setVerified(true);


        fundProduct = new FundProduct();
        fundProduct.setAmountInvested(new BigDecimal("2.3"));
        fundProduct.setCollected(new BigDecimal("2.3"));
        fundProduct.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        fundProduct.setFundProductId("42");
        fundProduct.setInterestRate(10.0d);
        fundProduct.setName("Name");
        fundProduct.setObjective("Objective");
        fundProduct.setRiskProfile("Risk Profile");
        fundProduct.setSize(new BigDecimal("2.3"));
        fundProduct.setStartDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        fundProduct.setTenor(1L);
        Optional<FundProduct> ofResult = Optional.of(fundProduct);
        when(fundProductManager.findFundProductByName(Mockito.<String>any())).thenReturn(ofResult);

        user2 = new User();
        user2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setDisabled(true);
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setRoles(new HashSet<>());
        user2.setUserId("42");
        user2.setVerified(true);
        when(userManager.findByEmail(Mockito.<String>any())).thenReturn(user2);

        fundProductRequest = new FundProductRequest();
        fundProductRequest.setAmountInvested(new BigDecimal("2.3"));
        fundProductRequest.setCollected(new BigDecimal("2.3"));
        fundProductRequest.setInterestRate(10.0d);
        fundProductRequest.setName("Name");
        fundProductRequest.setObjective("Objective");
        fundProductRequest.setRiskProfile("Risk Profile");
        fundProductRequest.setSize(new BigDecimal("2.3"));
        fundProductRequest.setTenor(1L);
    }

    @Test
    void shouldThrowExceptionWhenCreatingFundProductWithExistingName() {
        assertThrows(RuntimeException.class, () -> fundProductServiceImpl.createFundProduct("jane.doe@example.org", fundProductRequest));
        verify(fundProductManager).findFundProductByName(Mockito.<String>any());
        verify(userManager).findByEmail(Mockito.<String>any());
    }

    @Test
    void shouldThrowExceptionWhenCreatingFundProductWithDuplicateName() {
        when(userManager.findByEmail(Mockito.<String>any()))
                .thenThrow(new RuntimeException("Fund Product With this Name Already Exist"));

        assertThrows(RuntimeException.class, () -> fundProductServiceImpl.createFundProduct("jane.doe@example.org", fundProductRequest));
        verify(userManager).findByEmail(Mockito.<String>any());
    }


    @Test
    void shouldCreateFundProductSuccessfully() {
        doNothing().when(fundProductManager).saveFundProduct(Mockito.any());
        when(fundProductManager.findFundProductByName(Mockito.any())).thenReturn(Optional.empty());
        when(userManager.findByEmail(Mockito.any())).thenReturn(user2);

        // test
        ApiResponse actualCreateFundProductResult = fundProductServiceImpl.createFundProduct("jane.doe@example.org", fundProductRequest);

        // Verify that the methods were called as expected
        verify(fundProductManager).findFundProductByName(Mockito.any());
        verify(fundProductManager).saveFundProduct(Mockito.any());
        verify(userManager).findByEmail(Mockito.any());

        // Assert the result
        assertEquals("Fund Product Created Successfully", actualCreateFundProductResult.getData());
        assertTrue(actualCreateFundProductResult.isSuccessful());
    }

    @Test
    void portfolioManagerCanViewAllFundProduct() {
        List<FundProduct> sampleFundProducts = new ArrayList<>();
        Page<FundProduct> samplePage = new PageImpl<>(sampleFundProducts);
        PageRequest pageRequest = PageRequest.of(0, 5);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            PaginatedResponse<FundResponse> fundProducts = fundProductServiceImpl.viewAllFundProducts(pageRequest);
        });
        assertEquals("No fund products found.", exception.getMessage());
    }
    @Test
    void testViewFundProduct() {
        String sampleId = "1";
        FundProduct sampleFundProduct = new FundProduct();
        sampleFundProduct.setFundProductId(sampleId);
        FundResponse sampleResponse = new FundResponse();
        when(fundProductManager.findByFundProductById(sampleId)).thenReturn(sampleFundProduct);
        when(modelMapper.map(sampleFundProduct, FundResponse.class)).thenReturn(sampleResponse);
        FundResponse result = fundProductServiceImpl.viewFundProduct(sampleId);
        verify(fundProductManager).findByFundProductById(sampleId);
        verify(modelMapper).map(sampleFundProduct, FundResponse.class);
        assertNotNull(result);
    }

}
