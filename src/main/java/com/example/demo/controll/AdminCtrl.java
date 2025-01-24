package com.example.demo.controll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.entity.User;
import com.example.demo.form.ChatForm;
import com.example.demo.form.FormContents;
import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupMemberDeleteView;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.SchoolDisplay;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TeamsDisplay;
import com.example.demo.form.TeamsForm;
import com.example.demo.form.UserDisplay;
import com.example.demo.form.UserForm;
import com.example.demo.service.ChatServiceInterface;
import com.example.demo.service.GroupDisplayServiceInterface;
import com.example.demo.service.SchoolDisplayServiceInterface;
import com.example.demo.service.UserDisplayServiceInterface;

/**
 * 管理者のコントローラ
 */
@Controller
@RequestMapping("/taskdon/admin")
public class AdminCtrl {

	@Autowired
	@Qualifier("schoolDisplayService")
	SchoolDisplayServiceInterface schoolDisplayService;

	@Autowired
	@Qualifier("userDisplayImpl")
	UserDisplayServiceInterface userDisplayService;

	@Autowired
	HttpSession session;

	@Autowired
	@Qualifier("GroupDisplayImpl")
	GroupDisplayServiceInterface groupDispService;

	@Autowired
	@Qualifier("ChatService")
	ChatServiceInterface chatServise;
	
	private int school_id;
	private String user_id;
	
	/**
	 * ログイン画面を表示する
	 * @return
	 */
	@GetMapping("login")
	public String login() {
		return "common/login";
	}

	/**
	 * 末吉
	 * ログアウト
	 * @return
	 */
	@GetMapping("logout")
	public String logout() {

		session.invalidate();

		return "common/login";
	}

	/**
	 * 末吉
	 * メニュー画面を表示する
	 * @return
	 */
	@LoginRequired
	@GetMapping("menu")
	public String menu() {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();
		// エンティティの中のuser_idを取得
		user_id = userEntity.getUser_id();

		return "admin/menuAdmin";
	}

	/**
	 * 末吉
	 * メニュー画面を表示する(ログイン画面から)
	 * @return
	 */
	@LoginRequired
	@PostMapping("menu")
	public String menuLogin() {

		return "admin/menuAdmin";
	}

