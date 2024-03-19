package org.pinsoft.friendapp.controller;

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
import org.pinsoft.friendapp.utils.responseHandler.exceptions.BadRequestException;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.SERVER_ERROR_MESSAGE;

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


    @GetMapping(value = "/all/{fromUser}/{toUser}")
    @Operation(
            summary = "Get chat with fromUser and toUser",
            description = "Get chat with fromUser and toUser."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<MessageAllViewModel> getAllMessages(@PathVariable(value = "fromUser") String fromUser,@PathVariable(value = "toUser") String toUser) {
        List<MessageServiceModel> messageServiceModels = this.messageService.getAllMessages(fromUser, toUser);

        return messageServiceModels.stream()
                .map(messageServiceModel -> modelMapper.map(messageServiceModel, MessageAllViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{userId}/friend")
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
    @Operation(
            summary = "endpoint for message to someone",
            description = "Use this endpoint for message to someone."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void createPrivateChatMessages(@RequestBody @Valid MessageCreateBindingModel messageCreateBindingModel, Principal principal, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        MessageServiceModel message = this.messageService.createMessage(messageCreateBindingModel, principal.getName());
        MessageAllViewModel messageAllViewModel = this.modelMapper.map(message, MessageAllViewModel.class);

        if (messageAllViewModel != null) {
            String response = this.objectMapper.writeValueAsString(messageAllViewModel);
            template.convertAndSend("/user/" + message.getToUser().getUsername() + "/queue/position-update", response);
            template.convertAndSend("/user/" + message.getFromUser().getUsername() + "/queue/position-update", response);
            return;
        }
        throw new CustomException(SERVER_ERROR_MESSAGE);
    }
}
