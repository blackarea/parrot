package com.graduation.parrot.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TeachFreeDto {
    private String question;
    private int answerCount;
    private List<TeachFreeList> condition;
    private List<TeachFreeList> answer;
    private String notIncludeAnswer;

    public TeachFreeDto(String question, int answerCount, List<TeachFreeList> condition,
                        List<TeachFreeList> answer, String notIncludeAnswer) {
        this.question = question;
        this.answerCount = answerCount;
        this.condition = condition;
        this.answer = answer;
        this.notIncludeAnswer = notIncludeAnswer;
    }
    @ToString
    @Getter
    public static class TeachFreeList {
        private String content;
    }
}
