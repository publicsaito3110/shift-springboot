$(function() {

	//---------------------------
	//チャットボタン押下時の処理
	//---------------------------
	$(".chat-address-btn").on("click", function (){

		//押下した'.chat-address-btn'のみをdisableにする
		$(".chat-address-btn").prop("disabled", false);
		$(this).prop("disabled", true);

		//押下したボタンのユーザー名を取得し、セット
		$("#chat-user-name").text($(this).children(".chat-window").children(".user-name").text());

		//押下したボタンのユーザーidを取得し、セット
		$("#chat-send").val($(this).val());

		//ユーザー名を表示
		$("#chat-user-name").css("display", "block");

		//チャットフォームを表示
		$("#chat-send-form").css("display", "block");

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