package com.graduation.parrot.config;


import com.graduation.parrot.domain.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serializable;

@Getter
public class SecurityUser extends org.springframework.security.core.userdetails.User{
    private static final long serialVersionUID = 1L;
    private User user;

    public SecurityUser(User user){
        super(user.getLogin_id(), "{noop}"+user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user=user;
    }

}
