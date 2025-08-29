package com.example.auz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.auz.demo.UserDetails.UserDetailsImpl;
import com.example.auz.demo.model.User;
import com.example.auz.demo.repo.UserRepository;

@Service
public class UserService implements UserDetailsService {
    
    private UserRepository userRepository;

    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(
            String.format("User '%s' not found",username)
        ));
        
        return UserDetailsImpl.build(user);
    }
}
