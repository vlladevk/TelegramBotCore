package org.pl.pcz.yevkov.tgbottest.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pl.pcz.yevkov.tgbottest.application.command.response.ChatMemberInfo;
import org.pl.pcz.yevkov.tgbottest.application.command.response.GetChatMemberRequest;
import org.pl.pcz.yevkov.tgbottest.bot.adapter.BotApiAdapter;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.model.vo.ChatId;

import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;
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
