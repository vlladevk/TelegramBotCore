package org.pl.pcz.yevkov.botcore.infrastructure.mapper.message;


import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.command.response.DeleteMessageRequest;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

@Component
public class DeleteMessageMapper implements Mapper<DeleteMessageRequest, DeleteMessage> {

    @Override
    public DeleteMessage mapFrom(@NonNull DeleteMessageRequest dto) {
        return new DeleteMessage(
                dto.chatId().value().toString(),
                dto.messageId().value()
        );
    }
}