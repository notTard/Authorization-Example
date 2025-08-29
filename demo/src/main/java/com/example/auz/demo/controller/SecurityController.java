package com.example.auz.demo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auz.demo.jwt.JwtCore;
import com.example.auz.demo.model.User;
import com.example.auz.demo.model.dto.SigninRequest;
import com.example.auz.demo.model.dto.SignupRequest;
import com.example.auz.demo.model.enums.Role;
import com.example.auz.demo.repo.UserRepository;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }
    
    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name");

        }
        if(userRepository.existsByEmail(signupRequest.getEmail())){

        }
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(Role.ROLE_USER);
    
        // Если это первый пользователь - делаем админом
        if (userRepository.count() == 0) {
            defaultRoles.add(Role.ROLE_ADMIN);
        }
    
        user.setRoles(defaultRoles);
            userRepository.save(user);
            return ResponseEntity.ok("User successfully registered");
        }

    @PostMapping("/signin")
    ResponseEntity<?> signIn(@RequestBody SigninRequest signinRequest){
        System.out.println("Received: " + signinRequest.getUsername() + ", " + signinRequest.getPassword());
        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(),signinRequest.getPassword()));
        }catch(BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }

}

