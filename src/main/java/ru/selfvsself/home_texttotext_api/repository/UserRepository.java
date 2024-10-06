package ru.selfvsself.home_texttotext_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.selfvsself.home_texttotext_api.model.database.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);
}
