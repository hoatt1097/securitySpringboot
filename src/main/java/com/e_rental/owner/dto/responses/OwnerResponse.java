package com.e_rental.owner.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResponse extends Response {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Integer gender;
    private Integer provinceId;
    private String address;
}
