package com.graduation.parrot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class Teaching{

    @Id
    @GeneratedValue
    @Column(name = "teaching_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TeachType teachType;
    private String teachContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    public Teaching(TeachType teachType, String teachContent, User user) {
        this.teachType = teachType;
        this.teachContent = teachContent;
        setUser(user);
    }

    private void setUser(User user) {
        this.user = user;
        user.getTeachings().add(this);
    }
}
