$(function(){

	//-----------------------
	//ページ読み込み時の処理
	//-----------------------
	$(document).ready(function(){

		//一番下に自動的にスクロール
		var scrollHeight = $("#chat-box").get(0).scrollHeight;
		$("#chat-box").scrollTop(scrollHeight);
	});


	//-------------------------
	//ページスクロール時の処理
	//-------------------------
	$('#chat-box').scroll(function() {

		//現在のページの高さを取得
		var nowScrollHeight = $(this).scrollTop();

		//スクロールトップのとき
		if(nowScrollHeight <= 0) {

			//次のチャット件目がないとき
			var nextLastOffset = $("#next-last-offset").val();
			if (nextLastOffset <= 0) {
				return;
			}

			//divタグを追加
			$('#chat-box').prepend("<div></div>");
			var receiveUser = $("#receiveUser").val();

			//スクロールの高さを1pxに設定
			$(this).scrollTop(1);

			//非同期通信
			$("#chat-box").children("div:first").load("/dm/talk/road?receiveUser=" + receiveUser + "&nextLastOffset=" + nextLastOffset + " .chat-list")

			//最終チャット件目を更新
			var chatLimit = $("#chat-limit").val();
			$("#next-last-offset").val(nextLastOffset - chatLimit);
		}
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