package com.example.demo.controll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.form.SchoolDisplay;
import com.example.demo.form.UserDisplay;
import com.example.demo.form.UserForm;
import com.example.demo.service.SchoolDisplayServiceInterface;
import com.example.demo.service.SchoolServiceInterface;
import com.example.demo.service.UserDisplayServiceInterface;
import com.example.demo.service.UserServiceInterface;

/**
 * 管理者のコントローラ
 */
@Controller
@RequestMapping("/taskdon/admin")
public class AdminCtrl {

	//	@Autowired
	//	SchoolCrudRepository repo;

	@Autowired
	@Qualifier("schoolService")
	SchoolServiceInterface schoolS;
	
	@Autowired
	@Qualifier("userListService")
	UserServiceInterface userListService;

	@Autowired
	@Qualifier("schoolDisplayService")
	SchoolDisplayServiceInterface schoolDisplayService;
	
	@Autowired
	@Qualifier("userDisplayImpl")
	UserDisplayServiceInterface userDisplayService;
	
	
	/**
	 * ログイン画面を表示する
	 * @return
	 */
	@GetMapping("login")
	public String login() {
		return "admin/login";
	}

	/**
	 * 末吉
	 * メニュー画面を表示する
	 * @return
	 */
	@GetMapping("menu")
	public String menu() {
		return "admin/menu";
	}

	
	/**
	 * 末吉
	 * 学校情報詳細画面を表示する
	 * @return
	 */
	@GetMapping("schoolDetails")
	public ModelAndView schoolDetails(ModelAndView mav) {

		//schoolS.schoolDateils();
		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails();

		mav.addObject("schoolS", SchoolDetails);
		mav.setViewName("admin/schoolDetails");

		return mav;
	}
	
	
	/*
	 * 向江さん
	 * ユーザ一覧のリクエストハンドラメソッド
	 * @return ユーザ一覧
	 */

	@GetMapping("userList")
	public ModelAndView userList() {
		//ModelAndViewのインスタンス生成
		ModelAndView mav = new ModelAndView();

		//サービスのインスタンス生成
		//UserListServiceInterface userListService = new UserListServiceImpl();

		//サービスのメソッドを呼び出す
		Iterable<UserDisplay> userList = userDisplayService.userList();

		mav.addObject("users", userList);
		mav.setViewName("admin/userList");
		
		System.out.println(userList);
		
		//System.out.println(userList.toString());

		return mav;

		//return "admin/UserList";

	}
	
	
	/*
	 * 向江
	 * ユーザ詳細を表示するリクエストハンドラメソッド
	 *
	 */
	@PostMapping("userDetail")
	public ModelAndView userDetail(UserForm u, ModelAndView mav) {
		System.out.println("konnitihasiru");
		
		mav.addObject("user", u);
		mav.setViewName("admin/userDetail");
		
 
		return mav;
	
	}
	

	/**
	 * グループ一覧画面を表示する
	 * @return
	 */
	public String groopList() {
		return "groopList";
	}

	/**
	 * グループ詳細画面を表示する
	 * @return
	 */
	public String groopDetail() {
		return "groopDetail";
	}

	/**
	 * グループメンバ詳細画面を表示する
	 * @return
	 */
	public String memberDetails() {
		return "memberDetails";
	}

	/**
	 * グループ編集画面を表示する
	 * @return
	 */
	public String userUpdate() {
		return "userDetail";
	}

	/**
	 * グループメンバ追加画面を表示する
	 * @return
	 */
	public String memberAdd() {
		return "memberAdd";
	}

	/**
	 * グループメンバ追加確認画面を表示する
	 * @return
	 */
	public String memberAddConfirm() {
		return "memberAddConfirm";
	}

	/**
	 * グループメンバ削除確認画面を表示する
	 * @return
	 */
	public String memberDeleteConfirm() {
		return "memberDeleteConfirm";
	}

	/**
	 * グループ解散確認画面を表示する
	 * @return
	 */
	public String groopDeleteConfirm() {
		return "groopDeleteConfirm";
	}

	/**
	 * 	グループ作成画面を表示する
	 * @return
	 */
	public String groopCreate() {
		return "groopCreate";
	}

}