package africa.learnspace.cohort.mappers;

import africa.learnspace.cohort.data.request.CohortRequest;
import africa.learnspace.cohort.data.request.CreateCohortRequest;
import africa.learnspace.cohort.data.response.CohortLoanPerformanceResponse;
import africa.learnspace.cohort.data.response.CohortResponse;
import africa.learnspace.cohort.data.response.CreateCohortResponse;
import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.cohort.CohortLoanDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CohortMapper{
    Cohort createCohortRequestToCohort(CreateCohortRequest createCohortRequest);
    CreateCohortResponse cohortToCreateCohortResponse(Cohort cohort);
    CohortResponse cohortToCohortResponse(Cohort cohort);
    void updateCohortFromEditRequest(CohortRequest request, @MappingTarget Cohort cohort);
    CohortLoanPerformanceResponse detailsToResponse(CohortLoanDetail cohortLoanDetail);
}