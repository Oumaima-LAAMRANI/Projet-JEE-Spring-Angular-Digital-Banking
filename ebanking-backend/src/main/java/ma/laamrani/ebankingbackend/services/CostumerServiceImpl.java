package ma.laamrani.ebankingbackend.services;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.laamrani.ebankingbackend.dtos.CustomerDTO;
import ma.laamrani.ebankingbackend.entities.Customer;
import ma.laamrani.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.laamrani.ebankingbackend.mappers.BankAccountMapperImpl;
import ma.laamrani.ebankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CostumerServiceImpl implements CostumerService {

    private CustomerRepository customerRepository;
    private BankAccountMapperImpl bankAccountMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> bankAccountMapper.fromCustomer(customer))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.findCustomerByFirstNameContainsOrLastNameContains(keyword,keyword).stream()
                .map(customer -> bankAccountMapper.fromCustomer(customer))
                .collect(Collectors.toList());
    }
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");
        return bankAccountMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException {
        log.info("update customer");
        Customer customer = customerRepository.findById(customerDTO.getId()).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");
        customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        return bankAccountMapper.fromCustomer(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        log.info("delete customer");
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");
        customerRepository.deleteById(customerId);
    }


}
