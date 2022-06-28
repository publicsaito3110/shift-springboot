$(function() {

	//チャットボタン押下時の処理
	$('.chat-address-btn').on('click', function (){

		//押下した'.chat-address-btn'のみをdisableにする
		$(".chat-address-btn").prop("disabled", false);
		$(this).prop("disabled", true);

		//非同期処理
		$.ajax({
			url: "/dm/talk",
			type: "POST",
			data: {receiveUser : $(this).val()}
		}).done(function (result) {
			// 通信成功時のコールバック
			$("#talk").html(result);
		}).fail(function () {
			// 通信失敗時のコールバック
			alert("読み込みに失敗しました。");
		});
	});
});