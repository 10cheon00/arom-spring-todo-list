package com.taekcheonkim.todolist.user.service;

import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SignUpFormDto;
import com.taekcheonkim.todolist.user.exception.InvalidSignUpFormException;
import com.taekcheonkim.todolist.user.repository.UserRepository;
import com.taekcheonkim.todolist.user.util.EmailPatternValidator;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public User signUp(Optional<SignUpFormDto> maybeUserFormDto) throws InvalidSignUpFormException {
        if (maybeUserFormDto.isEmpty()) {
            throw new InvalidSignUpFormException("User form is empty.");
        }

        SignUpFormDto signUpFormDto = maybeUserFormDto.get();
        if(signUpFormDto.getNickname().isEmpty()) {
            throw new InvalidSignUpFormException("Nickname is empty.");
        }
        if (userRepository.isExistByEmail(signUpFormDto.getEmail())) {
            throw new InvalidSignUpFormException("Already exists email.");
        }
        EmailPatternValidator emailPatternValidator = new EmailPatternValidator();
        if (!emailPatternValidator.isValidEmail(signUpFormDto.getEmail())) {
            throw new InvalidSignUpFormException("Email is not valid.");
        }

        User user = new User(signUpFormDto);
        userRepository.save(user);

        try {
            // send email
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "Your company name.";

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom("taekcheonkim0205@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            String url = "http://localhost:8080" + "/users/verification?code=" + user.getVerificationCode().toString();
            content = content.replace("[[URL]]", url);

            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    public void verify(String code) {
        if (userRepository.isExistByVerificationCode(code)) {
            User user = userRepository.findByVerificationCode(code);
            user.setEnabled(true);
        }
    }
}
