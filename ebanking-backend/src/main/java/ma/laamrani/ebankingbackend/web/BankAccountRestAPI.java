package ma.laamrani.ebankingbackend.web;
import ma.laamrani.ebankingbackend.dtos.*;
import ma.laamrani.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.laamrani.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.laamrani.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;
    @GetMapping("/accounts")
    public List<BankAccountDTO> banks() {
        return bankAccountService.listBankAccounts();
    }
      @GetMapping("/accounts/{id}")
    public BankAccountDTO getbankaccount(@PathVariable(name = "id") String  accountid) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountid);
    }

}
