$(function() {

	//---------------------------
	//ニュースリスト押下時の処理
	//---------------------------
	$(".news-list").on("click", function (){

		//お知らせの日付, タイトル, 内容をモーダル1にセット
		$("#modal1-title").text($(this).children(".news-title").text());
		$("#modal1-date").text($(this).children(".news-date").text());
		$("#modal1-content").html($(this).children(".news-content-display").val());

		//お知らせの日付, タイトル, 内容, 修正前の値を取得し、モーダル2にセット
		$("#modal2-news-date").text($(this).children(".news-date").text());
		$("#modal2-news-title").val($(this).children(".news-title").text());
		$("#modal2-news-category").val($(this).children(".news-category").val());
		$("#modal2-news-content").html($(this).children(".news-content-display").val());
		$("#modal2-news-id").val($(this).children(".news-id").val());
		$("#modal2-news-ymd").val($(this).children(".news-ymd").val());
	});
});