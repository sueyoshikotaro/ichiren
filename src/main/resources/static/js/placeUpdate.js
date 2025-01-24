//メニュー画面・居場所更新
//休憩中の間リンクを押せなくするscript
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
}

document.addEventListener('DOMContentLoaded', toggleLinkStatus);
const selectElement = document.querySelector('.place-update');
if (selectElement) {
	selectElement.addEventListener('change', toggleLinkStatus);
}