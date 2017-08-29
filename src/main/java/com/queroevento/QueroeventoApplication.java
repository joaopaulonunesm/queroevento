package com.queroevento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.queroevento.filters.TokenFilter;

@SpringBootApplication
public class QueroeventoApplication {

	@Bean
	public FilterRegistrationBean filterJwt() {
		FilterRegistrationBean frb = new FilterRegistrationBean();
		frb.setFilter(new TokenFilter());
		frb.addUrlPatterns("/v1/*");
		return frb;
	}

	public static void main(String[] args) {
		SpringApplication.run(QueroeventoApplication.class, args);
	}
}
