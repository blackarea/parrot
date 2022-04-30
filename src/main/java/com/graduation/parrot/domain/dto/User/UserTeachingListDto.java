package com.graduation.parrot.domain.dto.User;

import com.graduation.parrot.domain.TeachType;
import com.graduation.parrot.domain.Teaching;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserTeachingListDto {

    private Long id;
    private TeachType teachType;
    private String question;

    public UserTeachingListDto(Teaching teaching) {
        this.id = teaching.getId();
        this.teachType = teaching.getTeachType();
        this.question = teaching.getTeachContent().split(",")[0];
    }
}
