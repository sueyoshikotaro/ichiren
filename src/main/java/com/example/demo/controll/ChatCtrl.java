package com.example.demo.controll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDetailView;
import com.example.demo.service.ChatServiceInterface;

/**
 * チャットのコントローラ
 */
@Controller
@RequestMapping("/taskdon/admin/chat")
public class ChatCtrl {

	@Autowired
	@Qualifier("ChatService")
	ChatServiceInterface chatServise;
	
	//セッション保持のインスタンス
	AdminCtrl adminCtrl = new AdminCtrl();
	
	//セッション情報
	int school_id = adminCtrl.school_id;
	String user_id = adminCtrl.user_id;
	
	/**
	 * 末吉
	 * チャット画面
	 * @return
	 */
	@LoginRequired
	@GetMapping("chatDefalt")
	public ModelAndView chat(ModelAndView mav) {

		//チャットの通信可能相手を格納
		List<GroupDetailView> chatPartner = chatServise.setChatUser(school_id);

		mav.addObject("chatPartnerMember", chatPartner);
		mav.setViewName("common/chat");
		return mav;
	}

	/**
	 * 末吉
	 * チャット相手検索
	 * @return
	 */
	@LoginRequired
	@PostMapping("chatSearch")
	public ModelAndView chatSearch(ModelAndView mav,
			@RequestParam(name = "search", required = false) String search) {
		//チャット相手を検索し、Listに格納する
		List<GroupDetailView> chatPartner = chatServise.chatPartnerSearch(school_id, search, "リーダ");

		mav.addObject("chatPartnerMember", chatPartner);
		mav.setViewName("common/chat");

		return mav;
	}

	/**
	 * 末吉
	 * チャット画面にチャット履歴を表示する
	 * @return
	 */
	@LoginRequired
	@PostMapping("getChatHistory")
	public ModelAndView getChatHistory(ModelAndView mav,
			@RequestParam(name = "chatUserId", required = false) String chatUser_id) {
		List<ChatForm> chatHistory = chatServise.getChatHistory(user_id, chatUser_id);

		mav.addObject("chatHistory", chatHistory);
		mav.setViewName("common/chat");

		return mav;
	}

	/**
	 * 末吉
	 * チャット送信
	 * @return
	 */
	@LoginRequired
	@PostMapping("sendChat")
	public ModelAndView sendChat(ModelAndView mav,
			@RequestParam(name = "sendInput", required = false) String sendText,
			@RequestParam(name = "chatPartnerUserId", required = false) String chatPartnerUserId) {

		List<ChatForm> chatHistory = chatServise.sendChat(user_id, chatPartnerUserId, sendText);

		mav.addObject("chatHistory", chatHistory);
		mav.setViewName("common/chat");

		return mav;
	}
}
