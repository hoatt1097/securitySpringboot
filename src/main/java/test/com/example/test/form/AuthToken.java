package test.com.example.test.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthToken {
    private String accessToken;
    private String refreshToken;
}