package com.ufo.NettySocketioWebDemo.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ufo.NettySocketioClient.Message;
import com.ufo.NettySocketioClient.MessageTypeEnum;
import com.ufo.NettySocketioClient.NettySocketioClient;
import com.ufo.NettySocketioWebDemo.Model.ChatMessage;
import com.ufo.NettySocketioWebDemo.Model.ChatModel;
import com.ufo.NettySocketioWebDemo.Model.ChatTypeEnum;
import com.ufo.NettySocketioWebDemo.Model.ResultModel;
import com.ufo.NettySocketioWebDemo.Model.UserModel;

@Controller
public class DefaultController {

    @Value("#{configProperties['ip_address']}")
    private String ip_address;

    private static boolean isFirst = true;
    private static List<UserModel> userModelList = new ArrayList<>();
    private static List<ChatModel> chatModelList = new ArrayList<>();
    private static List<ChatMessage> messagesList = new ArrayList<>();

    private UserModel getUserModelByID(String id) {
        UserModel model = null;
        for (UserModel userModel : userModelList) {
            if (userModel.getSID().equals(id)) {
                model = userModel;
                break;
            }
        }
        return model;
    }

    private ChatModel getChatModelByID(String id) {
        ChatModel model = null;
        for (ChatModel chatModel : chatModelList) {
            if (chatModel.getSID().equals(id)) {
                model = chatModel;
                break;
            }
        }
        return model;
    }

    private String makeHeadPortrait(String name) {
        String url = String.format("http://%s/NettySocketioWebDemo/img/%s.jpg", ip_address, name);
        return url;
    }

    @RequestMapping("/hello")
    public @ResponseBody
    String hello(HttpServletRequest request) {

        if (isFirst) {

            userModelList.add(new UserModel("AAA", "wangnan", "王南", "123", makeHeadPortrait("1")));
            userModelList.add(new UserModel("BBB", "zhaoyuhuan", "赵宇环", "123", makeHeadPortrait("2")));
            userModelList.add(new UserModel("CCC", "cuihao", "崔浩", "123", makeHeadPortrait("3")));
            userModelList.add(new UserModel("DDD", "cuizhenling", "崔振岭", "123", makeHeadPortrait("4")));
            userModelList.add(new UserModel("EEE", "chenwanhai", "陈万海", "123", makeHeadPortrait("5")));

            userModelList.add(new UserModel("FFF", "test1", "测试1", "123", makeHeadPortrait("6")));
            userModelList.add(new UserModel("GGG", "test2", "测试2", "123", makeHeadPortrait("7")));
            userModelList.add(new UserModel("OOO", "test3", "测试3", "123", makeHeadPortrait("8")));
            userModelList.add(new UserModel("PPP", "test4", "测试4", "123", makeHeadPortrait("9")));
            userModelList.add(new UserModel("QQQ", "test5", "测试5", "123", makeHeadPortrait("10")));
            userModelList.add(new UserModel("RRR", "test6", "测试6", "123", makeHeadPortrait("11")));
            userModelList.add(new UserModel("SSS", "test7", "测试7", "123", makeHeadPortrait("12")));

            String groupUsers = "";
            for (int i = 0; i < userModelList.size(); i++) {
                for (int j = i + 1; j < userModelList.size(); j++) {

                    String chatID = UUID.randomUUID().toString();
                    long timeNow = System.currentTimeMillis();

                    ChatModel chatModel = new ChatModel();
                    chatModel.setSID(chatID);
                    chatModel.setUsers(userModelList.get(i).getSID() + "," + userModelList.get(j).getSID());
                    chatModel.setChatType(ChatTypeEnum.SINGLECHAT);
                    // chatModel.setBody("hello");
                    chatModel.setTime(timeNow);
                    chatModel.setCreateTime(timeNow);

                    chatModelList.add(chatModel);
                }
                groupUsers += userModelList.get(i).getSID();
                if (i < userModelList.size() - 1) {
                    groupUsers += ",";
                }
            }

            String chatID = UUID.randomUUID().toString();
            long timeNow = System.currentTimeMillis();

            // 为WebDemo演示效果使用
            request.getSession().setAttribute("groupChatID", chatID);

            ChatModel chatModel = new ChatModel();
            chatModel.setSID(chatID);
            chatModel.setUsers(groupUsers);
            chatModel.setChatType(ChatTypeEnum.GROUPCHAT);
            // chatModel.setBody("hello");
            chatModel.setTime(timeNow);
            chatModel.setCreateTime(timeNow);

            chatModelList.add(chatModel);

        }
        isFirst = false;

        System.out.println("in hello method");
        return "hello world";
    }

