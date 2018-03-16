package com.xhb.sockserv.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES_CBC_NoPadding {

	public static byte[] decodeAES(byte[] data) throws Exception{
		byte[] bytes = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03,
				(byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
				(byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
				(byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F, (byte) 0x10 };
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] decryptedBytes = cipher.doFinal(data);
		//去除末尾ascii0x00
		int length = 0;
		while(length < decryptedBytes.length){
			if(decryptedBytes[length] == 0x00){
				break;
			}
			length++;
		}
		byte[] decryptedBytes2 = new byte[length];
		for(int i=0; i<length; i++)	decryptedBytes2[i] = decryptedBytes[i];
		
		return decryptedBytes2;
	}
	
	public static byte[] codeAES(byte[] data) throws Exception{
		byte[] bytes = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03,
				(byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
				(byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
				(byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F, (byte) 0x10 };
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		if(data.length % 16 != 0){ //待加密长度不是16的倍数时，用空格填充
			byte[] data2 = new byte[(data.length/16 + 1) * 16];
			for(int i=0; i<data.length; i++) data2[i] = data[i];
			for(int i=data.length; i<data2.length; i++) data2[i] = 0x20;
			byte[] encryptedBytes = cipher.doFinal(data2);
			return encryptedBytes;
		}
		else{
			byte[] encryptedBytes = cipher.doFinal(data);
			return encryptedBytes;
		}
	}
}
