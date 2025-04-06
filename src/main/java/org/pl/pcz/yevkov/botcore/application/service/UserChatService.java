package org.pl.pcz.yevkov.botcore.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatCreateDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatUpdateDto;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.entity.UserChat;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.userChat.UserChatCreateMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.userChat.UserChatReadMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.userChat.UserChatUpdateMapper;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.pl.pcz.yevkov.botcore.infrastructure.repository.UserChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class UserChatService {
    private final UserChatRepository userChatRepository;
    private final UserChatCreateMapper userChatCreateMapper;
    private final UserChatReadMapper userChatReadMapper;
    private final UserChatUpdateMapper userChatUpdateMapper;

    public void  createUserChat(UserChatCreateDto user) {
        var userChat = userChatCreateMapper.mapFrom(user);
        log.debug("Mapped UserChat entity: {}", userChat);
        var saved =  userChatRepository.save(userChat);
        log.info("Created UserChat: id={}, userId={}, chatId={}", saved.getId(), saved.getUser().getId(), saved.getChat().getId());}

    public List<UserChatReadDto> getAllUserChats() {
        return userChatRepository.findAll().stream().map(userChatReadMapper::mapFrom).toList();
    }

    public Optional<UserChatReadDto> getUserChatBy(ChatId chatId, UserId userId) {
        var userChatOptional = userChatRepository.findUserChatByChatIdAndUserId(chatId.value(), userId.value());
        if (userChatOptional.isPresent()) {
            log.debug("UserChat found for chatId={}, userId={}", chatId, userId);
        } else {
            log.warn("UserChat not found for chatId={}, userId={}", chatId, userId);
        }
        return userChatOptional.map(userChatReadMapper::mapFrom);
    }

    public void updateChatStatus(ChatId chatId, UserId userId, UserRole userRole) {
        var userChatOptional = userChatRepository
                .findUserChatByChatIdAndUserId(chatId.value(), userId.value());
        if (userChatOptional.isPresent()) {
            var userChat = userChatOptional.get();
            userChat.setUserRole(userRole);
            log.info("Updated role to {} for chatId={}, userId={}", userRole, chatId, userId);
        } else {
            log.warn("Cannot update role. UserChat not found. chatId={}, userId={}, role={}", chatId, userId, userRole);
        }
    }

    public void updateUserChat(Long id, UserChatUpdateDto userChatUpdateDto) {
        Optional<UserChat> userChatOptional =  userChatRepository.findById(id);
        if (userChatOptional.isPresent()) {
            userChatUpdateMapper.updateFromDto(userChatUpdateDto, userChatOptional.get());
            log.info("Updated UserChat with id={}", id);
        } else {
            log.warn("UserChat not found for id {} {}", id, userChatUpdateDto);
        }
    }

    public List<UserChatReadDto> getUserChatsByFirstName(ChatId chatId, String firstName) {
        List<UserChat> userChat = userChatRepository.findAllByUserNameAndChatId(firstName, chatId.value());
        return userChat.stream().map(userChatReadMapper::mapFrom).toList();
    }

    public List<UserChatReadDto> getUserChatsByChatId(ChatId chatId) {
        var userChats = userChatRepository.findAllByChatId(chatId.value());
        log.debug("Fetched {} user chats for chatId={}", userChats.size(), chatId);
        return userChats.stream()
                .map(userChatReadMapper::mapFrom)
                .toList();
    }

    public Optional<UserChatReadDto> getUserChatByUserName(ChatId chatId, String username) {
        Optional<UserChat> userChat = userChatRepository.findUserChatByUserUserNameAndChatId(username, chatId.value());
        return userChat.map(userChatReadMapper::mapFrom);
    }
}
