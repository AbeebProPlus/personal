package africa.learnspace.trainee.util;

import africa.learnspace.loan.models.trainee.BankDetail;
import africa.learnspace.loan.models.trainee.Payment;
import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.loan.models.trainee.TraineeLoanDetail;
import africa.learnspace.trainee.dto.*;
import africa.learnspace.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TraineeMapper {

    TraineeDto traineeToTraineeDto(UserDto user, Trainee trainee, TraineeLoanDetailDto traineeLoanDetail,
                                   List<PaymentDto> paymentDto, List<BankDetailDto> bankDetails);
    TraineeLoanDetailDto traineeLoanDetailToTraineeLoanDetailDto(TraineeLoanDetail traineeLoanDetail);
    BankDetailDto bankDetailsToBankDetailDto(BankDetail bankDetails);
    List<BankDetailDto> bankDetailsDtoToBankDetails(List<BankDetail> bankDetails);
    PaymentDto paymentsToPaymentDto(Payment payments);
    List<PaymentDto> paymentsToPaymentDto(List<Payment> payments);
}
