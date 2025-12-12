package com.crypto.backend.user;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody UserSignupRequest request) {
        return userService.signup(request);
    }
    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest request) {
        return userService.login(request);
    }

}
