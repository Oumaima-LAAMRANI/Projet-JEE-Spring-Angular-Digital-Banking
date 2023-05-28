package ma.laamrani.ebankingbackend.services;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.laamrani.ebankingbackend.dtos.AccountHistoryDTO;
import ma.laamrani.ebankingbackend.dtos.AccountOperationDTO;
import ma.laamrani.ebankingbackend.entities.AccountOperation;
import ma.laamrani.ebankingbackend.entities.BankAccount;
import ma.laamrani.ebankingbackend.enums.OperationType;
import ma.laamrani.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.laamrani.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.laamrani.ebankingbackend.mappers.BankAccountMapperImpl;
import ma.laamrani.ebankingbackend.repositories.AccountOperationRepository;
import ma.laamrani.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {

    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountService bankAccountService;
    private BankAccountMapperImpl bankAccountMapper;
    @Override
    public List<AccountOperationDTO> historique(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream()
                .map(op -> bankAccountMapper.fromAccountOperation(op))
                .collect(Collectors.toList());
    }
    @Override
    public AccountHistoryDTO HistoriquePages(String accountId, int page, int size) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException("Bank Account Not Found");
        }

        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount.getBalance() < amount) throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }
    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDest, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDest);
        credit(accountIdDest, amount, "Transfer from " + accountIdSource);
    }


}
