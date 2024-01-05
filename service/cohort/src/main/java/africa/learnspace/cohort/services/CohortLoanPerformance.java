package africa.learnspace.cohort.services;

import africa.learnspace.cohort.data.response.CohortLoanPerformanceResponse;

public interface CohortLoanPerformance {
    CohortLoanPerformanceResponse viewCohortLoanPerformance(String cohortId);
}