    /**
     * 登陆
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResultModel login(HttpServletRequest request,
                      @RequestParam(value = "username", required = true) String username,
                      @RequestParam(value = "password", required = true) String password) {
        for (UserModel userModel : userModelList) {
            if (userModel.getUserName().equals(username.trim()) && userModel.getPassWord().equals(password.trim())) {
                ResultModel resultModel = new ResultModel(true, userModel, null);

                // 为WebDemo演示效果
                request.getSession().setAttribute("currentUser", userModel);

                return resultModel;
            }
        }
        ResultModel resultModel = new ResultModel(false, null, "用户名或密码错误!");
        return resultModel;
    }

    /**
     * 联系人列表(新)
     *
     * @param userID
     * @param last
     * @param current
     * @return
     */
    @RequestMapping(value = "/chatList", method = RequestMethod.GET)
    public @ResponseBody
    List<ChatModel> chatList(@RequestParam(value = "userID", required = true) String userID,
                             @RequestParam(value = "last", required = true) long last,
                             @RequestParam(value = "current", required = true) long current) {
        List<ChatModel> list = new ArrayList<>();
        for (ChatModel chatModel : chatModelList) {

            if (chatModel.getCreateTime() > last && chatModel.getCreateTime() <= current) {
                if (chatModel.getChatType().equals(ChatTypeEnum.SINGLECHAT)) {
                    String[] users = chatModel.getUsers().split(",");
                    if (users[0].equals(userID)) {
                        UserModel model = getUserModelByID(users[1]);
                        chatModel.setName(model.getNickName());
                        chatModel.setImg(model.getHeadPortrait());
                        list.add(chatModel);
                    }
                    if (users[1].equals(userID)) {
                        UserModel model = getUserModelByID(users[0]);
                        chatModel.setName(model.getNickName());
                        chatModel.setImg(model.getHeadPortrait());
                        list.add(chatModel);
                    }
                } else {
                    chatModel.setName("测试群");
                    chatModel.setImg(makeHeadPortrait("0"));
                    list.add(chatModel);
                }
            }

        }
        return list;
    }

    /**
     * 用户列表(废弃)
     *
     * @return
     */
    // @RequestMapping(value = "/userList", method = RequestMethod.GET)
    // public @ResponseBody List<UserModel> userList(@RequestParam(value =
    // "userID", required = true) String userID,
    // @RequestParam(value = "last", required = true) long last,
    // @RequestParam(value = "current", required = true) long current) {
    // List<UserModel> list = new ArrayList<>();
    // for (UserModel userModel : userModelList) {
    // if (!userModel.getSID().equals(userID)) {
    // list.add(userModel);
    // }
    // }
    // return list;
    // }

    /**
     * 创建群组(废弃)
     *
     * @param senderID
     * @param receiverID
     * @return
     */
    // @RequestMapping(value = "/createChat", method = RequestMethod.POST)
    // public @ResponseBody ChatModel createChat(@RequestParam(value =
    // "senderID", required = true) String senderID,
    // @RequestParam(value = "receiverID", required = true) String receiverID,
    // @RequestParam(value = "chatType", required = true) String chatType) {
    //
    // if (chatType.equals(ChatTypeEnum.SINGLECHAT)) {
    //
    // UserModel senderUserModel = getUserModelByID(senderID);
    //
    // String chatID = UUID.randomUUID().toString();
    // long timeNow = System.currentTimeMillis();
    //
    // ChatModel chatModel = new ChatModel();
    // chatModel.setSID(chatID);
    // chatModel.setUsers(receiverID + "," + senderID);
    // chatModel.setChatType(ChatTypeEnum.SINGLECHAT);
    // chatModel.setBody("hello");
    // chatModel.setTime(timeNow);
    // chatModel.setName(senderUserModel.getNickName());
    // chatModel.setImg(senderUserModel.getHeadPortrait());
    //
    // chatModelList.add(chatModel);
    //
    // Message message = new Message();
    // message.setBody(senderUserModel.getNickName() + ":hello");
    // message.setCategory("custom");
    // message.setIsAlert(true);
    // message.setMessageType(MessageTypeEnum.TEXT);
    // message.setOthers(chatModel);
    // message.setOthersType("chat");
    // HashSet<String> hashSet = new HashSet<>();
    // hashSet.add(receiverID);
    // message.setReceiverIDs(hashSet);
    // message.setSenderID(senderID);
    // message.setSID(UUID.randomUUID().toString());
    // message.setTime(timeNow);
    // message.setTitle(senderUserModel.getNickName());
    //
    // try {
    // NettySocketioClient.getInstance().sendNews(message);
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // return chatModel;
    //
    // }
    //
    // return null;
    // }

