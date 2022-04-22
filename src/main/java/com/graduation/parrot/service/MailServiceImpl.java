package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public boolean findPassword(String email) {
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isEmpty()) {
            return false;
        }
        String password = getRandomPassword(10);

        foundUser.get().updatePassword(bCryptPasswordEncoder.encode(password));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Parrot 임시 비밀번호");
        message.setText("Parrot을 이용해주셔서 감사합니다. \n 임시 비밀번호는 " + password + " 입니다.");
        javaMailSender.send(message);
        return true;
    }

    public String getRandomPassword(int size) {
        char[] charSet = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&'};
        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());
        int idx = 0;
        int len = charSet.length;
        for (int i = 0; i < size; i++) {
            idx = sr.nextInt(len);
            sb.append(charSet[idx]);
        }
        return sb.toString();
    }

}
