package test.com.example.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import test.com.example.test.configuration.JWTUtil;
import test.com.example.test.form.AuthToken;
import test.com.example.test.form.LoginForm;
import test.com.example.test.type.TokenType;
import test.com.example.test.model.User;
import test.com.example.test.repository.UserRepository;
import test.com.example.test.service.UserService;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/token")
@RestController
public class TokenController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository repo;
    private final UserService service;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTUtil jwtTokenUtil;

    public TokenController(UserService service, BCryptPasswordEncoder passwordEncoder, JWTUtil jwtTokenUtil, UserRepository repo) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.repo = repo;
    }

    @RequestMapping(method = POST, path = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> token(@RequestBody @Valid LoginForm newUser) {
        // Check token type
        try {
            logger.info("/************** Generate Token START ********************/");
            return Mono.just(newUser.getTokenType()).defaultIfEmpty(TokenType.ACCESS_TOKEN).flatMap(
                    token -> {
                        if (token == TokenType.ACCESS_TOKEN) {
                            return Mono.zip(service.findByUsername(newUser.getUsername()).defaultIfEmpty(new User()),
                                    service.updateLastLogin(newUser.getUsername()).defaultIfEmpty(new User()))
                                    .flatMap(tuple3 -> {

                                        if (tuple3.getT1().getUsername() == null) {
                                            return Mono.just(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))).map(list -> ResponseEntity.badRequest().body(list));
                                        }

                                        if (!passwordEncoder.matches(newUser.getPassword(), tuple3.getT1().getPassword())) {
                                            return Mono.just(new ResponseEntity<Void>(HttpStatus.OK)).map(list -> ResponseEntity.badRequest().body(list));
                                        }

                                        return Mono.just(new AuthToken(jwtTokenUtil.generateToken(tuple3.getT1(), tuple3.getT2(), TokenType.ACCESS_TOKEN), jwtTokenUtil.generateToken(tuple3.getT1(), tuple3.getT2(), TokenType.REFRESH_TOKEN)))
                                                .map(ResponseEntity::ok);

                                    });
                        }
                        else {

                            return Mono.justOrEmpty(newUser.getRefreshToken()).defaultIfEmpty("").flatMap(refresh -> {
                                if (refresh.equals("")) {
                                    return Mono.just(new ResponseEntity<Void>(HttpStatus.OK)).map(list -> ResponseEntity.badRequest().body(list));
                                } else {
                                    return Mono.just(jwtTokenUtil.getUsernameFromToken(refresh, TokenType.REFRESH_TOKEN)).defaultIfEmpty("").flatMap(username -> {
                                        if (!username.equals("")) {
                                            return Mono.zip(service.findByUsername(username), repo.findUserByUsername(username))
                                                    .map(tuple2 -> new AuthToken(jwtTokenUtil.generateToken(tuple2.getT1(), tuple2.getT2(), TokenType.ACCESS_TOKEN), jwtTokenUtil.generateToken(tuple2.getT1(), tuple2.getT2(), TokenType.REFRESH_TOKEN)))
                                                    .map(ResponseEntity::ok);

                                        } else {
                                            return Mono.just(new ResponseEntity<Void>(HttpStatus.OK)).map(list -> ResponseEntity.badRequest().body(list));
                                        }
                                    });
                                }

                            });
                        }
                        //return Mono.just(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))).map(list -> ResponseEntity.badRequest().body(list));
                    });
        } finally {
            logger.info("/************** Generate Token FINISH ********************/");
        }
    }
}
