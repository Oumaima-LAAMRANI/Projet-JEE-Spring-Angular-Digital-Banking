package ma.laamrani.ebankingbackend.services;
import ma.laamrani.ebankingbackend.dtos.AccountHistoryDTO;
import ma.laamrani.ebankingbackend.dtos.AccountOperationDTO;
import ma.laamrani.ebankingbackend.exceptions.BalanceNotSufficientException;
import ma.laamrani.ebankingbackend.exceptions.BankAccountNotFoundException;
import java.util.List;
public interface OperationService {
    List<AccountOperationDTO> historique(String accountId);
    AccountHistoryDTO HistoriquePages(String accountId, int page, int size) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDest, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
