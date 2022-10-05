$(function(){

	//ファイルアップロード可能な最大ファイルサイズ
	const FILE_MAX_LIMIT_SIZE_BYTE = 1000000;

	//-----------------------------
	//ファイルアップロード時の処理
	//-----------------------------
	$("#icon-input").on("change", function (e) {

		//アップロードされたファイルサイズを取得
		var fileSize = this.files[0].size;

		//ファイルサイズがアップロード可能最大ファイルサイズを超えたとき
		if (FILE_MAX_LIMIT_SIZE_BYTE < fileSize) {

			//ファイルサイズをBからMBへ換算し、アラートを表示
			var fileSizeMB = fileSize / FILE_MAX_LIMIT_SIZE_BYTE;
			alert("ファイルの容量が大きすぎます。1MB以下のファイルのみ有効です。\n" + fileSizeMB + "MB");
		}

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