package africa.learnspace.services;

import static org.junit.jupiter.api.Assertions.*;

import africa.learnspace.common.services.LoanCalculator;
import africa.learnspace.common.services.LoanCalculatorImpl;
import africa.learnspace.loan.models.trainee.TraineeLoanDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class LoanCalculatorTest {

    private LoanCalculator loanCalculator;

    @BeforeEach
    public void setUp() {
        loanCalculator = new LoanCalculatorImpl();
    }

    @Test
    public void testSummation() {
        // Arrange
        List<TraineeLoanDetail> loanDetails = new ArrayList<>();

        TraineeLoanDetail traineeLoanDetail1 = new TraineeLoanDetail();
        TraineeLoanDetail traineeLoanDetail2 = new TraineeLoanDetail();

        traineeLoanDetail1.setTotalAmountRepaid(BigDecimal.valueOf(100));
        traineeLoanDetail2.setTotalAmountRepaid(BigDecimal.valueOf(200));

        loanDetails.add(traineeLoanDetail1);
        loanDetails.add(traineeLoanDetail2);

        // Act
        BigDecimal result = loanCalculator.summation(loanDetails, TraineeLoanDetail::getTotalAmountRepaid);

        // Assert
        assertEquals(BigDecimal.valueOf(300), result);
    }

    @Test
    public void testDebtPercentage() {
        // Arrange
        BigDecimal totalOutstanding = BigDecimal.valueOf(500);
        BigDecimal amountDisbursed = BigDecimal.valueOf(1000);

        // Act
        double result = loanCalculator.debtPercentage(totalOutstanding, amountDisbursed);

        // Assert
        assertEquals(50.0, result);
    }

    @Test
    public void testRepaymentPercentage() {
        // Arrange
        BigDecimal totalAmountRepaid = BigDecimal.valueOf(200);
        BigDecimal amountDisbursed = BigDecimal.valueOf(1000);

        // Act
        double result = loanCalculator.repaymentPercentage(totalAmountRepaid, amountDisbursed);

        // Assert
        assertEquals(20.0, result);
    }

    //
}
