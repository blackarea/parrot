package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.User.QUserActivityDto;
import com.graduation.parrot.domain.dto.User.UserActivityListDto;
import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Stream;

import static com.graduation.parrot.domain.QBoard.board;
import static com.graduation.parrot.domain.QComment.comment;
import static com.graduation.parrot.domain.QRecommend.recommend;

@Service
@Slf4j
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
    public boolean updatePassword(String login_id, String oldPassword, String newPassword) {
        User user = userRepository.findByLogin_id(login_id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다 login_id = " + login_id));

        if(confirmPassword(oldPassword, user.getPassword())){
            user.updatePassword(bCryptPasswordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }

    boolean confirmPassword(String rawPassword, String encodedPassword){
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
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
    public Page<UserActivityListDto> getUserActivityPaging(String login_id, Pageable pageable) {

        List<UserActivityListDto> boardList = queryFactory
                .select(new QUserActivityDto(board.title, board.author))
                .from(board)
                .where(board.user.login_id.eq(login_id))
                .fetch().stream().map(userActivityDto -> new UserActivityListDto("board", userActivityDto))
                .collect(Collectors.toList());

        List<UserActivityListDto> commentList = queryFactory
                .select(new QUserActivityDto(board.title, board.author))
                .from(board)
                .join(board.comments, comment)
                .where(comment.user.login_id.eq(login_id))
                .fetch().stream().map(userActivityDto -> new UserActivityListDto("comment", userActivityDto))
                .collect(Collectors.toList());

        List<UserActivityListDto> recommendList = queryFactory
                .select(new QUserActivityDto(board.title, board.author))
                .from(board)
                .join(board.recommends, recommend)
                .where(recommend.user.login_id.eq(login_id))
                .fetch().stream().map(userActivityDto -> new UserActivityListDto("recommend", userActivityDto))
                .collect(Collectors.toList());

        List<UserActivityListDto> allList = Stream.concat(boardList.stream(), commentList.stream()).collect(Collectors.toList());
        allList.addAll(recommendList);

        return new PageImpl<>(allList, pageable, allList.size());
    }

    @Override
    public boolean validateDuplicateUser(String login_id) {
        Optional<User> user = userRepository.findByLogin_id(login_id);
        return user.isPresent();
    }

    @Override
    public boolean validateDuplicateUsername(String username) {
        Optional<User> user = userRepository.findByName(username);
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
