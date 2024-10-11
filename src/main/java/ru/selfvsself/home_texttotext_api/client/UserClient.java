package ru.selfvsself.home_texttotext_api.client;

import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.database.User;
import ru.selfvsself.home_texttotext_api.repository.UserRepository;

import java.util.Optional;

@Service
public class UserClient {

    private final UserRepository userRepository;

    public UserClient(UserRepository userRepository) {
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