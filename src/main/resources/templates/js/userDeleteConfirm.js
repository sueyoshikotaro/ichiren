/**
 * Created by 向江 on 2024/12/17.
 * userDeleteConfirm.js
 */

// ユーザ削除確認画面に完了のポップアップを表示する
document.querySelector('.popup-link').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'block';
});

//// ユーザ削除確認画面のポップアップを閉じる
//document.querySelector('.close').addEventListener('click', function() {
//	document.querySelector('.popup').style.display = 'none';
//});

// ユーザ削除確認画面に完了のポップアップを閉じる
document.querySelector('.popup-content button').addEventListener('click', function() {
	document.querySelector('.popup').style.display = 'none';
});

// ポップアップの文字の改行部分を調整する
document.querySelectorAll('.popup-content p, .popup-content li').forEach(function(element) {
	element.style.wordWrap = 'break-word';
});