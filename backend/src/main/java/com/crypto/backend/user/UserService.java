package com.crypto.backend.user;
import org.springframework.stereotype.Service;
import com.crypto.backend.user.UserRepository;
import java.time.LocalDateTime;
import com.crypto.backend.user.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    // constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signup(UserSignupRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    public User login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return null; // later we’ll handle errors better
        }
        if (!user.getPassword().equals(request.getPassword())) {
            return null;
        }
        return user;
    }

}
