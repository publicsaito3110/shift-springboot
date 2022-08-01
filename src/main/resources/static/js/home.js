$(function() {

	//---------------------
	//ニュースリスト押下時
	//---------------------
	$(".news-list").on("click", function () {

		//お知らせの日付, タイトル, 内容をモーダルにセット
		$("#modal-news-title").text($(this).children(".news-title").text());
		$("#modal-news-date").text($(this).children(".news-date").text());
		$("#modal-news-content").html($(this).children(".news-content").val());
	})
});