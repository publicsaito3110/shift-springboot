package com.shift.common;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author saito
 *
 */
public class FileUploadLogic {


	/**
	 * ファイルアップロード処理(MultipartFile)
	 *
	 * <p>アップロードするファイル(uploadFile)を指定したパスにアップロードする<br>
	 * また、パスが存在しない場合、パスが有効の場合に限りフォルダが自動的に作成される<br>
	 * ただし、ファイルアップロードに失敗したときはfalseとなる
	 * </p>
	 *
	 * @param uploadFile MultipartFile<br>
	 * ただし、ファイル自体に異常がある場合は失敗する可能性がある
	 * @param uploadRootDir アップロードするファイルのアップロード先のパス(フォルダ)<br>
	 * ただし、nullのときは必ず失敗する
	 * @param fileName アップロードするファイルの名前<br>
	 * ただし、nullまたはファイル名及び拡張子がない場合は失敗する
	 * @return boolean<br>
	 * true: ファイルのアップロードが成功したとき<br>
	 * false: ファイルのアップロードが失敗したとき
	 */
	public boolean uploadFileByMultipartFile(MultipartFile uploadFile, String uploadRootDir, String fileName) {

		try {

			//アップロード先のパスが存在しないとき、フォルダを作成
			File dirFile = new File(uploadRootDir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			//アップロードするファイルをByteへ変換する
			byte[] fileByte = uploadFile.getBytes();

			//アップロードするファイルのパスを取得
			Path uploadFilePath = Paths.get(uploadRootDir + fileName);

			//ファイルをアップロード
			OutputStream outputStream = Files.newOutputStream(uploadFilePath);
			outputStream.write(fileByte);

			//アップロード成功時、trueを返す
			return true;
		}catch (Exception e) {

			//例外発生時、falseを返す
			e.printStackTrace();
			return false;
		}
	}
}
