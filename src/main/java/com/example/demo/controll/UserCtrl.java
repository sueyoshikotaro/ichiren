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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.entity.Task;
import com.example.demo.entity.Tdlist;
import com.example.demo.entity.User;
import com.example.demo.form.GroupDisplay;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TaskReqForm;
import com.example.demo.form.TaskView;
import com.example.demo.repository.GroupCrudRepository;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.GroupServiceInterface;
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

	/**
	 * ログイン画面を表示 
	 * @return
	 */
	@GetMapping("login")
	public String login() {

		return "common/login";
	}

	/**
	 * ログアウト画面を表示
	 * @return
	 */
	@GetMapping("logout")
	public String logout() {

		session.invalidate();

		return "common/login";
	}

	/**
	 * 在籍チェック
	 * ID重複チェック
	 * パスワードチェック
	 * パスワード再設定画面に遷移
	 * @return
	 */
	@PostMapping("passReset")
	public ModelAndView userIdCheckForResetPass(ModelAndView mav, String user_id, String user_pass) {

		Optional<User> user;

		user = userCrudRepo.findById(user_id);

		if (user.get().getUser_flg() == 1 && user.isPresent() && user.get().getUser_pass().equals(user_pass)) {

			if (user.get().getUser_pass().equals("taskdon1")) {

				mav.setViewName("common/passReset"); //パスワード再設定
			}
		} else {

			mav.setViewName("common/login");
			mav.addObject("errMsg", "ログインできませんでした");
		}

		return mav;
	}

	/**
	 * パスワード再設定
	 * @return
	 */
	@GetMapping("passReset")
	public String resetPass() {

		//確認ボタンを押下
		//if (button.equals("reset")) {
		//	mav.addObject("user_pass", user_pass);
		//	mav.setViewName("common/passReset");
		//}

		return "common/passReset";
	}

	/**
	 * 在籍チェック
	 * ID重複チェック
	 * パスワードチェック
	 * ログイン後、メニュー(管理者)画面に遷移
	 * @return
	 */
	@PostMapping("/taskdon/admin/menu")
	public ModelAndView userIdCheckForAdmin(ModelAndView mav, String user_id, String user_pass) {

		Optional<User> user;

		user = userCrudRepo.findById(user_id);

		if (user.get().getUser_flg() == 1 && user.isPresent() && user.get().getUser_pass().equals(user_pass)) {

			if (user.get().getUser_id().equals("admin") && user.get().getUser_pass().equals("admin")) {
				//admin無効化のSQL
				mav.setViewName("admin/menuAdmin"); //管理者ログイン(上位管理者,初回のみ)
				session.setAttribute("user", user.get());
			} else if (user.get().getUser_id().contains("te") || user.get().getUser_id().contains("ad")) {
				mav.setViewName("admin/menuAdmin"); //管理者ログイン(通常)
				session.setAttribute("user", user.get());
			}
		} else {
			mav.setViewName("common/login");
			mav.addObject("errMsg", "ログインできませんでした");
		}
		return mav;
	}

	/**
	 * 在籍チェック
	 * ID重複チェック
	 * パスワードチェック
	 * ログイン後、所属グループ一覧画面に遷移
	 * @return
	 */
	@PostMapping("deptGroupList")
	public ModelAndView userIdCheckForDeptGroupList(ModelAndView mav, String user_id, String user_pass) {

		Optional<User> user;

		if (user_id != null) {

			List<GroupDisplay> deptGroupList = groupService.deptGroupList(user_id);

			user = userCrudRepo.findById(user_id);

			if (user.get().getUser_flg() == 1 && user.isPresent() && user.get().getUser_pass().equals(user_pass)) {

				if (!(user.get().getUser_id().equals("admin") && user.get().getUser_pass().equals("admin"))) {

					System.out.println(user);
					System.out.println(user_id);
					for (GroupDisplay group : deptGroupList) {
						System.out.println(group);
					}

					mav.setViewName("common/deptGroupList");
					mav.addObject("groupS", deptGroupList);
					session.setAttribute("deptGroupList", deptGroupList);
					session.setAttribute("user", user.get());
				} else {
					mav.setViewName("common/login");
					mav.addObject("errMsg", "ログインできませんでした");
				}
			} else {
				mav.setViewName("common/login");
				mav.addObject("errMsg", "ログインできませんでした");
			}
		} else {
			mav.setViewName("common/deptGroupList");
		}
		return mav;
	}

	/**
	 * 所属グループ一覧の表示
	 * @return
	 */
	@GetMapping("reDeptGroup")
	public ModelAndView reDeptGroup(ModelAndView mav, String user_id) {

		Optional<User> user;

		if (user_id != null) {

			List<GroupDisplay> deptGroupList = groupService.deptGroupList(user_id);

			user = userCrudRepo.findById(user_id);

			mav.setViewName("common/deptGroupList");
			mav.addObject("groupS", deptGroupList);
			session.setAttribute("deptGroupList", deptGroupList);
			session.setAttribute("user", user.get());
		} else {
			mav.setViewName("common/deptGroupList");
		}

		return mav;
	}

	/**
	 * メニュー(ユーザ)
	 * @return
	 */
	@LoginRequired
	@PostMapping("menu")
	public String menu(@RequestParam(name = "group_id", required = false) Integer group_id,
			@RequestParam(name = "user_roll", required = false) String user_roll) {

		if (group_id != null && user_roll != null) {
			//セッションに値を設定
			session.setAttribute("groupUser", TaskService.taskUserSearch(group_id)); //ユーザ名,担当者検索用
			session.setAttribute("groupId", group_id); //グループID,
			session.setAttribute("user_roll", user_roll); //役職,ユーザ種別分類用
		}
		return "common/menuUser";
	}

	/**
	 * メニュー(ユーザ)の表示
	 * @return
	 */
	@LoginRequired
	@GetMapping("menu")
	public String reMenu() {

		return "common/menuUser";
	}

	/**
	 * メンバ一覧画面を表示
	 * @return
	 */

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
			score = score - Integer.valueOf(t.getTask_weight());
			TaskService.userUpScore(score, t.getUser_name(), groupId);
			t.setTask_weight(String.valueOf(score));
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

		String weight = String.valueOf(Integer.valueOf(t.getTask_priority()) * Integer.valueOf(t.getTask_level()));
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
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		mav.addObject("taskAppComp", true);
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
	//	@LoginRequired
	@GetMapping("todoList")
	public ModelAndView todoList(ModelAndView mav) {
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
	//	@LoginRequired
	@PostMapping("todoListChange")
	public ModelAndView todoList(ModelAndView mav,
			@RequestParam(name = "flexRadioDefault", required = false) Integer tdlist_id,
			@RequestParam("button") String button) {
		List<Tdlist> todo = null;
		User user = (User) session.getAttribute("user");
		String user_id = user.getUser_id();
		if (tdlist_id != null) {
			todo = TodoService.selectTodo(tdlist_id);
		}

		mav.addObject("button", button);
		mav.addObject("id", tdlist_id);
		mav.addObject("todo", todo);
		System.out.println("aaaaaaaaaaaaaaaa"+todo);
		mav.addObject("todoList", TodoService.selectTodoList(user_id));
		mav.setViewName("common/todoList");
		return mav;
	}

	/**
	 * 連絡事項作成画面を表示
	 * @return
	 */

	/**
	 * チャット画面を表示
	 * @return
	 */

}
