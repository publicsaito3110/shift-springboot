$(function() {

	//---------------------
	//ニュースリスト押下時
	//---------------------
	$(".news-list").on("click", function () {

		$("#exampleModalLiveLabel").text($(this).children(".news-title").text());
		$("#modal-content").text($(this).children(".news-content").val());
	})
});