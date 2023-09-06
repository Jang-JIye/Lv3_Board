package com.sparta.lv3_board.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
//로깅에 대한 추상 레이어를 제공하는 인터페이스의 모음
//로깅 = 프로그램이 동작하면서 발생하는 다양한 정보를 저장하는 기록, 로그(log)를 생성하도록 시스템을 작성하는 활동
//@Slf4j 어노테이션을 달면, 해당 클래스 내에서 log라는 인스턴스 사용이 가능함
@Component
@RequiredArgsConstructor
public class JwtUtil {
// Jwt 데이터 영역

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token 식별자 (토큰 생성 시 앞에 붙음)
    public static final String BEARER_PREFIX = "Bearer ";
    // Token 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분


    @Value("${jwt.secret.key}")
    // Base64 Encode 한 SecretKey
    // Application.properties 에 넣어놓은 값을 가져옴

    private String secretKey;
    private Key key; // Token 생성 시 넣을 Key 값
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    //로그
    public static final Logger logger = LoggerFactory.getLogger("JWT 로그");

    @PostConstruct
    // 의존성 주입 후 초기화를 하는 메서드
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); // 디코드
        key = Keys.hmacShaKeyFor(bytes); // 주어진 byte 배열을 기반으로 HMAC-SHA 키를 생성,WT(JSON Web Token) 서명을 생성하거나 검증하는 데 사용
    }

// JWT 생성 영역

    //토큰 생성
    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별값 (id)
                        .setIssuedAt(date) // 발급일
                        .setExpiration(new Date(date.getTime()+TOKEN_TIME)) // 만료 시간
                        .signWith(key, signatureAlgorithm) //암호화 알고리즘
                        .compact();// String 형식의 JWT 토큰으로 반환
    }

    public void addJwtToCookie(String token, HttpServletResponse res) throws IOException {
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8.toString()); // URL 인코딩 -> 토큰에 포함된 특수문자 처리
        // "Authorization" 헤더에 인코딩된 토큰 값을 추가
        res.setHeader("Authorization",encodedToken);
    }

    public String subStringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX))
        {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // JWT를 전송하기 위해 사용
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { //bearerToken 이 유효한 값인지 확인
            return bearerToken.substring(7); //문자열에서 인덱스 7부터 끝까지의 부분 문자열을 추출
        }
        return null;
    }

    //토큰 검증
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            //Jwts.parserBuilder().setSigningKey(key)
            // -> JWT 생성하는 빌더객체 생성 후 JWT 설정키를 설정(JWT 생성 시 사용된 키값과 일치할 것) =>  서명 키를 사용하여 JWT의 유효성을 검증
            //.build() : JWT 빌드
            //parseClaimsJws(token) : 주어진 JWT 토큰으로 클레임 추출
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            //보안, 형식, 서명이 유효하지 않은 경우  log.error 를 사용하여 로그 메시지 기록
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    //토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

    }
}