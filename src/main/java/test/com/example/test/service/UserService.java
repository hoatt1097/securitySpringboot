package test.com.example.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import test.com.example.test.model.User;
import test.com.example.test.repository.UserRepository;

import java.util.Date;

@Service
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        return userRepository.findByUsername(s);
    }

    public Mono<User> updateLastLogin(String username) {
        return userRepository.findUserByUsername(username);
    }
}
