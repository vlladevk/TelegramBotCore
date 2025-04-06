package org.pl.pcz.yevkov.botcore.infrastructure.mapper.userChat;

import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.botcore.application.dto.user.UserReadDto;
import org.pl.pcz.yevkov.botcore.domain.entity.UserChat;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.user.UserReadMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.chat.ChatReadMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserChatReadMapper implements Mapper<UserChat, UserChatReadDto> {
    private final UserReadMapper userReadMapper;
    private final ChatReadMapper chatReadMapper;

    @Override
    public UserChatReadDto mapFrom(UserChat userChat) {
        UserReadDto userReadDto = userReadMapper.mapFrom(userChat.getUser());
        ChatReadDto chatReadDto = chatReadMapper.mapFrom(userChat.getChat());

        return UserChatReadDto.builder()
                .id(userChat.getId())
                .userRole(userChat.getUserRole())
                .remainingTokens(userChat.getRemainingTokens())
                .chatReadDto(chatReadDto)
                .userReadDto(userReadDto)
                .build();
    }
}
