package ma.laamrani.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.laamrani.ebankingbackend.dtos.BankAccountDTO;
import ma.laamrani.ebankingbackend.entities.BankAccount;
import ma.laamrani.ebankingbackend.entities.CurrentAccount;
import ma.laamrani.ebankingbackend.entities.Customer;
import ma.laamrani.ebankingbackend.entities.SavingAccount;
import ma.laamrani.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.laamrani.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.laamrani.ebankingbackend.mappers.BankAccountMapperImpl;
import ma.laamrani.ebankingbackend.repositories.BankAccountRepository;
import ma.laamrani.ebankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private BankAccountMapperImpl bankAccountMapper;

    //get all BankAccounts
    @Override
    public List<BankAccountDTO> listBankAccounts() {
        return bankAccountRepository.findAll().stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<BankAccountDTO> listBankAccountsOfCustomer(Long id) {
        return bankAccountRepository.getBankAccountByCustomer_Id(id).stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
    }
    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentBankAccount(currentAccount);
        }
    }
    @Override
    public BankAccount saveCurrentBankAccount(double initialBalance, Long customerId, double overDraft) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public BankAccount saveSavingBankAccount(double initialBalance, Long customerId, double interestRate) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        return bankAccountRepository.save(savingAccount);
    }
}
