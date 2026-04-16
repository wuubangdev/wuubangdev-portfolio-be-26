package com.wuubangdev.portfolio.modules.user.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.user.application.dto.UserResponse;
import com.wuubangdev.portfolio.modules.user.application.mapper.UserApplicationMapper;
import com.wuubangdev.portfolio.modules.user.domain.model.Role;
import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryPort userRepository;
    private final UserApplicationMapper mapper;
    private final MessageSource messageSource;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException(getMessage("user.list.empty"));
        }
        return users.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("user.not.found")));
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserRoles(Long id, List<Role> roles) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("user.not.found")));
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        return mapper.toResponse(savedUser);
    }

    private String getMessage(String key, Object... args) {
        Locale locale = getCurrentLocale();
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            return key; // Fallback to key if message not found
        }
    }

    private Locale getCurrentLocale() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return (Locale) attrs.getRequest().getSession().getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE");
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }
}
