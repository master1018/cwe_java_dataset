package com.bijay.onlinevotingsystem.controller;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class SHA256 {
	public String getSHA(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] messageDigest = md.digest(password.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			String hashPass = no.toString(16);
			while (hashPass.length() < 32) {
				hashPass = "0" + hashPass;
			}
			return hashPass;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
