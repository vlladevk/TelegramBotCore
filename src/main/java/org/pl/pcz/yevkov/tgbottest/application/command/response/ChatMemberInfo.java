package org.pl.pcz.yevkov.tgbottest.application.command.response;

import lombok.Builder;
import org.pl.pcz.yevkov.tgbottest.entity.UserRole;
import org.pl.pcz.yevkov.tgbottest.model.vo.UserId;

@Builder
public record ChatMemberInfo(UserId userId, UserRole status) {}
