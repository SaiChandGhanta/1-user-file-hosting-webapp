package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.exception.CustomErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) throws Exception {
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new CustomErrorException("UserName must be set", HttpStatus.BAD_REQUEST);
        if (!isValidMail(user.getUsername()))
            throw new CustomErrorException("Invalid mail for user : " + user.getUsername(), HttpStatus.BAD_REQUEST);
        User prevUser = userRepository.findByUsername(user.getUsername());
        if (prevUser != null) {
            throw new CustomErrorException("Mail already exists", HttpStatus.BAD_REQUEST, prevUser);
        }
        user.setAccount_created(new Date());
        user.setAccount_updated(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user) throws Exception {
        User prevUser = userRepository.findByUsername(user.getUsername());
        if (user.getAccount_updated() != null ||
                user.getAccount_created() != null ||
                user.getId() != null
        ) throw new CustomErrorException("Cannot update given fields", HttpStatus.BAD_REQUEST, prevUser);
        if (user.getLast_name() == null && user.getPassword() == null && user.getFirst_name() == null)
            throw new CustomErrorException("Nothing to update", HttpStatus.BAD_REQUEST, prevUser);
        Set<ConstraintViolation<User>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(user);
        if (!violations.isEmpty())
            throw new CustomErrorException("Correct the violations", HttpStatus.BAD_REQUEST, violations.stream().map(ConstraintViolation::getMessage));

        boolean changed = false;
        if (!prevUser.getFirst_name().equals(user.getFirst_name())) {
            prevUser.setFirst_name(user.getFirst_name());
            changed = true;
        }
        if (!prevUser.getLast_name().equals(user.getLast_name())) {
            prevUser.setLast_name(user.getLast_name());
            changed = true;
        }

        if (!passwordEncoder.matches(user.getPassword(), prevUser.getPassword())) {
            prevUser.setPassword(passwordEncoder.encode(user.getPassword()));
            changed = true;
        }

        if (changed) prevUser.setAccount_updated(new Date());
        else {
            throw new CustomErrorException("Nothing to update", HttpStatus.BAD_REQUEST, prevUser);
        }
        return userRepository.save(prevUser);
    }

    public User getUser(String userName) {
        return userRepository.findByUsername(userName);
    }

    public Optional<User> getByUserId(String uuid) {
        return userRepository.findById(UUID.fromString(uuid));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user = getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found in users DB");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    private boolean isValidMail(String mail) {
        String regex = "^\\S+@\\S+\\.\\S+$";
        return Pattern.compile(regex)
                .matcher(mail)
                .matches();
    }
}
