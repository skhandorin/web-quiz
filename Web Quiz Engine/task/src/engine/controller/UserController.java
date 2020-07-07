package engine.controller;

import engine.UserAlreadyExistsException;
import engine.model.User;
import engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "/api/register")
    public User register (@Valid @RequestBody User newUser) {
        User old = userRepository.findByEmail(newUser.getEmail());
        if (old != null) {
            throw new UserAlreadyExistsException(newUser.getEmail());
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    @GetMapping("/api/users")
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
}
