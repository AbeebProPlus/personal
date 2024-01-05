package africa.learnspace.investor.service;

import africa.learnspace.common.responses.PaginatedResponse;
import africa.learnspace.investor.dto.request.FundProductRequest;
import africa.learnspace.investor.dto.response.ApiResponse;
import africa.learnspace.investor.dto.response.*;
import org.springframework.data.domain.PageRequest;

public interface FundProductService {
    ApiResponse createFundProduct(String email, FundProductRequest fundProductRequest);

    PaginatedResponse<FundResponse> viewAllFundProducts(PageRequest pageRequest);

    FundResponse viewFundProduct(String id);
}
