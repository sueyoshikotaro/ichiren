package com.example.demo.controll;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import jakarta.servlet.http.HttpSession;

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
	public String resetPass() {

		return "common/resetPass";
	}

	/**
	 * 在籍チェック
	 * ID重複チェック
	 * パスワードチェック
	 * 所属グループ一覧画面を表示
	 * @return
	 */
	@PostMapping("deptGroupList")
	public ModelAndView userIdCheck(ModelAndView mav, String user_id, String user_pass) {

		Optional<User> user;

		user = userCrudRepo.findById(user_id);

		if (user.get().getUser_flg() == 1 && user.isPresent() && user.get().getUser_pass().equals(user_pass)) {

			if (user.get().getUser_id().equals("admin") && user.get().getUser_pass().equals("admin")) {

				//admin無効化のSQL挿入欄
				
				mav.setViewName("admin/menuAdmin"); //管理者ログイン(上位管理者,初回のみ)
			} else if (user.get().getUser_id().contains("te")) {

				mav.setViewName("admin/menuAdmin"); //管理者ログイン(通常)
			} else if (user.get().getUser_pass().equals("taskdon1")) {

				mav.setViewName("common/passReset"); //パスワード再設定
			} else {

				List<GroupDisplay> deptGroupList = groupService.deptGroupList(user_id);

				mav.setViewName("common/deptGroupList");
				mav.addObject("groupS", deptGroupList);
				session.setAttribute("user", user.get());
			}
		} else {

			mav.setViewName("common/login");
			mav.addObject("errMsg", "ログインできませんでした");
		}
		return mav;
	}

	//for (GroupDisplay i : deptGroupList) {
	//	System.out.println(i);
	//}

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
		List<Task> task = null;
		String user = "all";
		if (selectedValue == null || selectedValue.equals("全員")) {
			task = TaskService.taskDisplayList(user, groupId);
		} else {
			mav.getModel().clear();
			user = selectedValue;
			task = TaskService.taskDisplayList(user, groupId);
		}

		mav.addObject("tasks", task);
		mav.setViewName("leader/taskList");
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
		try {
			st_date = sdf.parse(t.getStart_date());
			end_date = sdf.parse(t.getEnd_date());
			TaskService.taskRegister(t.getTask_category(), t.getTask_name(), t.getTask_content(), "未着手",
					st_date, end_date, t.getTask_priority(), t.getTask_level(), t.getTask_weight(), t.getUser_name(),
					group_id);
			System.out.println(t.getGroup_id());
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
		mav.setViewName("leader/taskDetails");
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
		mav.addObject("task", t);
		//編集ボタンを押下
		if (button.equals("edit")) {
			mav.setViewName("leader/taskEdit");
			//削除ボタンを押下
		} else if (button.equals("delete")) {
			mav.setViewName("leader/taskDeleteConfirm");
		}
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
	 * (未)スコアの更新
	 * @param mav
	 * @return
	 */
	@LoginRequired
	@PostMapping("taskEditComplete")
	public ModelAndView taskEditComplete(ModelAndView mav, TaskForm t) {

		TaskService.taskUpdate(t.getTask_id(), t.getTask_category(), t.getTask_name(), t.getTask_content(),
				t.getTask_priority(), t.getTask_weight(), t.getUser_name());
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
	@PostMapping("taskDeleteConfirm")
	public ModelAndView taskDeleteConfirm(ModelAndView mav, TaskForm t) {

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
		mav.setViewName("leader/taskUnapprovedConfirm");
		return mav;
	}
	
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
