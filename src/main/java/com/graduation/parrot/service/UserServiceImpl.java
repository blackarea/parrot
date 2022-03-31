package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static com.graduation.parrot.domain.QBoard.board;
import static com.graduation.parrot.domain.QComment.comment;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                           EntityManager entityManager) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public User create(UserSaveDto userSaveDto) {
        User user;
        if (userSaveDto.getEmail() == null) {
            user = User.builder()
                    .login_id(userSaveDto.getLogin_id())
                    .password(bCryptPasswordEncoder.encode(userSaveDto.getPassword()))
                    .name(userSaveDto.getUsername())
                    .build();
        } else {
            user = User.builder()
                    .login_id(userSaveDto.getLogin_id())
                    .password(bCryptPasswordEncoder.encode(userSaveDto.getPassword()))
                    .name(userSaveDto.getUsername())
                    .email(userSaveDto.getEmail())
                    .build();
        }
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public void updatePassword(String login_id, String password) {
        User user = userRepository.findByLogin_id(login_id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다 login_id = " + login_id));
        user.updatePassword(bCryptPasswordEncoder.encode(password));
    }

    @Transactional
    @Override
    public void updateName(String login_id, String username) {
        User user = userRepository.findByLogin_id(login_id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다 login_id = " + login_id));

        /*게시글의 저자 이름도 변경*/
        queryFactory
                .update(board)
                .where(board.author.eq(user.getName()))
                .set(board.author, username)
                .execute();

        /*댓글의 저자 이름도 변경*/
        queryFactory
                .update(comment)
                .where(comment.author.eq(user.getName()))
                .set(comment.author, username)
                .execute();

        user.updateName(username);
    }

    @Transactional
    @Override
    public void updateEmail(String login_id, String email) {
        User user = userRepository.findByLogin_id(login_id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다 login_id = " + login_id));
        user.updateEmail(email);
    }

    @Override
    public Page<BoardListResponseDto> getUserBoardList(String login_id, Pageable pageable) {
        User user = userRepository.findByLogin_id(login_id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다 login_id = " + login_id));

        List<BoardListResponseDto> userBoardList = queryFactory
                .selectFrom(board)
                .where(board.author.eq(user.getName()))
                .orderBy(board.id.desc())
                .fetch()
                .stream().map(BoardListResponseDto::new).collect(Collectors.toList());

        return new PageImpl<>(userBoardList, pageable, userBoardList.size());
    }

    @Override
    public boolean validateDuplicateUser(String login_id) {
        Optional<User> user = userRepository.findByLogin_id(login_id);
        return user.isPresent();
    }

    @Override
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

}
