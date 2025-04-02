package org.pl.pcz.yevkov.tgbottest.mapper;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.UserReadDto;
import org.pl.pcz.yevkov.tgbottest.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {
    @Override
    public UserReadDto mapFrom(@NonNull User user) {
        return new UserReadDto(user.getId(), user.getName(), user.getUserName());
    }
}
