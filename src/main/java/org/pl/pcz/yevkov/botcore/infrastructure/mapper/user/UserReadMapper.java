package org.pl.pcz.yevkov.botcore.infrastructure.mapper.user;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.user.UserReadDto;
import org.pl.pcz.yevkov.botcore.domain.entity.User;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {
    @Override
    public UserReadDto mapFrom(@NonNull User user) {
        return UserReadDto.builder()
                .id(new UserId(user.getId()))
                .name(user.getName())
                .userName(user.getUserName())
                .build();
    }
}
