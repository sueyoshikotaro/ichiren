package com.example.demo.controll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.example.demo.entity.Teams;
import com.example.demo.form.FormContents;
import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupMenberDetailView;
import com.example.demo.form.SchoolDisplay;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TeamsDisplay;
import com.example.demo.form.UserDisplay;
import com.example.demo.form.UserForm;
import com.example.demo.service.GroupDisplayServiceInterface;
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

	@Autowired
	HttpSession session;

	@Autowired
	@Qualifier("GroupDisplayImpl")
	GroupDisplayServiceInterface groupDispService;

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

		return "admin/menuAdmin";
	}

	/**
	 * 末吉
	 * 学校情報詳細画面を表示する
	 * @return
	 */
	@GetMapping("schoolDetails")
	public ModelAndView schoolDetails(ModelAndView mav, Model model) {

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
	public ModelAndView schoolDetailsChange(@RequestParam("button") String button,
			@RequestParam("flexRadioDefault") int room_id, @RequestParam("before_room_name") String room_name,
			ModelAndView mav) {

		//ラジオボタンで選択したデータを取得
		List<SchoolDisplay> EditSchoolDetails = schoolDisplayService.EditSchoolDetails(room_id);

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

		System.out.println(s.getBefore_room_name());

		//確認ボタンを押下
		if (button.equals("確認")) {

			mav.addObject("SchoolDisplay", s);
			mav.setViewName("admin/schoolEditConfirm");

			return mav;

			//戻るボタンを押下し学校情報詳細画面を表示
		} else {

			List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails();

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

			List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails();

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

		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails();

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
		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails();

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
		mav.setViewName("admin/updateComp");

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

		Iterable<UserDisplay> teList = userDisplayService.teList();

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

		mav.setViewName("admin/teUpdateComp");

		return mav;
	}

	/*
	 * 向江
	 * 講師編集画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("teUpdate")
	public ModelAndView dispTeUpdate(UserDisplay u, ModelAndView mav) {

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
	public ModelAndView teUpdateComp(@RequestParam("button") String button,UserDisplay u, ModelAndView mav) {

		// 編集ボタンを押下
		if(button.equals("編集")) {
			
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

		List<Teams> group = null;
		String dropid = null;
		String dropdown = "--";
		mav.getModel().clear();
		if (drop != null) {
			dropdown = selectedValue;
			group = groupDispService.groupList(dropdown, drop);
		} else {
			group = groupDispService.groupList(dropdown, dropid);
		}

		System.out.println(group);
		mav.addObject("groups", group);
		mav.setViewName("admin/groupList");

		return mav;
	}

	/**
	 * 向江
	 * グループ詳細画面を表示する
	 * @return
	 */
	@GetMapping("groupDetail")
	public ModelAndView groupDetail(ModelAndView mav, GroupDetailView g) {

		System.out.println(g.getGroup_id());
		
		List<GroupDetailView> group = groupDispService.groupDetail(g.getGroup_id());

		mav.addObject("group", group);
		mav.setViewName("admin/groupDetails");

		return mav;
	}

	/**
	 * 向江
	 * グループメンバ詳細画面を表示する
	 * @return
	 */
	@GetMapping("groupMenberDetails")
	public ModelAndView memberDetails(ModelAndView mav,
			@RequestParam(name="user_name") String user_name) {

		System.out.println(user_name);
		
		List<GroupMenberDetailView> group = groupDispService.groupMemberDetail(user_name);
		
		mav.addObject("group", group);
		mav.setViewName("admin/groupMenberDetails");

		return mav;
	}

	/*
	 * 向江
	 * タスク詳細閲覧画面を表示する
	 * @return
	 */
	@GetMapping("taskDeails")
	public ModelAndView taskDeails(ModelAndView mav, TaskForm t) {
		
		List<TaskForm> task = groupDispService.taskDetail(t.getUser_id(), t.getTask_id());
		
		mav.addObject("task", task);
		mav.setViewName("admin/taskDeails");
		
		return mav;
		
	}
	
	/**
	 * グループ編集画面を表示する
	 * @return
	 */
	public String groupUpdate() {
		return "groupUpdate";
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
	public String groupDeleteConfirm() {
		return "groupDeleteConfirm";
	}

	/**
	 * 末吉
	 * グループ作成画面を表示する
	 * @return
	 */
	@GetMapping("groupCreate")
	public ModelAndView groupCreate(ModelAndView mav,
			@RequestParam(name = "check", required = false) String[] check,
			@RequestParam(name = "userId", required = false) String[] userId,
			@RequestParam(name = "userName", required = false) String[] userName) {

		//グループ一覧の作成ボタンから遷移してきた場合
		if (userId == null) {
			mav.addObject("Msg", "ユーザを選択してください");
			mav.setViewName("admin/groupCreate");

			//ユーザ選択画面から遷移してきた場合
		} else {

			//チェックボックスで選択したユーザIDとユーザ名を格納
			List<UserDisplay> userList = new ArrayList<>();
			for (int i = 0; i < check.length; i++) {
				UserDisplay user = new UserDisplay();
				user.setUser_id(userId[i]);
				user.setUser_name(userName[i]);
				userList.add(user);
			}

			mav.addObject("Msg", "リーダにするメンバにチェックを入れてください");
			mav.addObject("selectUser", userList);
			mav.setViewName("admin/groupCreate");
		}

		return mav;
	}

	/**
	 * 末吉
	 * ユーザ選択画面を表示する
	 * @return
	 */
	@PostMapping("groupMemberSelect")
	public ModelAndView groupMemberSelect(ModelAndView mav) {

		//サービスのメソッドを呼び出す
		Iterable<UserDisplay> userList = userDisplayService.userList();

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
	public ModelAndView groupCreateConfirm(ModelAndView mav, TeamsDisplay t,
			@RequestParam(name = "check", required = false) String[] check,
			@RequestParam(name = "userId", required = false) String[] userId) {

		//リーダに任命するメンバ
		List<String> checkedUserId = new ArrayList<>();
		
		//リーダ以外のメンバ
		List<String> uncheckedUserId = new ArrayList<>();

		System.out.println("kita!!!");
		
		if (userId != null && check != null) {
			for (int i = 0; i < userId.length; i++) {
				if (check != null && Arrays.asList(check).contains(userId[i])) {
					checkedUserId.add(userId[i]);
				} else {
					uncheckedUserId.add(userId[i]);
				}
			}
			//ユーザが選択されていない場合
		} else if(userId == null) {
			
			mav.addObject("Msg", "ユーザを選択してください");
			mav.setViewName("admin/groupCreate");
			
		} else {
			mav.addObject("Msg", "一人以上のリーダを選択してください");
			mav.setViewName("admin/groupCreate");
		}
		
		System.out.println("checkyu-zaId");
		for(String id1 : checkedUserId) {
			System.out.println(id1);
		}
		
		System.out.println("uncheckyu-zaId");
		for(String id2 : uncheckedUserId) {
			System.out.println(id2);
		}

		mav.addObject("checkedUserId", checkedUserId);
		mav.addObject("uncheckedUserId", uncheckedUserId);
		mav.setViewName("admin/groupCreateConfirm");

		return mav;
	}

}
