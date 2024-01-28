package org.example;

public class Main {
    public static void main(String[] args) {
        // key and plaintext in hexadecimal format
        String hexKey = "006400c8012c019001f4025802bc0320";
        String hexPlaintext = "05320a6414c819fa";

        byte[] key = hexStringToByteArray(hexKey);
        byte[] plaintext = hexStringToByteArray(hexPlaintext);

        // Encrypt
        IDEA_Algorithm ideaEncrypt = new IDEA_Algorithm(key, "encrypt");
        byte[] ciphertext = plaintext.clone();
        ideaEncrypt.convert(ciphertext);

        // Convert ciphertext to hex string
        String hexCiphertext = byteArrayToHexString(ciphertext);

        // Decrypt
        IDEA_Algorithm ideaDecrypt = new IDEA_Algorithm(key, "decrypt");
        byte[] decryptedText = ciphertext.clone();
        ideaDecrypt.convert(decryptedText);

        // Convert decrypted text to hex string
        String hexDecryptedText = byteArrayToHexString(decryptedText);

        // Print Key, Plaintext, Ciphertext, and Decrypted text
        System.out.println("Key:         " + hexKey);
        System.out.println("Plaintext:   " + hexPlaintext);
        System.out.println("Ciphertext:  " + hexCiphertext);
        System.out.println("Decrypted:   " + hexDecryptedText);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}