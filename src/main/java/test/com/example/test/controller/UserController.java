package test.com.example.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import test.com.example.test.configuration.JWTUtil;
import test.com.example.test.repository.UserRepository;
import test.com.example.test.service.UserService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping("/users")
@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository repo;
    private final UserService service;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTUtil jwtTokenUtil;

    public UserController(UserService service, BCryptPasswordEncoder passwordEncoder, JWTUtil jwtTokenUtil, UserRepository repo) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.repo = repo;
    }

    @RequestMapping(method = GET, path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> userGet() {
        return ReactiveSecurityContextHolder.getContext()
                .map(c -> c.getAuthentication().getPrincipal().toString())
                .flatMap( c -> service.findByUsername(c)
                        .map(list -> ResponseEntity.ok().body(list)));
    }

}
