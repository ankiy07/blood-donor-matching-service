package codes.anmol.blooddonormatchingservice.controller;

import codes.anmol.blooddonormatchingservice.model.LoginRequest;
import codes.anmol.blooddonormatchingservice.model.RegisterRequest;
import codes.anmol.blooddonormatchingservice.model.User;
import codes.anmol.blooddonormatchingservice.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private static final String COOKIE_NAME = "auth_token";
    private static final int COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User createdUser = authService.register(request);
        // Don't leak the password hash back to the client
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", createdUser.getId(),
                        "name", createdUser.getName(),
                        "email", createdUser.getEmail()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);

        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}