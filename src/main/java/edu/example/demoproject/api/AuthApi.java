package edu.example.demoproject.api;


import edu.example.demoproject.dtos.auth.JwtRequest;
import edu.example.demoproject.dtos.user.UserCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(AuthApi.DICTS_API_PATH)
@Tag(name = "Methods to work with authentication", description = AuthApi.DICTS_API_PATH)
public interface AuthApi {
    String DICTS_API_PATH = "/api/auth";

    @GetMapping("/")
    @Operation(summary = "Get the page for registration new user")
    String getRegPage();

    @PostMapping("/registration")
    @Operation(summary = "Registration new user")
    ResponseEntity<?> createNewUser(@RequestBody @Valid UserCreateDto dto);

    @PostMapping("/getAuth")
    @Operation(summary = "Create authentication token")
    ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest);
}
