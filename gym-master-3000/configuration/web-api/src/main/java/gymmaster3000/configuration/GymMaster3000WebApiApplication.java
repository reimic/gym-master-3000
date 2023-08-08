package gymmaster3000.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication(scanBasePackages = {
        "gymmaster3000",
})
@EnableJpaRepositories(basePackages = {
        "gymmaster3000",
})
@EntityScan(basePackages = {
        "gymmaster3000",
})
public class GymMaster3000WebApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymMaster3000WebApiApplication.class, args);
    }

}
