$(function() {

	//---------------------------
	//ニュースリスト押下時の処理
	//---------------------------
	$(".news-list").on("click", function (){

		//お知らせの日付, タイトル, 内容をモーダル1にセット
		$("#modal1-title").text($(this).children(".news-title").text());
		$("#modal1-date").html($(this).children(".news-date").text());
		$("#modal1-content").html($(this).children(".news-content-display").val());

		//お知らせの表示用修正前の値を取得し、モーダル2にセット
		$("#modal2-news-ymd").val($(this).children(".news-ymd").val());
		$("#modal2-news-title-before").val($(this).children(".news-title").val());
		$("#modal2-news-category-before").val($(this).children(".news-category").val());
		$("#modal2-news-content-before").val($(this).children(".news-content").val());
	});
});