$(function() {

	//form内の入力値を全て取得
	var forms = document.querySelectorAll('.needs-validation');

	//formの入力値を全てチェックする
	Array.prototype.slice.call(forms).forEach(function(form) {

		//formが送信されたときの処理
		form.addEventListener('submit', function(event) {

			//未入力項目があるとき送信をキャンセルし、alertを表示
			if (!form.checkValidity()) {
				event.preventDefault();
				event.stopPropagation();
			}

			form.classList.add('was-validated');
		}, false)
	})
});