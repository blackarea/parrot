package com.graduation.parrot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "login_id", "password"})
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String login_id;

    @JsonIgnore
    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 15, unique = true)
    private String name;

    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Teaching> teachings = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "parrot_state_id")
    private ParrotState parrotState;

    @Builder
    public User(String login_id, String password, String name, String email) {
        this.login_id = login_id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateName(String username){
        this.name = username;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void setParrotState(ParrotState parrotState){
        this.parrotState = parrotState;
    }
}
