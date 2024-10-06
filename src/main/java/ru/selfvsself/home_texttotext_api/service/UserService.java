package ru.selfvsself.home_texttotext_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.database.User;
import ru.selfvsself.home_texttotext_api.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUserIfNotExists(Long chatId, String name) {
        Optional<User> existingUser = userRepository.findByChatId(chatId);
        if (existingUser.isPresent()) {
        }
        User newUser = new User(chatId, name);
        return userRepository.save(newUser);
    }
}