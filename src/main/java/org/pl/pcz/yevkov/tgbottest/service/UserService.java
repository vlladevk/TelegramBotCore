package org.pl.pcz.yevkov.tgbottest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.dto.user.UserCreateDto;
import org.pl.pcz.yevkov.tgbottest.dto.user.UserReadDto;
import org.pl.pcz.yevkov.tgbottest.mapper.user.UserCreateMapper;
import org.pl.pcz.yevkov.tgbottest.mapper.user.UserReadMapper;
import org.pl.pcz.yevkov.tgbottest.repository.UserRepository;
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

    public Optional<UserReadDto> findUserById(Long userId) {
        log.debug("Searching for User with id={}", userId);
        var userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            log.debug("User found: id={}", userId);
        } else {
            log.warn("User not found: id={}", userId);
        }
        return userOptional.map(userReadMapper::mapFrom);
    }

}
