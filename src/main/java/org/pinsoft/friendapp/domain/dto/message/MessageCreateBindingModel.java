package org.pinsoft.friendapp.domain.dto.message;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MessageCreateBindingModel {
    private String fromUser;
    private String toUserId;
    private String content;

    @NotNull
    @NotEmpty
    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    @NotNull
    @NotEmpty
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NotNull
    @NotEmpty
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUserId(String fromUser) {
        this.fromUser = fromUser;
    }
}
