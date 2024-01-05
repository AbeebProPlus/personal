package africa.learnspace.web.controller.fundproductcontroller;
import africa.learnspace.investor.dto.request.FundProductRequest;
import africa.learnspace.investor.dto.response.ApiResponse;
import africa.learnspace.investor.service.FundProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import africa.learnspace.investor.dto.response.FundResponse;
import africa.learnspace.common.responses.PaginatedResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/fundProduct")
@RestController
@RequiredArgsConstructor
public class FundProductController {

   private final FundProductService fundProductService;

    @PostMapping("add")
    public ResponseEntity<ApiResponse> createFundProduct(@AuthenticationPrincipal Jwt token, @RequestBody FundProductRequest fundProductRequest){
        String email = token.getClaim("email");
        return new ResponseEntity<>(fundProductService.createFundProduct(email, fundProductRequest), HttpStatus.CREATED);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<?> viewAllFundProduct(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        PaginatedResponse<FundResponse> responses = fundProductService.viewAllFundProducts(PageRequest.of(page, pageSize));
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/view/{fundProductId}")
    public ResponseEntity<?> viewFundProduct(@PathVariable String fundProductId){
        FundResponse response = fundProductService.viewFundProduct(fundProductId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
