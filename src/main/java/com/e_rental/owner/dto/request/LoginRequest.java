package com.e_rental.owner.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginRequest {
    private String loginId;
    private String password;
}
