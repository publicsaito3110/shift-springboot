package com.shift.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 空白文字及び改行のみ判定アノテーション
 *
 * <p>空白文字及び改行のみではなく、何かしらの文字が含まれているを判別するアノテーション<br>
 * ただし、値がnullまたは空文字のときは必ずバリデーション成功(true)になる<br>
 * true: 空白文字及び改行のみ<br>
 * false: 空白文字及び改行のみで何かしらの文字が含まれていない
 * </p>
 *
 * @author saito
 */
@Target({ElementType.FIELD})  //アノテーションを付与する対象(フィールド)
@Constraint(validatedBy = {NotOnlyWhitespaceValidator.class})  //入力チェックの実装クラス
@Retention(RetentionPolicy.RUNTIME)  //読み込みタイミング(RUNTIME:実行時)
@Documented  //Javadoc APIドキュメントの出力対象
public @interface NotOnlyWhitespace {

	//表示するエラーメッセージ
	String message() default "空白文字または改行のみは禁止されています";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface List {
		NotOnlyWhitespace[] value();
	}
}
