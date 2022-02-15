package com.graduation.parrot.config.auth;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService.loadUserByUsername");
        Optional<User> optional = userRepository.findByLogin_id(username);

        if(!optional.isPresent()){
            throw new UsernameNotFoundException(username+ "사용자 없음");
        }else{
            User user = optional.get();
            return new PrincipalDetails(user);
        }
    }
}
