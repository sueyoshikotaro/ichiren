package com.example.demo.controll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.form.FormContents;
import com.example.demo.form.SchoolDisplay;
import com.example.demo.form.UserDisplay;
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
	
	public String logout() {
		return "admin/login";
	}

	/**
	 * 末吉
	 * メニュー画面を表示する
	 * @return
	 */
	@LoginRequired
	@GetMapping("menu")
	public String menu() {
		return "admin/menuAdmin";
	}

	/**
	 * 末吉
	 * 学校情報詳細画面を表示する
	 * @return
	 */
	@GetMapping("schoolDetails")
	public ModelAndView schoolDetails(ModelAndView mav, Model model) {

		//schoolS.schoolDateils();
		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails();

		
		//ラジオボタンの情報を取得
		model.addAttribute("FormContent", new FormContents());
		
		mav.addObject("schoolS", SchoolDetails);
		mav.setViewName("admin/schoolDetails");

		return mav;
	}

	/**
	 * 末吉
	 * 学校情報編集、削除、追加画面の表示する
	 * @return
	 */
	@PostMapping("schoolDetailsChange")
	public ModelAndView schoolDetailsChange(@RequestParam("button") String button, @ModelAttribute FormContents formcontents, ModelAndView mav) {
		
		List<SchoolDisplay> EditSchoolDetails = schoolDisplayService.EditSchoolDetails(formcontents.getContent());
		
		//編集ボタンを押下
		if (button.equals("edit")){
			
			mav.addObject("schoolEdit", EditSchoolDetails);
			mav.setViewName("admin/schoolEdit");

		//追加ボタンを押下
		} else if (button.equals("add")) {
			
			mav.addObject("schoolAdd", schoolDisplayService.SchoolDetails());
			mav.setViewName("admin/schoolAdd");
      
		} else {
//			mav.addObject("schoolEdit", EditSchoolDetails);
//			mav.setViewName("admin/deleteSchoolDetails");
		}

		return mav;
	}
	
	
	/**
	 * 末吉
	 * 学校情報編集確認画面を表示する
	 * @return
	 */	
	@PostMapping("schoolEditConfirm")
	public ModelAndView schoolEditConfirm(SchoolDisplay s, ModelAndView mav) {
		
		mav.addObject("SchoolDisplay", s);
		mav.setViewName("admin/schoolEditConfirm");
		
		return mav;
	}
	
	
	/**
	 * 末吉
	 * 学校情報編集完了画面を表示する
	 * @return
	 */	
	@PostMapping("schoolEditComp")
	public ModelAndView schoolEditComp(SchoolDisplay r, ModelAndView mav) {
		
		schoolDisplayService.EditSchoolDetailsComp(r.getRoom_name(), r.getPc_flg(), r.getHall(), r.getFloor(), r.getSchool_id(), r.getRoom_id());
		
		mav.setViewName("admin/schoolEditConfirm");
		
		return mav;
	}
	
	
	/**
	 * 末吉
	 * 学校情報追加確認画面を表示する
	 * @return
	 */	
	@PostMapping("schoolAddConfirm")
	public ModelAndView schoolAddConfirm(SchoolDisplay s, ModelAndView mav) {
		
		mav.addObject("SchoolDisplay", s);
		mav.setViewName("admin/schoolAddConfirm");
		
		return mav;
	}
	
	
	/**
	 * 末吉
	 * 学校情報追加完了
	 * @return
	 */
	@PostMapping("schoolAddComp")
	public ModelAndView schoolAddComp(SchoolDisplay r, ModelAndView mav) {
		
		schoolDisplayService.AddSchoolDetailsComp(r.getRoom_name(), r.getPc_flg(), r.getHall(), r.getFloor(), r.getSchool_id());
		
		mav.setViewName("admin/menuAdmin");
		
		return mav;
	}

	/*
	 * 向江
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

		return mav;

	}

	/*
	 * 向江
	 * ユーザ削除確認画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("userDelete")
	public ModelAndView userDelete(UserDisplay u, ModelAndView mav) {

		// サービスのメソッドを呼び出す
		//				userDisplayService.DeleteUser(u.getUser_id());

		mav.addObject("user", u);
		mav.setViewName("admin/userDeleteConfirm");
		return mav;
	}

	/*
	 * 向江
	 * ユーザ削除完了画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("userDeleteConfirm")
	public ModelAndView userDeleteConfirm(UserDisplay u, ModelAndView mav) {

		// サービスのメソッドを呼び出す
		userDisplayService.DeleteUser(u.getUser_id());

		mav.setViewName("admin/updateComp");

		return mav;
	}

	/*
	 * 向江
	 * パスワード初期化確認画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("passClear")
	public ModelAndView passClear(UserDisplay u, ModelAndView mav) {

		mav.addObject("user", u);

		mav.setViewName("admin/passClear");

		return mav;
	}

	/*
	 * 向江
	 * パスワード初期化完了後の画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("passClearConfirm")
	public ModelAndView passClearConfirm(UserDisplay u, ModelAndView mav) {

		// サービスのメソッドを呼び出す
		userDisplayService.PassReset(u.getUser_id());

		//mav.addObject("user",u);
		mav.setViewName("admin/updateComp");

		return mav;
	}

	/*
	 * 向江
	 * 新規講師登録画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@GetMapping("teInfoRegist")
	public String dispRegist() {

		return "admin/teInfoRegist";
	}

	/*
	 * 向江
	 * 新規講師登録確認画面を表示するリクエストハンドラメソッド
	 * @param u
	 * @param mav
	 */
	@PostMapping("teInfoRegistConfirm")
	public ModelAndView dispTeInfoRegistConf(UserDisplay u, ModelAndView mav) {

		if (userDisplayService.userIDCheck(u.getUser_id())) {
			
			mav.setViewName("admin/teInfoRegistConfirm");
			mav.addObject("te", u);
			
			
			
		} else {
			
			// IDが重複していた場合
			mav.addObject("errMsg", "IDが重複しています。");
			mav.setViewName("admin/teInfoRegist");
		}
		
		return mav;
	}

	/*
	 * 向江
	 * 新規講師登録完了画面を表示するリクエストハンドラメソッド
	 * @param t
	 * @param mav 
	 * @return
	 */
	@PostMapping("teInfoRegistComp")
	public ModelAndView dispTeInfoRegistComp(UserDisplay u, ModelAndView mav) {
		
		userDisplayService.InsertTeach(u.getUser_id(), u.getUser_name(), u.getUser_pass(), u.getSchool_name(), u.getEnr_year(), 1);
		
		mav.setViewName("teInfoRegistComp");
		
		return mav;
	}
}
