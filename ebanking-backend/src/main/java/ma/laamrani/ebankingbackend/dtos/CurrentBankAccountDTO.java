package ma.laamrani.ebankingbackend.dtos;
import lombok.Data;
import ma.laamrani.ebankingbackend.enums.AccountStatus;
import java.util.Date;
@Data
public class CurrentBankAccountDTO extends BankAccountDTO {
    private double overDraft;
}
