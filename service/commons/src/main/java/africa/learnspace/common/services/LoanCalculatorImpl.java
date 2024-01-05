package africa.learnspace.common.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

@Service
public class LoanCalculatorImpl implements LoanCalculator {

    @Override
    public <T> BigDecimal summation(List<T> list, Function<T, BigDecimal> field) {
        return list.stream()
                .map(field)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public double debtPercentage(BigDecimal totalOutstanding, BigDecimal totalAmountDisbursed) {
        return totalOutstanding
                .divide(totalAmountDisbursed, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    @Override
    public double repaymentPercentage(BigDecimal totalAmountRepaid, BigDecimal totalAmountDisbursed) {
        return totalAmountRepaid
                .divide(totalAmountDisbursed, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    @Override
    public BigDecimal interestIncurred(){
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal totalOutstanding(BigDecimal amountDisbursed, BigDecimal amountRepaid) {
        return amountDisbursed.subtract(amountRepaid);
    }

    @Override
    public BigDecimal totalAmountRepaid(BigDecimal amountRepaid) {
        return BigDecimal.ZERO;
    }
}