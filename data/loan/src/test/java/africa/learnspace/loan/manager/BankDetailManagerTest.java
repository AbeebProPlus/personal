package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.trainee.BankDetail;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BankDetailManagerTest {

    private BankDetail bankDetail;
    @Autowired
    private BankDetailManager bankDetailManager;

    @BeforeEach
    void setUp() {
        bankDetail = new BankDetail();
        bankDetail.setBankCode("058");
        bankDetail.setBankName("GTB");
        bankDetail.setAccountName("LearnSpace Africa");
        bankDetail.setAccountNumber("123456789");
    }

    @Test
    public void testToCreateBankAccount(){
        BankDetail createdBank = bankDetailManager.createBankDetail(bankDetail);
        assertNotNull(createdBank);
    }
}