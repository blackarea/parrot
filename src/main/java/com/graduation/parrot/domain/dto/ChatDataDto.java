package com.graduation.parrot.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ChatDataDto {

    private int profile;
    private String contents;
    private int position;
    private String time;
    private int visibility;
    private int textBox;
    private int radio;
    private int timeText;

    public ChatDataDto(int profile, String contents, int position, String time, int visibility, int textBox, int radio, int timeText) {
        this.profile = profile;
        this.contents = contents;
        this.position = position;
        this.time = time;
        this.visibility = visibility;
        this.textBox = textBox;
        this.radio = radio;
        this.timeText = timeText;
    }
}
