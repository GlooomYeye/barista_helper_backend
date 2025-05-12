package my.cousework.barista_helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BaristaHelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaristaHelperApplication.class, args);
	}
}
