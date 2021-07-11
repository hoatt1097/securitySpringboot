package com.e_rental.owner.handling;

import com.e_rental.owner.dto.ErrorDto;
import com.e_rental.owner.enums.StatusCode;
import com.e_rental.owner.dto.responses.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@ControllerAdvice(basePackages = "com.e_rental.owner.controllers")
public class GlobalExceptionHandling extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(ErrorDto.class)
    private ResponseEntity<? extends Response> processErrorDto(ErrorDto err) {
        Response res = new Response();
        res.setCode(StatusCode.BAD_REQUEST.getCode());
        res.setMessage(err.getMessage());
        return ResponseEntity.badRequest().body(res);
    }
}
