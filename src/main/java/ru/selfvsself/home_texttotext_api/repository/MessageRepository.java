package ru.selfvsself.home_texttotext_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.selfvsself.home_texttotext_api.model.database.Message;
import ru.selfvsself.home_texttotext_api.model.database.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
}