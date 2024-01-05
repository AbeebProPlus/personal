package africa.learnspace.web.controller.financemanagercontroller;

import africa.learnspace.financemanager.data.dto.request.PortfolioManagerRequest;
import africa.learnspace.financemanager.data.dto.response.ApiResponse;
import africa.learnspace.financemanager.data.dto.response.InstituteResponse;
import africa.learnspace.common.responses.PaginatedResponse;
import africa.learnspace.financemanager.service.PortfolioManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("api/v1/finance")
@RestController
@RequiredArgsConstructor
public class FinanceManagerController {
    private final PortfolioManagerService portfolioManagerService;
    @PostMapping("/invite")
    public ResponseEntity<?> inviteTrainingInstitute (@RequestBody @Valid PortfolioManagerRequest portfolioManagerRequest){
        portfolioManagerService.inviteTrainingInstitute(portfolioManagerRequest);
        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccessful(true)
                .message("invite sent")
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/viewAll")
    public ResponseEntity<?> viewAllTrainingInstitute(
            @RequestParam(value = "page", defaultValue = "0", required = false)int page,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false)int pageSize
    ) {
       PaginatedResponse<InstituteResponse> response = portfolioManagerService.viewAllTrainingInstitutes(PageRequest.of(page, pageSize));
       return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/view/{instituteId}")
    public ResponseEntity<?> viewTrainingInstitute(@PathVariable String instituteId){
        InstituteResponse institute = portfolioManagerService.viewTrainingInstitutes(instituteId);
        return new ResponseEntity<>(institute, HttpStatus.OK);
    }


    @PatchMapping("/deactivate")
        public ResponseEntity<?> deactivateTrainingInstitutes(@RequestParam("instituteId") String instituteId){
            portfolioManagerService.deactivateTrainingInstitute(instituteId);
            ApiResponse apiResponse = ApiResponse.builder()
                    .isSuccessful(true)
                    .message("TrainingInstitute DeActivate SuccessFully")
                    .status(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }