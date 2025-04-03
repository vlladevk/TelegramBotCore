package org.pl.pcz.yevkov.tgbottest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatCreateDto;
import org.pl.pcz.yevkov.tgbottest.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.tgbottest.entity.Chat;
import org.pl.pcz.yevkov.tgbottest.entity.ChatStatus;
import org.pl.pcz.yevkov.tgbottest.mapper.chat.ChatCreateMapper;
import org.pl.pcz.yevkov.tgbottest.mapper.chat.ChatReadMapper;
import org.pl.pcz.yevkov.tgbottest.repository.ChatRepository;
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

    public Optional<ChatReadDto> findChatById(Long chatId) {
        log.debug("Fetching Chat by id={}", chatId);
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            log.debug("Chat found: id={}", chatId);
        } else {
            log.warn("Chat not found: id={}", chatId);
        }
        return chat.map(chatReadMapper::mapFrom);
    }

    public void markChatAsStatus(Long chatId, ChatStatus status) {
        var chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            var chat = chatOptional.get();
            chat.setChatStatus(status);
            log.info("Updated Chat status to {} for chatId={}", status, chatId);
        } else {
            log.warn("Chat not found. Cannot update status. chatId={}, newStatus={}", chatId, status);
        }
    }

    public Optional<ChatReadDto> changeLimit(Long chatId, Long newLimit) {
        var chatOptional = chatRepository.findById(chatId);
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
