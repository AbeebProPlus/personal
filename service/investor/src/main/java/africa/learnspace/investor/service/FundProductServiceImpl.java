package africa.learnspace.investor.service;

import africa.learnspace.common.responses.PaginatedResponse;
import africa.learnspace.investment.manager.FundProductManager;
import africa.learnspace.investment.model.FundProduct;
import africa.learnspace.investor.dto.request.FundProductRequest;
import africa.learnspace.investor.dto.response.ApiResponse;
import africa.learnspace.investor.dto.response.GenerateResponse;
import africa.learnspace.investor.dto.response.*;
import africa.learnspace.common.exceptions.ResourceNotFoundException;

import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundProductServiceImpl implements FundProductService{
    private final UserManager userManager;

    private final ModelMapper modelMapper;

    private final FundProductManager fundProductManager;

    @Override
    public ApiResponse createFundProduct(String email, FundProductRequest fundProductRequest) {
        User user = userManager.findByEmail(email);
        Optional<FundProduct> foundFundProduct = fundProductManager.findFundProductByName(fundProductRequest.getName());
        if (foundFundProduct.isPresent()){
            throw new RuntimeException("Fund Product With this Name Already Exist");
        }
        FundProduct fundProduct = createFundProduct(fundProductRequest , user);
        fundProductManager.saveFundProduct(fundProduct);
        return GenerateResponse.createdResponse("Fund Product Created Successfully");
    }

    private static FundProduct createFundProduct(FundProductRequest fundProductRequest, User user) {
        return FundProduct.builder()
                .name(fundProductRequest.getName())
                .objective(fundProductRequest.getObjective())
                .riskProfile(fundProductRequest.getRiskProfile())
                .size(fundProductRequest.getSize())
                .collected(fundProductRequest.getCollected())
                .interestRate(fundProductRequest.getInterestRate())
                .tenor(fundProductRequest.getTenor())
                .amountInvested(fundProductRequest.getAmountInvested())
                .startDate(LocalDateTime.now())
                .createdBy(user.getUserId())
                .build();

    }

    @Override
    public PaginatedResponse<FundResponse> viewAllFundProducts(PageRequest pageRequest) {
        Page<FundProduct> fundProducts = fundProductManager.viewAllFundProduct(pageRequest);
        if (fundProducts == null) {
            throw new ResourceNotFoundException("No fund products found.");
        }
        List<FundProduct> response = fundProducts.getContent();
        log.info("response {}",response);
        List<FundResponse> fundResponse = response.stream()
                .map(fundProduct -> modelMapper.map(fundProduct, FundResponse.class))
                .collect(Collectors.toList());
        return PaginatedResponse.<FundResponse>builder()
                .content(fundResponse)
                .pageNo(fundProducts.getNumber())
                .pageSize(fundProducts.getSize())
                .total(fundProducts.getTotalPages())
                .last(fundProducts.isLast())
                .build();
    }

    @Override
    public FundResponse viewFundProduct(String id) {
        log.info("Request received to view Fund Product with ID: {}", id);
        FundProduct fundProduct = fundProductManager.findByFundProductById(id);
        log.info("Good morning : {}", id);
        if (fundProduct == null) {
            throw new ResourceNotFoundException("Fund Product not found");
        }
        return modelMapper.map(fundProduct, FundResponse.class);
    }
}

