package org.pl.pcz.yevkov.tgbottest.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "userChats")
@Builder
public class Chat implements BaseEntity<Long> {
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long hourLimit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatStatus chatStatus;

    @OneToMany(mappedBy = "chat")
    @Builder.Default
    private List<UserChat> userChats = new ArrayList<>();
}
