package com.alvirg.ecommerce.customer;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.foreign.AddressLayout;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
}
