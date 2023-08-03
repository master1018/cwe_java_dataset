package com.bijay.onlinevotingsystem.controller;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
public class SHA256 {
	private static final String SSHA_PREFIX = "{SSHA}";
	private static final int SSHA_256_LENGTH = 32; 
	private static final int SALT_LENGTH = 16; 
	public String getSHA(String password) {
		try {
			byte[] salt = getSalt();
			String cipher = getCipher(password, salt);
			return cipher;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static boolean validatePassword(String password, String cipherText) {
		boolean isValid = false;
		try {
			String cipher = cipherText.substring(SSHA_PREFIX.length());
			byte[] cipherBytes = Base64.getDecoder().decode(cipher.getBytes());
			byte[] salt = new byte[SALT_LENGTH];
			System.arraycopy(cipherBytes, SSHA_256_LENGTH, salt, 0, SALT_LENGTH);
			String result = getCipher(password, salt);
			isValid = result.equals(cipherText);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return isValid;
	}
	private static byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH];
		random.nextBytes(salt);
		return salt;
	}
	private static String getCipher(String password, byte[] salt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(salt);
		byte[] passBytes = password.getBytes();
		byte[] allBytes = new byte[passBytes.length + SALT_LENGTH];
		System.arraycopy(passBytes, 0, allBytes, 0, passBytes.length);
		System.arraycopy(salt, 0, allBytes, passBytes.length, SALT_LENGTH);
		byte[] cipherBytes = new byte[SSHA_256_LENGTH + SALT_LENGTH];
		byte[] messageDigest = md.digest(allBytes);
		System.arraycopy(messageDigest, 0, cipherBytes, 0, SSHA_256_LENGTH);
		System.arraycopy(salt, 0, cipherBytes, SSHA_256_LENGTH, SALT_LENGTH);
		String result = SSHA_PREFIX + Base64.getEncoder().encodeToString(cipherBytes);
		return result;
	}
}
