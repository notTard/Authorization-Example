package com.example.auz.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.auz.demo.model.User;
import com.example.auz.demo.model.enums.Role;
import com.example.auz.demo.repo.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void whenSaveUser_thenRolesAreSaved() {
        // Подготовка данных
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Set.of(Role.ROLE_USER));
        
        // Сохранение
        User savedUser = userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        
        // Проверка
        User foundUser = userRepository.findById(savedUser.getId()).orElseThrow();
        
        // Проверки через стандартные assert-методы
        assertNotNull(foundUser, "Пользователь должен существовать");
        assertEquals(1, foundUser.getRoles().size(), "Должна быть 1 роль");
        assertTrue(foundUser.getRoles().contains(Role.ROLE_USER), "Должна быть роль ROLE_USER");
    }
}
