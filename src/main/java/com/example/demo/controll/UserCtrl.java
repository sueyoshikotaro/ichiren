package com.example.demo.controll;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.TaskServiceInterface;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/taskdon/user")
public class UserCtrl {

	//フィールド
	@Autowired
	UserCrudRepository userCrudRepo;

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
		//		if (user_id.equals("admin") && user_pass.equals("admin")) {
		//
		//			//上位管理者の初回ログイン時の処理
		//			return "admin/adminRegister";
		//		} else {
		//
		//			if (firstLogin) {
		//
		//				// 初回ログイン時の処理
		//				return "redirect:/common/resetPass";
		//			} else {
		//
		//				// 通常のログイン処理
		//				return "common/login";
		//			}
		//		}
		//	}

	/**
	 * ID重複をチェック
	 */
	@PostMapping("deptGroupList")
	public ModelAndView userIdCheck(ModelAndView mav, String user_id) {

		boolean flg;
		Optional<User> user;

		//flg = userCrudRepo.existsById(user_id);

		user = userCrudRepo.findById(user_id);

		if (user.get().getUser_id().equals(user_id)) {

			mav.setViewName("common/deptGroupList");
			session.setAttribute("user_id", user_id);
		} else {

			mav.setViewName("common/login");
			mav.addObject("errMsg", "IDが一致しません。");
		}

		return mav;
	}

	/**
	 * 所属グループ一覧画面を表示
	 * @return
	 */
	@GetMapping("deptGroupList")
	public String deptGroupList() {

		return "common/deptGroupList";
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
	 * 管理者登録画面を表示
	 * @return
	 */
	@GetMapping("adminRegister")
	public String adminRegister() {

		return "admin/adminRegister";
	}

	/**
	 * タスク一覧画面を表示するリクエストハンドラメソッド
	 * 湊原
	 * @return
	 */
	@GetMapping("taskList")
	public ModelAndView taskList(ModelAndView mav) {
		List<Task> task = TaskService.taskDisplayList();

		mav.addObject("tasks", task);
		mav.setViewName("leader/taskList");
		return mav;
	}
}
