package africa.learnspace.web.controller.cohortcontroller;


import africa.learnspace.cohort.data.request.CohortRequest;
import africa.learnspace.cohort.data.request.CreateCohortRequest;
import africa.learnspace.cohort.data.response.CohortLoanPerformanceResponse;
import africa.learnspace.cohort.data.response.CohortResponse;
import africa.learnspace.cohort.data.response.CreateCohortResponse;
import africa.learnspace.cohort.data.response.ViewCohortsResponse;
import africa.learnspace.cohort.services.CohortLoanPerformance;
import africa.learnspace.cohort.services.CohortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/cohorts")
public class CohortController {

    private final CohortService cohortService;
    private final CohortLoanPerformance cohortLoanPerformance;

    @Autowired
    public CohortController(CohortService cohortService, CohortLoanPerformance cohortLoanPerformance) {
        this.cohortService = cohortService;
        this.cohortLoanPerformance = cohortLoanPerformance;
    }

    @PostMapping("/{instituteId}/{programId}/create")
    public ResponseEntity<CreateCohortResponse> createCohort(
            @PathVariable String instituteId,
            @PathVariable String programId,
            @RequestBody CreateCohortRequest cohortRequest) {
        CreateCohortResponse createdCohort = cohortService.createCohort(instituteId, programId, cohortRequest);
        return new ResponseEntity<>(createdCohort, HttpStatus.CREATED);
    }

    @GetMapping("/{instituteId}/{programId}/{pageNumber}/{pageSize}/view-all")
    public ResponseEntity<ViewCohortsResponse> viewAllCohorts(
            @PathVariable String instituteId,
            @PathVariable String programId,
            @PathVariable int pageNumber,
            @PathVariable int pageSize) {
        ViewCohortsResponse cohorts = cohortService.viewAllCohorts(instituteId, programId, pageNumber, pageSize);
        return new ResponseEntity<>(cohorts, HttpStatus.OK);
    }

    @GetMapping("/{instituteId}/{programId}/{cohortId}/view")
    public ResponseEntity<CohortResponse> viewCohort(
            @PathVariable String instituteId,
            @PathVariable String programId,
            @PathVariable String cohortId) {
        CohortResponse cohort = cohortService.viewCohort(instituteId, programId, cohortId);
        return new ResponseEntity<>(cohort, HttpStatus.OK);
    }

    @PatchMapping("/{instituteId}/{programId}/{cohortId}/edit")
    public ResponseEntity<CohortResponse> editCohort(
            @PathVariable String instituteId,
            @PathVariable String programId,
            @PathVariable String cohortId,
            @RequestBody CohortRequest editRequest) {
        CohortResponse updatedCohort = cohortService.editCohort(instituteId, programId, cohortId, editRequest);
        return new ResponseEntity<>(updatedCohort, HttpStatus.OK);
    }

    @DeleteMapping("/{instituteId}/{programId}/{cohortId}/delete")
    public ResponseEntity<String> deleteCohort(
            @PathVariable String instituteId,
            @PathVariable String programId,
            @PathVariable String cohortId) {
        String message = cohortService.deleteCohort(instituteId, programId, cohortId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{cohortId}/cohort-loan-performance")
    public ResponseEntity<CohortLoanPerformanceResponse> viewCohortLoanPerformance(@PathVariable String cohortId){
        return new ResponseEntity<>( cohortLoanPerformance.viewCohortLoanPerformance(cohortId), HttpStatus.OK);
    }
}