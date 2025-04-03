package org.pl.pcz.yevkov.tgbottest.mapper.user;

import lombok.NonNull;
import org.pl.pcz.yevkov.tgbottest.dto.user.UserReadDto;
import org.pl.pcz.yevkov.tgbottest.entity.User;
import org.pl.pcz.yevkov.tgbottest.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {
    @Override
    public UserReadDto mapFrom(@NonNull User user) {
        return new UserReadDto(user.getId(), user.getName(), user.getUserName());
    }
}
