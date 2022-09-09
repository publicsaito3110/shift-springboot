$(function() {

	//---------------------
	//カレンダー押下時
	//---------------------
	$(".modal-toggle").on("click", function () {

		//カレンダーの日付をモーダルにセット
		$("#form-input-day").val($(this).children(".calendar-day").text());
	});
});