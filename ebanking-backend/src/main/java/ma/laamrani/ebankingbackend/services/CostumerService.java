package ma.laamrani.ebankingbackend.services;

import ma.laamrani.ebankingbackend.dtos.CustomerDTO;
import ma.laamrani.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;


public interface CostumerService {


    /*costumer methods */
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    /*get all customers */
    List<CustomerDTO> listCustomers();

    /*search for customers */
    List<CustomerDTO> searchCustomers(String keyword);



    //updating a costumer
    CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException;

    //delete  a costumer using id
    void deleteCustomer(Long customerId) throws CustomerNotFoundException;
}
