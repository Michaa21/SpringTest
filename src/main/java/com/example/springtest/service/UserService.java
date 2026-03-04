package com.example.springtest.service;

import com.example.springtest.api.dto.request.UserCreateRequest;
import com.example.springtest.api.dto.response.UserResponse;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.mapper.ProfileMapper;
import com.example.springtest.mapper.UserApiMapper;
import com.example.springtest.domain.Profile;
import com.example.springtest.domain.User;
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
    private final ProfileMapper profileMapper;


    @Transactional
    public UserResponse create(UserCreateRequest request) {
        User user = userApiMapper.toEntity(request);
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
        user.setUsername(request.getUsername());
        if (request.getProfile() != null) {
            if (user.getProfile() == null) {
                Profile profile = profileMapper.toEntity(request.getProfile());
                profile.setUser(user);
                user.setProfile(profile);
            } else {
                profileMapper.update(request.getProfile(),
                        user.getProfile());
            }
        }
        return userApiMapper.toResponse(user);
    }

    @Transactional
    public void delete(UUID id) {
        userRepository.delete(findUser(id));
    }

    @Transactional
    public User findUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with id {} not found", id);
                    return new EntityNotFoundException("User", id);
                });
    }
}

