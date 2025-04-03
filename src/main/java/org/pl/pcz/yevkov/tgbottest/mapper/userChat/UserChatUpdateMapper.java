package org.pl.pcz.yevkov.tgbottest.mapper.userChat;

import org.pl.pcz.yevkov.tgbottest.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.tgbottest.entity.UserChat;
import org.pl.pcz.yevkov.tgbottest.mapper.Updater;
import org.springframework.stereotype.Component;

@Component
public class UserChatUpdateMapper implements Updater<UserChatUpdateDto, UserChat> {
    @Override
    public void updateFromDto(UserChatUpdateDto dto, UserChat entity) {
        if (dto.remainingTokens() != null) {
            entity.setRemainingTokens(dto.remainingTokens());
        }
        if (dto.userRole() != null) {
            entity.setUserRole(dto.userRole());
        }
    }
}
