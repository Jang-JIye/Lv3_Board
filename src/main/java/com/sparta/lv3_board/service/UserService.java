package com.sparta.lv3_board.service;

import com.sparta.lv3_board.dto.LoginRequestDto;
import com.sparta.lv3_board.dto.SignupRequestDto;
import com.sparta.lv3_board.dto.UserResponseDto;
import com.sparta.lv3_board.entity.User;
import com.sparta.lv3_board.entity.UserRoleEnum;
import com.sparta.lv3_board.jwt.JwtUtil;
import com.sparta.lv3_board.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //회원 가입
    public UserResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 유저 중복확인
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
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
        UserResponseDto userResponseDto = new UserResponseDto("회원가입 완료", 200);
        return userResponseDto;
    }
    //ADMIN_TOKEN 확인하기
    private UserRoleEnum validateAdminToken(String adminToken) {
        if (ADMIN_TOKEN.equals(adminToken)) {
            return UserRoleEnum.ADMIN;
        } else {
            throw new IllegalArgumentException("admin 토큰을 다시 확인하세요.");
        }
    }


    //로그인
    @Transactional(readOnly = true)
    public UserResponseDto login(LoginRequestDto requestDto, HttpServletResponse httpServletResponse) throws IOException {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        //username 확인
        User user = (User) userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        //password 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        //로그인 성공
        //JWT Token 생성 및 쿠키에 저장
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
        //Response 객체에 추가,
        jwtUtil.addJwtToCookie(token, httpServletResponse);

        // 로그인 성공
        UserResponseDto userResponseDto = new UserResponseDto("로그인 성공!", 200);
        return userResponseDto;
    }

}
