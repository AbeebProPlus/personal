package africa.learnspace.web.controller.authcontroller;

import africa.learnspace.usermanagement.request.CreatePasswordRequest;
import africa.learnspace.usermanagement.request.LoginRequest;
import africa.learnspace.usermanagement.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    @PatchMapping()
    public ResponseEntity<?> createPassword(@RequestBody CreatePasswordRequest passwordRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        authService.createPassword(token, passwordRequest.getPassword());
        return ResponseEntity.ok("Password created successfully");
    }

    @PutMapping()
    public ResponseEntity<?> forgetPassword(@RequestBody String email) {
        authService.forgetPassword(email);
        return ResponseEntity.ok("Email Sent Successfully");
    }
}