	/**
	 * 末吉
	 * 学校情報詳細画面を表示する
	 * @return
	 */
	@GetMapping("schoolDetails")
	public ModelAndView schoolDetails(ModelAndView mav, Model model) {
		
		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails(school_id);

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
	public ModelAndView schoolDetailsChange(@RequestParam("button") String button,
			@RequestParam("flexRadioDefault") int room_id, @RequestParam("before_room_name") String room_name,
			ModelAndView mav) {
		
		//ラジオボタンで選択したデータを取得
		List<SchoolDisplay> EditSchoolDetails = schoolDisplayService.EditSchoolDetails(room_id, school_id);

		//選択したデータの教室名を編集前の教室名として保持
		EditSchoolDetails.get(0).setBefore_room_name(EditSchoolDetails.get(0).getRoom_name());

		//学校IDと学校名のみ表示
		List<SchoolDisplay> SchoolDetails = new ArrayList<SchoolDisplay>();

		SchoolDetails.add(new SchoolDisplay());
		SchoolDetails.get(0).setSchool_id(EditSchoolDetails.get(0).getSchool_id());
		SchoolDetails.get(0).setSchool_name(EditSchoolDetails.get(0).getSchool_name());

		//編集ボタンを押下
		if (button.equals("edit")) {

			mav.addObject("schoolEdit", EditSchoolDetails);
			mav.setViewName("admin/schoolEdit");

			//追加ボタンを押下
		} else if (button.equals("add")) {

			mav.addObject("schoolAdd", SchoolDetails);
			mav.setViewName("admin/schoolAdd");

			//削除ボタンを押下
		} else {

			mav.addObject("schoolDelete", EditSchoolDetails);
			mav.setViewName("admin/schoolDelete");

		}

		return mav;
	}

	/**
	 * 末吉
	 * 学校情報編集確認画面を表示する
	 * @return
	 */
	@PostMapping("schoolEditConfirm")
	public ModelAndView schoolEditConfirm(@RequestParam("button") String button, SchoolDisplay s, ModelAndView mav,
			Model model) {

		//確認ボタンを押下
		if (button.equals("確認")) {

			mav.addObject("SchoolDisplay", s);
			mav.setViewName("admin/schoolEditConfirm");

			return mav;

			//戻るボタンを押下し学校情報詳細画面を表示
		} else {

			List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails(school_id);

			//ラジオボタンの情報を取得
			model.addAttribute("FormContent", new FormContents());

			mav.addObject("schoolS", SchoolDetails);
			mav.setViewName("admin/schoolDetails");

			return mav;
		}
	}

	/**
	 * 末吉
	 * 学校情報編集完了画面を表示する
	 * @return
	 */
	@PostMapping("schoolEditComp")
	public ModelAndView schoolEditComp(@RequestParam("button") String button, SchoolDisplay s, ModelAndView mav) {

		//編集ボタンを押下
		if (button.equals("編集")) {

			//編集前と編集後の教室名が同じ場合
			if (s.getRoom_name().equals(s.getBefore_room_name())) {

				schoolDisplayService.EditSchoolDetailsComp(s.getRoom_name(), s.getPc_flg(), s.getHall(),
						s.getFloor(),
						s.getSchool_id(), s.getRoom_id());

				// ポップアップを表示するために、画面遷移をしないようにする
				mav.addObject("schoolEditComp", true);
				mav.setViewName("admin/schoolEditConfirm");

			} else {
				//同じ教室名が登録されている場合
				if (schoolDisplayService.isExistRoomName(s.getRoom_name(), s.getSchool_id())) {

					mav.addObject("errMsg", "※同じ教室名がすでに登録されています。");
					mav.addObject("schoolEdit", s);
					mav.setViewName("admin/schoolEdit");

					//まだ同じ教室名が登録されていない場合
				} else {

					schoolDisplayService.EditSchoolDetailsComp(s.getRoom_name(), s.getPc_flg(), s.getHall(),
							s.getFloor(),
							s.getSchool_id(), s.getRoom_id());

					// ポップアップを表示するために、画面遷移をしないようにする
					mav.addObject("schoolEditComp", true);
					mav.setViewName("admin/schoolEditConfirm");

				}
			}

			//戻るボタンを押下
		} else {

			mav.addObject("schoolEdit", s);
			mav.setViewName("admin/schoolEdit");
		}

		return mav;
	}

	/**
	 * 末吉
	 * 学校情報追加確認画面を表示する
	 * @return
	 */
	@PostMapping("schoolAddConfirm")
	public ModelAndView schoolAddConfirm(@RequestParam("button") String button, SchoolDisplay s, ModelAndView mav,
			Model model) {

		//確認ボタンを押下
		if (button.equals("確認")) {
			mav.addObject("SchoolDisplay", s);
			mav.setViewName("admin/schoolAddConfirm");

			return mav;

			//戻るボタンを押下し学校情報詳細画面を表示
		} else {

			List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails(school_id);

			mav.addObject("schoolS", SchoolDetails);
			mav.setViewName("admin/schoolDetails");

			return mav;
		}

	}

	/**
	 * 末吉
	 * 学校情報追加完了
	 * @return
	 */
	@PostMapping("schoolAddComp")
	public ModelAndView schoolAddComp(@RequestParam("button") String button, SchoolDisplay s, ModelAndView mav,
			Model model) {

		//追加ボタンを押下
		if (button.equals("追加")) {

			//同じ教室名が登録されている場合
			if (schoolDisplayService.isExistRoomName(s.getRoom_name(), s.getSchool_id())) {
				mav.addObject("errMsg", "※同じ教室名がすでに登録されています。");
				mav.addObject("schoolAdd", s);
				mav.setViewName("admin/schoolAdd");

				//まだ同じ教室名が登録されていない場合
			} else {
				schoolDisplayService.AddSchoolDetailsComp(s.getRoom_name(), s.getPc_flg(), s.getHall(), s.getFloor(),
						s.getSchool_id());

				// ポップアップを表示するために、画面遷移をしないようにする
				mav.addObject("schoolAddComp", true);
				mav.setViewName("admin/schoolAddConfirm");
			}

			//戻るボタンを押下
		} else {

			System.out.println(s);
			mav.addObject("schoolAdd", s);
			mav.setViewName("admin/schoolAdd");

		}

		return mav;
	}

	/**
	 * 末吉
	 * 学校情報削除画面の戻るボタンを押下
	 * @return
	 */
	@PostMapping("schoolDeleteConfirm")
	public ModelAndView schoolDeleteConfirm(@RequestParam("button") String button, SchoolDisplay s, ModelAndView mav,
			Model model) {

		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails(school_id);

		//ラジオボタンの情報を取得
		model.addAttribute("FormContent", new FormContents());

		mav.addObject("schoolS", SchoolDetails);
		mav.setViewName("admin/schoolDetails");

		return mav;

	}

	/**
	 * 末吉
	 * 学校情報削除完了
	 * @return
	 */
	@PostMapping("schoolDeleteComp")
	public ModelAndView schoolDeleteComp(@RequestParam("button") String button, SchoolDisplay s, ModelAndView mav,
			Model model) {

		schoolDisplayService.DeleteSchoolDetails(s.getSchool_id(), s.getRoom_id());

		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails(school_id);

		//ラジオボタンの情報を取得
		model.addAttribute("FormContent", new FormContents());

		mav.addObject("schoolS", SchoolDetails);
		mav.setViewName("admin/schoolDetails");

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

		//サービスのメソッドを呼び出す
		Iterable<UserDisplay> userList = userDisplayService.userList(school_id);

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

		mav.addObject("userDeleteConfirm", true);
		mav.setViewName("admin/userDeleteConfirm");

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
		userDisplayService.PassFormat(u.getUser_id());

		//mav.addObject("user",u);
		mav.addObject("passClearConfirm", true);
		mav.setViewName("admin/passClear");

		return mav;
	}

	/**
	 * 末吉
	 * ユーザ作成画面を表示する
	 * @return
	 */
	@GetMapping("userRegist")
	public String userRegist() {

		return "admin/userRegist";
	}

	/**
	 * 末吉
	 * ユーザ作成確認画面を表示する
	 * @return
	 */
	@PostMapping("userRegistConfirm")
	public ModelAndView userRegistConfirm(@RequestParam("csvFile") MultipartFile csvFile, ModelAndView mav) {
		// CSVファイルを読み込み、ユーザ情報を取得する
		List<UserForm> users = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				UserForm user = new UserForm();

				values[0] = values[0].replaceAll("\\p{C}", "");
				user.setUser_id(values[0]);
				user.setUser_name(values[1]);
				user.setUser_pass(values[2]);
				user.setSchool_id(values[3]);
				user.setEnr_year(values[4]);
				user.setUser_flg(Integer.parseInt(values[5]));
				users.add(user);
			}
		} catch (IOException e) {

		}

