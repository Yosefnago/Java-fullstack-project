package com.v1.service;


import com.v1.entity.AccountantUser;
import com.v1.repository.UserRepository;
import com.vaadin.flow.component.notification.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountantUser saveUser(String username, String password, String email, String phone, String licenseNumber) {
        if (userRepository.findByUsername(username) != null) {
            Notification.show("User with this username already exists", 3000, Notification.Position.TOP_CENTER);
            return null;
        }

        String encodedPassword = passwordEncoder.encode(password);

        AccountantUser user = new AccountantUser();
        user.setUsername(username);
        user.setEmailAddress(email);
        user.setPhone(phone);
        user.setLicenseNumber(licenseNumber);
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public AccountantUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
