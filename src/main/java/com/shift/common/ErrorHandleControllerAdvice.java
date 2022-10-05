package com.shift.common;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @author saito
 *
 */
@Slf4j
@ControllerAdvice
public class ErrorHandleControllerAdvice {


	/**
	 * エラーハンドリング処理
	 *
	 * <p>例外発生情報(Exception)を取得し、エラーログをlogファイルに出力し、エラー画面へ遷移させる<br>
	 * また、Exceptionのスーパークラスも含む<br>
	 * ただし、HttpStatusは500になる
	 * </p>
	 *
	 * @param e Exception
	 * @return ModelAndView
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception e) {

		//Exceptionクラスを取得
		String exceptionClassName = e.toString();

		//Exception発生個所を取得
		StackTraceElement[] stackTraceArray = e.getStackTrace();
		String errorClassName = stackTraceArray[0].toString();

		//HttpStatusを500に設定し、Statusコードを取得
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String httpStatus = String.valueOf(response.getStatus());

		//エラーログをlogファイルに出力
		log.info("↓↓エラーログ↓↓");
		log.info("Exception: " + exceptionClassName);
		log.info("Error : " + errorClassName);

		//ModelAndViewにエラー情報をセットし、error.htmlへ遷移
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exceptionClassName", exceptionClassName);
		modelAndView.addObject("errorClassName", errorClassName);
		modelAndView.addObject("httpStatus", httpStatus);
		modelAndView.addObject("errorMassage", "予期せぬエラーが発生しました");

		modelAndView.setViewName("error");
		return modelAndView;
	}


	/**
	 * アップロードファイルサイズオーバーエラーハンドリング処理
	 *
	 * <p>例外発生情報(MaxUploadSizeExceededException)を取得し、エラーログをlogファイルに出力し、エラー画面へ遷移させる<br>
	 * また、MaxUploadSizeExceededExceptionが優先される<br>
	 * ただし、HttpStatusは416になる
	 * </p>
	 *
	 * @param e Exception
	 * @return ModelAndView
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView maxUploadSizeExceededExceptionHandler(Exception e) {

		//Exceptionクラスを取得
		String exceptionClassName = e.toString();

		//Exception発生個所を取得
		StackTraceElement[] stackTraceArray = e.getStackTrace();
		String errorClassName = stackTraceArray[0].toString();

		//HttpStatusを416に設定し、Statusコードを取得
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		response.setStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
		String httpStatus = String.valueOf(response.getStatus());

		//ModelAndViewにエラー情報をセットし、error.htmlへ遷移
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exceptionClassName", exceptionClassName);
		modelAndView.addObject("errorClassName", errorClassName);
		modelAndView.addObject("httpStatus", httpStatus);
		modelAndView.addObject("errorMassage", "ファイルの容量が大きすぎます。1MB以下のファイルのみ有効です。");

		modelAndView.setViewName("error");
		return modelAndView;
	}
}
