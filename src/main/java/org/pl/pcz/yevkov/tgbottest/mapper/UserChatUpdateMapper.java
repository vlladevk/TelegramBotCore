package org.pl.pcz.yevkov.tgbottest.mapper;

import org.pl.pcz.yevkov.tgbottest.dto.UserChatUpdateDto;
import org.pl.pcz.yevkov.tgbottest.entity.UserChat;
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
