package de.oth.jit.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {
	public static String hashString(String text) throws NoSuchAlgorithmException {
		if (text != null) {
			return hashBytes(text.getBytes());	
		} else {
			return "";
		}
	}
	
	public static String hashBytes(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digested = md.digest(bytes);
        
        StringBuilder builder = new StringBuilder();
        
        for (byte b : digested) {
            int value = b & 0xFF; // & 0xFF to treat byte as "unsigned"
            
            builder.append(Integer.toHexString(value & 0x0F));
            builder.append(Integer.toHexString(value >>> 4));
        }
        
        return builder.toString();
	}
}
