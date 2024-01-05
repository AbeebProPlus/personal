package africa.learnspace.investor.service;

import africa.learnspace.investor.data.request.EditInvestorRequest;
import africa.learnspace.investor.data.response.EditInvestorResponse;

import africa.learnspace.investor.dto.request.AddInvestorRequest;
import africa.learnspace.investor.dto.response.ApiResponse;

public interface InvestorService {

    ApiResponse addInvestor(String email, AddInvestorRequest addInvestorRequest);

    EditInvestorResponse editInvestorDetails(String investorId, EditInvestorRequest editInvestor);
}
