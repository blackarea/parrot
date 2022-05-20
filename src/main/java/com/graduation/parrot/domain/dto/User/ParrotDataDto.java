package com.graduation.parrot.domain.dto.User;

import com.graduation.parrot.domain.ParrotData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@ToString
@NoArgsConstructor
public class ParrotDataDto {

    private int page;
    private String date;
    private int pState;
    private int feed;
    private int feedCount;
    private int playType;
    private int playResult;
    private int chatCount;

    public ParrotDataDto(int page, String date, int pState, int feed, int feedCount, int playType, int playResult, int chatCount) {
        this.page = page;
        this.date = date;
        this.pState = pState;
        this.feed = feed;
        this.feedCount = feedCount;
        this.playType = playType;
        this.playResult = playResult;
        this.chatCount = chatCount;
    }

    public ParrotDataDto(ParrotData parrotData) {
        this.page = parrotData.getPage();
        this.date = parrotData.getDate();
        this.pState = parrotData.getPState();
        this.feed = parrotData.getFeed();
        this.feedCount = parrotData.getFeedCount();
        this.playType = parrotData.getPlayType();
        this.playResult = parrotData.getPlayResult();
        this.chatCount = parrotData.getChatCount();
    }

    public ParrotData toEntity(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ParrotData.builder()
                .page(page)
                .date(date)
                .pState(pState)
                .feed(feed)
                .feedCount(feedCount)
                .playType(playType)
                .playResult(playResult)
                .chatCount(chatCount)
                .build();
    }
}
