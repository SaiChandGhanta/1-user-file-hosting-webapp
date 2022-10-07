package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.exception.CustomErrorException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/v1")
@SecurityRequirement(name = "webservice-user-api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public List<User> getAllusers() {
        return userService.getAllUsers();
    }

    @PostMapping(value = "/account")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/v1/user").toUriString());
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.created(uri)
                    .body(savedUser);
        } catch (CustomErrorException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomErrorException(e.getMessage(), HttpStatus.UNAUTHORIZED, user);
        }

    }

    @PutMapping(value = "/account/{id}")

    public ResponseEntity updateUserInfo(@RequestBody Map<String, Object> body, @PathVariable String id) throws Exception {
        String userName = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
        User userById = userService.getByUserId(id)
                .orElseThrow(() ->
                        new CustomErrorException("Cannot find given user id", HttpStatus.BAD_REQUEST));

        if (!userById.getUsername().equals(userName))
            throw new CustomErrorException("Trying to access other user", HttpStatus.FORBIDDEN);
        if (body.containsKey("id") || body.containsKey("account_created") || body.containsKey("account_updated"))
            throw new CustomErrorException("Cannot update given fields", HttpStatus.BAD_REQUEST);


        User user = new User();
        user.setUsername(userName);
        if (body.containsKey("first_name")) user.setFirst_name(body.get("first_name").toString());
        if (body.containsKey("last_name")) user.setLast_name(body.get("last_name").toString());
        if (body.containsKey("password")) user.setPassword(body.get("password").toString());
        userService.updateUser(user);
        return ResponseEntity.noContent().build();

    }

    @GetMapping(value = "/account/{id}")
    public ResponseEntity<User> getUserInfo(@PathVariable String id) {
        String userName = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
        User user = userService.getByUserId(id)
                .orElseThrow(() ->
                        new CustomErrorException("Cannot find given user id", HttpStatus.BAD_REQUEST));

        if (!user.getUsername().equals(userName))
            throw new CustomErrorException("Trying to access other user", HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(userService.getUser(userName));
    }

}