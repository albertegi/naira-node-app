package com.alvirg.ecommerce.customer;

import com.alvirg.ecommerce.customer.request.CustomerRequest;
import com.alvirg.ecommerce.customer.response.CustomerResponse;
import com.alvirg.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public Void updateCustomer(CustomerRequest request) {

        var customer = repository.findById(request.id())
                .orElseThrow(()-> new CustomerNotFoundException(
                        String.format("Cannot update customer:: No customer found with the provided Id:: %s", request.id())));

        mergeCustomer(customer, request);
        repository.save(customer);
        return null;
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())){
            customer.setFirstName(request.firstName());
        }

        if(StringUtils.isNotBlank(request.lastName())){
            customer.setLastName(request.lastName());
        }

        if(StringUtils.isNotBlank(request.email())){
            customer.setEmail(request.email());
        }

        if(request.address() != null){
            customer.setAddress(request.address());
        }


    }

    public List<CustomerResponse> findAllCustomer() {
        return repository.findAll()
                .stream()
                .map(mapper::toCustomerResponse)
                .toList();

    }

    public Boolean existsById(final String customerId) {
        return repository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(final String customerId) {
        return repository.findById(customerId)
                .map(mapper::toCustomerResponse)
                .orElseThrow(()-> new CustomerNotFoundException(
                        String.format("No customer found with the provide Id:: %s ", customerId)
                ));
    }

    public void deleteCustomer(final String customerId) {
        this.repository.deleteById(customerId);
    }
}
