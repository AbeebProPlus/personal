package africa.learnspace.financemanager.service;

import africa.learnspace.financemanager.data.dto.request.PortfolioManagerRequest;
import africa.learnspace.financemanager.data.dto.response.ApiResponse;
import africa.learnspace.financemanager.data.dto.response.InstituteResponse;
import africa.learnspace.common.responses.PaginatedResponse;
import org.springframework.data.domain.PageRequest;

public interface PortfolioManagerService {

     String inviteTrainingInstitute (PortfolioManagerRequest portfolioManagerRequest);


     PaginatedResponse<InstituteResponse> viewAllTrainingInstitutes(PageRequest pageRequest);
     InstituteResponse viewTrainingInstitutes(String id);
     ApiResponse deactivateTrainingInstitute(String id);



}
