package com.example.springtest.service;

import com.example.springtest.api.model.UserCreateRequest;
import com.example.springtest.api.model.UserResponse;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.mapper.ProfileMapper;
import com.example.springtest.mapper.UserApiMapper;
import com.example.springtest.model.Profile;
import com.example.springtest.model.User;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }
}