    /**
     * 发送文本
     *
     * @param senderID
     * @param senderDeviceToken
     * @param chatID
     * @param messageID
     * @param body
     * @return
     */
    @RequestMapping(value = "/sendText", method = RequestMethod.POST)
    public @ResponseBody
    ChatMessage sendText(@RequestParam(value = "senderID", required = true) String senderID,
                         @RequestParam(value = "senderDeviceToken", required = true) String senderDeviceToken,
                         @RequestParam(value = "chatID", required = true) String chatID,
                         @RequestParam(value = "messageID", required = true) String messageID,
                         @RequestParam(value = "body", required = true) String body) {

        UserModel senderModel = getUserModelByID(senderID);
        ChatModel chatModel = getChatModelByID(chatID);

        long time = System.currentTimeMillis();


        Message message = new Message();

        message.setBody(body);
        message.setTitle(senderModel.getNickName());
        if (chatModel.getChatType().equals(ChatTypeEnum.GROUPCHAT)) {
            message.setBody(senderModel.getNickName() + ":" + body);
            message.setTitle(chatModel.getName());
        } else {
            message.setBody(body);
            message.setTitle(senderModel.getNickName());
        }

        message.setIsAlert(true);
        message.setMessageType(MessageTypeEnum.TEXT);
        message.setSID(messageID);
        message.setSenderDeviceToken(senderDeviceToken);
        message.setTime(time);
        message.setCategory("custom");
        message.setSenderID(senderID);

        String[] receiverIDs = chatModel.getUsers().split(",");
        HashSet<String> hashSet = new HashSet<>();
        for (String receiverID : receiverIDs) {
            if (!receiverID.equals(senderID)) {
                hashSet.add(receiverID);
            }
        }
        message.setReceiverIDs(hashSet);

        chatModel.setTime(time);
        if (chatModel.getChatType().equals(ChatTypeEnum.GROUPCHAT)) {
            chatModel.setBody(senderModel.getNickName() + ":" + body);
        } else {
            chatModel.setBody(body);
        }

        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setSID(message.getSID());
        chatMessage.setReceiverIDs(message.getReceiverIDs());
        chatMessage.setSenderDeviceToken(message.getSenderDeviceToken());
        chatMessage.setBody(body);
        chatMessage.setSenderID(message.getSenderID());
        chatMessage.setTime(time);
        chatMessage.setMessageType(message.getMessageType());
        chatMessage.setNickName(senderModel.getNickName());
        chatMessage.setHeadPortrait(senderModel.getHeadPortrait());
        chatMessage.setChatID(chatID);

        message.setOthersType("message");
        message.setOthers(chatMessage);

        messagesList.add(chatMessage);

        try {
            NettySocketioClient.getInstance().sendNews(message);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return chatMessage;
    }

    /**
     * 获取对话
     *
     * @param userID
     * @param last
     * @param current
     * @return
     */
    @RequestMapping(value = "/chatMessageList", method = RequestMethod.GET)
    public @ResponseBody
    List<ChatMessage> chatMessageList(
            @RequestParam(value = "userID", required = true) String userID,
            @RequestParam(value = "last", required = true) long last,
            @RequestParam(value = "current", required = true) long current) {

        List<ChatMessage> list = new ArrayList<>();

        for (ChatMessage chatMessage : messagesList) {

            if (chatMessage.getTime() > last && chatMessage.getTime() <= current) {

                for (String item : chatMessage.getReceiverIDs()) {

                    if (item.equals(userID)) {
                        list.add(chatMessage);
                    }
                }


                if (chatMessage.getSenderID().equals(userID)) {
                    list.add(chatMessage);
                }
            }
        }

        return list;
    }

	/*-----------------------------------------------------------------------------------------*/

    @RequestMapping("/indexView")
    public String indexView(HttpServletRequest request, ModelMap model) {
        UserModel userModel = (UserModel) request.getSession().getAttribute("currentUser");

        model.addAttribute("SID", userModel.getSID());
        model.addAttribute("userName", userModel.getUserName());
        model.addAttribute("nickName", userModel.getNickName());
        model.addAttribute("groupChatID", chatModelList.get(chatModelList.size() - 1).getSID());

        return "indexView";
    }

    @RequestMapping("/loginView")
    public String loginView() {
        return "loginView";
    }

}
