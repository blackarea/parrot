package com.graduation.parrot.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecommendDto {

    private boolean canRecommend;
    private int recommendCount;

    public RecommendDto(boolean canRecommend) {
        this.canRecommend = canRecommend;
    }

    public RecommendDto(boolean canRecommend, int recommendCount) {
        this.canRecommend = canRecommend;
        this.recommendCount = recommendCount;
    }
}
