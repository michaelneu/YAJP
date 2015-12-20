package de.oth.jit.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides utilities to create hashed strings. It uses the SHA-1 
 * hashing algorithm.
 * 
 * @author Michael Neu
 */
public final class HashUtils {
	/**
	 * Create the hash of a string. 
	 * 
	 * @param text Which string to hash
	 * 
	 * @return The hash of the string
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public static String hashString(String text) throws NoSuchAlgorithmException {
		if (text != null) {
			return hashBytes(text.getBytes());	
		} else {
			return "";
		}
	}
	
	/**
	 * Create the hash of a byte array. 
	 * 
	 * @param bytes Which byte array to hash
	 * 
	 * @return The hashed string
	 * 
	 * @throws NoSuchAlgorithmException
	 */
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
