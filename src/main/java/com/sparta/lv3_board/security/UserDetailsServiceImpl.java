package com.sparta.lv3_board.security;

import com.sparta.lv3_board.entity.User;
import com.sparta.lv3_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService { // 사용자 정보 로드

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) //username 호출
            throws UsernameNotFoundException { // username 이 존재하지 않는 경우 예외처리

        User user = userRepository.findByUsername(username) // user name 조회
                // 데이터가 비어있을 경우 예외처리("Not Found " + username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        return new UserDetailsImpl(user); // 조회된 정보를 UserDetailsImpl 로 반환 => 사용자 인증 및 권한관리에 사용됨
    }
}