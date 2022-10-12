package com.shift.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 禁止文字判定アノテーション
 *
 * <p>禁止文字が含まれているかを判別するアノテーション<br>
 * ただし、値がnullまたは空文字のときは必ずバリデーション成功(true)になる<br>
 * true: 禁止文字が含まれていない<br>
 * false: 禁止文字が含まれている
 * </p>
 *
 * @author saito
 */
@Target({ElementType.FIELD})  //アノテーションを付与する対象(フィールド)
@Constraint(validatedBy = {NotNgCharValidator.class})  //入力チェックの実装クラス
@Retention(RetentionPolicy.RUNTIME)  //読み込みタイミング(RUNTIME:実行時)
@Documented  //Javadoc APIドキュメントの出力対象
public @interface NotNgChar {

	//表示するエラーメッセージ
	String message() default "入力禁止文字が含まれています";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface List {
		NotNgChar[] value();
	}
}
