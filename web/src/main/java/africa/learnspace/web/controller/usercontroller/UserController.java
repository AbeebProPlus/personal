package africa.learnspace.web.controller.usercontroller;

import africa.learnspace.usermanagement.request.PasswordUpdateRequest;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import africa.learnspace.usermanagement.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/invite")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> inviteColleague(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(userService.inviteColleague(userRegistrationRequest));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal Jwt jwt,
                                                 @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        String email = jwt.getClaim("email");
        return ResponseEntity.ok(userService.changePassword(passwordUpdateRequest, email));
    }
}