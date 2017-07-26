package com.ufo.NettySocketioWebDemo.Model;

public class ChatModel {

    private String SID;
    private String Users;
    private String Name;
    private String Img;
    private String ChatType;
    private long Time;
    private long CreateTime;
    private String Body;

    public String getSID() {
        return SID;
    }

    public void setSID(String sID) {
        SID = sID;
    }

    public String getUsers() {
        return Users;
    }

    public void setUsers(String users) {
        Users = users;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getChatType() {
        return ChatType;
    }

    public void setChatType(String chatType) {
        ChatType = chatType;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }


}
