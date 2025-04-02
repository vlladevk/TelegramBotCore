package org.pl.pcz.yevkov.tgbottest.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.dto.UserChatCreateDto;

import org.pl.pcz.yevkov.tgbottest.entity.Chat;
import org.pl.pcz.yevkov.tgbottest.entity.User;
import org.pl.pcz.yevkov.tgbottest.entity.UserChat;
import org.pl.pcz.yevkov.tgbottest.repository.ChatRepository;
import org.pl.pcz.yevkov.tgbottest.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserChatCreateMapper implements Mapper<UserChatCreateDto, UserChat> {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public UserChat mapFrom(@NonNull UserChatCreateDto dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = chatRepository.findById(dto.chatId()).orElseThrow(() -> new RuntimeException("User not found"));

        return UserChat.builder()
                .chat(chat)
                .user(user)
                .remainingTokens(chat.getHourLimit())
                .userRole(dto.userRole())
                .build();
    }
}