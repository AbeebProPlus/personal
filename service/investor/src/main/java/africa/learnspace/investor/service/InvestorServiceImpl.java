package africa.learnspace.investor.service;

import africa.learnspace.cohort.exceptions.InvalidInputException;
import africa.learnspace.common.exceptions.ResourceNotFoundException;
import africa.learnspace.investment.manager.InvestorManager;
import africa.learnspace.investment.model.Investor;
import africa.learnspace.investor.data.request.EditInvestorRequest;
import africa.learnspace.investor.data.response.EditInvestorResponse;
import africa.learnspace.investor.dto.request.AddInvestorRequest;
import africa.learnspace.investor.dto.response.ApiResponse;
import africa.learnspace.investor.dto.response.GenerateResponse;
import africa.learnspace.investor.mappers.InvestorMapper;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestorServiceImpl implements InvestorService {
    @Autowired
    private final InvestorManager investorManager;
    private final UserManager userManager;
    private final InvestorMapper investorMapper;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {ResourceNotFoundException.class})
    public ApiResponse addInvestor(String portfolioManagerEmail, AddInvestorRequest addInvestorRequest) {
        String investorName = investorManager.findInvestorByName(addInvestorRequest.getEmail());
        if (investorName != null) throw new RuntimeException("Investor Already exist");
        User portfolioManager = userManager.findByEmail(portfolioManagerEmail);
        User savedInvestor = userManager.findById(userManager.saveUser(investorMapper.mapInvestorUserObject(addInvestorRequest)).getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Investor investor = Investor.builder().user(savedInvestor).createdBy(portfolioManager.getUserId())
                .amountInvested(addInvestorRequest.getAmountInvested() != null ? addInvestorRequest.getAmountInvested() : BigDecimal.ZERO).build();
        investorManager.createInvestor(investor);
        return GenerateResponse.createdResponse("Investor Add Successfully");

    }
        private User getInvestorUser (String userId){
            return userManager.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        }

    @Override
    public EditInvestorResponse editInvestorDetails(String investorId, EditInvestorRequest editInvestor) {
            if (editInvestor == null) throw new InvalidInputException("Invalid Input data");

            Investor investor = investorManager
                    .findInvestorUserById(investorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Investor not found"));

            User user = getInvestorUser(investor.getUser().getUserId());

            investorMapper.editInvestorFromEditRequest(editInvestor, user);

            User editedInvestor = userManager.saveUser(user);

            return investorMapper.investorToInvestorResponse(editedInvestor);
        }

}




