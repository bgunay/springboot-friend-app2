package org.pinsoft.friendapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pinsoft.friendapp.domain.dto.message.MessageAllViewModel;
import org.pinsoft.friendapp.domain.dto.message.MessageCreateBindingModel;
import org.pinsoft.friendapp.domain.dto.message.MessageFriendsViewModel;
import org.pinsoft.friendapp.domain.dto.message.MessageServiceModel;
import org.pinsoft.friendapp.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.pinsoft.friendapp.web.websocket.WebSocketEventName.CHAT_LOGS;

@RestController()
@RequestMapping(value = "/message")
@Tag(name = "message", description = "message")
@RequiredArgsConstructor
public class MessageController {
    // The SimpMessagingTemplate is used to send Stomp over WebSocket messages.
    private final SimpMessagingTemplate template;

    private final MessageService messageService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;


    @GetMapping(value = "/all/{fromUser}/{chatUserId}")
    @Operation(
            summary = "Get chat with fromUser and toUser",
            description = "Get chat with fromUser and toUser."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<MessageAllViewModel> getOneFriendMessages(@PathVariable(value = "fromUser") String fromUser, @PathVariable(value = "chatUserId") String chatUserId) {
        List<MessageServiceModel> messageServiceModels = this.messageService.getAllMessages(fromUser, chatUserId);

        return messageServiceModels.stream()
                .map(messageServiceModel -> modelMapper.map(messageServiceModel, MessageAllViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/friend/{userId}")
    @Operation(
            summary = "Get chat with fromUser",
            description = "Get chat with fromUser."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<MessageFriendsViewModel> getAllFriendMessages(@PathVariable(value = "userId") String userId) {
        return this.messageService.getAllFriendMessages(userId);
    }

    @MessageMapping("/message")
    public void createPrivateChatMessages(@RequestBody @Valid MessageCreateBindingModel messageCreateBindingModel) throws Exception {
        MessageServiceModel message =
                this.messageService.createMessage(messageCreateBindingModel, messageCreateBindingModel.getFromUser());
        MessageAllViewModel messageAllViewModel = this.modelMapper.map(message, MessageAllViewModel.class);

        if (messageAllViewModel != null) {
            template.convertAndSend(CHAT_LOGS.getDestination(),messageAllViewModel.getTime()
                    + ": " + messageAllViewModel.getFromUserUsername()  + "-> " + messageAllViewModel.getToUserUsername()+ ": " + messageAllViewModel.getContent()  );
        }
    }

    @MessageMapping("/hello")
    @SendTo("/chat/logs")
    public String  chatLog(String  message) throws Exception {
        return message;
    }

}
