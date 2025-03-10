/**
 * 末吉
 * チャット相手一覧の表示
 */
$(document).ready(function() {
	$(document).on('click', '#showChatPartnerButton', function() {
		// コントロールを呼び出す
		$.ajax({
			type: 'GET',
			url: 'chat',
			success: function(data) {

				var chatPartnerList = data;

				// チャット相手一覧を表示する
				var chatPartnerListElement = $('#chatPartnerList');

				// チャット相手一覧を表示するために、offcanvasExampleのdisplayスタイルをblockに設定
				var offcanvasExample = document.getElementById("offcanvasExample");
				if (offcanvasExample) {
					offcanvasExample.style.display = "block";
				}

				// チャット相手一覧をクリアする
				chatPartnerListElement.empty();

				// チャット相手一覧を取得する
				var chatPartner = $(data).find('#chatPartnerList li');

				if (chatPartner.length > 0) {
					var listItems = [];
					chatPartner.each(function() {
						var listItem = $('<li>').addClass('list-group-item list-group-item-action');
						listItem.text($(this).text());

						listItem.attr('data-user-id', $(this).attr('data-user-id')); // ここで属性を設定しています

						listItem.click(function() {
							var userId = $(this).attr('data-user-id');
							getChatHistory(userId);
						});

						listItems.push(listItem);
					});
					chatPartnerListElement.append(listItems);
				}
			}
		});
	});
});


/**
 * 末吉
 * チャット相手を検索する
 */
function chatSearch() {
	const form = document.getElementById("searchForm");
	const value = form.querySelector("input[name='search']").value;

	/*URL指定＆ 入力値の引き渡し*/
	var url = "chatSearch";
	var params = {
		"search": value
	};

	/*チャット相手一覧の再表示メソッドの呼び出し*/
	$.ajax({
		type: "POST",
		url: url,
		data: params,
		success: function(data) {
			// チャット相手一覧のリストを更新
			if (data) { // データが存在する場合のみ処理を実行

				var chatPartnerList = data;

				// チャット相手一覧を表示する
				var chatPartnerListElement = $('#chatPartnerList');

				// チャット相手一覧をクリアする
				chatPartnerListElement.empty();

				// チャット相手一覧を取得する
				var chatPartner = $(data).find('#chatPartnerList li');

				if (chatPartner.length > 0) {
					var listItems = [];
					chatPartner.each(function() {
						var listItem = $('<li>').addClass('list-group-item list-group-item-action');
						listItem.text($(this).text());

						listItem.attr('data-user-id', $(this).attr('data-user-id')); // ここで属性を設定しています

						listItem.click(function() {
							var userId = $(this).attr('data-user-id');
							getChatHistory(userId);
						});

						listItems.push(listItem);
					});
					chatPartnerListElement.append(listItems);
				}
			}
		}
	});
}


/**
 * 末吉
 * チャット履歴表示
 */
function getChatHistory(chatUserId) {
	// ローカルストレージにチャット相手の情報を保存する
	localStorage.setItem('chatPartnerUserId-' + userId, chatUserId);
	$.ajax({
		type: 'POST',
		url: 'getChatHistory',
		data: { chatUserId: chatUserId },
		success: function(data) {
			document.body.innerHTML = data; // HTML全体を再描画

			// チャット相手一覧を非表示にする
			var offcanvasExample = document.getElementById("offcanvasExample");
			if (offcanvasExample) {
				offcanvasExample.style.display = "none";
			}

			// バックドロップを非表示にする
			var offcanvasBackdrop = document.querySelector(".offcanvas-backdrop");
			if (offcanvasBackdrop) {
				offcanvasBackdrop.style.display = "none";
			}
		}
	});
}


/**
 * 末吉
 * チャットを送信
 */
function sendMessage() {
	var sendInput = document.getElementById("send_input").value;

	//Cookie情報を取得
	var chatPartnerUserId = localStorage.getItem('chatPartnerUserId-' + userId);

	if (chatPartnerUserId === null || chatPartnerUserId === '') {
		// コントロールを呼び出さない
		return;
	}

	// コントロールを呼び出す
	$.ajax({
		type: 'POST',
		url: 'sendChat',
		data: { sendInput: sendInput, chatPartnerUserId: chatPartnerUserId },
		success: function(data) {
			document.body.innerHTML = data; // HTML全体を再描画
		}
	});
}


/**
 * 画面を更新したときにチャット履歴を残しておく
 * (画面の更新ボタン・F15・Ctrl + R　など)
 */
window.addEventListener('load', function() {
	//Cookie情報を取得
	var chatPartnerUserId = localStorage.getItem('chatPartnerUserId-' + userId);
	//Cookieにデータが入っているときのみ処理を実行
	if (chatPartnerUserId !== null) {
		getChatHistory(chatPartnerUserId);
	}
});

var chatPartnerUserId = localStorage.getItem('chatPartnerUserId-' + userId);
if (chatPartnerUserId === null || chatPartnerUserId === '') {
	document.getElementById('chat-partner-status').innerHTML = 'チャット相手が選択されていません';
}