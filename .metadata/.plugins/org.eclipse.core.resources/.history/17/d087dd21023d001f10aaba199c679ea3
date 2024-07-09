package com.taskmanagement.userservice.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.json.JSONObject;

import com.taskmanagement.userservice.constants.Constants;

public class HashUtil {

	public static String generateMd5Hash(String password) throws NoSuchAlgorithmException {
		MessageDigest md5Instance = MessageDigest.getInstance(Constants.MD5);
		md5Instance.update(password.getBytes());
		byte[] digest = md5Instance.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String md5hash = bigInt.toString(16);
		while (md5hash.length() < 32) {
			md5hash = "0" + md5hash;
		}
		return md5hash;
	}
	
	public static String getUserNameFromJWTToken(String token) {
		token = token.split("Bearer ")[1];
		String[] headerAndPayload = token.split("\\.");
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload = new String(decoder.decode(headerAndPayload[1]));
		JSONObject payloadJSON = new JSONObject(payload);
		return payloadJSON.optString("preferred_username");
	}
}