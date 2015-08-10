package com.example.yuricesar.collective.chat;

/**
 * Created by ygorg_000 on 29/07/2015.
 */
public class ChatMessage {
    public boolean left;
    public String message;

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof ChatMessage) && ((ChatMessage) obj).left == (this.left) && ((ChatMessage) obj).message.equals(this.message)) {
            return true;
        } else {
            return false;
        }
    }
}
