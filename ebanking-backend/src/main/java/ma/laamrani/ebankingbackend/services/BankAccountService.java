package ma.laamrani.ebankingbackend.services;
import ma.laamrani.ebankingbackend.dtos.*;
import ma.laamrani.ebankingbackend.entities.BankAccount;
import ma.laamrani.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.laamrani.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;
public interface BankAccountService {
    BankAccount saveCurrentBankAccount(double initialBalance, Long customerId, double overDraft) throws CustomerNotFoundException;

    BankAccount saveSavingBankAccount(double initialBalance, Long customerId, double interestRate) throws CustomerNotFoundException;

    List<BankAccountDTO> listBankAccounts();

    List<BankAccountDTO> listBankAccountsOfCustomer(Long id);


    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
}
