package ma.laamrani.ebankingbackend.dtos;
import lombok.Data;
import ma.laamrani.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class BankAccountDTO {
    private String type;
    private String id;
    private Double balance;
    private Date CreatedAt;
    private String currency;
    private AccountStatus status;
    private CustomerDTO customerDTO;
}
