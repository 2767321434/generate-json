package my.yf;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

public class EncFileUtil {

		/**
		 * 根据参数生成KEY
		 */
		public static Key getKey(String strKey) {
			try {
				KeyGenerator _generator = KeyGenerator.getInstance("DES");
				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
				secureRandom.setSeed(strKey.getBytes());
				_generator.init(secureRandom);
				Key key = _generator.generateKey();
				_generator = null;
				return key;
			} catch (Exception e) {
				throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
			}
		}
		/**
		 * @Description: 加密
		 */
		public static void encrypt(File file, String destFile, String strKey) throws Exception {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, getKey(strKey));
			InputStream is = new FileInputStream(file);
			OutputStream out = new FileOutputStream(destFile);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			byte[] buffer = new byte[1024];
			int r;
			while ((r = cis.read(buffer)) > 0) {
				out.write(buffer, 0, r);
			}
			cis.close();
			is.close();
			out.close();
		}

		/**
		 * @Description: 解密
		 */
		public static void decrypt(String file, String dest, String strKey) throws Exception {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, getKey(strKey));
			InputStream is = new FileInputStream(file);
			OutputStream out = new FileOutputStream(dest);
			CipherOutputStream cos = new CipherOutputStream(out, cipher);
			byte[] buffer = new byte[1024];
			int r;
			while ((r = is.read(buffer)) >= 0) {
				cos.write(buffer, 0, r);
			}
			cos.close();
			out.close();
			is.close();
		}
}
