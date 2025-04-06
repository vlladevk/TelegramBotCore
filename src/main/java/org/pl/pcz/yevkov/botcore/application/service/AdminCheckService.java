package org.pl.pcz.yevkov.botcore.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.botcore.application.command.response.ChatMemberInfo;
import org.pl.pcz.yevkov.botcore.application.command.response.GetChatMemberRequest;
import org.pl.pcz.yevkov.botcore.infrastructure.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;

import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminCheckService {
    private final BotApiAdapter botApiAdapter;

    public boolean isUserAdmin(@NonNull ChatId chatId, @NonNull UserId userId) {
        GetChatMemberRequest chatMemberRequest = GetChatMemberRequest.builder()
                .userId(userId)
                .chatId(chatId)
                .build();
        ChatMemberInfo member = botApiAdapter.execute(chatMemberRequest);

        var status = member.status();
        return status == UserRole.CHAT_ADMIN || status == UserRole.CHAT_OWNER;
    }
}
