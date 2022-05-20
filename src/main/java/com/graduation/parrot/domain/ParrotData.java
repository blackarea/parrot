package com.graduation.parrot.domain;

import com.graduation.parrot.domain.dto.User.ParrotDataDto;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@ToString(of = {"id", "page", "date", "pState"})
@Entity
public class ParrotData {

    @Id
    @GeneratedValue
    @Column(name = "parrot_data_id")
    private Long id;

    private int page;
    private String date;
    private int pState;
    private int feed;
    private int feedCount;
    private int playType;
    private int playResult;
    private int chatCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Builder
    public ParrotData(int page, String date, int pState, int feed, int feedCount, int playType, int playResult, int chatCount, User user) {
        this.page = page;
        this.date = date;
        this.pState = pState;
        this.feed = feed;
        this.feedCount = feedCount;
        this.playType = playType;
        this.playResult = playResult;
        this.chatCount = chatCount;
        setUser(user);
    }

    public ParrotData(ParrotDataDto parrotDataDto, User user) {
        this.page = parrotDataDto.getPage();
        this.date = parrotDataDto.getDate();
        this.pState = parrotDataDto.getPState();
        this.feed = parrotDataDto.getFeed();
        this.feedCount = parrotDataDto.getFeedCount();
        this.playType = parrotDataDto.getPlayType();
        this.playResult = parrotDataDto.getPlayResult();
        this.chatCount = parrotDataDto.getChatCount();
        setUser(user);
    }

    public void setUser(User user) {
        this.user = user;
        user.getParrotDataList().add(this);
    }

    public void update(ParrotData parrotData){
        this.page = parrotData.getPage();
        this.date = parrotData.getDate();
        this.pState = parrotData.getPState();
        this.feed = parrotData.getFeed();
        this.feedCount = parrotData.getFeedCount();
        this.playType = parrotData.getPlayType();
        this.playResult = parrotData.getPlayResult();
        this.chatCount = parrotData.getChatCount();
    }
}
