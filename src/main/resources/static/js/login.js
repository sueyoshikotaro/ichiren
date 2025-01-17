//下記ログイン画面(動作)
// パスワード再設定説明のポップアップを表示する
document.querySelector('.popup-link').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'block';
});

// パスワード再設定説明のポップアップを閉じる
document.querySelector('.close').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'none';
});

//目マーク動作(パスワード入力欄)
//パスワード入力後に、目マークが消えて再び再入力する際に、見え隠しできない為の処理
const passwordToggle = document.querySelector('.input-group-text');
const passwordInput = document.querySelector('#user_pass');

passwordToggle.addEventListener('click', () => {
	if (passwordInput.type === 'password') {
		passwordInput.type = 'text';
		document.querySelector('#eye').style.display = 'none';
		document.querySelector('#eye-slash').style.display = 'block';
	} else {
		passwordInput.type = 'password';
		document.querySelector('#eye').style.display = 'block';
		document.querySelector('#eye-slash').style.display = 'none';
	}
});