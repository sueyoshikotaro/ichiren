package com.example.demo.controll;

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

import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.form.TaskForm;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.TaskServiceInterface;
import com.example.demo.service.UserServiceInterface;

@Controller
@RequestMapping("/taskdon/user")
public class UserCtrl {

	//フィールド
	@Autowired
	UserCrudRepository userCrudRepo;

	@Autowired
	@Qualifier("userService")
	UserServiceInterface userService;

	//	@Autowired
	//	@Qualifier("groupService")
	//	GroupServiceInterface groupService;

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

	//	/**
	//	 * ログイン画面を表示 
	//	 * @return
	//	 */
	//	@GetMapping("login")
	//	public String login(@RequestParam("firstLogin") boolean firstLogin, String user_id, String user_pass) {
	//
	//		if (firstLogin) {
	//
	//			// 初回ログイン時の処理
	//			return "redirect:/common/resetPass";
	//		} else {
	//
	//			// 通常・上位管理者ログイン時の処理
	//			return "common/login";
	//		}
	//	}

	/**
	 * ID重複をチェック
	 */
	@PostMapping("deptGroupList")
	public ModelAndView userIdCheck(ModelAndView mav, String user_id) {

		Optional<User> user;

		user = userCrudRepo.findById(user_id);

		if (user.get().getUser_id().equals(user_id)) {

			mav.setViewName("common/deptGroupList");
			session.setAttribute("user", user);
		} else {

			mav.setViewName("common/login");
			mav.addObject("errMsg", "IDが一致しません。");
		}

		return mav;
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
	 * 所属グループ一覧画面を表示
	 * @return
	 */
	//	@GetMapping("deptGroupList")
	//	public ModelAndView deptGroupList() {
	//
	//		ModelAndView mav = new ModelAndView();
	//
	//		Iterable<Teams> deptGroupList = groupService.deptGroupList();
	//
	//		mav.addObject("groups", deptGroupList);
	//		mav.setViewName("common/deptGroupList");
	//
	//		return mav;
	//	}

	/**
	 * メニュー画面を表示
	 * @return
	 */
	@GetMapping("menu_choose")
	public String menu() {

		return "leader/menu_choose";
	}

	/**
	 * タスク一覧画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@GetMapping("taskList")
	public ModelAndView taskList(ModelAndView mav,
			@RequestParam(name = "selectedValue", required = false) String selectedValue) {
		//削除予定
//		session.setAttribute("groupUser", TaskService.taskUserSearch());

		//		System.out.println(user_name);
		List<Task> task = null;
		//		task = TaskService.taskDisplayList(user_name);
		String user = "all";
		if (selectedValue == null || selectedValue.equals("全員")) {
			task = TaskService.taskDisplayList(user);
		} else {
			mav.getModel().clear();
			user = selectedValue;
			task = TaskService.taskDisplayList(user);
		}
		mav.addObject("tasks", task);
		System.out.println(task);
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

		TaskService.taskRegister(t.getTask_category(), t.getTask_name(), t.getTask_content(), "未着手", t.getStart_date(),
				t.getEnd_date(), t.getTask_priority(), t.getTask_level(), t.getTask_weight(), t.getUser_name(),
				t.getGroup_id());

		mav.setViewName("leader/taskRegistComplete");
		return mav;
	}

	/**
	 * タスク詳細画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@PostMapping("taskDetail")
	public ModelAndView taskDetail(ModelAndView mav, TaskForm t) {
		//		System.out.println(t);
		mav.addObject("task", t);
		mav.setViewName("leader/taskDetails");
		return mav;
	}
}
