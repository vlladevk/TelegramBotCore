package org.pl.pcz.yevkov.tgbottest.mapper;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.UserCreateDto;
import org.pl.pcz.yevkov.tgbottest.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserCreateMapper implements Mapper<UserCreateDto, User> {
    @Override
    public User mapFrom(@NonNull UserCreateDto object) {
        return User.builder()
                .id(object.Id())
                .name(object.Name())
                .userName(object.UserName())
                .build();
    }
}
