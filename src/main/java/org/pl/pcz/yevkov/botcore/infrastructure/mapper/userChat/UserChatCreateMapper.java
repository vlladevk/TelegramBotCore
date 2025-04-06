package org.pl.pcz.yevkov.botcore.infrastructure.mapper.userChat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatCreateDto;

import org.pl.pcz.yevkov.botcore.domain.entity.Chat;
import org.pl.pcz.yevkov.botcore.domain.entity.User;
import org.pl.pcz.yevkov.botcore.domain.entity.UserChat;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.pl.pcz.yevkov.botcore.infrastructure.repository.ChatRepository;
import org.pl.pcz.yevkov.botcore.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserChatCreateMapper implements Mapper<UserChatCreateDto, UserChat> {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public UserChat mapFrom(@NonNull UserChatCreateDto dto) {
        User user = userRepository.findById(dto.userId().value()).orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = chatRepository.findById(dto.chatId().value()).orElseThrow(() -> new RuntimeException("User not found"));

        return UserChat.builder()
                .chat(chat)
                .user(user)
                .remainingTokens(chat.getHourLimit())
                .userRole(dto.userRole())
                .build();
    }
}