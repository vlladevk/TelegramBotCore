package org.pl.pcz.yevkov.tgbottest.mapper.message;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.application.command.response.GetChatMemberRequest;
import org.pl.pcz.yevkov.tgbottest.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;

@Component
public class GetChatMemberMapper implements Mapper<GetChatMemberRequest, GetChatMember> {

    @Override
    public GetChatMember mapFrom(@NonNull GetChatMemberRequest dto) {
        GetChatMember request = new GetChatMember();
        request.setChatId(dto.chatId().value().toString());
        request.setUserId(dto.userId().value());
        return request;
    }
}