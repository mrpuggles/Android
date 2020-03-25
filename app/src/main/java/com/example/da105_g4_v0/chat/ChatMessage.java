package com.example.da105_g4_v0.chat;

public class ChatMessage {
    private String type;
    private String userName;
//    private String receiver;
    private String message;
    private String roomNO;
    private String currentBid;

    public ChatMessage() {
    }

    public ChatMessage(String type, String userName,  String message,  String roomNO) {
        this.type = type;
        this.userName = userName;
//        this.receiver = receiver;
        this.roomNO = roomNO;
        this.message = message;
    }

    public ChatMessage(String type, String currentBid) {
        this.type = type;
        this.currentBid = currentBid;


    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getReceiver() {
//        return receiver;
//    }
//
//    public void setReceiver(String receiver) {
//        this.receiver = receiver;
//    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoomNO() {
        return roomNO;
    }

    public void setRoomNO(String roomNO) {
        this.roomNO = roomNO;
    }

    public String getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(String currentBid) {
        this.currentBid = currentBid;
    }
}
