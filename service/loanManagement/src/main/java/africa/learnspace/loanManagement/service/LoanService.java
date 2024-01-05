package africa.learnspace.loanManagement.service;

import africa.learnspace.loanManagement.data.request.CreatLoanOfferRequest;
import africa.learnspace.loanManagement.utils.ApiResponse;

public interface LoanService {

 ApiResponse viewLoanRequest(String loanRequestId);

 ApiResponse viewLoanOffer(String loanOfferId);

 ApiResponse viewAllLoanRequests(int pageNumber, int size);

 ApiResponse creatLoanOffer(CreatLoanOfferRequest creatLoanOfferRequest, String loanRequestId, String traineeId);

 ApiResponse viewAllLoanOffer(int pageNumber, int pageSize);
}
