package com.example.demo.controll;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.example.demo.entity.Task;
import com.example.demo.entity.Tdlist;
import com.example.demo.entity.User;
import com.example.demo.form.GroupDisplay;
import com.example.demo.form.NoticeViewForm;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TaskReqForm;
import com.example.demo.form.TaskView;
import com.example.demo.form.TdlistForm;
import com.example.demo.repository.GroupCrudRepository;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.GroupServiceInterface;
import com.example.demo.service.NoticeServiceInterface;
import com.example.demo.service.TaskServiceInterface;
import com.example.demo.service.TodoServiceInterface;
import com.example.demo.service.UserServiceInterface;

@Controller
@RequestMapping("/taskdon/user")
public class UserCtrl {

	//フィールド
	@Autowired
	UserCrudRepository userCrudRepo;

	@Autowired
	GroupCrudRepository groupCrudRepo;

	@Autowired
	@Qualifier("userService")
	UserServiceInterface userService;

	@Autowired
	@Qualifier("groupService")
	GroupServiceInterface groupService;

	//湊原追加
	@Autowired
	@Qualifier("taskService")
	TaskServiceInterface TaskService;

	//湊原追加
	@Autowired
	@Qualifier("todoService")
	TodoServiceInterface TodoService;

	//セッション
	@Autowired
	HttpSession session;

	//向江追加
	@Autowired
	@Qualifier("NoticeService")
	NoticeServiceInterface NoticeService;

	/**
	 * ログアウト画面を表示
	 * @return
	 */
	@LoginRequired
	@GetMapping("logout")
	public String logout() {

		session.invalidate();

		return "common/login";
	}

	/**
	 * ログイン画面を表示 
	 * @return
	 */
	@LoginRequired
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

		Optional<User> user = userCrudRepo.findById(user_id);

		if (user.isPresent() && user.get().getUser_flg() == 1 && user.get().getUser_pass().equals(user_pass)) {

			session.setAttribute("user_id", user_id);
			session.setAttribute("user", user.get());

			ra.addFlashAttribute("user_id", user_id);

			if (user.get().getUser_id().contains("admin") && user.get().getUser_pass().equals("admin")) {

				return new ModelAndView("redirect:/taskdon/admin/menu");
			} else if (user.get().getUser_id().contains("te") || user.get().getUser_id().contains("ad")) {

				return new ModelAndView("redirect:/taskdon/admin/menu");
			} else if (user.get().getUser_pass().equals("taskdon1")) {

				return new ModelAndView("redirect:/taskdon/user/passReset");
			} else if (user.get().getUser_id().contains("st")) {

				return new ModelAndView("redirect:/taskdon/user/deptGroupList");
			} else {

				mav.setViewName("common/login");
			}
		} else {
			mav.addObject("loginError", "IDまたはパスワードが違います。");
			mav.setViewName("common/login");
		}

