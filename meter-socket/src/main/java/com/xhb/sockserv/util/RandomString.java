package com.xhb.sockserv.util;

import java.util.Random;

public class RandomString {
	private static Random strGen = new Random();
	private static char[] numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	
	/**
	 * Generate a string of a certain length
	 * @param length,the length of the string
	 * @return A string of specified length
	 */
	public static String create(int length){
		if (length <= 0) {
			return null;
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < length; i++) {
			randBuffer[i] = numbersAndLetters[strGen.nextInt(35)];
		}
		return new String(randBuffer);
	}
}
