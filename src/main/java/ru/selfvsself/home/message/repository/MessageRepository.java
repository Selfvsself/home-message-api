package ru.selfvsself.home.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.selfvsself.home.message.model.database.Message;
import ru.selfvsself.home.message.model.database.MessageStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findTop20ByUserIdAndStatusOrderByUpdatedAtDesc(UUID userId, MessageStatus status);
}