		mav.addObject("userRegist", users);
		mav.setViewName("admin/userRegistConfirm");

		return mav;
	}

	/**
	 * 末吉
	 * 新規ユーザ作成完了
	 * @return
	 */
	@PostMapping("userRegistComp")
	public ModelAndView userRegistComplete(@RequestParam("button") String button,
			@RequestParam("user_id") String[] userIds,
			@RequestParam("user_name") String[] userNames, @RequestParam("user_pass") String[] userPasses,
			@RequestParam("school_id") String[] schoolIds, @RequestParam("enr_year") String[] enrYears,
			@RequestParam("user_flg") int[] userFlgs, ModelAndView mav) {

		//作成ボタンを押下し、formに格納されているデータの数分繰り返しデータ追加
		if (button.equals("作成")) {
			for (int i = 0; i < userIds.length; i++) {
				//一件ずつ作成
				userDisplayService.InsertUser(userIds[i], userNames[i], userPasses[i], schoolIds[i], enrYears[i],
						userFlgs[i]);
			}

			mav.addObject("userRegistComp", true);
			mav.setViewName("admin/userRegistConfirm");

			return mav;

			//戻るボタンを押下
		} else {

			mav.addObject("userRegist", userIds);
			mav.setViewName("admin/userRegist");

			return mav;
		}
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

			mav.addObject("te", u);
			mav.setViewName("admin/teInfoRegistConfirm");

		} else {

			// IDが重複していた場合
			mav.addObject("errMsg", "IDが重複しています。");
			mav.setViewName("admin/teInfoRegist");
		}

		return mav;
	}

	/*
	 * 向江
	 * 新規講師登録完了
	 * @param t
	 * @param mav 
	 * @return
	 */
	@PostMapping("teInfoRegistComp")
	public ModelAndView teInfoRegistComp(@RequestParam("button") String button, UserDisplay u, ModelAndView mav,
			Model model) {

		//登録ボタンを押下
		if (button.equals("登録")) {
			userDisplayService.registerUser(u.getUser_id(), u.getUser_name(), "taskdon1", u.getSchool_name(),
					u.getEnr_year(), 1);

			// ポップアップを表示するために、画面遷移をしないようにする
			mav.addObject("teInfoRegistComp", true);
			mav.setViewName("admin/teInfoRegistConfirm");

			return mav;

			// 戻るボタンを押下	
		} else {

			mav.addObject("teInfoRegist", u);
			mav.setViewName("admin/teInfoRegist");
			return mav;
		}

	}

	/*
	 * 向江
	 * 講師一覧画面を表示するリクエストハンドラメソッド
	 * @return 講師一覧
	 */
	@GetMapping("teList")
	public ModelAndView dispTeList() {

		ModelAndView mav = new ModelAndView();

		Iterable<UserDisplay> teList = userDisplayService.teList(school_id);

		mav.addObject("tes", teList);
		mav.setViewName("admin/teList");

		return mav;
	}

	/*
	 * 向江
	 * 講師退職確認画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("teDeleteConfirm")
	public ModelAndView dispTeDelete(UserDisplay u, ModelAndView mav) {

		mav.addObject("te", u);
		mav.setViewName("admin/teDeleteConfirm");

		return mav;
	}

	/*
	 * 向江
	 * 講師情報退職完了画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("teDeleteComp")
	public ModelAndView TeDeleteComp(UserDisplay u, ModelAndView mav) {

		// サービスのメソッドを呼び出す
		userDisplayService.DeleteUser(u.getUser_id());

		mav.addObject("teDeleteComp", true);
		mav.setViewName("admin/teDeleteConfirm");

		return mav;
	}

	/*
	 * 向江
	 * 講師編集画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("teUpdate")
	public ModelAndView dispTeUpdate(UserDisplay u, ModelAndView mav) {

		
		System.out.println(u);
		mav.addObject("te", u);
		mav.setViewName("admin/teUpdate");

		return mav;
	}

	/*
	 * 向江
	 * 講師情報編集確認画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("teUpdateConfirm")
	public ModelAndView dispTeUpdateConfirm(UserDisplay u, ModelAndView mav) {

		mav.addObject("te", u);
		mav.setViewName("admin/teUpdateConfirm");

		return mav;
	}

	/*
	 * 向江
	 * 講師情報編集完了画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("teUpdateComp")
	public ModelAndView teUpdateComp(@RequestParam("button") String button, UserDisplay u, ModelAndView mav) {

		// 編集ボタンを押下
		if (button.equals("編集")) {

			userDisplayService.teInfoUpdate(u.getUser_id(), u.getUser_name(), u.getSchool_name(), u.getEnr_year(), 1);

			// ポップアップを表示するために、画面遷移しないようにする
			mav.addObject("teUpdateComp", true);
			mav.setViewName("admin/teUpdateConfirm");

			return mav;

			//戻るボタンを押下

		} else {

			mav.addObject("te", u);
			mav.setViewName("admin/teUpdate");
			return mav;
		}
	}

	/*
	 * 向江
	 * グループ一覧画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@GetMapping("groupList")
	public ModelAndView groupList(ModelAndView mav,
			@RequestParam(required = false) String selectedValue,
			@RequestParam(name = "dropdown", required = false) String drop) {

		List<TeamsForm> group;

		String dropid = null;
		String dropdown = "--";
		mav.getModel().clear();
		if (drop != null) {
			dropdown = selectedValue;
			group = groupDispService.groupList(dropdown, drop, school_id);
		} else {
			group = groupDispService.groupList(dropdown, dropid, school_id);
		}

		mav.addObject("groups", group);
		mav.setViewName("admin/groupList");

		return mav;
	}

	/**
	 * 向江
	 * グループ詳細画面を表示する
	 * @return
	 */
	@PostMapping("groupDetail")
	public ModelAndView groupDetail(ModelAndView mav,
			GroupMemberDeleteView g) {

		List<GroupDetailView> group = groupDispService.groupDetail(g.getGroup_id());

		mav.addObject("groups", group);
		mav.setViewName("admin/groupDetails");

		return mav;
	}

	/**
	 * 向江
	 * グループメンバ詳細画面を表示する
	 * @return
	 */
	@PostMapping("groupMemberDetails")
	public ModelAndView memberDetails(ModelAndView mav,
			GroupMemberDetailView gmdv) {

		System.out.println(gmdv);

		System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");

		List<GroupMemberDetailView> group = groupDispService.groupMemberDetail(gmdv.getUser_id(), gmdv.getGroup_id());

		for (GroupMemberDetailView g : group) {
			System.out.println(g);
		}

		mav.addObject("group", group);
		mav.setViewName("admin/groupMemberDetails");

		return mav;
	}

	/*
	 * 向江
	 * タスク詳細閲覧画面を表示する
	 * @return
	 */
	@GetMapping("taskDeails")
	public ModelAndView taskDeails(ModelAndView mav, TaskForm t) {

		List<TaskForm> task = groupDispService.taskDetail(t.getTask_name());

		mav.addObject("task", task);
		mav.setViewName("admin/groupMemberTaskDetails");

		return mav;

	}

	/**
	 * 末吉
	 * グループ編集画面を表示する
	 * @return
	 */
	@PostMapping("groupEdit")
	public ModelAndView groupEdit(ModelAndView mav, TeamsDisplay t,
			@RequestParam(name = "user_id", required = false) String[] user_id,
			@RequestParam(name = "user_name", required = false) String[] user_name,
			@RequestParam(name = "user_roll", required = false) String[] user_roll) {

		//リーダに任命するメンバ
		List<UserDisplay> leaderUser = new ArrayList<>();

		//リーダ以外のメンバ
		List<UserDisplay> memberUser = new ArrayList<>();

		for (int i = 0; i < user_id.length; i++) {

			//チェックボックスで選択したユーザIDとユーザ名を格納
			if (user_roll[i].equals("リーダ")) {

				UserDisplay leader = new UserDisplay();
				leader.setUser_id(user_id[i]);
				leader.setUser_name(user_name[i]);
				leaderUser.add(leader);

				//チェックボックスで選択しなかったユーザIDとユーザ名を格納
			} else {
				UserDisplay member = new UserDisplay();
				member.setUser_id(user_id[i]);
				member.setUser_name(user_name[i]);
				memberUser.add(member);
			}
		}

		mav.addObject("groupDetail", t);
		mav.addObject("leaderUser", leaderUser);
		mav.addObject("memberUser", memberUser);
		mav.setViewName("admin/groupEdit");

		return mav;
	}

	/*
	 * 末吉
	 * グループ編集確認画面を表示する
	 * @return
	 */
	@PostMapping("groupEditConfirm")
	public ModelAndView groupEditConfirm(ModelAndView mav, TeamsDisplay t,
			@RequestParam(name = "check", required = false) String[] check,
			@RequestParam(name = "selectedUserId", required = false) String[] user_id,
			@RequestParam(name = "selectUserName", required = false) String[] user_name) {

		//リーダ以外のメンバ
		List<UserDisplay> leaderUser = new ArrayList<>();
		List<UserDisplay> memberUser = new ArrayList<>();

		//リーダが選択されていない場合
		if (check == null) {
			for (int i = 0; i < user_id.length; i++) {
				UserDisplay user = new UserDisplay();
				user.setUser_id(user_id[i]);
				user.setUser_name(user_name[i]);
				memberUser.add(user);

				mav.addObject("errMsg", "リーダを選択してください");
				mav.addObject("groupDetail", t);
				mav.addObject("leaderUser", leaderUser);
				mav.addObject("memberUser", memberUser);
				mav.setViewName("admin/groupEdit");
			}
		} else {
			for (int i = 0; i < user_id.length; i++) {
				UserDisplay user = new UserDisplay();
				user.setUser_id(user_id[i]);
				user.setUser_name(user_name[i]);

				if (Arrays.asList(check).contains(user_id[i])) {
					leaderUser.add(user);
				} else {
					memberUser.add(user);
				}
			}

			mav.addObject("groupDetail", t);
			mav.addObject("leaderUser", leaderUser);
			mav.addObject("memberUser", memberUser);
			mav.setViewName("admin/groupEditConfirm");
		}

		return mav;
	}

	/**
	 * 末吉
	 * グループ編集完了
	 * @return
	 */
	@PostMapping("groupEditComp")
	public ModelAndView groupEditComp(ModelAndView mav, TeamsDisplay t,
			@RequestParam(name = "leaderUser_id", required = false) List<String> leaderUser_id,
			@RequestParam(name = "memberUser_id", required = false) List<String> memberUser_id) {

		if (memberUser_id != null) {

			// ここで、リーダーのデータを処理する
			for (String i : leaderUser_id) {
				groupDispService.groupEdit(i, t.getGroup_id(), "リーダ");
			}

			//ここで、メンバーのデータを処理する
			for (String i : memberUser_id) {
				groupDispService.groupEdit(i, t.getGroup_id(), "メンバ");
			}

		}

		mav.addObject("groupEditComp", true);
		mav.setViewName("admin/groupEditConfirm");

		return mav;
	}

	/*
	 * 向江・末吉
	 * グループメンバ削除確認画面を表示する
	 * @return
	 */
	@PostMapping("groupMemberDelDisp")
	public ModelAndView groupMemberDeleteDisp(ModelAndView mav,
			GroupMemberDeleteView g) {

		//グループメンバ削除確認画面のテーブルを表示する
		List<GroupMemberDeleteView> group = groupDispService.grMemDelDisp(g.getUser_id());

		mav.addObject("user", group);
		mav.setViewName("admin/groupMemberDelete");

		return mav;
	}

	/**
	 * 向江・末吉
	 * グループメンバ削除確認画面を表示する
	 * @return
	 */
	@PostMapping("groupMemberDeleteConfirm")
	public ModelAndView memberDeleteConfirm(@RequestParam("button") String button,
			GroupMemberDeleteView g, ModelAndView mav) {

		//グループメンバ削除確認画面のテーブルを表示する
		List<GroupMemberDeleteView> group = groupDispService.grMemDelDisp(g.getUser_id());

		System.out.println(group);

		for (int i = 0; i < group.size(); i++) {

			//更新後のスコアと進捗度を計算するサービスを呼び出す
			Object[] updateData = groupDispService.scoreCalc(g.getGroup_id(), g.getUser_id());

			//user_detailのtask_idを更新(タスクの自動振り分け)
			groupDispService.updateUserId(group.get(0).getTask_id(), (String) updateData[2]);

			//user_detailのscoreとuser_progressを更新
			groupDispService.updateScore((String) updateData[2], g.getGroup_id(), (int) updateData[0],
					(int) updateData[1]);
		}

		//user_detailテーブルの一列を削除
		groupDispService.groupMemberDelete(g.getGroup_id(), g.getUser_id());

		//グループの全体進捗を更新するサービスを呼び出す
		groupDispService.allProgress(g.getGroup_id());

		//グループ詳細画面を表示するためにグループ情報を格納
		List<GroupDetailView> groupDetails = groupDispService.groupDetail(g.getGroup_id());

		mav.addObject("group", groupDetails);
		mav.addObject("groupMemberDeleteComp", true);
		mav.setViewName("admin/groupMemberDelete");

		return mav;
	}

	/**
	 * グループ解散確認画面を表示する
	 * @return
	 */
	public String groupDeleteConfirm() {
		return "groupDeleteConfirm";
	}

	/**
	 * 末吉
	 * グループ作成画面を表示する
	 * @return
	 */
	@PostMapping("groupCreate")
	public ModelAndView groupCreate(ModelAndView mav,
			@RequestParam(name = "check", required = false) String[] check,
			@RequestParam(name = "userId", required = false) String[] userId,
			@RequestParam(name = "userName", required = false) String[] userName,
			@RequestParam(name = "group_id", required = false) Integer group_id,
			@RequestParam(name = "group_name", required = false) String group_name,
			@RequestParam(name = "genre", required = false) String genre) {

		//グループ一覧の作成ボタンから遷移してきた場合
		if (userId == null) {
			mav.addObject("Msg", "ユーザ選択ボタンを押下し、メンバを選択してください。");
			mav.setViewName("admin/groupCreate");

			//ユーザ選択画面から遷移してきた場合
		} else {

			if (check == null || check.length == 0) {

				//ユーザ一覧表示のメソッドを呼び出す
				Iterable<UserDisplay> userList = userDisplayService.userList(school_id);

				TeamsDisplay teamsDisplay = new TeamsDisplay();
				teamsDisplay.setGroup_id(group_id);
				teamsDisplay.setGroup_name(group_name);

				//グループメンバ追加の場合
				if (group_id != null) {

					//既に登録されているユーザIDを取得
					List<String> existUserIds = groupDispService.getExistUserIds(group_id);

					// ユーザリストからすでにグループに登録されているユーザを除外
					((Collection<UserDisplay>) userList).removeIf(user -> existUserIds.contains(user.getUser_id()));
				}

				mav.addObject("genre", genre);
				mav.addObject("groupDetail", teamsDisplay);
				mav.addObject("users", userList);
				mav.addObject("errMsg", "メンバを選択してください");
				mav.setViewName("admin/userSelectList");

			} else {

				//チェックボックスで選択したユーザIDとユーザ名を格納
				List<UserDisplay> userList = new ArrayList<>();
				for (int i = 0; i < userId.length; i++) {
					for (int j = 0; j < check.length; j++) {
						if (check[j].equals(userId[i])) {
							UserDisplay user = new UserDisplay();
							user.setUser_id(userId[i]);
							user.setUser_name(userName[i]);
							userList.add(user);
						}
					}
				}

				TeamsDisplay teamsDisplay = new TeamsDisplay();
				teamsDisplay.setGroup_id(group_id);
				teamsDisplay.setGroup_name(group_name);

				mav.addObject("genre", genre);
				mav.addObject("groupDetail", teamsDisplay);
				mav.addObject("selectUser", userList);

				if (session.getAttribute("button").equals("グループ作成")) {

					mav.setViewName("admin/groupCreate");
					mav.addObject("Msg", "リーダにするメンバにチェックを入れてください");

				} else {
					mav.setViewName("admin/groupMemberAdd");
				}
			}
		}

		return mav;
	}

	/**
	 * 末吉
	 * ユーザ選択画面を表示する
	 * @return
	 */
	@PostMapping("groupMemberSelect")
	public ModelAndView groupMemberSelect(ModelAndView mav,
			@RequestParam(name = "selectedUserId", required = false) String selectedUserId,
			@RequestParam(name = "group_id", required = false) Integer group_id,
			@RequestParam(name = "group_name", required = false) String group_name,
			@RequestParam(name = "genre", required = false) String genre,
			@RequestParam(name = "button", required = false) String button) {
		
		TeamsDisplay teamsDisplay = new TeamsDisplay();

		//ユーザ一覧表示のメソッドを呼び出す
		Iterable<UserDisplay> userList = userDisplayService.userList(school_id);

		//グループメンバ追加の場合
		if (group_id != null) {
			teamsDisplay.setGroup_id(group_id);

			//既に登録されているユーザIDを取得
			List<String> existUserIds = groupDispService.getExistUserIds(group_id);

			// ユーザリストからすでにグループに登録されているユーザを除外
			((Collection<UserDisplay>) userList).removeIf(user -> existUserIds.contains(user.getUser_id()));
		}

		teamsDisplay.setGroup_name(group_name);

		//選択しているユーザのIDを元に、チェックボックスをチェックする
		List<String> selectedUserIds = new ArrayList<>();
		if (selectedUserId != null) {
			String[] ids = selectedUserId.split(",");
			selectedUserIds = Arrays.asList(ids);
		}

		//選択されているユーザのチェックボックスをチェックする
		for (UserDisplay user : userList) {
			if (selectedUserIds.contains(user.getUser_id())) {
				user.setChecked(true);
			} else {
				user.setChecked(false);
			}
		}

		//どの画面から遷移してきたのかを格納
		session.setAttribute("button", button);

		mav.addObject("groupDetail", teamsDisplay);
		mav.addObject("genre", genre);
		mav.addObject("users", userList);
		mav.setViewName("admin/userSelectList");

		return mav;
	}

	/**
	 * 末吉
	 * グループ作成確認画面
	 * @return
	 */
	@PostMapping("groupCreateConfirm")
	public ModelAndView groupCreateConfirm(ModelAndView mav,
			@RequestParam(name = "check", required = false) String[] check,
			@RequestParam(name = "selectedUserId", required = false) String[] user_id,
			@RequestParam(name = "selectUserName", required = false) String[] user_name,
			@RequestParam(name = "group_name", required = false) String group_name,
			@RequestParam(name = "genre", required = false) String genre) {

		TeamsDisplay teamsDisplay = new TeamsDisplay();
		teamsDisplay.setGroup_name(group_name);
		teamsDisplay.setGenre(genre);

		//リーダに任命するメンバ
		List<UserDisplay> leaderUser = new ArrayList<>();

		//リーダ以外のメンバ
		List<UserDisplay> memberUser = new ArrayList<>();

		if (group_name == null || group_name.isEmpty()) {

			mav.addObject("errMsg", "グループ名を入力してください");
			mav.setViewName("admin/groupCreate");

		} else if (group_name.length() > 20) {

			mav.addObject("errMsg", "20文字以内のグループ名にしてください");
			mav.setViewName("admin/groupCreate");
		} else {

			if (user_id != null && check != null) {
				for (int i = 0; i < user_id.length; i++) {

					//チェックボックスで選択したユーザIDとユーザ名を格納
					if (check != null && Arrays.asList(check).contains(user_id[i])) {

						UserDisplay leader = new UserDisplay();
						leader.setUser_id(user_id[i]);
						leader.setUser_name(user_name[i]);
						leaderUser.add(leader);

						//チェックボックスで選択しなかったユーザIDとユーザ名を格納
					} else {
						UserDisplay member = new UserDisplay();
						member.setUser_id(user_id[i]);
						member.setUser_name(user_name[i]);
						memberUser.add(member);
					}
				}

				mav.addObject("groupDetail", teamsDisplay);
				mav.addObject("leaderUser", leaderUser);
				mav.addObject("memberUser", memberUser);
				mav.setViewName("admin/groupCreateConfirm");

				//ユーザが選択されていない場合
			} else if (user_id == null) {

				mav.addObject("errMsg", "ユーザを選択してください");
				mav.setViewName("admin/groupCreate");

			} else {
				mav.addObject("errMsg", "一人以上のリーダを選択してください");
				mav.setViewName("admin/groupCreate");
			}
		}

		return mav;
	}

	/**
	 * 末吉
	 * グループ作成完了
	 * @return
	 */
	@PostMapping("groupCreateComp")
	public ModelAndView groupCreateComplete(ModelAndView mav, TeamsDisplay teamsDisplay,
			@RequestParam(name = "group_name", required = false) String group_name,
			@RequestParam(name = "genre", required = false) String genre,
			@RequestParam(name = "leaderUser_id", required = false) List<String> leaderUser_id,
			@RequestParam(name = "memberUser_id", required = false) List<String> memberUser_id) {

		if (memberUser_id != null) {
			// セッション情報のUserFormを取得
			User userForm = (User) session.getAttribute("user");

			groupDispService.groupCreate(group_name, userForm.getSchool_id(), genre);

			//登録したグループIDを取得する
			int group_id = groupDispService.MaxGroupId(userForm.getSchool_id());

			// ここで、受け取ったデータを処理する
			for (int i = 0; i < leaderUser_id.size(); i++) {
				String leaderUserId = leaderUser_id.get(i);
				// ここで、リーダーのデータを処理する

				groupDispService.groupDetailCreate(leaderUserId, group_id, "リーダ", 0);
			}

			for (int i = 0; i < memberUser_id.size(); i++) {
				String memberUserId = memberUser_id.get(i);
				// ここで、メンバーのデータを処理する
				groupDispService.groupDetailCreate(memberUserId, group_id, "メンバ", 0);
			}
		}

		session.removeAttribute("button");

		mav.addObject("groupCreateComp", true);
		mav.setViewName("admin/groupCreateConfirm");

		return mav;
	}

	/**
	 * 末吉
	 * グループメンバ追加
	 * @return
	 */
	@PostMapping("groupMemberAdd")
	public ModelAndView groupMemberAdd(ModelAndView mav, TeamsDisplay teamsDisplay) {

		mav.addObject("Msg", "ユーザ選択ボタンを押下し、メンバを選択してください。");
		mav.addObject("groupDetail", teamsDisplay);
		mav.setViewName("admin/groupMemberAdd");

		return mav;
	}

	/**
	 * 末吉
	 * グループメンバ追加確認
	 * @return
	 */
	@PostMapping("groupMemberAddConfirm")
	public ModelAndView groupMemberAddConfirm(ModelAndView mav, TeamsDisplay teamsDisplay,
			@RequestParam(name = "selectedUserId", required = false) String[] user_id,
			@RequestParam(name = "selectUserName", required = false) String[] user_name) {

		//リーダ以外のメンバ
		List<UserDisplay> addMember = new ArrayList<>();

		//グループIDとグループ名を格納
		//		TeamsDisplay teamsDisplay = new TeamsDisplay();
		//		teamsDisplay.setGroup_name(group_name);
		//		teamsDisplay.setGroup_id(group_id);

		if (user_id != null) {
			//追加するメンバをListに格納
			for (int i = 0; i < user_id.length; i++) {
				UserDisplay member = new UserDisplay();
				member.setUser_id(user_id[i]);
				member.setUser_name(user_name[i]);
				addMember.add(member);
			}

			mav.addObject("groupDetail", teamsDisplay);
			mav.addObject("addMember", addMember);
			mav.setViewName("admin/groupMemberAddConfirm");
		} else {
			mav.addObject("groupDetail", teamsDisplay);
			mav.addObject("errMsg", "追加するメンバを選択してください。");
			mav.setViewName("admin/groupMemberAdd");
		}

		return mav;
	}

	/**
	 * 末吉
	 * グループメンバ追加完了
	 */
	@PostMapping("groupMemberAddComp")
	public ModelAndView groupMemberAddComp(ModelAndView mav,
			@RequestParam(name = "memberUser_id", required = false) String user_id,
			@RequestParam(name = "group_id", required = false) int group_id) {

		groupDispService.groupDetailCreate(user_id, group_id, "メンバ", 0);

		session.removeAttribute("button");

		mav.addObject("groupMemberAddComp", true);
		mav.setViewName("admin/groupMemberAddConfirm");

		return mav;
	}

	/**
	 * 末吉
	 * グループ解散確認
	 * @return
	 */
	@PostMapping("groupDissConfirm")
	public ModelAndView groupDelete(ModelAndView mav, TeamsDisplay teamsDisplay,
			@RequestParam("user_id") String[] user_id,
			@RequestParam("user_name") String[] user_name,
			@RequestParam("user_roll") String[] user_roll) {

		System.out.println("kaisann");
		System.out.println(teamsDisplay.getGroup_id());
		System.out.println(teamsDisplay.getGroup_name());
		for (String id : user_id) {
			System.out.println(id);

		}
		for (String name : user_name) {
			System.out.println(name);
		}

		List<TeamsDisplay> leaderList = new ArrayList<>();
		List<TeamsDisplay> memberList = new ArrayList<>();

		for (int i = 0; i < user_id.length; i++) {
			TeamsDisplay userInfo = new TeamsDisplay();
			userInfo.setUser_id(user_id[i]);
			userInfo.setUser_name(user_name[i]);
			userInfo.setUser_roll(user_roll[i]);

			if (user_roll[i].equals("リーダ")) {
				leaderList.add(userInfo);
			} else {
				memberList.add(userInfo);
			}
		}

		mav.addObject("leaderUser", leaderList);
		mav.addObject("memberUser", memberList);
		mav.addObject("teamsDisplay", teamsDisplay);
		mav.setViewName("admin/groupDissConfirm");

		return mav;
	}

	/**
	 * 末吉
	 * グループ解散完了
	 * @return
	 */
	@PostMapping("groupDissComp")
	public ModelAndView groupDissComp(ModelAndView mav,
			@RequestParam(name = "group_id", required = false) int group_id) {

		groupDispService.groupDiss(group_id);

		mav.addObject("groupDissComp", true);
		mav.setViewName("admin/groupDissConfirm");

		return mav;
	}

	/**
	 * 末吉
	 * チャット画面
	 * @return
	 */
	@GetMapping("chat")
	public ModelAndView chat(ModelAndView mav) {
		
		//チャットの通信可能相手を格納
		List<GroupDetailView> chatPartner = chatServise.setChatUser(school_id, "リーダ");
		
		mav.addObject("chatPartner", chatPartner);
		mav.setViewName("common/chat");
		return mav;
	}
	
	/**
	 * 末吉
	 * チャット相手検索
	 * @return
	 */
	@PostMapping("chatSearch")
	public ModelAndView chatSearch(ModelAndView mav,
	        @RequestParam(name = "search", required = false) String search) {

		//チャット相手を検索し、Listに格納する
	    List<GroupDetailView> chatPartner = chatServise.chatPartnerSearch(school_id, search, "リーダ");
	    mav.addObject("chatPartner", chatPartner);
	    mav.setViewName("common/chat");
	    
	    return mav;
	}
	
	/**
	 * 末吉
	 * チャット画面にチャット履歴を表示する
	 * @return
	 */
	@PostMapping("getChatHistory")
	public ModelAndView getChatHistory(ModelAndView mav,
	        @RequestParam(name = "chatUserId", required = false) String chatUser_id) {System.out.println(chatUser_id);
		
		List<ChatForm> chatHistory = chatServise.getChatHistory(user_id, chatUser_id);
		
		System.out.println(chatHistory);
		
		mav.addObject("chatHistory", chatHistory);
		mav.setViewName("common/chat");
		
		return mav;
	}

}
