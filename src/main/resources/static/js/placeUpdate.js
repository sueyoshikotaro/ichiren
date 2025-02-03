//居場所更新用

//絶対に呼び出す処理
toggleLinkStatus();

//'休憩中'の間リンクを無効化
function toggleLinkStatus() {
	const selectElement = document.querySelector('.place-update');

	const selectedOption = selectElement.options[selectElement.selectedIndex].text;
	const links = document.querySelectorAll('.menu-link');

	links.forEach(link => {
		if (selectedOption === '休憩中') {
			console.log("seikou!!!!");
			link.classList.add('disabled');
			link.style.pointerEvents = 'none';
			link.setAttribute('aria-disabled', 'true');
		} else {
			console.log("sippai!!!!");
			link.classList.remove('disabled');
			link.style.pointerEvents = 'auto';
			link.removeAttribute('aria-disabled');
		}
	});

	sessionStorage.setItem('selectedPlace', selectedOption); // Session Storageに保存
}

//チェックボックスが押されていない間ボタンを無効化
$(document).ready(function() {
	const deleteButton = $('#deleteButton');

	// 親要素（form要素）にイベントを委譲
	$('#noticeForm').on('change', '.notice-checkbox', function() {
		const checkedCount = $('.notice-checkbox:checked').length;
		deleteButton.prop('disabled', checkedCount === 0);
	});

	// 初期状態を設定（ページロード時）
	const checkedCount = $('.notice-checkbox:checked').length;
	deleteButton.prop('disabled', checkedCount === 0);
});

//居場所更新
function updateStatus(updateStatus) {
	console.log("updateStatus関数が実行されました");

	var updateStatus;

	/*URL指定＆ドロップダウンリストの値引き渡し*/
	var url = "updateStatus";
	var params = {
		"updateStatus": updateStatus
	};

	/*グループ一覧表示メソッドの呼び出し*/
	$.ajax({
		type: "POST",
		url: url,
		data: params
	});

	console.log("updateStatusの値は" + updateStatus + "です");


	// toggleLinkStatus関数を呼び出す
	toggleLinkStatus(updateStatus);

}