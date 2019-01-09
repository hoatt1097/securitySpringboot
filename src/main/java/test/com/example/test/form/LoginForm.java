package test.com.example.test.form;

import lombok.Getter;
import lombok.Setter;
import test.com.example.test.type.TokenType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginForm {
    private String username;

    private String password;

    @NotNull
    private TokenType tokenType;
    private String refreshToken;


    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        if (tokenType.equals("refresh_token")) {
            this.tokenType = TokenType.REFRESH_TOKEN;
        } else {
            this.tokenType = TokenType.ACCESS_TOKEN;
        }
    }
}
