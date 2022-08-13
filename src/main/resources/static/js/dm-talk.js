$(function(){

	//-----------------------
	//ページ読み込み時の処理
	//-----------------------
	$(document).ready(function(){

		//一番下に自動的にスクロール
		var scrollHeight = $("#chat-box").get(0).scrollHeight;
		$("#chat-box").scrollTop(scrollHeight);
	});

	//-------------------------------
	//メッセージテキスト入力時の処理
	//-------------------------------
	$("#msg-text").on("input", function(){

		//textareaの高さを自動調整
		if ($(this).outerHeight() > this.scrollHeight){
			$(this).height(1);
		}
		while ($(this).outerHeight() < this.scrollHeight){
			$(this).height($(this).height() + 1);
		}


		//送信ボタンの表示
		if (!$(this).val().trim()) {

			//メッセージが入力されていないとき送信ボタンを非表示
			$("#send-btn").prop("disabled", true);
		}
		if ($(this).val().trim()) {

			//メッセージが入力されているとき送信ボタンを表示
			$("#send-btn").prop("disabled", false);
		}
	});
});