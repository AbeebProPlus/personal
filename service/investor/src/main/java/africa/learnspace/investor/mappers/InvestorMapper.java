package africa.learnspace.investor.mappers;

import africa.learnspace.investor.data.request.EditInvestorRequest;
import africa.learnspace.investor.data.response.EditInvestorResponse;
import africa.learnspace.investor.dto.request.AddInvestorRequest;
import africa.learnspace.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InvestorMapper {

    User mapInvestorUserObject(AddInvestorRequest addInvestorRequest);
    void editInvestorFromEditRequest(EditInvestorRequest editRequest, @MappingTarget User user);
    EditInvestorResponse investorToInvestorResponse(User user);
}
