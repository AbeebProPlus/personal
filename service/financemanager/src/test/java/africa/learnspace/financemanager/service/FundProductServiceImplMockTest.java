//package africa.learnspace.financemanager.service;
//
//import africa.learnspace.financemanager.data.dto.response.FundResponse;
//import africa.learnspace.financemanager.data.dto.response.PaginatedResponse;
//import africa.learnspace.financemanager.exception.PortfolioManagerException;
//import africa.learnspace.investment.manager.FundProductManager;
//import africa.learnspace.investment.model.FundProduct;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
////@ExtendWith(MockitoExtension.class)
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = FundProductServiceImpl.class)
//class FundProductServiceImplMockTest {
//
//    @Autowired
//    private FundProductServiceImpl fundProductService;
//    @MockBean
//    private FundProductManager fundProductManager;
//    @MockBean
//    private ModelMapper modelMapper;
//
//    @Mock
//    private PageRequest pageRequest;
//
//
//
//    @BeforeEach
//    void setUp() {
//
//    }
//
//    @Test
//    void portfolioManagerCanViewAllFundProduct() {
//        List<FundProduct> sampleFundProducts = new ArrayList<>();
//        Page<FundProduct> samplePage = new PageImpl<>(sampleFundProducts);
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        PortfolioManagerException exception = assertThrows(PortfolioManagerException.class, () -> {
//            PaginatedResponse<FundResponse> fundProducts = fundProductService.viewAllFundProducts(pageRequest);
//        });
//        assertEquals("No fund products found.", exception.getMessage());
//    }
//    @Test
//    void testViewFundProduct() {
//        String sampleId = "1";
//        FundProduct sampleFundProduct = new FundProduct();
//        sampleFundProduct.setFundProductId(sampleId);
//        FundResponse sampleResponse = new FundResponse();
//        when(fundProductManager.findByFundProductById(sampleId)).thenReturn(sampleFundProduct);
//        when(modelMapper.map(sampleFundProduct, FundResponse.class)).thenReturn(sampleResponse);
//        FundResponse result = fundProductService.viewFundProduct(sampleId);
//        verify(fundProductManager).findByFundProductById(sampleId);
//        verify(modelMapper).map(sampleFundProduct, FundResponse.class);
//        assertNotNull(result);
//    }
//
//}