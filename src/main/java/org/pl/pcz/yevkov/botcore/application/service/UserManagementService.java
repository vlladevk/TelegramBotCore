package org.pl.pcz.yevkov.botcore.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pl.pcz.yevkov.botcore.application.dto.event.ChatMessageReceivedDto;
import org.pl.pcz.yevkov.botcore.application.dto.user.UserCreateDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatCreateDto;
import org.pl.pcz.yevkov.botcore.application.dto.userChat.UserChatReadDto;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatStatus;
import org.pl.pcz.yevkov.botcore.domain.entity.ChatType;
import org.pl.pcz.yevkov.botcore.domain.entity.UserRole;
import org.pl.pcz.yevkov.botcore.domain.vo.ChatId;
import org.pl.pcz.yevkov.botcore.domain.vo.UserId;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
public class UserManagementService {

    private final ChatService chatService;
    private final UserService userService;
    private final UserChatService userChatService;


    public void ensureUserRegisteredInChat(ChatMessageReceivedDto receivedMessage) {
        if (receivedMessage.chatType() == ChatType.PRIVATE) return;

        UserId userId = receivedMessage.userId();
        ChatId chatId = receivedMessage.chatId();

        var chatOptional = chatService.findChatById(chatId);
        if (chatOptional.isEmpty() || chatOptional.get().chatStatus() != ChatStatus.ACTIVE) return;

        var userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            registerUser(userId, receivedMessage.firstName(), receivedMessage.username());
            registerUserChat(chatId, userId);
        }

        Optional<UserChatReadDto> userChatReadDto = userChatService.getUserChatBy(chatId, userId);
        if (userChatReadDto.isEmpty()) {
            registerUserChat(chatId, userId);
        }
    }

    private void registerUser(UserId userId, String firstName, String userName) {
        UserCreateDto dto = UserCreateDto.builder()
                .id(userId)
                .name(firstName)
                .userName(userName)
                .build();
        userService.createUser(dto);
    }

    private void registerUserChat(ChatId chatId, UserId userId) {
        UserChatCreateDto dto = UserChatCreateDto.builder()
                .chatId(chatId)
                .userId(userId)
                .userRole(UserRole.USER)
                .build();
        userChatService.createUserChat(dto);
    }
}