package com.ufo.NettySocketioWebDemo.Model;


public class UserModel {
    // 主键
    private String SID;
    // 用户名
    private String UserName;
    // 密码
    private String PassWord;
    // 真实姓名
    private String NickName;
    // 头像
    private String HeadPortrait;

    public UserModel() {

    }

    public UserModel(String sid, String username, String nickname, String password, String headportrait) {
        SID = sid;
        UserName = username;
        NickName = nickname;
        PassWord = password;
        HeadPortrait = headportrait;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String sID) {
        SID = sID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
