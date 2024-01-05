package africa.learnspace.loanManagement.utils;

import africa.learnspace.loan.models.trainee.LoanOffer;
import africa.learnspace.loan.models.trainee.LoanProduct;
import africa.learnspace.loan.models.trainee.LoanRequest;
import africa.learnspace.loanManagement.data.request.CreatLoanOfferRequest;
import africa.learnspace.loanManagement.data.response.LoanRequestResponse;
import africa.learnspace.loanManagement.data.response.ViewAllLoanOfferResponse;
import africa.learnspace.loanManagement.data.response.ViewAllLoanResponse;
import africa.learnspace.loanManagement.data.response.ViewLoanOfferResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper( componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoanMapper {
    LoanRequestResponse mapWithLoanRequestResponse(LoanRequest loanRequest);

    ViewLoanOfferResponse mapWithViewLoanOfferResponse(LoanOffer loanOffer);

    ViewAllLoanResponse mapWithViewAllLoanRequestResponse(LoanRequest loanRequest);

    LoanOffer mapWithLoanOffer(CreatLoanOfferRequest creatLoanOfferRequest, LoanRequest loanRequest, LoanProduct loanProduct);

    ViewAllLoanOfferResponse mapWithViewAllLoanOfferResponse(LoanOffer loanOffer);

    LoanProduct mapLoanProduct(CreatLoanOfferRequest creatLoanOfferRequest);
}