		return mav;
	}

	/**
	 * パスワード再設定画面
	 * @return
	 */
	@GetMapping("passReset")
	public ModelAndView passReset(ModelAndView mav, @ModelAttribute("user_id") String user_id) {

		System.out.println(user_id);

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

		Optional<User> user = userCrudRepo.findById(user_id);

		User u = user.get();

		String passwordPattern = "^[a-zA-Z0-9]{8,15}$"; // 8-15桁の英数字

		System.out.println(user_id);
		System.out.println(newPass);
		System.out.println(confirmPass);

		if (!newPass.matches(passwordPattern)) {

			mav.addObject("passwordError", "パスワードは8桁以上16桁以下の英数字でなければなりません。");
			mav.addObject("user_id", user_id); // user_idを再度渡す
			mav.setViewName("common/passReset");

			return mav;
		} else if (!newPass.equals(confirmPass)) {

			mav.addObject("passwordError", "新パスワードと確認パスワードが一致しません。");
			mav.addObject("user_id", user_id); // user_idを再度渡す
			mav.setViewName("common/passReset");

			return mav;
		} else if (!(newPass.equals("taskdon1") && confirmPass.equals("taskdon1"))) {

			mav.addObject("passwordError", "'taskdon1'は登録できません。");
			mav.addObject("user_id", user_id); // user_idを再度渡す
			mav.setViewName("common/passReset");

			return mav;
		} else {

			userService.userPassReset(u.getUser_id(), newPass);

			System.out.println("パスワード更新完了");

			//mav.setViewName("redirect:/taskdon/user/login"); // ログイン画面へリダイレクト

			return mav;
		}
	}

	/**
	 * メニュー(管理者)画面
	 * @return
	 */
	@LoginRequired
	@GetMapping("/taskdon/admin/menu")
	public ModelAndView adminMenu(ModelAndView mav, @ModelAttribute("user_id") String user_id) {

		Optional<User> user = userCrudRepo.findById(user_id);

		mav.setViewName("admin/menuAdmin");

		return mav;
	}

	/**
	 * 所属グループ一覧画面に遷移
	 * @return
	 */
	@GetMapping("deptGroupList")
	public ModelAndView deptGroupList(ModelAndView mav, @ModelAttribute("user_id") String user_id) {

		if (user_id == null || user_id.isEmpty()) {

			// セッションからも取得を試みる
			user_id = (String) session.getAttribute("user_id");
		}

		Optional<User> user = userCrudRepo.findById(user_id);

		if (user.isPresent()) {

			List<GroupDisplay> deptGroupList = groupService.deptGroupList(user_id);

			mav.setViewName("common/deptGroupList");
			mav.addObject("groupS", deptGroupList);
			session.setAttribute("deptGroupList", deptGroupList);
			session.setAttribute("user", user.get());
		} else {

			// ユーザーが存在しない場合のエラー処理
			mav.setViewName("error");
			mav.addObject("errorMessage", "ユーザーが見つかりません。");
		}

		return mav;
	}

	/**
	 * メニュー(ユーザ)
	 * @return
	 */
	@LoginRequired
	@PostMapping("menu")
	public ModelAndView menu(@RequestParam(name = "group_id", required = false) Integer group_id,
			@RequestParam(name = "user_roll", required = false) String user_roll,
			ModelAndView mav) {

		if (group_id != null && user_roll != null) {
			//セッションに値を設定
			session.setAttribute("groupUser", TaskService.taskUserSearch(group_id)); //ユーザ名,担当者検索用
			session.setAttribute("groupId", group_id); //グループID,
			session.setAttribute("user_roll", user_roll); //役職,ユーザ種別分類用
		}
		List<NoticeViewForm> noticeList = NoticeService.noticeDisp((int) session.getAttribute("groupId"));

		System.out.println(noticeList);

		mav.addObject("noticeList", noticeList);
		mav.setViewName("user/menuUser");
		mav.setViewName("common/menuUser");
		return mav;
	}

	/**
	 * メニュー(ユーザ)の表示
	 * @return
	 */
	@LoginRequired
	@GetMapping("menu")
	public String menu() {

		return "common/menuUser";
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
		int groupId = (int) session.getAttribute("groupId");
		String score = "--";
		List<Task> task = null;
		if (selectedValue == null || selectedValue.equals("全員")) {
			selectedValue = "全員";
		} else {
			score = String.valueOf(TaskService.userScore(selectedValue, groupId));
		}

		task = TaskService.taskDisplayList(selectedValue, groupId);
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
	@PostMapping("taskRegistConfirm")
	public ModelAndView taskRegistConfirm(TaskForm t, ModelAndView mav) {

		mav.addObject("tasks", t);
		mav.setViewName("leader/taskRegistConfirm");
		return mav;
	}

	/**
	 * タスク登録完了画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@PostMapping("taskRegistComplete")
	public ModelAndView taskRegistComplete(ModelAndView mav, TaskForm t) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date st_date = null;
		Date end_date = null;
		int group_id = (int) session.getAttribute("groupId");
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
	public ModelAndView taskDetail(@RequestParam(name = "taskProgress", required = false) String progress,
			@RequestParam(name = "task_id") Integer task_id, ModelAndView mav) {
		mav.getModel().clear();
		if (progress != null) {
			TaskService.taskUpProgress(task_id, Integer.valueOf(progress));
		}
		mav.addObject("task", TaskService.taskDetails(task_id, (int) session.getAttribute("groupId")));
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
		//セッション情報の取得
		int groupId = (int) session.getAttribute("groupId");
		//編集ボタンを押下
		if (button.equals("edit")) {
			//古いスコアの減算
			int score = TaskService.userScore(t.getUser_name(), groupId);
			score = score - t.getTask_weight();
			TaskService.userUpScore(score, t.getUser_name(), groupId);
			t.setTask_weight(score);
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
		//セッション情報の取得
		int groupId = (int) session.getAttribute("groupId");

		//スコアの足しこみ
		int score = TaskService.userScore(t.getUser_name(), groupId);
		score = score + Integer.valueOf(t.getTask_priority()) * Integer.valueOf(t.getTask_level());
		TaskService.userUpScore(score, t.getUser_name(), groupId);

		int weight = t.getTask_priority() * t.getTask_level();
		TaskService.taskUpdate(t.getTask_id(), t.getTask_category(), t.getTask_name(), t.getTask_content(),
				t.getTask_priority(), weight, t.getUser_name());

		mav.addObject("taskEditComp", true);
		mav.addObject("task", t);
		mav.setViewName("leader/taskEditConfirm");

		return mav;
	}

	/**
	 * タスク削除確認画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @param mav
	 * @param t
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskDeleteComplete")
	public ModelAndView taskDeleteConfirm(ModelAndView mav, TaskForm t) {
		//セッション情報の取得
		int groupId = (int) session.getAttribute("groupId");
		//スコアの減算
		int score = TaskService.userScore(t.getUser_name(), groupId);
		score = score - Integer.valueOf(t.getTask_weight());
		TaskService.userUpScore(score, t.getUser_name(), groupId);

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
		mav.addObject("taskNonapp", TaskService.selectTaskUnapproved((int) session.getAttribute("groupId")));
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
	public ModelAndView taskAppComplete(ModelAndView mav, TaskView t) {
		System.out.println(t);
		//日付型定義
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//変数定義
		Date st_date = null;
		Date end_date = null;
		int group_id = (int) session.getAttribute("groupId");
		int score = TaskService.userScore(t.getUser_name(), group_id);
		try {
			//開始予定日と終了予定日の型変換
			st_date = sdf.parse(t.getStart_date());
			end_date = sdf.parse(t.getEnd_date());
			//登録が出来た場合
			if (TaskService.taskRegister(t.getReq_category(), t.getReq_name(), t.getReq_content(), "未着手",
					st_date, end_date, t.getTask_priority(), t.getTask_level(), t.getTask_weight(), t.getUser_name(),
					group_id)) {

				//タスク承認(フラグ更新)
				TaskService.taskReqFlg(t.getRequest_id());

				//スコアの足しこみ
				score = score + Integer.valueOf(t.getTask_weight());
				TaskService.userUpScore(score, t.getUser_name(), group_id);

				mav.addObject("taskAppComp", true);
			} else {
				mav.addObject("taskAppComp", false);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		mav.addObject("taskAppConf", t);
		mav.setViewName("leader/taskApprovedConfirm");
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(date));
		TaskService.registerTaskReq(t.getReq_category(), t.getReq_name(), t.getReq_content(), t.getReq_reason(), date,
				t.getUser_name(), (int) session.getAttribute("groupId"));
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
		System.out.println(tdlist_id);
		if (tdlist_id != null) {
			if (checked != null) {
				if (checked) {
					System.out.println(checked);
					TodoService.todoUpFlg(tdlist_id, 1);
				} else {
					TodoService.todoUpFlg(tdlist_id, 0);
				}
			}
		}
		User user = (User) session.getAttribute("user");
		String user_id = user.getUser_id();
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
		List<Tdlist> todo = null;
		User user = (User) session.getAttribute("user");
		String user_id = user.getUser_id();
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
		User user = (User) session.getAttribute("user");
		String user_id = user.getUser_id();
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

	/**

	 * 向江
	 * 連絡事項一覧画面を表示
	 * 埋め込み前
	 * @return
	 */
	@GetMapping("noticeDisp")
	public ModelAndView noticeDisp(ModelAndView mav) {

		// ログインユーザのエンティティを取得
		User userEntity = (User) session.getAttribute("user");

		// エンティティの中のuser_idを取得
		String user_id = userEntity.getUser_id();

		List<NoticeViewForm> noticeList = NoticeService.noticeDisp((int) session.getAttribute("groupId"));

		System.out.println(noticeList);

		mav.addObject("noticeList", noticeList);
		mav.setViewName("user/menuUser");

		return mav;
	}

	/*
	 * 向江
	 * 連絡事項作成画面を表示するリクエストハンドラメソッド
	 * @return
	 */
	@PostMapping("noticeRegist")
	public ModelAndView noticeRegist(ModelAndView mav) {

		mav.setViewName("leader/noticeRegist");

		return mav;
	}

	/*
	 * 向江
	 * 連絡事項作成確認画面を表示するリクエストハンドラメソッド
	 * @return
	 */
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
	//	@PostMapping("noticeRegistComp")
	//	public ModelAndView noticeRegistComp(ModelAndView mav, NoticeViewForm n) {
	//		
	//		
	//		//作成ボタンを押下
	//		if (button.equals("作成")) {
	//			NoticeService.;
	//
	//			// ポップアップを表示するために、画面遷移をしないようにする
	//			mav.addObject("teInfoRegistComp", true);
	//			mav.setViewName("admin/teInfoRegistConfirm");
	//
	//			return mav;
	//
	//			// 戻るボタンを押下	
	//		} else {
	//
	//			mav.addObject("teInfoRegist", u);
	//			mav.setViewName("admin/teInfoRegist");
	//			return mav;
	//		}
	//
	//		
	//		
	//		mav.addObject("notice", n);
	//		mav.setViewName("leader/");
	//		
	//		return mav;
	//	}
	
	/**
	 * チャット画面を表示
	 * @return
	 */

}
