package org.pl.pcz.yevkov.botcore.infrastructure.mapper.message;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.command.response.ChatMemberInfo;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Component
public class ChatMemberMapper implements Mapper<ChatMember, ChatMemberInfo> {

    @Override
    public ChatMemberInfo mapFrom(@NonNull ChatMember member) {
        return ChatMemberInfo.builder()
                .userId(new UserId(member.getUser().getId()))
                .status(mapTelegramStatusToUserRole(member.getStatus()))
                .build();
    }

    private UserRole mapTelegramStatusToUserRole(String status) {
        return switch (status) {
            case "creator" -> UserRole.CHAT_OWNER;
            case "administrator" -> UserRole.CHAT_ADMIN;
            default -> UserRole.USER;
        };
    }
}