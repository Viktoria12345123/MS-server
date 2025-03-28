package skytales.Carts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {
		"skytales.Carts",
		"skytales.common"
})
public class CartsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartsApplication.class, args);
	}

}
