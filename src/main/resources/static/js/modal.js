/**
 * ポップアップ表示用
 */
$(document).ready(function() {
	if (comp == true) {
		$('#Modal').modal('show');
	}
});


/**
 * チェックボックスが押されていない場合ボタンを押せなくする
 */
$(document).ready(function() {
	$('#flexCheckDefault').on('change', function() {
		if ($(this).is(':checked')) {
			$('button[name="button"]').prop('disabled', false);
			$('#message').hide();
		} else {
			$('button[name="button"]').prop('disabled', true);
			$('#message').show();
		}
	});
	$('button[name="button"]').prop('disabled', true);
	$('#message').show();
});