package com.example.demo.controll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.entity.Tdlist;
import com.example.demo.entity.User;
import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDisplay;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.NoticeViewForm;
import com.example.demo.form.SchoolDisplay;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TaskReqForm;
import com.example.demo.form.TaskView;
import com.example.demo.form.TdlistForm;
import com.example.demo.repository.UserDisplayCrudRepository;
import com.example.demo.service.ChatServiceInterface;
import com.example.demo.service.GroupDisplayServiceInterface;
import com.example.demo.service.NoticeServiceInterface;
import com.example.demo.service.TaskServiceInterface;
import com.example.demo.service.TodoServiceInterface;
import com.example.demo.service.UserDisplayServiceInterface;

@Controller
@RequestMapping("/taskdon/user")
public class UserCtrl {

	//フィールド

	@Autowired
	UserDisplayCrudRepository groupCrudRepo;

	//坂本追加
	@Autowired
	@Qualifier("userDisplayImpl")
	UserDisplayServiceInterface userDisplayService;

	//坂本追加
	@Autowired
	@Qualifier("groupService")
	GroupDisplayServiceInterface groupDispService;

	//湊原追加
	@Autowired
	@Qualifier("taskService")
	TaskServiceInterface TaskService;

	//湊原追加
	@Autowired
	@Qualifier("todoService")
	TodoServiceInterface TodoService;

	//向江追加
	@Autowired
	@Qualifier("NoticeService")
	NoticeServiceInterface NoticeService;

	//末吉追加
	@Autowired
	@Qualifier("ChatService")
	ChatServiceInterface chatServise;

	//セッション
	@Autowired
	HttpSession session;

	//末吉追加(セッション情報格納)
	private int school_id;
	private int group_id = 0;
	private String user_id;
	private String user_roll;

	//湊原追加
	private int progress;
	private int score;

	/**
	 * ログアウト画面を表示
	 * @return
	 */
	@GetMapping("logout")
	public String logout(HttpServletResponse response) {

		if (session.getAttribute("groupId") != null && user_id.startsWith("st")) {
			int group_id = (int) session.getAttribute("groupId");

			//居場所を'休憩中'に更新
			groupDispService.roomUpdate("休憩中", group_id);
		}

		//セッション情報を削除
		session.invalidate();

		return "redirect:/taskdon/user/login";
	}

	/**
	 * ログイン画面を表示 
	 * @return
	 */
	@GetMapping("login")
	public String login() {

		return "common/login";
	}

	/**
	 * ログイン処理・遷移分岐
	 * 在籍チェック
	 * ID重複チェック
	 * パスワードチェック
	 * @return
	 */
	@LoginRequired
	@PostMapping("login")
	public ModelAndView loginCheck(ModelAndView mav, String user_id, String user_pass, RedirectAttributes ra) {

		Optional<User> user = groupCrudRepo.findById(user_id);

		if (user.isPresent() && user.get().getUser_flg() == 1 && user.get().getUser_pass().equals(user_pass)) {

			session.setAttribute("user_id", user_id);
			session.setAttribute("user", user.get());

			// ログインユーザのエンティティを取得
			User userEntity = (User) session.getAttribute("user");

			// エンティティの中の値をそれぞれフィールドに設定_末吉追加
			this.user_id = userEntity.getUser_id();

			ra.addFlashAttribute("user_id", user_id);

			if (user.get().getUser_id().contains("admin") && user.get().getUser_pass().equals("admin")) {

				return new ModelAndView("redirect:/taskdon/admin/teInfoRegist"); //上位管理者がログインした場合(初回のみ)

			} else if (user.get().getUser_pass().equals("taskdon1")) {

				return new ModelAndView("redirect:/taskdon/user/passReset"); //パスワード再設定に遷移

			} else if (user.get().getUser_id().contains("te") || user.get().getUser_id().contains("ad")) {

				return new ModelAndView("redirect:/taskdon/admin/menu"); //管理者がログインした場合

			} else if (user.get().getUser_id().contains("st")) {

				return new ModelAndView("redirect:/taskdon/user/deptGroupList"); //ユーザがログインした場合

			} else {
				mav.addObject("errMsg", "IDまたはパスワードが違います。");
				mav.setViewName("common/login"); //ログイン失敗
			}
		} else {
			mav.addObject("errMsg", "IDまたはパスワードが違います。");
			mav.setViewName("common/login"); //ログイン失敗
		}

		return mav;
	}

