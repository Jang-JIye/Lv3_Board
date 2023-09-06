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
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

//        // 사용자 ROLE 확인
//        UserRoleEnum role = UserRoleEnum.USER;
//        if (requestDto.isAdmin()) {
//            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
//                throw new IllegalArgumentException("admin 번호를 다시 확인해주세요.");
//            }
//            role = UserRoleEnum.ADMIN;
//        }
//        String authkey = requestDto.getAuthKey();
        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(username, password, role);
        userRepository.save(user);
        // DB에 중복된 username 이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
        UserResponseDto userResponseDto = new UserResponseDto("회원가입 완료", 200);
        return userResponseDto;
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
        String token = jwtUtil.createToken(user.getUsername());
        //Response 객체에 추가,
        jwtUtil.addJwtToCookie(token, httpServletResponse);

        // 로그인 성공
        UserResponseDto userResponseDto = new UserResponseDto("로그인 성공!", 200);
        return userResponseDto;
    }

}