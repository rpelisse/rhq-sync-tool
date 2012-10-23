package org.jboss.rhq.sync.tool.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.log4j.Logger;
import org.jboss.security.plugins.PBEUtils;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public final class PasswordUtil {

	private static String SALT = "df34bn67he32";
	private static String MASTER = "jonmasterpassword";
	private static String ALGORITHM = "PBEwithMD5andDES";

	private static Logger logger = Logger.getLogger(PasswordUtil.class);

	private PasswordUtil() {}

	public static String encode(String password) {
		byte[] salt = SALT.substring(0, 8).getBytes();
		int count = 15;
		char[] masterPassword = MASTER.toCharArray();
		byte[] passwordToEncode;
		try {
			passwordToEncode = password.getBytes("UTF-8");
			PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, count);
			PBEKeySpec keySpec = new PBEKeySpec(masterPassword);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
			SecretKey cipherKey = factory.generateSecret(keySpec);

			return PBEUtils.encode64(passwordToEncode, ALGORITHM,
					cipherKey, cipherSpec);
		} catch (UnsupportedEncodingException e) {
			logger.warn("Fail to encode password:" + e.getMessage());
			throw new IllegalStateException(e);
		} catch (Exception e) {
			logger.warn("Fail to encode password:" + e.getMessage());
			throw new IllegalStateException(e);
		}

	}


	public static String decode(String password) {
		try {
			byte[] salt = SALT.substring(0, 8).getBytes();
			int count = 15;
			char[] masterPassword = MASTER.toCharArray();
			PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, count);
			PBEKeySpec keySpec = new PBEKeySpec(masterPassword);
			SecretKeyFactory factory;
			factory = SecretKeyFactory.getInstance(ALGORITHM);
			SecretKey cipherKey;
			cipherKey = factory.generateSecret(keySpec);

			return PBEUtils.decode64(password, ALGORITHM, cipherKey, cipherSpec);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException("could not decode password " + e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
