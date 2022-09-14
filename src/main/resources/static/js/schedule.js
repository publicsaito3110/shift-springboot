$(function() {

	//---------------------------
	//一括選択ボタン押下時の処理
	//---------------------------

	//スケジュール時間区分ボタン1押下したとき、チェックボックス1をチェック
	$("#btn-time-kbn1").on("click", function () {
		$(".input-time-kbn1").prop("checked", true);
	});

	//スケジュール時間区分ボタン2押下したとき、チェックボックス2をチェック
	$("#btn-time-kbn2").on("click", function () {
		$(".input-time-kbn2").prop("checked", true);
	});

	//スケジュール時間区分ボタン3押下したとき、チェックボックス3をチェック
	$("#btn-time-kbn3").on("click", function () {
		$(".input-time-kbn3").prop("checked", true);
	});

	//スケジュール時間区分ボタン4押下したとき、チェックボックス4をチェック
	$("#btn-time-kbn4").on("click", function () {
		$(".input-time-kbn4").prop("checked", true);
	});

	//スケジュール時間区分ボタン5押下したとき、チェックボックス5をチェック
	$("#btn-time-kbn5").on("click", function () {
		$(".input-time-kbn5").prop("checked", true);
	});

	//スケジュール時間区分ボタン6押下したとき、チェックボックス6をチェック
	$("#btn-time-kbn6").on("click", function () {
		$(".input-time-kbn6").prop("checked", true);
	});

	//スケジュール時間区分ボタン7押下したとき、チェックボックス7をチェック
	$("#btn-time-kbn7").on("click", function () {
		$(".input-time-kbn7").prop("checked", true);
	});

	//全てチェックボタン押下したとき、チェックボックスを全てチェック
	$("#button-tim-all").on("click", function () {
		$(".input-all").prop("checked", true);
	});

	//クリアボタン押下したとき、チェックボックスを全て外す
	$("#button-tim-clear").on("click", function () {
		$(".input-all").prop("checked", false);
	});
});