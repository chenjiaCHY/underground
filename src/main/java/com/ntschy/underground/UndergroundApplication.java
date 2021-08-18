package com.ntschy.underground;

import com.ntschy.underground.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.ntschy.underground.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.ntschy.underground.datasource.dynamic.DynamicDatasourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAspectJAutoProxy
@Import(DynamicDatasourceRegister.class)
@SpringBootApplication
@EnableTransactionManagement
@EnableZuulProxy
public class UndergroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(UndergroundApplication.class, args);
	}

	@Bean
	public DynamicDataSourceAnnotationAdvisor dynamicDataSourceAnnotationAdvisor() {
		return new DynamicDataSourceAnnotationAdvisor(new DynamicDataSourceAnnotationInterceptor());
	}

}
