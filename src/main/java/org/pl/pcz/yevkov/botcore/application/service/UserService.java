package org.pl.pcz.yevkov.botcore.application.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.user.UserCreateDto;
import org.pl.pcz.yevkov.botcore.application.dto.user.UserReadDto;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.user.UserCreateMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.user.UserReadMapper;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.pl.pcz.yevkov.botcore.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;

    public void createUser(UserCreateDto userCreateDto) {
        var user = userCreateMapper.mapFrom(userCreateDto);
        log.debug("Mapped User entity: {}", user);
        var saved = userRepository.save(user);
        log.info("Created User: id={}, userFirstName={} userName={}", saved.getId(), saved.getName(), saved.getUserName());
    }

    public Optional<UserReadDto> findUserById(UserId userId) {
        log.debug("Searching for User with id={}", userId);
        var userOptional = userRepository.findById(userId.value());
        if (userOptional.isPresent()) {
            log.debug("User found: id={}", userId);
        } else {
            log.warn("User not found: id={}", userId);
        }
        return userOptional.map(userReadMapper::mapFrom);
    }

}
