package africa.learnspace.cohort.services;

import africa.learnspace.cohort.data.request.CreateCohortRequest;
import africa.learnspace.cohort.data.request.CohortRequest;
import africa.learnspace.cohort.data.request.ViewCohortsRequest;
import africa.learnspace.cohort.data.response.CohortResponse;
import africa.learnspace.cohort.data.response.CreateCohortResponse;
import africa.learnspace.cohort.data.response.ViewCohortsResponse;
import africa.learnspace.loan.models.cohort.Cohort;

import java.util.List;

public interface CohortService {
    CreateCohortResponse createCohort(String instituteId, String programId, CreateCohortRequest cohortRequest);

    ViewCohortsResponse viewAllCohorts(String instituteId, String programId);

    ViewCohortsResponse viewAllCohorts(String instituteId, String programId, int pageNumber, int pageSize);
    CohortResponse viewCohort(String instituteId, String programId, String cohortId);
    CohortResponse editCohort(String instituteId, String programId, String cohortId, CohortRequest editRequest);
    String deleteCohort(String instituteId, String programId, String cohortId);
}
