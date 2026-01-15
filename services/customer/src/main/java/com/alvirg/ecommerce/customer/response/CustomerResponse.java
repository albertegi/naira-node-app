package com.alvirg.ecommerce.customer.response;

import com.alvirg.ecommerce.customer.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerResponse(

        String id,
        String firstName,
        String lastName,
        String email,
        Address address
) {
}
