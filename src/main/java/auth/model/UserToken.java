package auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String accountId;
    private boolean tokenIsValid;
    private String token;

    public UserToken(String accountId, boolean tokenIsValid, String  token){
        this.accountId = accountId;
        this.tokenIsValid = tokenIsValid;
        this.token = token;
    }

}
