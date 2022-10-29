$(function() {


	//-------------------------------
	//年月フォーム入力時の処理
	//-------------------------------
	$("#schedule-report-seach-month-input").change(function(){

		if ($(this).val().match(/^[0-9]{4}-(0[1-9]|1[0-2])$/)) {

			//年月が指定されているとき検索ボタンを表示
			$("#schedule-report-seach-btn").prop("disabled", false);
		} else {

			//年月が指定されていないとき検索ボタンを非表示
			$("#schedule-report-seach-btn").prop("disabled", true);
		}
	});


	//-------------------------------
	//勤務日数追加ボタン押下時の処理
	//-------------------------------

	//勤務日数追加ボタンを押下したとき、チェックボックス1をチェック
	$("#schedule-report-seach-btn").on("click", function (){

		//<tr>タグを<tbody>に追加
		$("#tbody-box").append("<tr></tr>");

		//inputの値を取得し、ymフォーマットに変換
		let ymDate = $("#schedule-report-seach-month-input").val();
		let ym = ymDate.replace("-", "");

		//非同期処理後、テーブルに結果を追加
		$("#tbody-box").children("tr:last").load("/schedule-decision/report/search?ym=" + ym + " .search-td1, .search-td2");
	});
});