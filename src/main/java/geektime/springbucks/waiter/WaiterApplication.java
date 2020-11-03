package geektime.springbucks.waiter;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import geektime.springbucks.waiter.controller.PerformanceInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class WaiterApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(WaiterApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PerformanceInterceptor()).
                addPathPatterns("/coffee/**").addPathPatterns("/order/**");
    }
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonBuilderCustomizer() {
		return bulider -> {
            bulider.indentOutput(true);
            bulider.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        };
	}

}






