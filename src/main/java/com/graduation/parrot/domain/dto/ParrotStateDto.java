package com.graduation.parrot.domain.dto;

import com.graduation.parrot.domain.ParrotState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ParrotStateDto {

    private String hunger;
    private String stress;
    private String boredom;

    private String affection;
    private String level;

    private String counter;
    private long lastTime;

    @Builder
    public ParrotStateDto(String hunger, String stress, String boredom, String affection, String level, String counter, long lastTime) {
        this.hunger = hunger;
        this.stress = stress;
        this.boredom = boredom;
        this.affection = affection;
        this.level = level;
        this.counter = counter;
        this.lastTime = lastTime;
    }

    public ParrotStateDto(ParrotState parrotState) {
        this.hunger = parrotState.getHunger();
        this.stress = parrotState.getStress();
        this.boredom = parrotState.getBoredom();
        this.affection = parrotState.getAffection();
        this.level = parrotState.getLevel();
        this.counter = parrotState.getCounter();
        this.lastTime = parrotState.getLastTime();
    }

    public ParrotState toEntity(){
        return ParrotState.builder()
                .hunger(hunger)
                .stress(stress)
                .boredom(boredom)
                .affection(affection)
                .level(level)
                .lastTime(lastTime)
                .counter(counter)
                .build();
    }
}
