package africa.learnspace.web.controller.investorcontroller;


import africa.learnspace.investor.data.request.EditInvestorRequest;
import africa.learnspace.investor.data.response.EditInvestorResponse;
import africa.learnspace.investor.dto.request.AddInvestorRequest;
import africa.learnspace.investor.dto.response.ApiResponse;
import africa.learnspace.investor.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/investors")
public class InvestorController {

    private final InvestorService investorService;

    @Autowired
    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @PatchMapping("/edit/{investorId}")
    public ResponseEntity<EditInvestorResponse> editInvestorDetails(@PathVariable String investorId, @RequestBody EditInvestorRequest editInvestorRequest) {
        EditInvestorResponse response = investorService.editInvestorDetails(investorId, editInvestorRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<ApiResponse> addInvestors(@AuthenticationPrincipal Jwt jwt, @RequestBody AddInvestorRequest addInvestorRequest) {
        return new ResponseEntity<>(investorService.addInvestor(jwt.getClaim("email"), addInvestorRequest), HttpStatus.CREATED);
    }


}
