//居場所更新のscript

//'休憩中'の間リンクを無効化
function toggleLinkStatus() {
	const selectElement = document.querySelector('.place-update');
	if (!selectElement) return;

	const selectedOption = selectElement.options[selectElement.selectedIndex].text;
	const links = document.querySelectorAll('.menu-link');

	console.log(selectedOption);

	links.forEach(link => {
		if (selectedOption === '休憩中') {
			link.classList.add('disabled');
			link.style.pointerEvents = 'none';
			link.setAttribute('aria-disabled', 'true');
		} else {
			link.classList.remove('disabled');
			link.style.pointerEvents = 'auto';
			link.removeAttribute('aria-disabled');
		}
	});

	sessionStorage.setItem('selectedPlace', selectedOption); // Session Storageに保存
}

//現在の選択項目を保持
function restoreSelectedPlace() {
	const selectElement = document.querySelector('.place-update');
	if (!selectElement) return;

	const savedPlace = sessionStorage.getItem('selectedPlace'); // Session Storageから取得

	if (savedPlace) {
		for (let i = 0; i < selectElement.options.length; i++) {
			if (selectElement.options[i].text === savedPlace) {
				selectElement.selectedIndex = i;
				break;
			}
		}
		toggleLinkStatus(); //リンクの状態を更新
	}
}

//ページロード時に呼出
document.addEventListener('DOMContentLoaded', () => {
	restoreSelectedPlace();
	toggleLinkStatus(); // 初期状態を設定
});

document.addEventListener('DOMContentLoaded', () => {
	const selectElement = document.querySelector('.place-update');
	if (selectElement) {
		selectElement.addEventListener('change', toggleLinkStatus);
	}
});


//ログアウト時、居場所を'休憩中'に設定
const logoutLink = document.querySelector('a[href="/taskdon/user/logout"]'); //ログアウトリンク

if (logoutLink) {
	logoutLink.addEventListener('click', (event) => {
		event.preventDefault();//デフォルトの遷移をキャンセル
		const selectElement = document.querySelector('.place-update');
		if (selectElement) {
			selectElement.value = "休憩中"; // select要素の値を変更
			toggleLinkStatus(); // リンクの状態を更新
		}
		sessionStorage.removeItem('selectedPlace');
		window.location.href = "/taskdon/user/logout";//遷移処理
	});
}

//所属グループ遷移時、居場所を'休憩中'に設定
const deptGroupLink = document.querySelector('a[href="/taskdon/user/getPlace"]'); //所属グループリンク

if (deptGroupLink) {
	deptGroupLink.addEventListener('click', (event) => {
		event.preventDefault();//デフォルトの遷移をキャンセル
		const selectElement = document.querySelector('.place-update');
		if (selectElement) {
			selectElement.value = "休憩中"; // select要素の値を変更
			toggleLinkStatus(); // リンクの状態を更新
		}
		sessionStorage.removeItem('selectedPlace');
		window.location.href = "/taskdon/user/getPlace";//遷移処理
	});
}