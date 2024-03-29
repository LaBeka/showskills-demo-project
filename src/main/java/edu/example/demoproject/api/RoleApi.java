package edu.example.demoproject.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(RoleApi.DICTS_API_PATH)
@Tag(name = "Methods to work with Roles", description = RoleApi.DICTS_API_PATH)
public interface RoleApi {
    String DICTS_API_PATH = "/api/roles";

    @GetMapping("/getRoles")
    @Operation(summary = "To Get list of Roles")
    ResponseEntity getListRoles();
}
