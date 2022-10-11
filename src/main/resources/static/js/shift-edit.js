$(function(){

	//入力フォームを表示・日表示するために値を保持するための変数
	var addInputBtnValue = 0;

	//------------------------------------
	// 入力フォーム追加ボタン押下時の処理
	//------------------------------------
	$("#add-input-btn").on("click", function (){

		if (addInputBtnValue == 0) {

			//追加ボタンの押下回数が0のとき、フォーム0を表示
			$("#input-form0").css("display", "block");
			//フォーム0を必須に変更
			$(".input0").prop("required", true);
			//カウントアップ
			addInputBtnValue++;
		} else if (addInputBtnValue == 1) {

			//追加ボタンの押下回数が1のとき、フォーム1を表示
			$("#input-form1").css("display", "block");
			//フォーム1を必須に変更
			$(".input1").prop("required", true);
			//カウントアップ
			addInputBtnValue++;
		} else if (addInputBtnValue == 2) {

			//追加ボタンの押下回数が2のとき、フォーム2を表示し
			$("#input-form2").css("display", "block");
			//フォーム2を必須に変更
			$(".input2").prop("required", true);
			//カウントアップ
			addInputBtnValue++;
		} else if (addInputBtnValue == 3) {

			//追加ボタンの押下回数が3のとき、フォーム3を表示し、カウントアップ
			$("#input-form3").css("display", "block");
			//フォーム3を必須に変更
			$(".input3").prop("required", true);
			//カウントアップ
			addInputBtnValue++;
		} else if (addInputBtnValue == 4) {

			//追加ボタンの押下回数が4のとき、フォーム4を表示し、カウントアップ
			$("#input-form4").css("display", "block");
			//フォーム4を必須に変更
			$(".input4").prop("required", true);
			//カウントアップ
			addInputBtnValue++;
		} else if (addInputBtnValue == 5) {

			//追加ボタンの押下回数が5のとき、フォーム5を表示
			$("#input-form5").css("display", "block");
			//フォーム5を必須に変更
			$(".input5").prop("required", true);
			//追加ラベルを薄くする
			$("#add-input-label").css("opacity", 0.5);
		}
	});


	//------------------------------------
	// 入力フォーム削除ボタン押下時の処理
	//------------------------------------
	$(".delete-input-btn").on("click", function (){

		if (addInputBtnValue == 0) {

			//追加ボタンの押下回数が0のとき、フォーム0を非表示
			$("#input-form0").css("display", "none");
			//フォーム0を不要に変更
			$(".input0").prop("required", false);
			//フォーム0をクリア
			$(".input0").val("");

		} else if (addInputBtnValue == 1) {

			//追加ボタンの押下回数が1のとき、フォーム1をクリア及び非表示
			$("#input-form1").css("display", "none");
			//フォーム1を不要に変更
			$(".input1").prop("required", false);
			//フォーム1をクリア
			$(".input1").val("");
			//カウントダウン
			addInputBtnValue--;
		} else if (addInputBtnValue == 2) {

			//追加ボタンの押下回数が2のとき、フォーム2をクリア及び非表示し、カウントダウン
			$("#input-form2").css("display", "none");
			//フォーム2を不要に変更
			$(".input2").prop("required", false);
			//フォーム2をクリア
			$(".input2").val("");
			//カウントダウン
			addInputBtnValue--;
		} else if (addInputBtnValue == 3) {

			//追加ボタンの押下回数が3のとき、フォーム3をクリア及び非表示し、カウントダウン
			$("#input-form3").css("display", "none");
			//フォーム3を不要に変更
			$(".input3").prop("required", false);
			//フォーム3をクリア
			$(".input3").val("");
			//カウントダウン
			addInputBtnValue--;
		} else if (addInputBtnValue == 4) {

			//追加ボタンの押下回数が4のとき、フォーム4をクリア及び非表示し、カウントダウン
			$("#input-form4").css("display", "none");
			//フォーム4を不要に変更
			$(".input4").prop("required", false);
			//フォーム4をクリア
			$(".input4").val("");
			//カウントダウン
			addInputBtnValue--;
		} else if (addInputBtnValue == 5) {

			//追加ボタンの押下回数が5のとき、フォーム5をクリア及び非表示し、カウントダウン
			$("#input-form5").css("display", "none");
			//フォーム5を不要に変更
			$(".input5").prop("required", false);
			//フォーム5をクリア
			$(".input5").val("");
			//カウントダウン
			addInputBtnValue--;
			//追加ラベルを濃くする
			$("#add-input-label").css("opacity", 1.0);
		}
	});
});