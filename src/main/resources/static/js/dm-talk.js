$(function(){

	//-------------------------------
	//メッセージテキスト入力時の処理
	//-------------------------------
	$("#msg-text").on("input", function(){

		if ($(this).outerHeight() > this.scrollHeight){
			$(this).height(1);
		}
		while ($(this).outerHeight() < this.scrollHeight){
			$(this).height($(this).height() + 1);
		}
	});
});