	/**
	 * パスワード再設定画面
	 * @return
	 */
	@GetMapping("passReset")
	public ModelAndView passReset(ModelAndView mav, @ModelAttribute("user_id") String user_id) {

		mav.addObject("user_id", user_id);
		mav.addObject("newPass");
		mav.addObject("confirmPass");
		mav.setViewName("common/passReset");

		return mav;
	}

	/**
	 * パスワード更新処理
	 * @return
	 */
	@PostMapping("updatePass")
	public ModelAndView updatePass(ModelAndView mav, @RequestParam("user_id") String user_id,
			@RequestParam("newPass") String newPass, @RequestParam("confirmPass") String confirmPass) {

		Optional<User> user = groupCrudRepo.findById(user_id);

		User u = user.get();

		String passwordPattern = "^[a-zA-Z0-9]{8,16}$"; // 8-16桁の英数字のみ設定可

		if (newPass.equals("") || confirmPass.equals("")) {

			mav.addObject("errMsg", "未入力の項目があります。");
			mav.addObject("user_id", user_id); // user_idを再度渡す
			mav.setViewName("common/passReset");

			return mav;
		} else if (!newPass.matches(passwordPattern)) {

			mav.addObject("errMsg", "パスワードは8桁以上16桁以下の英数字でなければなりません。");
			mav.addObject("user_id", user_id); // user_idを再度渡す
			mav.setViewName("common/passReset");

			return mav;
		} else if (!newPass.equals(confirmPass)) {

			mav.addObject("errMsg", "新パスワードと確認パスワードが一致しません。");
			mav.addObject("user_id", user_id); // user_idを再度渡す
			mav.setViewName("common/passReset");

			return mav;
		} else if (newPass.equals("taskdon1") && confirmPass.equals("taskdon1")) {

			mav.addObject("errMsg", "'taskdon1'はデフォルトパスワードのため、登録できません。");
			mav.addObject("user_id", user_id); // user_idを再度渡す
			mav.setViewName("common/passReset");

			return mav;
		} else {

			userDisplayService.userPassReset(u.getUser_id(), newPass); //新パスワードに更新

			mav.setViewName("redirect:/taskdon/user/login"); // ログイン画面へリダイレクト

			return mav;
		}
	}

	/**
	 * 所属グループ一覧画面に遷移
	 * @return
	 */
	@LoginRequired
	@GetMapping("deptGroupList")
	public ModelAndView deptGroupList(ModelAndView mav, @ModelAttribute("user_id") String user_id) {

		if (session.getAttribute("groupId") != null) {
			// エンティティの中の値をそれぞれフィールドに設定_末吉追加
			group_id = (int) session.getAttribute("groupId");

			//居場所を'休憩中'に更新_末吉追加
			groupDispService.roomUpdate("休憩中", group_id);
		}

		if (user_id == null || user_id.isEmpty()) {

			// セッションから取得
			user_id = (String) session.getAttribute("user_id");
		}

		Optional<User> user = groupCrudRepo.findById(user_id);

		if (user.isPresent()) {

			List<GroupDisplay> deptGroupList = groupDispService.deptGroupList(user_id);

			if (deptGroupList.isEmpty()) {
				mav.addObject("Msg", "所属グループがありません。");
			}

			mav.setViewName("common/deptGroupList");
			mav.addObject("groupS", deptGroupList);
			session.setAttribute("deptGroupList", deptGroupList);
			session.setAttribute("user", user.get());
		} else {

			// ユーザーが存在しない場合のエラー処理
			mav.setViewName("errMsg");
			mav.addObject("errorMessage", "ユーザーが見つかりません。");
		}

		return mav;
	}

