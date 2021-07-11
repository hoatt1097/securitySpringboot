package com.e_rental.owner.services;

import com.e_rental.owner.dto.ErrorDto;
import com.e_rental.owner.dto.request.LoginRequest;
import com.e_rental.owner.dto.request.SignUpRequest;
import com.e_rental.owner.dto.responses.OwnerResponse;
import com.e_rental.owner.entities.Owner;
import com.e_rental.owner.entities.OwnerInfo;
import com.e_rental.owner.enums.Role;
import com.e_rental.owner.enums.StatusCode;
import com.e_rental.owner.repositories.OwnerInfoRepository;
import com.e_rental.owner.repositories.OwnerRepository;
import com.e_rental.owner.dto.responses.LoginResponse;
import com.e_rental.owner.dto.responses.UserListResponse;
import com.e_rental.owner.security.SecurityConstants;
import com.e_rental.owner.security.UserAuthenticationProvider;
import com.e_rental.owner.security.UserPrincipal;
import com.e_rental.owner.utils.MessageSourceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerInfoRepository ownerInfoRepository;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    public ResponseEntity<List<UserListResponse>> getAll() {
        List<Owner> ownerList = ownerRepository.findAll();
        List<UserListResponse> response = ownerList.stream().map(user -> {
            UserListResponse res = new UserListResponse();
            res.name = user.getUsername();
            return res;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<LoginResponse> signIn(LoginRequest loginRequest) throws ErrorDto {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getLoginId(),
                            loginRequest.getPassword()
                    )
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            SecurityContextHolder.getContext().setAuthentication(authentication);

            LoginResponse res= new LoginResponse();
            res.setCode(StatusCode.SUCCESS.getCode());
            res.setRole(Role.ROLE_OWNER);
            res.setToken(userAuthenticationProvider.createToken(userPrincipal));
            res.setTokenType(SecurityConstants.TOKEN_PREFIX.strip());
            res.setExpiredTime(SecurityConstants.EXPIRATION_TIME);

            return ResponseEntity.ok(res);

        } catch (AuthenticationException e) {
            throw new ErrorDto(messageSourceUtil.getMessage("account.error"));
        }
    }

    @Transactional
    public ResponseEntity<OwnerResponse> signUp(SignUpRequest signUpRequest) throws ErrorDto {

        //TODO: Add validate signUpRequest

        if (ownerRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ErrorDto(messageSourceUtil.getMessage("account.exist"));
        }

        Owner owner = objectMapper.convertValue(signUpRequest, Owner.class);
        owner.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Create ownerInfo
        OwnerInfo ownerInfo = new OwnerInfo();
        ownerInfo.setGender(signUpRequest.getGender());
        ownerInfo.setFirstName(signUpRequest.getFirstName());
        ownerInfo.setLastName(signUpRequest.getLastName());
        ownerInfo.setAddress(signUpRequest.getAddress());
        ownerInfo.setProvinceId(signUpRequest.getProvinceId());

        owner.setInfo(ownerInfo);
        owner.setHasInfo(true);
        ownerInfo.setOwner(owner);
        ownerRepository.save(owner);

        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setUsername(owner.getUsername());
        ownerResponse.setPassword(owner.getPassword());
        ownerResponse.setEmail(owner.getEmail());
        ownerResponse.setFirstName(ownerInfo.getFirstName());
        ownerResponse.setLastName(ownerInfo.getLastName());
        ownerResponse.setGender(ownerInfo.getGender());
        ownerResponse.setProvinceId(ownerInfo.getProvinceId());
        ownerResponse.setAddress(ownerInfo.getAddress());

        return new ResponseEntity<OwnerResponse>(ownerResponse, HttpStatus.CREATED);
    }
}
