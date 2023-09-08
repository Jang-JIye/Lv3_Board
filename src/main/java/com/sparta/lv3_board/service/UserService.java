package com.sparta.lv3_board.service;

import com.sparta.lv3_board.dto.SignupRequestDto;
import com.sparta.lv3_board.dto.UserResponseDto;
import com.sparta.lv3_board.entity.User;
import com.sparta.lv3_board.entity.UserRoleEnum;
import com.sparta.lv3_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //회원 가입
    public UserResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 유저 중복확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            return new UserResponseDto("회원가입 실패", HttpStatus.BAD_REQUEST.value());
        }

        /// 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER; // 기본값은 USER

        if (requestDto.getAdminToken() != null && ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
            role = UserRoleEnum.ADMIN; // adminToken이 제공되면 ADMIN으로 설정
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(username, encodedPassword, role);
        userRepository.save(user);

        // DB에 중복된 username 이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
        return new UserResponseDto("회원가입 완료", HttpStatus.CREATED.value());
    }
}
