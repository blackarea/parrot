package com.graduation.parrot.service;

import com.graduation.parrot.domain.ParrotState;
import com.graduation.parrot.domain.TeachType;
import com.graduation.parrot.domain.Teaching;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.ParrotStateDto;
import com.graduation.parrot.domain.dto.User.*;
import com.graduation.parrot.repository.ParrotStateRepository;
import com.graduation.parrot.repository.TeachingRepository;
import com.graduation.parrot.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
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
import static com.graduation.parrot.domain.QRecommend.recommend;
import static com.graduation.parrot.domain.QTeaching.teaching;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final ParrotStateRepository parrotStateRepository;
    private final JPAQueryFactory queryFactory;
    private final TeachingRepository teachingRepository;

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
                           ParrotStateRepository parrotStateRepository, EntityManager entityManager, TeachingRepository teachingRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.parrotStateRepository = parrotStateRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.teachingRepository = teachingRepository;
    }

    @Transactional
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
        User savedUser = userRepository.save(user);

        ParrotState savedParrotState = parrotStateRepository.save(new ParrotState());
        savedUser.setParrotState(savedParrotState);
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
    public UserActivityPageDto getUserActivityPaging(String login_id, Pageable pageable) {
        List<UserActivityListDto> boardList = queryFactory
                .select(new QUserActivityDto(board.id, board.title, board.author, board.createdDate))
                .from(board)
                .where(board.user.login_id.eq(login_id))
                .fetch().stream().map(userActivityDto -> new UserActivityListDto("게시글", userActivityDto))
                .collect(Collectors.toList());

        List<UserActivityListDto> commentList = queryFactory
                .select(new QUserActivityDto(board.id, board.title, board.author, comment.createdDate))
                .from(board)
                .join(board.comments, comment)
                .where(comment.user.login_id.eq(login_id))
                .fetch().stream().map(userActivityDto -> new UserActivityListDto("댓글", userActivityDto))
                .collect(Collectors.toList());

        List<UserActivityListDto> recommendList = queryFactory
                .select(new QUserActivityDto(board.id, board.title, board.author, recommend.createdDate))
                .from(board)
                .join(board.recommends, recommend)
                .where(recommend.user.login_id.eq(login_id))
                .fetch().stream().map(userActivityDto -> new UserActivityListDto("추천", userActivityDto))
                .collect(Collectors.toList());

        List<UserActivityListDto> allList = new ArrayList<>();
        allList.addAll(boardList);
        allList.addAll(commentList);
        allList.addAll(recommendList);
        //날짜순 정렬
        Comparator<UserActivityListDto> comparingNameNatural =
                Comparator.comparing(UserActivityListDto::getCreatedDate, Comparator.reverseOrder());
        List<UserActivityListDto> sortedAllList =
                allList.stream().sorted(comparingNameNatural).collect(Collectors.toList());

        int start = (int)pageable.getOffset();
        int end = (start+ pageable.getPageSize()) > sortedAllList.size() ? sortedAllList.size() : (start + pageable.getPageSize());

        Page<UserActivityListDto> userActivity = new PageImpl<>(sortedAllList.subList(start,end),pageable,sortedAllList.size());

        return new UserActivityPageDto(boardList.size(), commentList.size(), userActivity);
    }

    @Override
    public void saveTeach(String login_id, TeachType teachType, String teachContent) {
        User user = userRepository.findByLogin_id(login_id).get();
        teachingRepository.save(new Teaching(teachType, teachContent, user));
    }

    @Override
    public String getTeachContent(Long teachingId) {
        return queryFactory
                .selectFrom(teaching)
                .where(teaching.id.eq(teachingId))
                .fetchOne().getTeachContent();
    }

    @Override
    public List<UserTeachingListDto> getUserTeachingList(String login_id) {
        return queryFactory
                .selectFrom(teaching)
                .where(teaching.user.login_id.eq(login_id))
                .orderBy(teaching.id.desc())
                .fetch().stream().map(UserTeachingListDto::new).collect(Collectors.toList());
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

    public void withdraw(String login_id) {
        User user = userRepository.findByLogin_id(login_id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다 login_id = " + login_id));
        userRepository.delete(user);
    }

    @Override
    public ParrotStateDto getParrotState(String login_id) {
        User user = userRepository.findByLogin_id(login_id).get();

        return new ParrotStateDto(user.getParrotState());
    }

    @Transactional
    @Override
    public void setParrotState(String login_id, ParrotStateDto parrotStateDto) {
        User user = userRepository.findByLogin_id(login_id).get();

        ParrotState parrotState = user.getParrotState();
        parrotState.updateState(parrotStateDto.toEntity());
    }
}
