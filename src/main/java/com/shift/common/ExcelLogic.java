package com.shift.common;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

/**
 * @author saito
 *
 */
public class ExcelLogic {


	/**
	 * 列取得処理 {Apache POI}
	 *
	 * <p>名前付きセルを取得し、distanceBaseCellRowだけ下の列情報を取得する<br>
	 * ただし、名前付きセルが取得できないまたは列を指定できないときはnullとなる<br>
	 * (注意) Apache POI の仕様により全く同じ列情報を複数回取得し、セルに書き込もうとすると最後にセルに書き込んだRowで上書きされる
	 * </p>
	 *
	 * @param workBook Excel読み込み済みのWorkBook
	 * @param sheet Excelのシート名取得済みのSheet
	 * @param cellNameBase シートにあらかじめ設定したセル名
	 * @param distanceBaseCellRow 名前付きセル(cellNameBase)を基準としてn列目を指定<br>
	 * 0: cellNameBaseのある列, 1: cellNameBaseの1つ下の列...
	 * @return Row Excelの列情報が代入されたRow
	 */
	public Row getRow(Workbook workBook, Sheet sheet, String cellNameBase, int distanceBaseCellRow) {

		try {

			//名前付きのセルを取得
			Name cellName = workBook.getName(cellNameBase);
			CellReference cellReference = new CellReference(cellName.getRefersToFormula());

			//名前付きのセルからdistanceBaseCellRowだけ下のRow(列)取得する
			Row cellRow = sheet.createRow(cellReference.getRow() + distanceBaseCellRow);

			//セルを取得
			return cellRow;
		}catch (Exception e) {

			//例外発生時、ログを出力
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * セル書き込み処理 {Apache POI}
	 *
	 * <p>取得した列情報から行(cellCols)を指定し、対象のセルに書き込む<br>
	 * ただし、セルを取得できないときはログが出力される<br>
	 * (注意) Apache POI の仕様により全く同じ列情報のRow(複数)でセルに書き込もうとすると、最後にセルに書き込んだRowで上書きされる
	 * また、書き出されたセルは呼び出し元で受け取る必要はない(Cellのスタイルを変更する場合は除く)
	 * </p>
	 *
	 * @param cellRow ExcelLogicから取得した
	 * @param cellCols 書き込みたいセルの行数<br>
	 * 左から振り分けられている 0: 1行目, 1: 2行目, 2: 3行目...
	 * @param cellValue セルに書き込む値
	 * @return Cell 書き出し済みのセルの値
	 */
	public Cell writeCellValueForCell(Row cellRow, int cellCols, String cellValue) {

		try {

			//対象のセルへ書き込み
			Cell cell = cellRow.createCell(cellCols);
			cell.setCellValue(cellValue);

			//書き込んだセルを返す
			return cell;
		}catch (Exception e) {

			//例外発生時、ログを出力し、nullを返す
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Excelファイル書き出し処理 {Apache POI}
	 *
	 * <p>書き込まれた全てのセルをExcelに反映させる<br>
	 * ただし、Excelファイル書き出し処理を複数回行っている又はExcelファイルに書き出せないときはログが出力される<br>
	 * (注意) Apache POI の仕様によりExcelファイル出力処理よりも前にExcelファイル書き出し処理を行っていないと必ず失敗する
	 * </p>
	 *
	 * @param workBook Excel読み込み済みのWorkBook
	 * @param fileOutputStream Excelの出力先が格納されたOutputStream
	 * @return boolean<br>
	 * true: Excelファイルへの書き出しが成功したとき<br>
	 * false: Excelファイルへの書き出しが成功したとき
	 */
	public boolean writeAllCellForExcel(Workbook workBook, OutputStream fileOutputStream) {

		try {

			//書き込んだセルをExcelへ書き出し、trueを返す
			workBook.write(fileOutputStream);
			return true;
		}catch (Exception e) {

			//例外発生時、ログを出力しfalseを返す
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Excelファイル出力処理 {Apache POI}
	 *
	 * <p>書き出されたExcelを出力する<br>
	 * ただし、Excelファイルを出力できないときはログが出力される<br>
	 * (注意) Apache POI の仕様によりExcelファイル出力処理よりも前にExcelファイル書き出し処理を行っていないと必ず失敗する
	 * </p>
	 *
	 * @param response HttpServletResponse
	 * @param outFilePath ダウンロードするExcelを保存するパス
	 * @param downloadFileName ダウンロードするExcelのファイル名
	 * @return boolean<br>
	 * true: Excelファイルの出力が成功したとき<br>
	 * false: Excelファイルの出力が成功したとき
	 */
	public boolean outputExcelFile(HttpServletResponse response, String outFilePath, String downloadFileName) {

		try (OutputStream responseOutputStream =  response.getOutputStream();) {

			//ダウンロード処理をし、trueを返す
			Path filePath = Paths.get(outFilePath);
			byte[] fileByte = Files.readAllBytes(filePath);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(downloadFileName, "UTF-8") + "\"");
			response.setContentLength(fileByte.length);
			responseOutputStream.write(fileByte);
			responseOutputStream.flush();
			return true;
		} catch (Exception e) {

			//例外発生時、ログを出力しfalseを返す
			e.printStackTrace();
			return false;
		}
	}
}
