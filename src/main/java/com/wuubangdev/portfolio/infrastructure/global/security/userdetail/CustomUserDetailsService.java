package com.wuubangdev.portfolio.infrastructure.global.security.userdetail;

import com.wuubangdev.portfolio.modules.user.domain.model.User;
import com.wuubangdev.portfolio.modules.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Tuân thủ: Dùng Port thay vì Repository trực tiếp
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lấy dữ liệu từ Domain thông qua Port
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Chuyển đổi từ Domain User sang Spring Security UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(Boolean.FALSE.equals(user.getEnabled()))
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name())) // Dùng enum.name() (ví dụ: "ROLE_ADMIN")
                        .collect(Collectors.toList()))
                .build();
    }
}
