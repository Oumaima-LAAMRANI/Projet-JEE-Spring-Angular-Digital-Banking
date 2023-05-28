package ma.laamrani.ebankingbackend;


import ma.laamrani.ebankingbackend.entities.*;
import ma.laamrani.ebankingbackend.enums.AccountStatus;
import ma.laamrani.ebankingbackend.enums.OperationType;
import ma.laamrani.ebankingbackend.repositories.AccountOperationRepository;
import ma.laamrani.ebankingbackend.repositories.BankAccountRepository;
import ma.laamrani.ebankingbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            List<String> firstNames = Arrays.asList("Hiba", "Aya", "Mohamed");
            List<String> lastNames = Arrays.asList("Benali", "El Amrani", "Choukri");

            for (int i = 0; i < firstNames.size(); i++) {
                Customer customer = new Customer();
                customer.setFirstName(firstNames.get(i));
                customer.setLastName(lastNames.get(i));
                customer.setEmail(firstNames.get(i) + "@gmail.com");
                customerRepository.save(customer);
            }
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i <10 ; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };

    }
}
