package com.ufo.NettySocketioWebDemo.Model;

import java.util.HashSet;

public class ChatMessage {

    // 主键
    private String SID;
    // 发送人ID
    private String SenderID;
    // 发送人设备编号
    private String SenderDeviceToken;
    // 接收人ID
    private HashSet<String> ReceiverIDs;
    // 标题
    private String Title;
    // 内容
    private String Body;
    // 时间
    private long Time;
    // 消息类型(文字、图片、文件、链接、音频、视频、表情等)
    private String MessageType;
    // 真实姓名
    private String NickName;
    // 头像
    private String HeadPortrait;
    // 会话ID
    private String ChatID;
    // 缩略图
    private String Thumbnail;
    // 原图
    private String Original;

    public String getSID() {
        return SID;
    }

    public void setSID(String sID) {
        SID = sID;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    public String getSenderDeviceToken() {
        return SenderDeviceToken;
    }

    public void setSenderDeviceToken(String senderDeviceToken) {
        SenderDeviceToken = senderDeviceToken;
    }

    public HashSet<String> getReceiverIDs() {
        return ReceiverIDs;
    }

    public void setReceiverIDs(HashSet<String> receiverIDs) {
        ReceiverIDs = receiverIDs;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadPortrait() {
        return HeadPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        HeadPortrait = headPortrait;
    }

    public String getChatID() {
        return ChatID;
    }

    public void setChatID(String chatID) {
        ChatID = chatID;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getOriginal() {
        return Original;
    }

    public void setOriginal(String original) {
        Original = original;
    }

}
