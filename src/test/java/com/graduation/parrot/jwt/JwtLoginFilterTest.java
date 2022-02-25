package com.graduation.parrot.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtLoginFilterTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void before(){
        userRepository.deleteAll();
        User user = User.builder()
                .login_id("login")
                .password(bCryptPasswordEncoder.encode("pwd"))
                .name("name")
                .build();

        userRepository.save(user);

        User user2 = User.builder()
                .login_id("login2")
                .password(bCryptPasswordEncoder.encode("pwd2"))
                .name("name")
                .build();

        userRepository.save(user2);
    }

    private URI uri(String path) throws URISyntaxException {
        return new URI(format("http://localhost:%d%s", port, path));
    }

    @DisplayName("1. jwt 로 로그인을 시도한다.")
    @Test
    void test_1() throws URISyntaxException {
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .login_id("login")
                .password("pwd")
                .build();
        HttpEntity<UserSaveDto> body = new HttpEntity<>(userSaveDto);

        ResponseEntity<String> response = restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(302); //redirect to "/"

    }

    @DisplayName("2. admin 유저는 userList 를 가져올 수 있다.")
    @Test
    void test_2() throws URISyntaxException, JsonProcessingException {
        String token = getToken("login", "pwd");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity entity = new HttpEntity("", headers);

        ResponseEntity<String> response = restTemplate.exchange(uri("/api/v1/admin/userlist"),
                HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());
    }

    public String getToken(String login_id, String password) throws URISyntaxException {
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .login_id(login_id)
                .password(password)
                .build();
        HttpEntity<UserSaveDto> body = new HttpEntity<>(userSaveDto);
        ResponseEntity<String> response = restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);
        return response.getHeaders().get("Authorization").get(0).substring("Bearer ".length());
    }

    /*@DisplayName("2. 비번이 틀리면 로그인을 하지 못한다.")
    @Test
    void test_2() throws URISyntaxException {
        UserLogin login = UserLogin.builder().username("user1@test.com")
                .password("1234").build();
        HttpEntity<UserLogin> body = new HttpEntity<>(login);

        assertThrows(HttpClientErrorException.class, ()->{
            restTemplate.exchange(uri("/login"), HttpMethod.POST, body, String.class);
            // expected 401 에러
        });
    }*/

}