package com.alvirg.ecommerce;

import com.alvirg.ecommerce.customer.Address;
import com.alvirg.ecommerce.customer.Customer;
import com.alvirg.ecommerce.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

//	@Bean
	public CommandLineRunner commandLineRunner(
			CustomerRepository repository
	){
		return args -> {
			var customer = Customer.builder()
					.firstName("Albert")
					.lastName("Egi")
					.email("albertegi@email.com")
					.address(Address.builder()
							.street("Pipkin Court")
							.houseNumber("90")
							.zipcode("50001")
							.build())
					.build();

			repository.insert(customer);
		};
	}

}
