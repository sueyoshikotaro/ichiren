package com.example.demo.controll;

/*
 * 
 * 
 */

//@Controller
//@RequestMapping("/taskdon/admin")
//public class UserListCtrl {
//
//	//@Autowired
//	//UserCrudRepository userCrudRepository;
//
//	@Autowired
//	@Qualifier("userListService")
//	UserListServiceInterface userListService;
//
//	/*
//	 * ユーザ一覧のリクエストハンドラメソッド
//	 * @return ユーザ一覧
//	 */
//
//	@GetMapping("userList")
//	public ModelAndView userList() {
//		//ModelAndViewのインスタンス生成
//		ModelAndView mav = new ModelAndView();
//
//		//サービスのインスタンス生成
//		//UserListServiceInterface userListService = new UserListServiceImpl();
//
//		//サービスのメソッドを呼び出す
//		Iterable<User> userList = userListService.userList();
//
//		mav.addObject("users", userList);
//		mav.setViewName("admin/UserList");
//		
//		//System.out.println(userList.toString());
//
//		return mav;
//
//		//return "admin/UserList";
//
//	}
//
//}
