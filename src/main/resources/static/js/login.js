//ログイン画面用
//パスワード再設定画面にも使用

// パスワード再設定説明のポップアップを開く[動作]
document.querySelector('.popup-link').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'block';
});

// パスワード再設定説明のポップアップを閉じる(×ボタン)[動作]
document.querySelector('.close').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'none';
});

//目マーク(パスワード入力欄)[動作]
const passwordToggle = document.querySelector('.input-group-text');
const passwordInput = document.querySelector('#user_pass');

//目印の押下時に,表示と非表示を2種類のアイコンで切替え(※デザインはCSS)
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
//(注釈)-現状,ウェブのデフォルト機能で,初入力時は目印が表示してある。
//,2回目以降の入力では,目印がでなくなってしまう問題点の為,直接機能として実装。