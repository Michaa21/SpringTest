package com.example.springtest.service;

import com.example.springtest.api.dto.request.UserCreateRequest;
import com.example.springtest.api.dto.response.UserResponse;
import com.example.springtest.domain.User;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.mapper.UserApiMapper;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserApiMapper userApiMapper;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        User user = userApiMapper.toEntity(request);
        linkProfile(user);
        User saved = userRepository.save(user);
        return userApiMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        return userApiMapper.toResponse(findUser(id));
    }

    @Transactional
    public UserResponse update(UUID id, UserCreateRequest request) {
        User user = findUser(id);
        userApiMapper.update(request, user);
        linkProfile(user);
        return userApiMapper.toResponse(user);
    }

    @Transactional
    public void delete(UUID id) {
        userRepository.delete(findUser(id));
    }

    @Transactional
    public User findUser(UUID id) {
        return userRepository.findWithProfileById(id)
                .orElseThrow(() -> {
                    log.warn("User with id {} not found", id);
                    return new EntityNotFoundException("User", id);
                });
    }

    private void linkProfile(User user) {
        if (user.getProfile() == null) {
            return;
        }
        user.getProfile().setUser(user);
    }
}

