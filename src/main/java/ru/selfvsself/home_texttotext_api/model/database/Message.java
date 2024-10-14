package ru.selfvsself.home_texttotext_api.model.database;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.selfvsself.home_texttotext_api.model.client.Role;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@ToString
@Table(name = "messages")
public class Message {

    @Id
    @Builder.Default
    @Column(unique = true, nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "user_id", unique = true, nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Integer tokens;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamp with time zone")
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp with time zone")
    private ZonedDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column()
    private String model;

    @Column(name = "request_id")
    private UUID requestId;

    public Message() {
        this.id = UUID.randomUUID();
    }
}