	/**
	 * 坂本・末吉
	 * メニュー(ユーザ)
	 * @return
	 */
	@LoginRequired
	@PostMapping("menu")
	public ModelAndView menu(@RequestParam(name = "group_id", required = false) Integer group_id,
			@RequestParam(name = "user_roll", required = false) String user_roll,
			ModelAndView mav) {

		//所属グループ一覧画面から遷移してきた場合
		if (group_id != null && user_roll != null) {
			//セッションに値を設定
			session.setAttribute("groupUser", TaskService.taskUserSearch(group_id)); //ユーザ名,担当者検索用
			session.setAttribute("groupId", group_id); //グループID,
			session.setAttribute("user_roll", user_roll); //役職,ユーザ種別分類用]

			// ログインユーザのエンティティを取得
			User userEntity = (User) session.getAttribute("user");

			// エンティティの中の値をそれぞれフィールドに設定_末吉追加
			school_id = userEntity.getSchool_id();
			this.group_id = (int) session.getAttribute("groupId");
		}
		//連絡事項一覧取得
		List<NoticeViewForm> noticeList = NoticeService.noticeDisp(this.group_id);

		mav.addObject("noticeList", noticeList);
		mav.setViewName("common/menuUser");

		//居場所リストに表示する内容取得
		List<SchoolDisplay> roomList = groupDispService.roomSelect(school_id);

		// リストの先頭の列に値を追加
		SchoolDisplay room = new SchoolDisplay();
		room.setRoom_name("休憩中");
		roomList.add(0, room);

		// リストの最後の列に値を追加
		room = new SchoolDisplay();
		room.setRoom_name("校外作業中");
		roomList.add(room);

		mav.addObject("status", "休憩中");
		mav.addObject("roomList", roomList);

		return mav;
	}

	/**
	 * 末吉
	 * 居場所更新
	 */
	@LoginRequired
	@PostMapping("updateStatus")
	public ModelAndView roomUpdate(ModelAndView mav,
			@RequestParam(name = "updateStatus", required = false) String updateStatus) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		school_id = userEntity.getSchool_id();
		group_id = (int) session.getAttribute("groupId");

		//居場所更新
		groupDispService.roomUpdate(updateStatus, group_id);

		//居場所情報取得
		List<SchoolDisplay> roomList = groupDispService.roomSelect(school_id);

		mav.addObject("status", updateStatus);
		mav.addObject("roomList", roomList);
		mav.setViewName("common/menuUser");

