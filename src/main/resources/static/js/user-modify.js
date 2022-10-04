$(function(){

	//-----------------------------
	//ファイルアップロード時の処理
	//-----------------------------
	$("#icon-input").on("change", function (e) {

		//画像を更新する変数
		var fileReader = new FileReader();
		fileReader.onload = function (e) {

		//画像ファイルを取得し、サイズを195px × 195pxにリサイズ
		$("#icon-preview").attr("src", e.target.result);
		$("#icon-preview").height("195px");
		$("#icon-preview").width("195px");
		}

		//変更した画像を反映する
		fileReader.readAsDataURL(e.target.files[0]);
	});
});