$(function(){

	//現在の表示されている入力フォームをカウントする
	var nowInputFormCount = 1;

	//------------------------------------
	// 入力フォーム追加ボタン押下時の処理
	//------------------------------------
	$("#add-input-btn").on("click", function (){

		if (nowInputFormCount == 1) {

			//表示中の入力フォームが1のとき、フォーム2を表示
			$("#input-form2").css("display", "block");
			//フォーム2を必須に変更
			$(".input2").prop("required", true);
			//カウントアップ
			nowInputFormCount++;
		} else if (nowInputFormCount == 2) {

			//表示中の入力フォームが2のとき、フォーム3を表示
			$("#input-form3").css("display", "block");
			//フォーム3を必須に変更
			$(".input3").prop("required", true);
			//カウントアップ
			nowInputFormCount++;
		} else if (nowInputFormCount == 3) {

			//表示中の入力フォームが3のとき、フォーム4を表示
			$("#input-form4").css("display", "block");
			//フォーム4を必須に変更
			$(".input4").prop("required", true);
			//カウントアップ
			nowInputFormCount++;
		} else if (nowInputFormCount == 4) {

			//表示中の入力フォームが4のとき、フォーム5を表示
			$("#input-form5").css("display", "block");
			//フォーム5を必須に変更
			$(".input5").prop("required", true);
			//カウントアップ
			nowInputFormCount++;
		} else if (nowInputFormCount == 5) {

			//表示中の入力フォームが5のとき、フォーム6を表示
			$("#input-form6").css("display", "block");
			//フォーム6を必須に変更
			$(".input6").prop("required", true);
			//カウントアップ
			nowInputFormCount++;
		} else if (nowInputFormCount == 6) {

			//表示中の入力フォームが6のとき、フォーム7を表示
			$("#input-form7").css("display", "block");
			//フォーム7を必須に変更
			$(".input7").prop("required", true);
			//カウントアップ
			nowInputFormCount++;
			//追加ラベルを薄くする
			$("#add-input-label").css("opacity", 0.5);
		}
	});


	//------------------------------------
	// 入力フォーム削除ボタン押下時の処理
	//------------------------------------
	$("#delete-input-btn").on("click", function (){

		if (nowInputFormCount == 2) {

			//表示中の入力フォームが2のとき、フォーム2を非表示
			$("#input-form2").css("display", "none");
			//フォーム2を不要に変更
			$(".input2").prop("required", false);
			//フォーム2をクリア
			$(".input2").val("");
			//カウントダウン
			nowInputFormCount--;

		} else if (nowInputFormCount == 3) {

			//表示中の入力フォームが3のとき、フォーム3を非表示
			$("#input-form3").css("display", "none");
			//フォーム3を不要に変更
			$(".input3").prop("required", false);
			//フォーム3をクリア
			$(".input3").val("");
			//カウントダウン
			nowInputFormCount--;
		} else if (nowInputFormCount == 4) {

			//表示中の入力フォームが4のとき、フォーム4を非表示
			$("#input-form4").css("display", "none");
			//フォーム4を不要に変更
			$(".input4").prop("required", false);
			//フォーム4をクリア
			$(".input4").val("");
			//カウントダウン
			nowInputFormCount--;
		} else if (nowInputFormCount == 5) {

			//表示中の入力フォームが5のとき、フォーム5を非表示
			$("#input-form5").css("display", "none");
			//フォーム5を不要に変更
			$(".input5").prop("required", false);
			//フォーム5をクリア
			$(".input5").val("");
			//カウントダウン
			nowInputFormCount--;
		} else if (nowInputFormCount == 6) {

			//表示中の入力フォームが6のとき、フォーム6を非表示
			$("#input-form6").css("display", "none");
			//フォーム6を不要に変更
			$(".input6").prop("required", false);
			//フォーム6をクリア
			$(".input6").val("");
			//カウントダウン
			nowInputFormCount--;
		} else if (nowInputFormCount == 7) {

			//表示中の入力フォームが7のとき、フォーム7を非表示
			$("#input-form7").css("display", "none");
			//フォーム7を不要に変更
			$(".input7").prop("required", false);
			//フォーム7をクリア
			$(".input7").val("");
			//カウントダウン
			nowInputFormCount--;
			//追加ラベルを濃くする
			$("#add-input-label").css("opacity", 1.0);
		}
	});
});