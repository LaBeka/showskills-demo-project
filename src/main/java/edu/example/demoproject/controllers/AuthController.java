package edu.example.demoproject.controllers;

import edu.example.demoproject.api.AuthApi;
import edu.example.demoproject.dtos.auth.JwtRequest;
import edu.example.demoproject.dtos.user.UserCreateDto;
import edu.example.demoproject.dtos.user.UserDto;
import edu.example.demoproject.entities.user.UserEntity;
import edu.example.demoproject.services.AuthService;
import edu.example.demoproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final UserService userService;
    private final AuthService authService;

    @Override
    public String getRegPage() {
        return "main";
    }

    @Override
    public ResponseEntity<?> createNewUser(UserCreateDto dto) {
        Optional<UserEntity> foundUser = userService.findByUsername(dto.getEmail());
        if (foundUser.isPresent()) {
            String data = String.format("Failed operation! There is %s registered. Try again!", dto.getEmail());
            return ResponseEntity.badRequest().body(data);
        }
        UserDto createdUser = userService.registerNewUser(dto);
        return ResponseEntity.ok().body(createdUser);
    }

    @Override
    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }
}