		return mav;
	}

	/**
	 * タスク一覧画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@GetMapping("taskList")
	public ModelAndView taskList(ModelAndView mav,
			@RequestParam(name = "selectedValue", required = false) String selectedValue) {

		group_id = (int) session.getAttribute("groupId");

		String score = "--";
		List<TaskForm> task = null;
		if (selectedValue == null || selectedValue.equals("全員")) {
			selectedValue = "全員";
		} else {
			score = String.valueOf(TaskService.userScore(selectedValue, group_id));
		}

		task = TaskService.taskDisplayList(selectedValue, group_id);

		mav.addObject("score", score);
		mav.addObject("user", selectedValue);
		mav.addObject("tasks", task);
		mav.setViewName("common/taskList");
		return mav;
	}

	/**
	 * タスク登録画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@GetMapping("taskRegister")
	public ModelAndView taskRegister(ModelAndView mav) {
		mav.setViewName("leader/taskRegist");
		return mav;
	}

	/**
	 * タスク登録確認画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskRegistConfirm")
	public ModelAndView taskRegistConfirm(TaskForm t, ModelAndView mav) {

		//開始予定日が終了予定日以前かどうかの判定
		int result = t.getStart_date().compareToIgnoreCase(t.getEnd_date());
		if (result <= 0 && t.getUser_name() != null) {
			mav.addObject("tasks", t);
			mav.setViewName("leader/taskRegistConfirm");
		} else {
			if (result > 0) {
				mav.addObject("DateErrMsg", "終了予定日は開始予定日以降に設定してください");
			}
			if (t.getUser_name() == null) {
				mav.addObject("UserErrMsg", "担当者を選択してください");
			}
			mav.setViewName("leader/taskRegist");
		}

		return mav;
	}

	/**
	 * タスク登録完了画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskRegistComplete")
	public ModelAndView taskRegistComplete(ModelAndView mav, TaskForm t) {

		group_id = (int) session.getAttribute("groupId");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date st_date = null;
		Date end_date = null;
		int score = TaskService.userScore(t.getUser_name(), group_id);
		try {
			st_date = sdf.parse(t.getStart_date());
			end_date = sdf.parse(t.getEnd_date());
			TaskService.taskRegister(t.getTask_category(), t.getTask_name(), t.getTask_content(), "未着手",
					st_date, end_date, t.getTask_priority(), t.getTask_level(), t.getTask_weight(), t.getUser_name(),
					group_id);
			//スコアの足しこみ
			score = score + Integer.valueOf(t.getTask_weight());
			TaskService.userUpScore(score, t.getUser_name(), group_id);

			mav.addObject("taskRegistComp", true);
			mav.addObject("tasks", t);
			mav.setViewName("leader/taskRegistConfirm");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mav;
	}

	/**
	 * タスク詳細画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskDetails")
	public ModelAndView taskDetail(@RequestParam(name = "taskProgress", required = false) Integer progress,
			@RequestParam(name = "task_id") Integer task_id, ModelAndView mav) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		group_id = (int) session.getAttribute("groupId");
		user_id = userEntity.getUser_id();

		mav.getModel().clear();
		List<TaskForm> detail = TaskService.taskDetails(task_id, group_id);
		this.progress = detail.get(0).getProgress();
		String taskstatus = null;
		if (progress != null) {
			if (progress == 100) {
				taskstatus = "完了";
			} else {
				if (this.progress == progress) {
					taskstatus = detail.get(0).getTask_status();
				} else {
					if (this.progress < progress) {
						taskstatus = "着手";
					} else {
						taskstatus = "出戻り";
					}
				}
			}

			TaskService.taskUpProgress(task_id, Integer.valueOf(progress), taskstatus);
			//メンバの進捗更新
			groupDispService.updateProgress(user_id, group_id);
			//全体進捗更新
			groupDispService.allProgress(group_id);

			this.progress = progress;
			detail = TaskService.taskDetails(task_id, group_id);
		}
		mav.addObject("task", detail);
		mav.setViewName("common/taskDetails");
		return mav;
	}

	/**
	 * タスク詳細から編集、削除画面を表示する
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskDetailsChange")
	public ModelAndView taskDetailChange(@RequestParam("button") String button, TaskForm t, ModelAndView mav) {

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		group_id = (int) session.getAttribute("groupId");

		//編集ボタンを押下
		if (button.equals("edit")) {
			this.score = t.getTask_weight();
			mav.setViewName("leader/taskEdit");
			//削除ボタンを押下
		} else if (button.equals("delete")) {
			mav.setViewName("leader/taskDeleteConfirm");
		}
		mav.addObject("task", t);
		return mav;
	}

	/**
	 * タスク編集確認画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @param mav
	 * @param t
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskEditConfirm")
	public ModelAndView taskEditConfirm(ModelAndView mav, TaskForm t) {
		mav.addObject("task", t);
		mav.setViewName("leader/taskEditConfirm");
		return mav;
	}

	/**
	 * タスク編集完了画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @param mav
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskEditComplete")
	public ModelAndView taskEditComplete(ModelAndView mav, TaskForm t) {

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		group_id = (int) session.getAttribute("groupId");

		//スコアの足しこみ
		int score = TaskService.userScore(t.getUser_name(), group_id);
		score = score - this.score;
		score = score + t.getTask_priority() * t.getTask_level();
		TaskService.userUpScore(score, t.getUser_name(), group_id);

		int weight = t.getTask_priority() * t.getTask_level();
		TaskService.taskUpdate(t.getTask_id(), t.getTask_category(), t.getTask_name(), t.getTask_content(),
				t.getTask_priority(), weight, t.getUser_name());

		mav.addObject("taskEditComp", true);
		mav.addObject("task", t);
		mav.setViewName("leader/taskEditConfirm");

		return mav;
	}

	/**
	 * タスク削除をするリクエストハンドラメソッド
	 * 湊原
	 * @param mav
	 * @param t
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskDeleteComplete")
	public ModelAndView taskDeleteConfirm(ModelAndView mav, TaskForm t) {

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		group_id = (int) session.getAttribute("groupId");

		//スコアの減算
		int score = TaskService.userScore(t.getUser_name(), group_id);
		score = score - t.getTask_weight();
		TaskService.userUpScore(score, t.getUser_name(), group_id);

		//グループの全体進捗を更新するサービスを呼び出す
		groupDispService.allProgress(group_id);

		TaskService.taskUpFlg(t.getTask_id());
		mav.addObject("taskDeleteComp", true);
		mav.addObject("task", t);
		mav.setViewName("leader/taskDeleteConfirm");

		return mav;
	}

	/**
	 * タスク未承認一覧画面を表示する
	 * 湊原
	 */
	@LoginRequired
	@GetMapping("taskUnapproved")
	public ModelAndView taskUnapproved(ModelAndView mav) {

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		group_id = (int) session.getAttribute("groupId");

		mav.addObject("taskNonapp", TaskService.selectTaskUnapproved(group_id));
		mav.setViewName("leader/taskUnapproved");
		return mav;
	}

	/*
	 * タスク承認確認画面を表示するリクエストハンドラメソッド
	 * 向江
	 */
	@LoginRequired
	@PostMapping("taskAppConfirm")
	public ModelAndView taskAppConfirm(ModelAndView mav, TaskReqForm t) {
		mav.addObject("taskAppConf", t);
		mav.setViewName("leader/taskApprovedConfirm");
		return mav;
	}

	/**
	 * タスク承認を行う
	 * 湊原
	 * @param mav
	 * @param t
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskAppComplete")
	public ModelAndView taskAppComplete(ModelAndView mav, TaskView t, @RequestParam(name = "button") String button) {
		//日付型定義
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//変数定義
		Date st_date = null;
		Date end_date = null;
		int score = 0;

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		this.group_id = (int) session.getAttribute("groupId");

		try {
			if (button.equals("app")) {
				score = TaskService.userScore(t.getUser_name(), group_id);
				//開始予定日が終了予定日以前かどうかの判定
				int result = t.getStart_date().compareToIgnoreCase(t.getEnd_date());
				if (result <= 0) {
					//開始予定日と終了予定日の型変換
					st_date = sdf.parse(t.getStart_date());
					end_date = sdf.parse(t.getEnd_date());
					//登録が出来た場合
					if (TaskService.taskRegister(t.getReq_category(), t.getReq_name(), t.getReq_content(), "未着手",
							st_date, end_date, t.getTask_priority(), t.getTask_level(), t.getTask_weight(),
							t.getUser_name(),
							group_id)) {

						//スコアの足しこみ
						score = score + t.getTask_weight();
						TaskService.userUpScore(score, t.getUser_name(), group_id);
						mav.addObject("taskAppComp", true);
					} else {
						mav.addObject("taskAppComp", false);
					}
				} else {
					mav.addObject("ErrMsg", "終了予定日は開始予定日以降に設定してください");
				}
				mav.setViewName("leader/taskApprovedConfirm");
			}
			//タスク承認(フラグ更新)
			TaskService.taskReqFlg(t.getRequest_id());
			if (button.equals("はい")) {
				return new ModelAndView("redirect:/taskdon/user/taskUnapproved");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		mav.addObject("taskAppConf", t);

		return mav;
	}

	/**
	 * タスク申請画面を表示
	 * @return
	 */
	@LoginRequired
	@GetMapping("taskRequest")
	public ModelAndView taskRequest(ModelAndView mav) {
		mav.setViewName("member/taskRequest");
		return mav;
	}

	/**
	 * タスク申請確認画面を表示
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskRequestConfirm")
	public ModelAndView taskRequestConfirm(ModelAndView mav, TaskReqForm t) {
		mav.addObject("taskRequest", t);
		mav.setViewName("member/taskRequestConfirm");
		return mav;
	}

	/**
	 * タスク申請完了
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskRequestComplete")
	public ModelAndView taskRequestComplete(ModelAndView mav, TaskReqForm t) {
		Date date = new Date();

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		group_id = (int) session.getAttribute("groupId");

		TaskService.registerTaskReq(t.getReq_category(), t.getReq_name(), t.getReq_content(), t.getReq_reason(), date,
				t.getUser_name(), group_id);
		mav.addObject("taskRequestComp", true);
		mav.addObject("taskRequest", t);
		mav.setViewName("member/taskRequestConfirm");
		return mav;
	}

	/**
	 * ToDoリスト画面を表示
	 * @return
	 */
	@LoginRequired
	@GetMapping("todoList")
	public ModelAndView todoList(ModelAndView mav,
			@RequestParam(name = "tdlist_id", required = false) Integer tdlist_id,
			@RequestParam(name = "checked", required = false) Boolean checked) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		user_id = userEntity.getUser_id();

		if (tdlist_id != null) {
			if (checked != null) {
				if (checked) {
					TodoService.todoUpFlg(tdlist_id, 1);
				} else {
					TodoService.todoUpFlg(tdlist_id, 0);
				}
			}
		}
		mav.addObject("todoList", TodoService.selectTodoList(user_id));
		mav.setViewName("common/todoList");
		return mav;
	}

	/**
	 * 選択されたToDoリスト画面を表示
	 * @return
	 */
	@LoginRequired
	@PostMapping("todoListChange")
	public ModelAndView todoListChange(ModelAndView mav,
			@RequestParam(name = "flexRadioDefault", required = false) Integer tdlist_id,
			@RequestParam(name = "button") String button) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		user_id = userEntity.getUser_id();

		List<Tdlist> todo = null;
		if (tdlist_id != null) {
			todo = TodoService.selectTodo(tdlist_id);
		}
		if (button.equals("regist")) {
			mav.setViewName("common/todoListRegist");
		} else if (button.equals("edit")) {
			mav.setViewName("common/todoListEdit");
		} else if (button.equals("delete")) {
			mav.setViewName("common/todoListDelete");
		}
		mav.addObject("button", button);
		mav.addObject("id", tdlist_id);
		mav.addObject("todo", todo);
		mav.addObject("todoList", TodoService.selectTodoList(user_id));
		return mav;
	}

	/**
	 * ToDoリスト登録を行う
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@PostMapping("registConfirm")
	public ModelAndView registConfirm(ModelAndView mav, TdlistForm t) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		user_id = userEntity.getUser_id();

		TodoService.todoRegister(user_id, t.getTdlist_content(), t.getImportance());
		return new ModelAndView("redirect:/taskdon/user/todoList");
	}

	/**
	 * ToDoリスト編集を行う
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@PostMapping("editConfirm")
	public ModelAndView editConfirm(ModelAndView mav, TdlistForm t) {
		TodoService.todoUpdate(t.getTdlist_id(), t.getTdlist_content(), t.getImportance());
		return new ModelAndView("redirect:/taskdon/user/todoList");
	}

	/**
	 * ToDoリスト削除を行う
	 * 湊原
	 * @return
	 */
	@LoginRequired
	@PostMapping("deleteConfirm")
	public ModelAndView deleteConfirm(ModelAndView mav, TdlistForm t) {
		TodoService.todoDelete(t.getTdlist_id());
		return new ModelAndView("redirect:/taskdon/user/todoList");
	}

	/*
	 * 向江
	 * 連絡事項作成画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@LoginRequired
	@GetMapping("noticeRegist")
	public ModelAndView noticeRegist(ModelAndView mav) {

		mav.setViewName("leader/noticeRegist");

		return mav;
	}

	/*
	 * 向江
	 * 連絡事項作成確認画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@LoginRequired
	@PostMapping("noticeRegistConfirm")
	public ModelAndView noticeregistConfirm(ModelAndView mav, NoticeViewForm n) {

		mav.addObject("notice", n);
		mav.setViewName("leader/noticeRegistConfirm");

		return mav;
	}

	/*
	 * 向江
	 * 連絡事項作成完了画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@LoginRequired
	@PostMapping("noticeRegistComp")
	public ModelAndView noticeRegistComp(ModelAndView mav,
			@ModelAttribute NoticeViewForm n,
			@RequestParam(name = "button") String button,
			@RequestParam(name = "title") String title) throws ParseException {

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		group_id = (int) session.getAttribute("groupId");

		//登録ボタンを押下
		if (button.equals("登録")) {
			NoticeService.noticeRegist(n.getTitle(), n.getContact_msg(), 0, n.getSend_by(), group_id);

			// ポップアップを表示するために、画面遷移をしないようにする
			mav.addObject("noticeRegistComp", true);
			mav.addObject("notice", n);
			mav.setViewName("leader/noticeRegistConfirm");

			// 戻るボタンを押下	
		} else {

			mav.addObject("noticeRegist", n);
			mav.setViewName("leader/noticeRegist");

		}

		return mav;
	}

	/*
	 * 向江
	 * 連絡事項削除確認画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@LoginRequired
	@PostMapping("noticeDelete")
	public ModelAndView noticeDelete(ModelAndView mav,
			@RequestParam(name = "check", required = false) Integer[] check) {

		//削除ボタンを押下
		//チェックボックスが選択されていない場合
		if (check == null || check.length == 0) {

			mav.addObject("errMsg", "連絡事項を選択してください");
			mav.setViewName("common/menuUser");

			// チェックボックスが選択されている場合
		} else {

			//チェックボックスで選択した連絡事項IDを格納
			List<NoticeViewForm> checkList = new ArrayList<>();
			for (int i = 0; i < check.length; i++) {
				NoticeViewForm notice = new NoticeViewForm();
				List<NoticeViewForm> noticeList = new ArrayList<>(NoticeService.selectNotice(check[i]));
				notice.setNotice_id(noticeList.get(0).getNotice_id());
				notice.setUser_name(noticeList.get(0).getUser_name());
				notice.setTitle(noticeList.get(0).getTitle());
				notice.setContact_msg(noticeList.get(0).getContact_msg());
				notice.setSend_date(noticeList.get(0).getSend_date());
				checkList.add(notice);
			}
			mav.addObject("notice", checkList);
			mav.setViewName("leader/noticeDeleteConfirm");
		}
		return mav;

	}

	/*
	 * 向江
	 * 連絡事項削除完了するリクエストハンドラメソッド
	 * @return
	 */
	@LoginRequired
	@PostMapping("noticeDeleteConfirm")
	public ModelAndView noticeDeleteConfirm(ModelAndView mav,
			@RequestParam(name = "notice_id", required = false) Integer[] notice_id,
			@RequestParam(name = "action") String action) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		school_id = userEntity.getSchool_id();
		group_id = (int) session.getAttribute("groupId");

		if (action.equals("yes") && notice_id != null) {

			for (int i = 0; i < notice_id.length; i++) {
				// 連絡事項削除のサービス
				NoticeService.noticeDelete((int) notice_id[i]);
			}
		}

		List<NoticeViewForm> noticeList = NoticeService.noticeDisp(group_id);

		mav.addObject("noticeList", noticeList);
		mav.setViewName("common/menuUser");

		List<SchoolDisplay> roomList = groupDispService.roomSelect(school_id);

		mav.addObject("roomList", roomList);

		return mav;
	}

	/**
	 * 湊原
	 * ユーザ側のメンバ一覧を表示するリクエストハンドラメソッド
	 * @param mav
	 * @param gmdv
	 * @return
	 */
	@LoginRequired
	@GetMapping("memberList")
	public ModelAndView memberList(ModelAndView mav) {

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		this.group_id = (int) session.getAttribute("groupId");

		List<GroupMemberDetailView> group = groupDispService.memberList(group_id);
		int allProgress = groupDispService.selectProgress(group_id);

		mav.addObject("progress", allProgress);
		mav.addObject("group", group);
		mav.setViewName("leader/memberList");
		return mav;
	}

	/**
	 * 湊原
	 * ユーザ側のメンバ詳細を表示するリクエストハンドラメソッド
	 * @param mav
	 * @param g
	 * @return
	 */
	@LoginRequired
	@PostMapping("memberDetails")
	public ModelAndView memberDetails(ModelAndView mav, GroupMemberDetailView g,
			@RequestParam(name = "groupId", required = false) Integer groupId,
			@RequestParam(name = "userId", required = false) String userId,
			@RequestParam(name = "selectedValue", required = false) String selectedValue) {

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		this.group_id = (int) session.getAttribute("groupId");

		//ドロップダウンリスト取得処理
		List<TaskForm> taskCategory = TaskService.selectCategory(group_id);

		List<GroupMemberDetailView> membertask = null;
		if (groupId == null || userId == null) {
			selectedValue = "選択なし";
			//ドロップダウンリストが選択されていない場合の処理
			membertask = groupDispService.memberDetail(g.getUser_id(), g.getGroup_id(), selectedValue);
		} else {
			//ドロップダウンリストが選択されている場合の処理
			membertask = groupDispService.memberDetail(userId, groupId, selectedValue);
		}
		//		mav.addObject("userProgress", g.getUser_progress());
		mav.addObject("Category", taskCategory);
		mav.addObject("memberTask", membertask);
		mav.setViewName("leader/memberDetails");
		return mav;
	}

	/**
	 * 末吉追加
	 * チャット画面を表示
	 * @return
	 */
	@LoginRequired
	@GetMapping("chat")
	public ModelAndView chat(ModelAndView mav, HttpServletRequest request) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		school_id = userEntity.getSchool_id();
		this.group_id = (int) session.getAttribute("groupId");
		user_id = userEntity.getUser_id();
		this.user_roll = (String) session.getAttribute("user_roll");

		//リーダの場合は管理者とグループメンバすべてを格納
		if (user_roll.equals("リーダ")) {

			//チャットの通信可能相手(管理者)を格納
			List<GroupDisplay> chatPartnerAdmin = chatServise.leaderSetChatAdmin(school_id);

			mav.addObject("chatPartnerAdmin", chatPartnerAdmin);
		}

		//チャットの通信可能相手を格納
		List<GroupDisplay> chatPartner = chatServise.memberSetChatUser(school_id, group_id);

		//ログインしているユーザの分のデータはListから除外する
		List<GroupDisplay> filteredChatPartner = chatPartner.stream()
				.filter(chat -> !chat.getUser_id().equals((String) session.getAttribute("user_id")))
				.collect(Collectors.toList());

		// チャット相手の情報を取得する
		boolean hasChatPartnerUserId = chatServise.CookieCheck(request);

		mav.addObject("hasChatPartnerUserId", hasChatPartnerUserId);
		mav.addObject("chatPartnerMember", filteredChatPartner);
		mav.setViewName("common/chat");

		return mav;
	}

	/**
	 * 末吉追加
	 * チャット相手検索
	 * @return
	 */
	@LoginRequired
	@PostMapping("chatSearch")
	public ModelAndView chatSearch(ModelAndView mav,
			@RequestParam(name = "search", required = false) String search) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		school_id = userEntity.getSchool_id();
		this.group_id = (int) session.getAttribute("groupId");
		user_id = userEntity.getUser_id();
		this.user_roll = (String) session.getAttribute("user_roll");

		//リーダの場合は管理者とグループメンバのすべてから検索し格納
		if (user_roll.equals("リーダ")) {
			//チャットの通信可能相手(管理者)を格納
			List<GroupDisplay> chatPartnerAdmin = chatServise.AdminChatPartnerSearch(school_id, search);
			mav.addObject("chatPartnerAdmin", chatPartnerAdmin);
		}

		//チャット相手を検索し、Listに格納
		List<GroupDisplay> chatPartner = chatServise.memberChatPartnerSearch(school_id, group_id, search);

		//ログインしているユーザの分のデータはListから除外
		List<GroupDisplay> filteredChatPartner = chatPartner.stream()
				.filter(chat -> !chat.getUser_id().equals(user_id))
				.collect(Collectors.toList());

		mav.addObject("chatPartnerMember", filteredChatPartner);
		mav.setViewName("common/chat");

		return mav;
	}

	/**
	 * 末吉追加
	 * チャット画面にチャット履歴を表示
	 * @return
	 */
	@LoginRequired
	@PostMapping("getChatHistory")
	public ModelAndView getChatHistory(ModelAndView mav, HttpServletRequest request,
			@RequestParam(name = "chatUserId", required = false) String chatUser_id) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		user_id = userEntity.getUser_id();

		List<ChatForm> chatHistory = chatServise.getChatHistory(user_id, chatUser_id);

		//Cookie情報にチャット相手が入っているかどうか
		boolean hasChatPartnerUserId = chatServise.CookieCheck(request);

		mav.addObject("hasChatPartnerUserId", hasChatPartnerUserId);
		mav.addObject("chatHistory", chatHistory);
		mav.setViewName("common/chat");

		return mav;
	}

	/**
	 * 末吉追加
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

		// エンティティの中の値をそれぞれフィールドに設定_末吉追加
		user_id = userEntity.getUser_id();

		List<ChatForm> chatHistory = chatServise.sendChat(user_id, chatPartnerUserId, sendText);

		mav.addObject("chatHistory", chatHistory);
		mav.setViewName("common/chat");

		return mav;
	}

}
