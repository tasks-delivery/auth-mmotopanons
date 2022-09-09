package auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import auth.util.JwtFilter;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@Configuration
public class Auth {

	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/edit", "/add", "/about", "/contact");
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(Auth.class, args);
	}

}
