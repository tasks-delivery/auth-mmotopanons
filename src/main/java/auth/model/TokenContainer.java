package auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenContainer {

    private boolean tokenIsValid;
    private String role;
    private String token;

    public TokenContainer(boolean tokenIsValid, String role){
        this.tokenIsValid = tokenIsValid;
        this.role = role;
    }

}
