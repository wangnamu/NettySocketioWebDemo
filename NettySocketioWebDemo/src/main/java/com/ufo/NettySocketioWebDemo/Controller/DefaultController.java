package com.ufo.NettySocketioWebDemo.Controller;

import java.util.Date;
import java.util.HashSet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ufo.NettySocketioClient.Message;
import com.ufo.NettySocketioClient.MessageTypeEnum;
import com.ufo.NettySocketioClient.NettySocketioClient;

@Controller
public class DefaultController {
	
	@RequestMapping("/hello")
	public @ResponseBody String hello() {
		System.out.println("in hello method");
		return "hello world";
	}
	
	@RequestMapping("/test")
	public @ResponseBody String test(){
		
		Message message = new Message();
		message.setAlert("something");
		message.setIsAlert(true);
		message.setMessageType(MessageTypeEnum.TEXT);
		message.setSID("1");
		message.setTime(new Date().getTime());
		message.setSenderID("a");
		
		HashSet<String> hashSet = new HashSet<>();
		hashSet.add("b");
		
		message.setReceiverIDs(hashSet);
		
		NettySocketioClient.getInstance().send(message);
		return "success";
	}
	
}
