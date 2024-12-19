// パスワード再設定説明のポップアップを表示する
document.querySelector('.popup-link').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'block';
});

// パスワード再設定説明のポップアップを閉じる
document.querySelector('.close').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'none';
});