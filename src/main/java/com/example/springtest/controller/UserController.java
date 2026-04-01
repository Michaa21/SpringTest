package com.example.springtest.controller;

import com.example.springtest.api.UserApi;
import com.example.springtest.api.dto.request.UserCreateRequest;
import com.example.springtest.api.dto.response.UserResponse;
import com.example.springtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> createUser(UserCreateRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID id, UserCreateRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

