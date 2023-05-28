package ma.laamrani.ebankingbackend.web;

import lombok.AllArgsConstructor;
import ma.laamrani.ebankingbackend.dtos.*;
import ma.laamrani.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.laamrani.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.laamrani.ebankingbackend.services.OperationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class OperationsRestController {

    private OperationService operationService;
     @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistorique(@PathVariable String accountId) {
        return operationService.historique(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getHistoriquePages(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException, BankAccountNotFoundException {

        return operationService.HistoriquePages(accountId, page, size);
    }
    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException, BalanceNotSufficientException {
        this.operationService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.operationService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }
   @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.operationService.transfer(
                transferDTO.getAccountSource(),
                transferDTO.getAccountDestination(),
                transferDTO.getAmount());
    }


}
