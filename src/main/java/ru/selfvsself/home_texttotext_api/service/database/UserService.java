package ru.selfvsself.home_texttotext_api.service.database;

import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.database.User;
import ru.selfvsself.home_texttotext_api.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUserIfNotExists(Long chatId, String name) {
        Optional<User> existingUser = userRepository.findByChatId(chatId);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        User newUser = new User(chatId, name);
        return userRepository.save(newUser);
    }
}