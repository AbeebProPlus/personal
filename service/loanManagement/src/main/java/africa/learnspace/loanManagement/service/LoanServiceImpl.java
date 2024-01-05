package africa.learnspace.loanManagement.service;

import africa.learnspace.loan.manager.LoanOfferManager;
import africa.learnspace.loan.manager.LoanRequestManager;
import africa.learnspace.loan.manager.TraineeManager;
import africa.learnspace.loan.models.trainee.*;
import africa.learnspace.loan.utils.ConstantUtils;
import africa.learnspace.loan.utils.LoanException;
import africa.learnspace.loanManagement.data.request.CreatLoanOfferRequest;
import africa.learnspace.loanManagement.data.response.ViewAllLoanOfferResponse;
import africa.learnspace.loanManagement.data.response.ViewAllLoanResponse;
import africa.learnspace.loanManagement.utils.ApiResponse;
import africa.learnspace.loanManagement.utils.GenerateResponse;
import africa.learnspace.loanManagement.utils.LoanMapper;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static africa.learnspace.loan.utils.ConstantUtils.INVALID_DETAILS;
import static africa.learnspace.loan.utils.ConstantUtils.LOAN_OFFER_CANNOT_BE_CREATED;
import static africa.learnspace.loanManagement.utils.GenerateResponse.okResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService{

    private  final LoanRequestManager loanRequestManager;

    private final LoanMapper loanMapper;

    private  final LoanOfferManager loanOfferManager;

    private final TraineeManager traineeManager;
    @Override public ApiResponse viewLoanRequest(String loanRequestId) {
        validateId(loanRequestId);
        return GenerateResponse.okResponse(loanMapper.mapWithLoanRequestResponse(loanRequestManager.findById(loanRequestId)));

    }

    @Override
    public ApiResponse viewLoanOffer(String loanOfferId) {
        validateId(loanOfferId);
        return okResponse(loanMapper.mapWithViewLoanOfferResponse(loanOfferManager.findById(loanOfferId)));
    }

    @Override
    public ApiResponse viewAllLoanRequests(int pageNumber, int size) {
        Page<LoanRequest> pageResult = loanRequestManager.findAll(createPageable(pageNumber,size));
        if (pageResult.isEmpty()) {
            return okResponse(new ArrayList<>());
        }
        List<LoanRequest> loanRequests = pageResult.getContent();
        List<ViewAllLoanResponse> viewAllLoanResponses = new ArrayList<>();
        for (LoanRequest loanRequest : loanRequests) {
            viewAllLoanResponses.add(loanMapper.mapWithViewAllLoanRequestResponse(loanRequest));
        }
        return okResponse(viewAllLoanResponses);
    }

    @Override
    public ApiResponse creatLoanOffer(CreatLoanOfferRequest creatLoanOfferRequest, String loanRequestId, String traineeId) {
        validateId(loanRequestId);  validateId(traineeId);
        Trainee savedTrainee = traineeManager.findById(traineeId);
        LoanRequest savedLoanRequest = loanRequestManager.findLoanRequestByTrainee(savedTrainee);
        LoanOffer foundLoanOffer = loanOfferManager.findByLoanOfferByLoanRequest(savedLoanRequest);
        if(foundLoanOffer != null) return okResponse(foundLoanOffer);
        if((!savedLoanRequest.getLoanRequestId().equals(loanRequestId))) throw new LoanException(LOAN_OFFER_CANNOT_BE_CREATED);
        if((!savedLoanRequest.getTrainee().isVerified()) || (!savedLoanRequest.getStatus().equals(LoanRequestStatus.APPROVED)))throw new LoanException(LOAN_OFFER_CANNOT_BE_CREATED);
        LoanOffer loanOffer= loanMapper.mapWithLoanOffer(creatLoanOfferRequest,savedLoanRequest,loanMapper.mapLoanProduct(creatLoanOfferRequest));;
        return okResponse(loanOffer);

    }

    @Override public ApiResponse viewAllLoanOffer(int pageNumber, int pageSize) {
        Page<LoanOffer> pageResult = loanOfferManager.findAll(createPageable(pageNumber,pageSize));
        if (pageResult.isEmpty()) {
            return okResponse(new ArrayList<>());
        }
        List<LoanOffer> loanRequests = pageResult.getContent();
        List<ViewAllLoanOfferResponse> viewAllLoanResponses = new ArrayList<>();
        for (LoanOffer loanOffer : loanRequests) {
            viewAllLoanResponses.add(loanMapper.mapWithViewAllLoanOfferResponse(loanOffer));
        }
        return okResponse(viewAllLoanResponses);
    }

    private void validateId(String loanRequestId) {
        if(StringUtils.isEmpty(loanRequestId) || ConstantUtils.UNDEFINED.equals(loanRequestId)){
            throw new LoanException(INVALID_DETAILS);
        }
    }
    private Pageable createPageable(int pageNumber, int pageSize){
        return  PageRequest.of(pageNumber, pageSize);
    }
}
