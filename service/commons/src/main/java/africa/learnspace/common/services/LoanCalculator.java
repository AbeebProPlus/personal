package africa.learnspace.common.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public interface LoanCalculator {
     <T> BigDecimal summation(List<T> list, Function<T, BigDecimal> field);

    double debtPercentage(BigDecimal totalOutstanding, BigDecimal totalAmountDisbursed);

    double repaymentPercentage(BigDecimal totalAmountRepaid, BigDecimal totalAmountDisbursed);

    BigDecimal interestIncurred();

    BigDecimal totalOutstanding(BigDecimal amountDisbursed, BigDecimal amountRepaid);

    BigDecimal totalAmountRepaid(BigDecimal amountRepaid);
}
