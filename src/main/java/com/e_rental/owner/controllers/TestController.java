package com.e_rental.owner.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<String>> getAll(){
        List<String> list = new ArrayList<String>();
        list.add("Test 1");
        list.add("Test 2");
        return ResponseEntity.ok(list);
    }
}