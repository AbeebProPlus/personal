package africa.learnspace.web.controller.loanController;

import africa.learnspace.loanManagement.data.request.CreatLoanOfferRequest;
import africa.learnspace.loanManagement.service.LoanService;
import africa.learnspace.loanManagement.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/loan/")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;


    @GetMapping("loan_request/{loanRequestId}")
    public ResponseEntity<ApiResponse> viewLoanRequest(@PathVariable String loanRequestId){
       return ResponseEntity.ok(loanService.viewLoanRequest(loanRequestId));
    }

    @GetMapping("loan_requests/{pageNumber}/{pageSize}")
    public ResponseEntity<ApiResponse> viewAllLoanRequests(@PathVariable(required = true) int pageNumber, @PathVariable(required = true) int pageSize){
        return ResponseEntity.ok(loanService.viewAllLoanRequests(pageNumber,pageSize));
    }


    @PostMapping("{traineeId}/{loanRequestId}/loan_offer")
    public ResponseEntity<ApiResponse> createLoanOffer(@PathVariable String traineeId, @PathVariable String loanRequestId, @RequestBody @Valid CreatLoanOfferRequest creatLoanOfferRequest){
        return ResponseEntity.ok(loanService.creatLoanOffer(creatLoanOfferRequest,loanRequestId, traineeId));
    }
    @GetMapping("loan_offer/{loanOfferId}")
    public ResponseEntity<ApiResponse> viewLoanOffer(@PathVariable String loanOfferId){
        return ResponseEntity.ok(loanService.viewLoanOffer(loanOfferId));
    }

    @GetMapping("loan_offers")
    public ResponseEntity<ApiResponse> viewAllLoanOffers(@RequestParam(value = "pageNumber",defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize",defaultValue ="10") int pageSize){
        return ResponseEntity.ok(loanService.viewAllLoanOffer(pageNumber,pageSize));
    }
}
