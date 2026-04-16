package com.wuubangdev.portfolio.modules.user.application.service;

import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;
import com.wuubangdev.portfolio.modules.user.application.mapper.UserApplicationMapper;
import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryPort userRepository;
    private final UserApplicationMapper mapper;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserRoles(Long id, List<Role> roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        return mapper.toResponse(savedUser);
    }
}
