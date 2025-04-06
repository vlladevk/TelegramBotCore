package org.pl.pcz.yevkov.botcore.infrastructure.repository;

import org.pl.pcz.yevkov.botcore.domain.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {
    Optional<UserChat> findUserChatByChatIdAndUserId(Long chatId, Long userId);
    List<UserChat> findAllByUserNameAndChatId(String name, Long chatId);
    List<UserChat> findAllByChatId(Long chatId);
    Optional<UserChat> findUserChatByUserUserNameAndChatId(String userName, Long chatId);
}
