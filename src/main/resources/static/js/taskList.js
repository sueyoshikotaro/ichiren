/**
 * 担当者選択後
 */
function taskList(selectedValue) {
	
	var url = "taskList";
	var params = {
		"selectedValue": selectedValue
	};
	
	console.log(selectedValue);
	
	$.ajax({
		type: "GET",
		url: url,
		data: params,
		success: function(data) {
			console.log(data);
		}
	});
}