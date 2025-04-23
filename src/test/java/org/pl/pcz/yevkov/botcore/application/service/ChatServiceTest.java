package org.pl.pcz.yevkov.botcore.application.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatCreateDto;
import org.pl.pcz.yevkov.botcore.application.dto.chat.ChatReadDto;
import org.pl.pcz.yevkov.botcore.domain.entity.Chat;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatStatus;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.chat.ChatCreateMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.mapper.chat.ChatReadMapper;
import org.pl.pcz.yevkov.botcore.infrastructure.repository.ChatRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    ChatRepository chatRepository;
    @Mock
    ChatCreateMapper chatCreateMapper;
    @Mock
    ChatReadMapper chatReadMapper;

    @InjectMocks
    ChatService chatService;

    @Mock
    ChatCreateDto createDto;
    @Mock
    ChatReadDto readDto;
    @Mock
    Chat chat;

    ChatId chatId = new ChatId(123L);

    @Nested
    class CreateChat {

        @Test
        void createChat_savesChatInRepository() {
            when(chatCreateMapper.mapFrom(createDto)).thenReturn(chat);
            when(chatRepository.save(chat)).thenReturn(chat);

            chatService.createChat(createDto);

            verify(chatCreateMapper).mapFrom(createDto);
            verify(chatRepository).save(chat);
        }
    }

    @Nested
    class FindChatById {

        @Test
        void findChatById_found_returnsDto() {
            when(chatRepository.findById(chatId.value())).thenReturn(Optional.of(chat));
            when(chatReadMapper.mapFrom(chat)).thenReturn(readDto);

            Optional<ChatReadDto> result = chatService.findChatById(chatId);

            assertTrue(result.isPresent());
            assertSame(readDto, result.get());
        }

        @Test
        void findChatById_notFound_returnsEmpty() {
            when(chatRepository.findById(chatId.value())).thenReturn(Optional.empty());

            Optional<ChatReadDto> result = chatService.findChatById(chatId);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class MarkChatAsStatus {

        @Test
        void markChatAsStatus_found_updatesStatus() {
            when(chatRepository.findById(chatId.value())).thenReturn(Optional.of(chat));

            chatService.markChatAsStatus(chatId, ChatStatus.ACTIVE);

            verify(chat).setChatStatus(ChatStatus.ACTIVE);
        }

        @Test
        void markChatAsStatus_notFound_doesNothing() {
            when(chatRepository.findById(chatId.value())).thenReturn(Optional.empty());

            chatService.markChatAsStatus(chatId, ChatStatus.ACTIVE);

            verify(chat, never()).setChatStatus(any());
        }
    }

    @Nested
    class ChangeLimit {

        @Test
        void changeLimit_found_updatesLimitAndReturnsDto() {
            when(chatRepository.findById(chatId.value())).thenReturn(Optional.of(chat));
            when(chatReadMapper.mapFrom(chat)).thenReturn(readDto);

            Optional<ChatReadDto> result = chatService.changeLimit(chatId, 50L);

            assertTrue(result.isPresent());
            assertSame(readDto, result.get());
            verify(chat).setHourLimit(50L);
        }

        @Test
        void changeLimit_notFound_returnsEmpty() {
            when(chatRepository.findById(chatId.value())).thenReturn(Optional.empty());

            Optional<ChatReadDto> result = chatService.changeLimit(chatId, 50L);

            assertTrue(result.isEmpty());
        }
    }
}