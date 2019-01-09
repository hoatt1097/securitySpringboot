package test.com.example.test.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import test.com.example.test.model.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {
    @Query("{'username' : ?0}")
    Mono<User> findUserByUsername(String username);


    @Query("{'username' : ?0}")
    Mono<UserDetails> findByUsername(String username);
}
