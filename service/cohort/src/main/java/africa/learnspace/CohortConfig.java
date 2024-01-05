package africa.learnspace;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"africa.learnspace.**"})
public class CohortConfig {
}