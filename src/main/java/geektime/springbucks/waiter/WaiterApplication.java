package geektime.springbucks.waiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WaiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaiterApplication.class, args);
	}

}
