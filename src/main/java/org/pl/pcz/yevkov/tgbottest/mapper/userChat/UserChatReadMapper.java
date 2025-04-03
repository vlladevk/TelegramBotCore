package org.pl.pcz.yevkov.tgbottest.mapper.userChat;

import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.tgbottest.dto.user.UserReadDto;
import org.pl.pcz.yevkov.tgbottest.entity.UserChat;
import org.pl.pcz.yevkov.tgbottest.mapper.Mapper;
import org.pl.pcz.yevkov.tgbottest.mapper.user.UserReadMapper;
import org.pl.pcz.yevkov.tgbottest.mapper.chat.ChatReadMapper;
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
        return new UserChatReadDto(
                userChat.getId(),
                userChat.getUserRole(),
                userChat.getRemainingTokens(),
                chatReadDto,
                userReadDto
        );
    }
}
