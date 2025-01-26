//メニュー画面・居場所更新
//休憩中の間リンクを押せなくするscript
function toggleLinkStatus() {
	// ... (既存のコード)
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

	// 選択された値を保存
	sessionStorage.setItem('selectedPlace', selectedOption); // Session Storageに保存
}

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
		toggleLinkStatus(); // リンクの状態を更新
	}
}

document.addEventListener('DOMContentLoaded', () => {
	restoreSelectedPlace(); // ページロード時に復元
	toggleLinkStatus(); // 初期状態を設定
});

const selectElement = document.querySelector('.place-update');
if (selectElement) {
	selectElement.addEventListener('change', toggleLinkStatus);
}