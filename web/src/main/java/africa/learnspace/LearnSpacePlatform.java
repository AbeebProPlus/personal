package africa.learnspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"africa.learnspace.**"})
@EnableAsync
@EnableTransactionManagement
public class LearnSpacePlatform {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpacePlatform.class, args);
    }
}