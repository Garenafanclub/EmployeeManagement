package com.example.EmpManagement.Controller;

import com.example.EmpManagement.Service.Imp.JWTService;
import com.example.EmpManagement.DTOs.AuthRequestDTO;
import com.example.EmpManagement.DTOs.AuthResponseDTO;
import com.example.EmpManagement.DTOs.ChangePasswordRequestDTO;
import com.example.EmpManagement.Exceptions.BadRequestException;
import com.example.EmpManagement.Exceptions.ResourceNotFoundException;
import com.example.EmpManagement.Model.User;
import com.example.EmpManagement.Repository.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("${api.version}/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JWTService jwtService, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO){

        // Hand the raw email and password to the Spring Security Engine.
        // It will automatically check the database via CustomUserDetailsService and verify the hash.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword())
        );

        // Extract the verified User details from the engine
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken();

        assert userDetails != null;
        AuthResponseDTO responseDTO = AuthResponseDTO.builder()
                .token(token)
                .email(userDetails.getUsername())
                // Get the role (e.g., "ROLE_ADMIN") and return it to the client
                .role(userDetails.getAuthorities().iterator().next().getAuthority())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePass(@RequestBody ChangePasswordRequestDTO passwordRequestDTO, Principal principal)
    {
        String loggedEmail = principal.getName();

        User user = userRepo.findByEmail(loggedEmail)
                .orElseThrow(()-> new ResourceNotFoundException("Employee", "Email", loggedEmail));

        if(!passwordEncoder.matches(passwordRequestDTO.getOldPassword(), user.getPassword()))
        {
            throw new BadRequestException("The old password provided is incorrect");
        }

        user.setPassword(passwordEncoder.encode(passwordRequestDTO.getNewPassword()));
        userRepo.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }
}
