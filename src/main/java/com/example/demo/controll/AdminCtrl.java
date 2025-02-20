package com.example.demo.controll;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
import com.example.demo.entity.School;
import com.example.demo.entity.User;
import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDisplay;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.SchoolDisplay;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TeamsDisplay;
import com.example.demo.form.UserDisplay;
import com.example.demo.repository.SchoolDisplayCrudRepository;
import com.example.demo.service.ChatServiceInterface;
import com.example.demo.service.GroupDisplayServiceInterface;
import com.example.demo.service.SchoolDisplayServiceInterface;
import com.example.demo.service.TaskServiceInterface;
import com.example.demo.service.UserDisplayServiceInterface;

/**
 * 管理者のコントローラ
 */
@Controller
@RequestMapping("/taskdon/admin")
public class AdminCtrl {

	//フィールド
	@Autowired
	SchoolDisplayCrudRepository schoolCrudRepo;

	@Autowired
	@Qualifier("schoolDisplayService")
	SchoolDisplayServiceInterface schoolDisplayService;

	@Autowired
	@Qualifier("userDisplayImpl")
	UserDisplayServiceInterface userDisplayService;

	@Autowired
	@Qualifier("groupService")
	GroupDisplayServiceInterface groupDispService;

	//湊原追加
	@Autowired
	@Qualifier("taskService")
	TaskServiceInterface TaskService;

	@Autowired
	@Qualifier("ChatService")
	ChatServiceInterface chatServise;

	@Autowired
	HttpSession session;

	//セッション情報のフィールド変数
	public int school_id;
	public String user_id;

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
	 * 学校情報詳細画面を表示する
	 * @return
	 */
	@LoginRequired
	@GetMapping("schoolDetails")
	public ModelAndView schoolDetails(ModelAndView mav) {
		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

		//教室情報取得
		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails(school_id);
		//学校情報取得
		List<School> SchoolInfo = schoolDisplayService.SchoolInfo(school_id);

		if (SchoolDetails.isEmpty()) {
			mav.addObject("Msg", "教室情報がありません");
		}

		mav.addObject("schoolS", SchoolDetails);
		mav.addObject("schoolInfo", SchoolInfo);
		mav.setViewName("admin/schoolDetails");

		return mav;
	}

	/**
	 * 末吉
	 * 学校情報追加、編集、削除画面を表示する
	 * @return
	 */
	@LoginRequired
	@PostMapping("schoolDetailsChange")
	public ModelAndView schoolDetailsChange(@RequestParam("button") String button,
			@RequestParam(name = "flexRadioDefault", required = false) Integer room_id,
			ModelAndView mav) {

		try {
			// ログインユーザのエンティティを取得
			User userEntity = (User) session.getAttribute("user");
			// エンティティの中のschool_idを取得
			school_id = userEntity.getSchool_id();

			//学校情報取得
			List<School> SchoolDetails = schoolDisplayService.SchoolInfo(school_id);

			if (room_id != null) {
				//ラジオボタンで選択したデータを取得
				List<SchoolDisplay> EditSchoolDetails = schoolDisplayService.EditSchoolDetails(room_id, school_id);

				//選択したデータの教室名を編集前の教室名として保持
				EditSchoolDetails.get(0).setBefore_room_name(EditSchoolDetails.get(0).getRoom_name());

				//編集ボタンを押下
				if (button.equals("edit")) {

					mav.addObject("schoolEdit", EditSchoolDetails);
					mav.setViewName("admin/schoolEdit");

					//削除ボタンを押下
				} else if (button.equals("delete")) {

					mav.addObject("schoolDelete", EditSchoolDetails);
					mav.setViewName("admin/schoolDelete");

				}
			}

			//学校情報がない場合
			if (button.equals("add")) {
				mav.addObject("schoolAdd", SchoolDetails);
				mav.setViewName("admin/schoolAdd");
			}

		} catch (Exception e) {

			return new ModelAndView("redirect:/admin/schoolDetails");
		}

		return mav;
	}

	/**
	 * 末吉
	 * 学校情報編集確認画面を表示する
	 * @return
	 */
	@LoginRequired
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
	@LoginRequired
	@PostMapping("schoolEditComp")
	public ModelAndView schoolEditComp(@RequestParam("button") String button, SchoolDisplay s, ModelAndView mav) {

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
						s.getFloor(), s.getSchool_id(), s.getRoom_id());

