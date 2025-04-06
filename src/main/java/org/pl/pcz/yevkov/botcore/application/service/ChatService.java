package org.pl.pcz.yevkov.botcore.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatCreateDto;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.botcore.domain.entity.Chat;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatStatus;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.chat.ChatCreateMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.chat.ChatReadMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatCreateMapper chatCreateMapper;
    private final ChatReadMapper chatReadMapper;


    public void createChat(ChatCreateDto chatCreateDto) {
        var chat = chatCreateMapper.mapFrom(chatCreateDto);
        log.debug("Mapped Chat entity from DTO: {}", chatCreateDto);
        var saved = chatRepository.save(chat);
        log.info("Created Chat: id={}, status={}", saved.getId(), saved.getChatStatus());
    }


    public Optional<ChatReadDto> findChatById(ChatId chatId) {
        log.debug("Fetching Chat by id={}", chatId);
        Optional<Chat> chat = chatRepository.findById(chatId.value());
        if (chat.isPresent()) {
            log.debug("Chat found: id={}", chatId);
        } else {
            log.warn("Chat not found: id={}", chatId);
        }
        return chat.map(chatReadMapper::mapFrom);
    }


    public void markChatAsStatus(ChatId chatId, ChatStatus status) {
        var chatOptional = chatRepository.findById(chatId.value());
        if (chatOptional.isPresent()) {
            var chat = chatOptional.get();
            chat.setChatStatus(status);
            log.info("Updated Chat status to {} for chatId={}", status, chatId);
        } else {
            log.warn("Chat not found. Cannot update status. chatId={}, newStatus={}", chatId, status);
        }
    }


    public Optional<ChatReadDto> changeLimit(ChatId chatId, Long newLimit) {
        var chatOptional = chatRepository.findById(chatId.value());
        if (chatOptional.isPresent()) {
            var chat = chatOptional.get();
            chat.setHourLimit(newLimit);
            log.info("Updated Chat Limit to {} for chatId={}", newLimit, chatId);
        } else {
            log.warn("Chat not found. Cannot update limit. chatId={}, newLimit={}", chatId, newLimit);
        }
        return chatOptional.map(chatReadMapper::mapFrom);
    }

}
