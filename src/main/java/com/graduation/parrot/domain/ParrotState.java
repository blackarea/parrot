package com.graduation.parrot.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@Entity
@Table(name = "PARROT_STATE")
@NoArgsConstructor
public class ParrotState {

    @Id @GeneratedValue
    @Column(name = "parrot_state_id")
    private Long id;

    private String hunger;
    private String stress;
    private String boredom;

    private String affection;
    private String level;

    private String counter;
    private long lastTime;

    @Builder
    public ParrotState(String hunger, String stress, String boredom,
                       String affection, String level, String counter, long lastTime) {
        this.hunger = hunger;
        this.stress = stress;
        this.boredom = boredom;
        this.affection = affection;
        this.level = level;
        this.counter = counter;
        this.lastTime = lastTime;
    }

    public void updateState(ParrotState parrotState){
        this.hunger = parrotState.getHunger();
        this.stress = parrotState.getStress();
        this.boredom = parrotState.getBoredom();
        this.affection = parrotState.getAffection();
        this.level = parrotState.getLevel();
        this.counter = parrotState.getCounter();
        this.lastTime = parrotState.getLastTime();
    }
}