				// ポップアップを表示するために、画面遷移をしないようにする
				mav.addObject("schoolEditComp", true);
				mav.setViewName("admin/schoolEditConfirm");
			}
		}

		return mav;
	}

	/**
	 * 末吉
	 * 学校情報追加確認画面を表示する
	 * @return
	 */
	@LoginRequired
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
	@LoginRequired
	@PostMapping("schoolAddComp")
	public ModelAndView schoolAddComp(SchoolDisplay s, ModelAndView mav) {

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

		return mav;
	}

	/**
	 * 末吉
	 * 学校情報削除完了
	 * @return
	 */
	@LoginRequired
	@PostMapping("schoolDeleteComp")
	public ModelAndView schoolDeleteComp(SchoolDisplay s, ModelAndView mav) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

		schoolDisplayService.DeleteSchoolDetails(s.getSchool_id(), s.getRoom_id());

		//教室情報取得
		List<SchoolDisplay> SchoolDetails = schoolDisplayService.SchoolDetails(school_id);
		//学校情報取得
		List<School> SchoolInfo = schoolDisplayService.SchoolInfo(school_id);

		if (SchoolDetails.isEmpty()) {
			mav.addObject("Msg", "教室情報がありません");
		}

		mav.addObject("schoolS", SchoolDetails);
		mav.addObject("schoolInfo", SchoolInfo);
		mav.setViewName("admin/schoolDetails");

		return mav;
	}

	/*
	 * 向江
	 * ユーザ一覧のリクエストハンドラメソッド
	 * @return ユーザ一覧
	 */
	@LoginRequired
	@GetMapping("userList")
	public ModelAndView userList(@RequestParam(name = "selectedSchool", required = false) Integer selectedSchool,
			@RequestParam(name = "selectedYear", required = false) String selectedYear, ModelAndView mav) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

		//結成年度取得
		List<TeamsDisplay> year = groupDispService.selectEstYear("user");
		//所属学校
		List<TeamsDisplay> school = groupDispService.selectSchool();

		Calendar calendar = Calendar.getInstance();
		int y = calendar.get(Calendar.YEAR);
		Iterable<UserDisplay> userList = null;

		//サービスのメソッドを呼び出す
		if ((selectedSchool == null && selectedYear == null) || (selectedSchool == 0 && selectedYear.equals("選択なし"))) {
			userList = userDisplayService.userFilterList(school_id, String.valueOf(y));
		} else {
			userList = userDisplayService.userFilterList((int) selectedSchool, selectedYear);
		}

		mav.addObject("school", school);
		mav.addObject("year", year);
		mav.addObject("users", userList);
		mav.setViewName("admin/userList");

		return mav;

	}

	/*
	 * 向江
	 * ユーザ削除確認画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@LoginRequired
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
	@LoginRequired
	@PostMapping("userDeleteConfirm")
	public ModelAndView userDeleteConfirm(UserDisplay u, ModelAndView mav) {

		//所属グループ一覧取得
		List<GroupDisplay> deptGroupList = groupDispService.deptGroupList(u.getUser_id());

		//
		for (GroupDisplay i : deptGroupList) {
			//所属するグループの削除するユーザ情報を取得
			List<GroupMemberDetailView> group = groupDispService.grMemDelDisp(u.getUser_id(), i.getGroup_id());

			if (!group.isEmpty()) {
				for (int j = 0; j < group.size(); j++) {
					//更新後のスコアと進捗度を計算するサービスを呼び出す
					Object[] updateData = groupDispService.scoreCalc(i.getGroup_id(), u.getUser_id());

					//user_detailのtask_idを更新(タスクの自動振り分け)
					groupDispService.updateUserId(group.get(j).getTask_id(), (String) updateData[2]);

					//user_detailのscoreとuser_progressを更新
					groupDispService.updateScore((String) updateData[2], i.getGroup_id(), (int) updateData[0],
							(int) updateData[1]);
				}
			}

			//user_detailテーブルの一列を削除
			groupDispService.groupMemberDelete(i.getGroup_id(), u.getUser_id());

			//グループの全体進捗を更新するサービスを呼び出す
			groupDispService.allProgress(i.getGroup_id());

		}

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
	@LoginRequired
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
	@LoginRequired
	@PostMapping("passClearConfirm")
	public ModelAndView passClearConfirm(UserDisplay u, ModelAndView mav) {

		// サービスのメソッドを呼び出す
		userDisplayService.PassFormat(u.getUser_id());

		mav.addObject("passClearConfirm", true);
		mav.setViewName("admin/passClear");

		return mav;
	}

	/**
	 * 末吉
	 * ユーザ作成画面を表示する
	 * @return
	 */
	@LoginRequired
	@GetMapping("userRegist")
	public String userRegist() {

		return "admin/userRegist";
	}

	/**
	 * 末吉
	 * ユーザ作成確認画面を表示する
	 * @return
	 */
	@LoginRequired
	@PostMapping("userRegistConfirm")
	public ModelAndView userRegistConfirm(@RequestParam("csvFile") MultipartFile csvFile, ModelAndView mav) {
		// CSVファイルを読み込み、ユーザ情報を取得する
		List<UserDisplay> users = new ArrayList<>();

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), "UTF-8"));
			String line;
			//CSVファイルの中身を取得
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				UserDisplay user = new UserDisplay();

				values[0] = values[0].replaceAll("\\p{C}", "");
				user.setUser_id(values[0]);
				user.setUser_name(values[1]);
				user.setUser_pass("taskdon1");
				user.setSchool_id(school_id);
				user.setEnr_year(values[3]);
				user.setUser_flg(Integer.parseInt(values[4]));
				users.add(user);
			}

			if (users.isEmpty()) {
				mav.addObject("errMsg1", "CSVファイルを選択してください");
				mav.setViewName("admin/userRegist");
			} else {
				mav.addObject("userRegist", users);
				mav.setViewName("admin/userRegistConfirm");
			}

		} catch (Exception e) {
			mav.addObject("errMsg1", "登録内容に不備があります。CSVファイルの中身を確認して下さい。");
			mav.setViewName("admin/userRegist");
		}

		return mav;
	}

	/**
	 * 末吉
	 * 新規ユーザ作成完了
	 * @return
	 */
	@LoginRequired
	@PostMapping("userRegistComp")
	public ModelAndView userRegistComplete(@RequestParam("user_id") String[] userIds,
			@RequestParam("user_name") String[] userNames, @RequestParam("user_pass") String[] userPasses,
			@RequestParam("school_id") String[] schoolIds, @RequestParam("enr_year") String[] enrYears,
			@RequestParam("user_flg") int[] userFlgs, ModelAndView mav) {

		try {
			//formに格納されているデータの数分繰り返しデータ追加
			for (int i = 0; i < userIds.length; i++) {
				//一件ずつ作成
				userDisplayService.InsertUser(userIds[i], userNames[i], userPasses[i], schoolIds[i], enrYears[i],
						userFlgs[i]);
			}

			mav.addObject("userRegistComp", true);
			mav.setViewName("admin/userRegistConfirm");

		} catch (Exception e) {
			mav.addObject("errMsg1", "すでに登録されているユーザが含まれているか、CSVファイルの形式が正しくありません。");
			mav.addObject("errMsg2", "※CSVファイルにはユーザID,ユーザ名,入学年度が入力されていること");
			mav.setViewName("admin/userRegist");
		}

		return mav;
	}

	/*
	 * 向江
	 * 新規講師登録画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@LoginRequired
	@GetMapping("teInfoRegist")
	public ModelAndView dispRegist(ModelAndView mav) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();
		// エンティティの中のuser_idを取得
		user_id = userEntity.getUser_id();

		Optional<School> school = schoolCrudRepo.findById(school_id);

		mav.addObject("school_name", school.get().getSchool_name());
		mav.setViewName("admin/teInfoRegist");

		return mav;
	}

	/*
	 * 向江
	 * 新規講師登録確認画面を表示するリクエストハンドラメソッド
	 * @param u
	 * @param mav
	 */
	@LoginRequired
	@PostMapping("teInfoRegistConfirm")
	public ModelAndView dispTeInfoRegistConf(UserDisplay u, ModelAndView mav) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のuser_idを取得
		user_id = userEntity.getUser_id();

		//入力したユーザIDを取得
		String userId = u.getUser_id();

		//admin○○アカウントでログインしているとき
		if (user_id.startsWith("admin")) {
			if (Pattern.matches("ad\\d{8}", userId)) {
				if (userDisplayService.userIDCheck(userId)) {
					mav.addObject("te", u);
					mav.setViewName("admin/teInfoRegistConfirm");

				} else {

					// IDが重複していた場合
					mav.addObject("errMsg", "IDが重複しています。");
					mav.setViewName("admin/teInfoRegist");
				}

				//adユーザ以外を作成しようとしている場合
			} else {

				mav.addObject("errMsg", "adで始まる登録者自身の情報を入力して下さい。");
				mav.setViewName("admin/teInfoRegist");
			}

			//ad〇〇アカウントでログインしているとき
		} else {
			if (!Pattern.matches("te\\d{8}", userId)) {

				mav.addObject("errMsg", "講師IDは「te」 + 8桁の数字です。");
				mav.setViewName("admin/teInfoRegist");

			} else {

				if (userDisplayService.userIDCheck(userId)) {

					mav.addObject("te", u);
					mav.setViewName("admin/teInfoRegistConfirm");

				} else {

					// IDが重複していた場合
					mav.addObject("errMsg", "IDが重複しています。");
					mav.setViewName("admin/teInfoRegist");
				}
			}
		}

		mav.addObject("school_name", u.getSchool_name());
		return mav;
	}

	/*
	 * 向江
	 * 新規講師登録完了
	 * @param t
	 * @param mav 
	 * @return
	 */
	@LoginRequired
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
	@LoginRequired
	@GetMapping("teList")
	public ModelAndView dispTeList(ModelAndView mav, UserDisplay u) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

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
	@LoginRequired
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
	@LoginRequired
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
	@LoginRequired
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
	@LoginRequired
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
	@LoginRequired
	@PostMapping("teUpdateComp")
	public ModelAndView teUpdateComp(@RequestParam("button") String button, UserDisplay u, ModelAndView mav) {

		// 編集ボタンを押下
		if (button.equals("編集")) {

			userDisplayService.teInfoUpdate(u.getUser_name(), u.getSchool_name(), u.getEnr_year(), u.getUser_id());

			// ポップアップを表示するために、画面遷移しないようにする
			mav.addObject("teUpdateComp", true);
			mav.setViewName("admin/teUpdateConfirm");

			//戻るボタンを押下

		} else {

			mav.addObject("te", u);
			mav.setViewName("admin/teUpdate");

		}

		return mav;
	}

	/*
	 * 向江
	 * グループ一覧画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@LoginRequired
	@GetMapping("groupList")
	public ModelAndView groupList(ModelAndView mav,
			@RequestParam(name = "selectedSchool", required = false) Integer selectedSchool,
			@RequestParam(name = "selectedYear", required = false) String selectedYear,
			@RequestParam(name = "selectedGenre", required = false) String selectedGenre) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

		//結成年度取得
		List<TeamsDisplay> year = groupDispService.selectEstYear("group");
		//所属学校
		List<TeamsDisplay> school = groupDispService.selectSchool();
		//ジャンル
		List<TeamsDisplay> genre = groupDispService.selectGenre();

		List<TeamsDisplay> group = null;
		Calendar calendar = Calendar.getInstance();
		int y = calendar.get(Calendar.YEAR);

		if (selectedGenre == null) {

			group = groupDispService.groupList(String.valueOf(y), selectedGenre, school_id);

		} else if (selectedSchool != null || selectedYear != null) {

			group = groupDispService.groupList(selectedYear, selectedGenre, (int) selectedSchool);
		}

		mav.addObject("school", school);
		mav.addObject("genre", genre);
		mav.addObject("year", year);
		mav.addObject("groups", group);
		mav.setViewName("admin/groupList");

		return mav;
	}

	/**
	 * 向江
	 * グループ詳細画面を表示する
	 * @return
	 */
	@LoginRequired
	@PostMapping("groupDetail")
	public ModelAndView groupDetail(ModelAndView mav,
			GroupMemberDetailView g) {

		//グループメンバの情報を格納
		List<GroupDisplay> group = groupDispService.groupDetail(g.getGroup_id());
		//グループ情報を格納
		List<GroupDisplay> groupInfo = groupDispService.groupInfo(g.getGroup_id());

		//グループメンバがいない場合メッセージを表示する
		if (group.isEmpty()) {
			mav.addObject("Msg", "グループメンバがいません");
		}

		mav.addObject("groups", group);
		mav.addObject("groupInfo", groupInfo);
		mav.setViewName("admin/groupDetails");

		return mav;
	}

	/**
	 * 向江
	 * グループメンバ詳細画面を表示する
	 * @return
	 */
	@LoginRequired
	@PostMapping("groupMemberDetails")
	public ModelAndView memberDetails(ModelAndView mav,
			GroupMemberDetailView gmdv,
			@RequestParam(name = "groupId", required = false) Integer groupId,
			@RequestParam(name = "userId", required = false) String userId,
			@RequestParam(name = "selectedValue", required = false) String selectedValue) {

		//ドロップダウンリスト取得処理
		List<TaskForm> taskCategory = null;

		List<GroupMemberDetailView> group = null;

		if (groupId == null || userId == null) {
			taskCategory = TaskService.selectCategory(gmdv.getGroup_id());
			selectedValue = "選択なし";
			//ドロップダウンリストが選択されていない場合の処理
			group = groupDispService.memberDetail(gmdv.getUser_id(), gmdv.getGroup_id(), selectedValue);
		} else {
			taskCategory = TaskService.selectCategory(groupId);
			//ドロップダウンリストが選択されている場合の処理
			group = groupDispService.memberDetail(userId, groupId, selectedValue);
		}

		mav.addObject("Category", taskCategory);
		mav.addObject("group", group);
		mav.setViewName("admin/groupMemberDetails");

		return mav;
	}

	/*
	 * 向江
	 * タスク詳細閲覧画面を表示する
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskDeails")
	public ModelAndView taskDeails(ModelAndView mav, TaskForm t) {

		List<TaskForm> task = groupDispService.taskDetail(t.getTask_id());

		mav.addObject("task", task);
		mav.setViewName("admin/groupMemberTaskDetails");

		return mav;

	}

	/**
	 * 末吉
	 * グループ編集画面を表示する
	 * @return
	 */
	@LoginRequired
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
	@LoginRequired
	@PostMapping("groupEditConfirm")
	public ModelAndView groupEditConfirm(ModelAndView mav, TeamsDisplay t,
			@RequestParam(name = "check", required = false) String[] check,
			@RequestParam(name = "selectedUserId", required = false) String[] user_id,
			@RequestParam(name = "selectUserName", required = false) String[] user_name) {

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
	@LoginRequired
	@PostMapping("groupEditComp")
	public ModelAndView groupEditComp(ModelAndView mav, TeamsDisplay t,
			@RequestParam(name = "leaderUser_id", required = false) List<String> leaderUser_id,
			@RequestParam(name = "memberUser_id", required = false) List<String> memberUser_id) {

		// ここで、リーダーのデータを処理する
		if (leaderUser_id != null) {
			for (String i : leaderUser_id) {
				groupDispService.groupEdit(i, t.getGroup_id(), "リーダ");
			}
		}

		//ここで、メンバーのデータを処理する
		if (memberUser_id != null) {
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
	@LoginRequired
	@PostMapping("groupMemberDelDisp")
	public ModelAndView groupMemberDeleteDisp(ModelAndView mav,
			GroupMemberDetailView g) {

		//グループメンバ削除確認画面のテーブルを表示する
		List<GroupMemberDetailView> group = groupDispService.grMemDelDisp(g.getUser_id(), g.getGroup_id());

		if (group.isEmpty()) {
			mav.addObject("Msg", "タスク情報がありません");
			mav.addObject("user_name", g.getUser_name());
			mav.addObject("user_id", g.getUser_id());
			mav.addObject("group_id", g.getGroup_id());
		}

		mav.addObject("user", group);
		mav.setViewName("admin/groupMemberDelete");

		return mav;
	}

	/**
	 * 向江・末吉
	 * グループメンバ削除完了
	 * @return
	 */
	@LoginRequired
	@PostMapping("groupMemberDeleteConfirm")
	public ModelAndView memberDeleteConfirm(GroupMemberDetailView g, ModelAndView mav) {

		//グループメンバ削除確認画面のテーブルを表示する
		List<GroupMemberDetailView> group = groupDispService.grMemDelDisp(g.getUser_id(), g.getGroup_id());

		if (!group.isEmpty()) {
			for (int i = 0; i < group.size(); i++) {
				//更新後のスコアと進捗度を計算するサービスを呼び出す
				Object[] updateData = groupDispService.scoreCalc(g.getGroup_id(), g.getUser_id());

				//user_detailのtask_idを更新(タスクの自動振り分け)
				groupDispService.updateUserId(group.get(i).getTask_id(), (String) updateData[2]);

				//user_detailのscoreとuser_progressを更新
				groupDispService.updateScore((String) updateData[2], g.getGroup_id(), (int) updateData[0],
						(int) updateData[1]);

			}
		}

		//user_detailテーブルの一列を削除
		groupDispService.groupMemberDelete(g.getGroup_id(), g.getUser_id());

		//グループの全体進捗を更新するサービスを呼び出す
		groupDispService.allProgress(g.getGroup_id());

		mav.addObject("group_id", g.getGroup_id());
		mav.addObject("groupMemberDeleteComp", true);
		mav.setViewName("admin/groupMemberDelete");

		return mav;
	}

	/**
	 * 末吉
	 * グループ作成画面を表示する
	 * @return
	 */
	@LoginRequired
	@PostMapping("groupCreate")
	public ModelAndView groupCreate(ModelAndView mav,
			@RequestParam(name = "check", required = false) String[] check,
			@RequestParam(name = "userId", required = false) String[] userId,
			@RequestParam(name = "userName", required = false) String[] userName,
			@RequestParam(name = "group_id", required = false) Integer group_id,
			@RequestParam(name = "group_name", required = false) String group_name,
			@RequestParam(name = "genre", required = false) String genre) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

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
	@LoginRequired
	@PostMapping("groupMemberSelect")
	public ModelAndView groupMemberSelect(ModelAndView mav,
			@RequestParam(name = "selectedUserId", required = false) String selectedUserId,
			@RequestParam(name = "group_id", required = false) Integer group_id,
			@RequestParam(name = "group_name", required = false) String group_name,
			@RequestParam(name = "genre", required = false) String genre,
			@RequestParam(name = "button", required = false) String button) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

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
	@LoginRequired
	@PostMapping("groupCreateConfirm")
	public ModelAndView groupCreateConfirm(ModelAndView mav,
			@RequestParam(name = "check", required = false) String[] check,
			@RequestParam(name = "selectedUserId", required = false) String[] user_id,
			@RequestParam(name = "selectUserName", required = false) String[] user_name,
			@RequestParam(name = "group_name", required = false) String group_name,
			@RequestParam(name = "genre", required = false) String genre) {

		TeamsDisplay teamsDisplay = new TeamsDisplay();
		teamsDisplay.setGenre(genre);

		//リーダに任命するメンバ
		List<UserDisplay> leaderUser = new ArrayList<>();

		//リーダ以外のメンバ
		List<UserDisplay> memberUser = new ArrayList<>();

		//エラー画面に遷移したときに選択したユーザを確認できるように
		List<User> userIdAndName = new ArrayList<>();
		for (int i = 0; i < user_id.length; i++) {
			User user = new User();
			user.setUser_id(user_id[i]);
			user.setUser_name(user_name[i]);
			userIdAndName.add(user);
		}

		if (group_name == null || group_name.isEmpty()) {

			mav.addObject("selectUser", userIdAndName);
			mav.addObject("genre", genre);
			mav.addObject("groupDetail", teamsDisplay);
			mav.addObject("errMsg", "グループ名を入力してください");
			mav.setViewName("admin/groupCreate");

		} else if (group_name.length() > 20) {

			mav.addObject("selectUser", userIdAndName);
			mav.addObject("genre", genre);
			mav.addObject("groupDetail", teamsDisplay);
			mav.addObject("errMsg", "20文字以内のグループ名にしてください");
			mav.setViewName("admin/groupCreate");
		} else {

			teamsDisplay.setGroup_name(group_name);
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

			} else {

				mav.addObject("selectUser", userIdAndName);
				mav.addObject("groupDetail", teamsDisplay);
				mav.addObject("genre", genre);
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
	@LoginRequired
	@PostMapping("groupCreateComp")
	public ModelAndView groupCreateComplete(ModelAndView mav, TeamsDisplay teamsDisplay,
			@RequestParam(name = "group_name", required = false) String group_name,
			@RequestParam(name = "genre", required = false) String genre,
			@RequestParam(name = "leaderUser_id", required = false) List<String> leaderUser_id,
			@RequestParam(name = "memberUser_id", required = false) List<String> memberUser_id) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

		//グループ作成
		groupDispService.groupCreate(group_name, school_id, genre);

		//登録したグループIDを取得する
		int group_id = groupDispService.MaxGroupId(school_id);

		// ここで、リーダーのデータを処理する
		for (int i = 0; i < leaderUser_id.size(); i++) {
			String leaderUserId = leaderUser_id.get(i);
			groupDispService.groupDetailCreate(leaderUserId, group_id, "リーダ", 0);
		}

		// ここで、メンバーのデータを処理する
		if (memberUser_id != null) {
			for (int i = 0; i < memberUser_id.size(); i++) {
				String memberUserId = memberUser_id.get(i);
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
	@LoginRequired
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
	@LoginRequired
	@PostMapping("groupMemberAddConfirm")
	public ModelAndView groupMemberAddConfirm(ModelAndView mav, TeamsDisplay teamsDisplay,
			@RequestParam(name = "selectedUserId", required = false) String[] user_id,
			@RequestParam(name = "selectUserName", required = false) String[] user_name) {

		//リーダ以外のメンバ
		List<UserDisplay> addMember = new ArrayList<>();

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
	@LoginRequired
	@PostMapping("groupMemberAddComp")
	public ModelAndView groupMemberAddComp(ModelAndView mav,
			@RequestParam(name = "memberUser_id", required = false) String[] user_id,
			@RequestParam(name = "group_id", required = false) int group_id) {

		try {
			session.removeAttribute("button");

			//メンバ追加
			for (String memberUser_id : user_id) {
				groupDispService.groupDetailCreate(memberUser_id, group_id, "メンバ", 0);
			}

			mav.addObject("group_id", group_id);
			mav.addObject("groupMemberAddComp", true);
			mav.setViewName("admin/groupMemberAddConfirm");

			return mav;

		} catch (Exception e) {
			//グループ一覧にリダイレクト
			return new ModelAndView("redirect:groupList");
		}
	}

	/**
	 * 末吉
	 * グループ解散確認
	 * @return
	 */
	@LoginRequired
	@PostMapping("groupDissConfirm")
	public ModelAndView groupDelete(ModelAndView mav, TeamsDisplay teamsDisplay,
			@RequestParam(name = "user_id", required = false) String[] user_id,
			@RequestParam(name = "user_name", required = false) String[] user_name,
			@RequestParam(name = "user_roll", required = false) String[] user_roll) {

		List<TeamsDisplay> leaderList = new ArrayList<>();
		List<TeamsDisplay> memberList = new ArrayList<>();

		if (user_id != null) {
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
	@LoginRequired
	@PostMapping("groupDissComp")
	public ModelAndView groupDissComp(ModelAndView mav,
			@RequestParam(name = "group_id", required = false) int group_id) {

		//解散に伴う処理
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
	@LoginRequired
	@GetMapping("chat")
	public ModelAndView chat(ModelAndView mav) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

		//チャットの通信可能相手を格納
		List<GroupDisplay> chatPartner = chatServise.setChatUser(school_id);

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

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のschool_idを取得
		school_id = userEntity.getSchool_id();

		//チャット相手を検索し、Listに格納する
		List<GroupDisplay> chatPartner = chatServise.chatPartnerSearch(school_id, search, "リーダ");

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

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のuser_idを取得
		user_id = userEntity.getUser_id();

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

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");
		// エンティティの中のuser_idを取得
		user_id = userEntity.getUser_id();

		List<ChatForm> chatHistory = chatServise.sendChat(user_id, chatPartnerUserId, sendText);

		mav.addObject("chatHistory", chatHistory);
		mav.setViewName("common/chat");

		return mav;
	}
}