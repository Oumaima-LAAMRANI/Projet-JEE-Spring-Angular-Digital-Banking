package ma.laamrani.ebankingbackend.dtos;
import lombok.Data;
import ma.laamrani.ebankingbackend.enums.AccountStatus;

import java.util.Date;
@Data
public class SavingBankAccountDTO extends BankAccountDTO {

    private double interestRate;
}
