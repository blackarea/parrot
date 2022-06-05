package com.graduation.parrot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.graduation.parrot.domain.dto.ChatDataDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(of = {"id", "radio", "profile", "contents"})
@NoArgsConstructor
@Entity
public class ChatData {

    @Id @GeneratedValue
    @Column(name = "chat_data_id")
    private Long id;

    private int profile;
    private String contents;
    private int position;
    private String time;
    private int visibility;
    private int textBox;
    private int radio;
    private int timeText;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ChatData(User user, ChatDataDto chatDataDto) {
        this.profile = chatDataDto.getProfile();
        this.contents = chatDataDto.getContents();
        this.position =chatDataDto.getPosition();
        this.time = chatDataDto.getTime();
        this.visibility = chatDataDto.getVisibility();
        this.textBox = chatDataDto.getTextBox();
        this.radio = chatDataDto.getRadio();
        this.timeText = chatDataDto.getTimeText();
        setUser(user);
    }

    public void setUser(User user){
        this.user = user;
        user.getChatDataList().add(this);
    }

    public void updateRadio(int radio) {
        this.radio = radio;
    }
}
