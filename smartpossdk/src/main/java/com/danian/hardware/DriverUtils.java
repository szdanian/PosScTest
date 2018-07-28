package com.danian.hardware;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DriverUtils {

    public static void delayMs(int ms) {
        if (ms > 0) {
            try {
                Thread.sleep((long) ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DesUtils {
        public static final int ENCRYPT_MODE = Cipher.ENCRYPT_MODE;
        public static final int DECRYPT_MODE = Cipher.DECRYPT_MODE;

        public static byte[] singleDes(byte[] data, byte[] key, int mode) {
            return baseEncrypt("DES", data, key, mode);
        }

        public static byte[] tripleDes(byte[] data, byte[] key, int mode) {
            return baseEncrypt("DESede", data, key, mode);
        }

        private static byte[] baseEncrypt(String method, byte[] data, byte[] key, int mode) {
            SecretKey secretKey = new SecretKeySpec(key, method);
            try {
                Cipher cipher = Cipher.getInstance(method + "/ECB/NoPadding");
                cipher.init(mode, secretKey);
                return cipher.doFinal(data);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class HelpUtils {
        public static String hexToString(byte[] bdata, int len) {
            String str = "";
            for (int num = 0; num < len; num++) {
                String hex = Integer.toHexString(bdata[num] & 0xFF);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                str += hex + "";
            }
            return str.toUpperCase();
        }

        public static String asciiToString(byte[] bdata, int start, int len) {
            String str = "";
            for (int num = 0; num < len; num++) {
                char temp = (char) bdata[start + num];
                str += temp;
            }
            return str;
        }

        public static byte[] stringToHex(String str) {
            byte[] temp = str.getBytes();
            for (int num = 0; num < temp.length; num++) {
                if (temp[num] > '9') {
                    temp[num] -= '7';
                } else {
                    temp[num] -= '0';
                }
            }

            byte[] bdata = new byte[temp.length / 2];
            for (int num = 0; num < bdata.length; num++) {
                bdata[num] = (byte) ((temp[2 * num] << 4) | temp[2 * num + 1]);
            }
            return bdata;
        }
    }
}
