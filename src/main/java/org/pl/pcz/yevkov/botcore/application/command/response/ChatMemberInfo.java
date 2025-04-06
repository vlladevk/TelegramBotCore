package org.pl.pcz.yevkov.botcore.application.command.response;

import lombok.Builder;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;

@Builder
public record ChatMemberInfo(UserId userId, UserRole status) {
}
