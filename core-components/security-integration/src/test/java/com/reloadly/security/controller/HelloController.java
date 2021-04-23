package com.reloadly.security.controller;

import com.reloadly.security.model.HelloResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HelloController {

    @ResponseBody
    @GetMapping(path = "/hello", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HelloResponse> hello() {
        return new ResponseEntity<>(new HelloResponse("Hello World!"), HttpStatus.OK);
    }
}
