package com.graduation.parrot.service;

import com.graduation.parrot.domain.TeachType;
import com.graduation.parrot.domain.Teaching;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.ParrotStateDto;
import com.graduation.parrot.domain.dto.User.UserActivityPageDto;
import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.domain.dto.User.UserTeachingListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public interface UserService {
    User create(UserSaveDto userSaveDto);
    boolean updatePassword(String login_id, String oldPassword, String newPassword);
    void updateName(String login_id, String username);
    void updateEmail(String login_id, String email);
    UserActivityPageDto getUserActivityPaging(String login_id, Pageable pageable);
    void saveTeach(String login_id, TeachType teachType, String teachContent);
    String getTeachContent(Long teachingId);
    List<UserTeachingListDto> getUserTeachingList(String login_id);
    boolean validateDuplicateUser(String login_id);
    boolean validateDuplicateUsername(String username);
    Map<String, String> validateHandling(Errors errors);
    ParrotStateDto getParrotState(String login_id);
    void setParrotState(String login_id, ParrotStateDto parrotStateDto);
}
