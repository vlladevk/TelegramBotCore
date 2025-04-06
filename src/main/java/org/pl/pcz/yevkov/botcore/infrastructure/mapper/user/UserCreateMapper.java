package org.pl.pcz.yevkov.botcore.infrastructure.mapper.user;

import lombok.NonNull;
import org.pl.pcz.yevkov.botcore.application.dto.user.UserCreateDto;
import org.pl.pcz.yevkov.botcore.domain.entity.User;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.Mapper;
import org.springframework.stereotype.Component;


@Component
public class UserCreateMapper implements Mapper<UserCreateDto, User> {
    @Override
    public User mapFrom(@NonNull UserCreateDto object) {
        return User.builder()
                .id(object.id().value())
                .name(object.name())
                .userName(object.userName())
                .build();
    }
}
