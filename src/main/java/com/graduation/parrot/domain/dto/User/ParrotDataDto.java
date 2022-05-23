package com.graduation.parrot.domain.dto.User;

import com.graduation.parrot.domain.ParrotData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ParrotDataDto {

    private int page;
    private String date;
    private int parrotState;
    private int feed;
    private int feedCount;
    private int playType;
    private int playResult;
    private int chatCount;

    public ParrotDataDto(int page, String date, int parrotState, int feed, int feedCount, int playType, int playResult, int chatCount) {
        this.page = page;
        this.date = date;
        this.parrotState = parrotState;
        this.feed = feed;
        this.feedCount = feedCount;
        this.playType = playType;
        this.playResult = playResult;
        this.chatCount = chatCount;
    }

    public ParrotDataDto(ParrotData parrotData) {
        this.page = parrotData.getPage();
        this.date = parrotData.getDate();
        this.parrotState = parrotData.getPState();
        this.feed = parrotData.getFeed();
        this.feedCount = parrotData.getFeedCount();
        this.playType = parrotData.getPlayType();
        this.playResult = parrotData.getPlayResult();
        this.chatCount = parrotData.getChatCount();
    }

}
