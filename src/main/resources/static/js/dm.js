$(function(){

	//------------------------
	// 検索ボタン押下時の処理
	//------------------------
	$("#user-address-seach-btn").on("click", function (){

		//非同期通信
		$.ajax({
		url: "/dm/address",
		type: "POST",
		data: {keyword : $("#user-address-seach-keyword").val()}
		}).done(function (result) {
			// 通信成功時のコールバック
			$("#user-address-box").html(result);
		}).fail(function () {
			// 通信失敗時のコールバック
			alert("読み込みに失敗しました。");
		});
	});
});