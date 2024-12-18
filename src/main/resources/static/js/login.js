// login.js

// パスワード再設定説明のポップアップを表示する
document.querySelector('.popup-link').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'block';
});

// パスワード再設定説明のポップアップを閉じる
document.querySelector('.close').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'none';
});

// 入力欄の横幅を調整する
document.querySelectorAll('.form-group input[type="text"], .form-group input[type="password"]').forEach(function(input) {
	input.style.width = '100%';
	input.style.boxSizing = 'border-box';
	input.style.padding = '10px';
});

// ポップアップの文字の改行部分を調整する
document.querySelectorAll('.popup-content p, .popup-content li').forEach(function(element) {
	element.style.wordWrap = 'break-word';
});