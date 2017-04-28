package com.ufo.NettySocketioWebDemo.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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
	public @ResponseBody String hello() {

		if (isFirst) {

			userModelList.add(new UserModel("AAA", "wangnan", "王南", "123", makeHeadPortrait("1")));
			userModelList.add(new UserModel("BBB", "zhaoyuhuan", "赵宇环", "123", makeHeadPortrait("2")));
			userModelList.add(new UserModel("CCC", "cuihao", "崔浩", "123", makeHeadPortrait("3")));
			userModelList.add(new UserModel("DDD", "cuizhenling", "崔振岭", "123", makeHeadPortrait("4")));
			userModelList.add(new UserModel("EEE", "chenwanhai", "陈万海", "123", makeHeadPortrait("5")));

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

					chatModelList.add(chatModel);
				}
				groupUsers += userModelList.get(i).getSID();
				if (i < userModelList.size() - 1) {
					groupUsers += ",";
				}
			}

			String chatID = UUID.randomUUID().toString();
			long timeNow = System.currentTimeMillis();

			ChatModel chatModel = new ChatModel();
			chatModel.setSID(chatID);
			chatModel.setUsers(groupUsers);
			chatModel.setChatType(ChatTypeEnum.GROUPCHAT);
			// chatModel.setBody("hello");
			chatModel.setTime(timeNow);

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
	public @ResponseBody ResultModel login(@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password) {
		for (UserModel userModel : userModelList) {
			if (userModel.getUserName().equals(username.trim()) && userModel.getPassWord().equals(password.trim())) {
				ResultModel resultModel = new ResultModel(true, userModel, null);
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
	public @ResponseBody List<ChatModel> chatList(@RequestParam(value = "userID", required = true) String userID,
			@RequestParam(value = "last", required = true) long last,
			@RequestParam(value = "current", required = true) long current) {
		List<ChatModel> list = new ArrayList<>();
		for (ChatModel chatModel : chatModelList) {
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
	 * @param receiverID
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/sendText", method = RequestMethod.POST)
	public @ResponseBody ChatMessage sendText(@RequestParam(value = "senderID", required = true) String senderID,
			@RequestParam(value = "chatID", required = true) String chatID,
			@RequestParam(value = "messageID", required = true) String messageID,
			@RequestParam(value = "body", required = true) String body) {

		UserModel senderModel = getUserModelByID(senderID);
		ChatModel chatModel = getChatModelByID(chatID);

		long sendTime = System.currentTimeMillis();

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

		try {
			Thread.sleep(1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		long receiveTime = System.currentTimeMillis();

		message.setIsAlert(true);
		message.setMessageType(MessageTypeEnum.TEXT);
		message.setSID(messageID);
		message.setTime(receiveTime);
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

		chatModel.setTime(receiveTime);
		if (chatModel.getChatType().equals(ChatTypeEnum.GROUPCHAT)) {
			chatModel.setBody(senderModel.getNickName() + ":" + body);
		} else {
			chatModel.setBody(body);
		}

		ChatMessage chatMessage = new ChatMessage();

		chatMessage.setSID(message.getSID());
		chatMessage.setReceiverIDs(message.getReceiverIDs());
		chatMessage.setBody(body);
		chatMessage.setSenderID(message.getSenderID());
		chatMessage.setTime(sendTime);
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
	public @ResponseBody List<ChatMessage> chatMessageList(
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

			}
		}

		return list;
	}

}
