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
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.TaskServiceInterface;
 
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
	public String login(String user_id, String user_pass) {
 
		return "leader/login";
	}
 
	/**
	 * ID重複をチェック
	 */
	@PostMapping("deptGroupList")
	public ModelAndView userIdCheck(ModelAndView mav, String id) {
 
		boolean flg;
		Optional<User> user;
 
		//flg = userCrudRepo.existsById(user_id);
 
		user = userCrudRepo.findById(id);
 
		if (user.get().getUser_id().equals(id)) {
 
			mav.setViewName("leader/deptGroupList");
			session.setAttribute("user_id", id);
		} else {
 
			mav.setViewName("leader/login");
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
 
		return "learder/deptGroupList";
	}
 
	/**
	 * パスワード再設定画面を表示
	 * @return
	 */
	@GetMapping("resetPass")
	public String resetPass() {
 
		return "leader/resetPass";
	}
 
	/**
	 * 新規管理者登録画面を表示
	 * @return
	 */
	@GetMapping("newAdminRegister")
	public String newAdminRegister() {
 
		return "admin/newAdminRegister";
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
