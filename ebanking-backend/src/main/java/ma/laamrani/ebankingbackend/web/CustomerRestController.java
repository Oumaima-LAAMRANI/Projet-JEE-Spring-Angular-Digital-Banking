package ma.laamrani.ebankingbackend.web;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.laamrani.ebankingbackend.dtos.BankAccountDTO;
import ma.laamrani.ebankingbackend.dtos.CustomerDTO;
import ma.laamrani.ebankingbackend.services.BankAccountService;
import ma.laamrani.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.laamrani.ebankingbackend.services.CostumerService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;
    private CostumerService costumerService;
    @GetMapping("/customers")
    public List<CustomerDTO> customers() {
        return costumerService.listCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return costumerService.searchCustomers("" + keyword);
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getcustomer(@PathVariable(name = "id") Long costumerid) throws CustomerNotFoundException {
        return costumerService.getCustomer(costumerid);
    }
    @PostMapping("/customers")
    public CustomerDTO addcustomer(@RequestBody CustomerDTO customerDTO) {
        return costumerService.saveCustomer(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    public void deletecustomer(@PathVariable Long id) throws CustomerNotFoundException {
        costumerService.deleteCustomer(id);
    }
    @PutMapping("/customers/{id}")
    public void deletecustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(id);
        costumerService.updateCustomer(customerDTO);

    }
    @GetMapping("/customer/{id}/accounts")
    public List<BankAccountDTO> banksOfCustomer(@PathVariable(name = "id") Long customerid) {
        return bankAccountService.listBankAccountsOfCustomer(customerid);
    }

}
