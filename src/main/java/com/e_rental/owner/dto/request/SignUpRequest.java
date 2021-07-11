package com.e_rental.owner.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SignUpRequest {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Integer gender;
    private Integer provinceId;
    private String address;
}
