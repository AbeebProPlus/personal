//package africa.learnspace.financemanager.service;
//
//import africa.learnspace.financemanager.data.dto.response.FundResponse;
//import africa.learnspace.financemanager.data.dto.response.PaginatedResponse;
//import africa.learnspace.financemanager.exception.PortfolioManagerException;
//import africa.learnspace.investment.manager.FundProductManager;
//import africa.learnspace.investment.model.FundProduct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FundProductServiceImpl implements FundProductService{
//
//    private final ModelMapper modelMapper;
//
//    private final FundProductManager fundProductManager;
//
//    @Override
//    public PaginatedResponse<FundResponse> viewAllFundProducts(PageRequest pageRequest) {
//        Page<FundProduct> fundProducts = fundProductManager.viewAllFundProduct(pageRequest);
//        if (fundProducts == null) {
//            throw new PortfolioManagerException("No fund products found.");
//        }
//        List<FundProduct> response = fundProducts.getContent();
//        log.info("response {}",response);
//        List<FundResponse> fundResponse = response.stream()
//                .map(fundProduct -> modelMapper.map(fundProduct, FundResponse.class))
//                .collect(Collectors.toList());
//        return PaginatedResponse.<FundResponse>builder()
//                .content(fundResponse)
//                .pageNo(fundProducts.getNumber())
//                .pageSize(fundProducts.getSize())
//                .total(fundProducts.getTotalPages())
//                .last(fundProducts.isLast())
//                .build();
//    }
//
//    @Override
//    public FundResponse viewFundProduct(String id) {
//        log.info("Request received to view Fund Product with ID: {}", id);
//        FundProduct fundProduct = fundProductManager.findByFundProductById(id);
//        log.info("Good morning : {}", id);
//        if (fundProduct == null) {
//            throw new PortfolioManagerException("Fund Product not found with ID: " + id);
//        }
//        return modelMapper.map(fundProduct, FundResponse.class);
//    }
//}
