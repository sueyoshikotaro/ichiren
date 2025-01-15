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
import com.example.demo.entity.User;
import com.example.demo.form.GroupDisplay;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TaskReqForm;
import com.example.demo.form.TaskView;
import com.example.demo.repository.GroupCrudRepository;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.GroupServiceInterface;
import com.example.demo.service.TaskServiceInterface;
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
	 * パスワード再設定画面を表示
	 * @return
	 */
	@GetMapping("resetPass")
	public ModelAndView resetPass(@RequestParam("newPass") String user_pass, String button, ModelAndView mav) {

		//確認ボタンを押下
		if (button.equals("reset")) {
			mav.addObject("user_pass", user_pass);
			mav.setViewName("common/resetPass");
		}

		return mav;
	}

	/**
	 * 在籍チェック
	 * ID重複チェック
	 * パスワードチェック
	 * メニュー画面(管理者)を表示
	 * @return
	 */
	@PostMapping("menu")
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
	 * 所属グループ一覧画面を表示
	 * @return
	 */
	@PostMapping("deptGroupList")
	public ModelAndView userIdCheckForDeptGroupList(ModelAndView mav, String user_id, String user_pass) {

		Optional<User> user;

		List<GroupDisplay> deptGroupList = groupService.deptGroupList(user_id);

		user = userCrudRepo.findById(user_id);

		if (user.get().getUser_flg() == 1 && user.isPresent() && user.get().getUser_pass().equals(user_pass)) {

			if (!(user.get().getUser_id().equals("admin") && user.get().getUser_pass().equals("admin"))) {

				mav.setViewName("common/deptGroupList");
				mav.addObject("groupS", deptGroupList);
				session.setAttribute("user", user.get());
			} else {

				mav.setViewName("common/login");
				mav.addObject("errMsg", "ログインできませんでした");
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
	 * パスワード再設定画面を表示
	 * @return
	 */
	@PostMapping("resetPass")
	public ModelAndView userIdCheckForResetPass(ModelAndView mav, String user_id, String user_pass) {

		Optional<User> user;

		user = userCrudRepo.findById(user_id);

		if (user.get().getUser_flg() == 1 && user.isPresent() && user.get().getUser_pass().equals(user_pass)) {

			if (user.get().getUser_pass().equals("taskdon1")) {

				mav.setViewName("common/passReset"); //パスワード再設定
				mav.addObject("");
			}
		} else {

			mav.setViewName("common/login");
			mav.addObject("errMsg", "ログインできませんでした");
		}

		return mav;
	}

	/**
	 * メニュー画面を表示
	 * @return
	 */
	@LoginRequired
	@GetMapping("menu")
	public String menu(int group_id, String user_roll) {

		//セッションに値を設定
		session.setAttribute("groupUser", TaskService.taskUserSearch(group_id)); //ユーザ名,担当者検索用
		session.setAttribute("groupId", group_id); //グループID,
		session.setAttribute("user_roll", user_roll); //役職,ユーザ種別分類用

		//System.out.println(group_id);
		//System.out.println(user_roll);

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
			System.out.println(t);
			//スコアの足しこみ
			score = score + Integer.valueOf(t.getTask_weight());
			TaskService.userUpScore(score, t.getUser_name(), group_id);

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
	@PostMapping("taskDetails")
	public ModelAndView taskDetail(ModelAndView mav, TaskForm t) {

		mav.addObject("task", t);
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
		System.out.println(t.getTask_weight());
		//スコアの足しこみ
		int score = TaskService.userScore(t.getUser_name(), groupId);
		System.out.println(score);
		score = score - Integer.valueOf(t.getTask_weight());
		TaskService.userUpScore(score, t.getUser_name(), groupId);

		TaskService.taskUpFlg(t.getTask_id());
		mav.addObject("task", t);
		mav.setViewName("leader/taskDeleteConfirm");

		return mav;
	}

	/**
	 * タスク未承認画面を表示する
	 * 湊原
	 */
	@LoginRequired
	@GetMapping("taskUnapproved")
	public ModelAndView taskUnapproved(ModelAndView mav) {
		mav.addObject("taskNonapp", TaskService.selectTaskUnapproved());
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
		System.out.println(t);
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
		mav.addObject("taskAppConf", t);
		mav.setViewName("leader/taskUnapprovedConfirm");
		return mav;
	}

	/**
	 * 連絡事項作成画面を表示
	 * @return
	 */

	/**
	 * ToDoリスト画面を表示
	 * @return
	 */

	/**
	 * チャット画面を表示
	 * @return
	 */

}
