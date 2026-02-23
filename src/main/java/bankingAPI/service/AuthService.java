package bankingAPI.service;

import bankingAPI.model.User;
import bankingAPI.repository.UserRepository;
import bankingAPI.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String register(String email, String password, String firstName, String lastName, String phone){
        if (userRepository.existsByEmail(email)){
            throw new RuntimeException("Email уже занят");
        }
        if (userRepository.existsByPhone(phone)){
            throw new RuntimeException("Номер телефона уже занят");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .build();

        userRepository.save(user);
        return jwtService.generateToken(email);
    }

    public String login(String email, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return jwtService.generateToken(email);
    }
}
