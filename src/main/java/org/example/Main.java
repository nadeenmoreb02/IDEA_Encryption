package org.example;


import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        IDEA_128("hello this is a test");
        IDEA_256("hello this is a test");
    }

    private static void IDEA_128(String plaintextString) {

        System.out.println("------------------IDEA_128------------------");
        // key in hex string
        String hexKey = "006400c8012c019001f4025802bc0320";

        byte[] key = hexStringToByteArray(hexKey);
        byte[] plaintext = plaintextString.getBytes(StandardCharsets.UTF_8);

        // Process plaintext in 8-byte blocks
        byte[] ciphertext = new byte[plaintext.length];
        byte[] decryptedText = new byte[plaintext.length];

        int totalLength = plaintext.length;

        //number of 8-byte blocks
        int blockCount = (totalLength + 7) / 8;

        for (int i = 0; i < totalLength; i += 8) {
            int blockSize = Math.min(8, totalLength - i);
            byte[] block = Arrays.copyOfRange(plaintext, i, i + blockSize);
            byte[] paddedBlock = padPlaintext(new String(block, StandardCharsets.UTF_8));

            // Encrypt
            IDEA_Algorithm ideaEncrypt = new IDEA_Algorithm(key, "encrypt");
            ideaEncrypt.convert(paddedBlock);

            // Decrypt
            IDEA_Algorithm ideaDecrypt = new IDEA_Algorithm(key, "decrypt");
            ideaDecrypt.convert(paddedBlock);

            // Copy to ciphertext and decryptedText arrays
            System.arraycopy(paddedBlock, 0, ciphertext, i, blockSize);
            System.arraycopy(paddedBlock, 0, decryptedText, i, blockSize);
        }

        // Convert ciphertext to hex string
        String hexCiphertext = byteArrayToHexString(ciphertext);

        // Convert decrypted text to normal string
        String decryptedString = new String(decryptedText, StandardCharsets.UTF_8).trim();

        // Print Key, Plaintext, Ciphertext, and Decrypted text
        System.out.println("Key:         " + hexKey);
        System.out.println("Plaintext:   " + plaintextString);
        System.out.println("Ciphertext:  " + hexCiphertext);
        System.out.println("Decrypted:   " + decryptedString);
        System.out.println("------------------Finished------------------");
    }

    private static void IDEA_256(String plaintextString) {
        System.out.println("------------------IDEA_265------------------");
        // key in hex string
        String hexKey = "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F";

        byte[] key = hexStringToByteArray(hexKey);
        byte[] plaintext = plaintextString.getBytes(StandardCharsets.UTF_8);

        // Process plaintext in 8-byte blocks
        byte[] ciphertext = new byte[plaintext.length];
        byte[] decryptedText = new byte[plaintext.length];

        int totalLength = plaintext.length;

        //number of 8-byte blocks
        int blockCount = (totalLength + 7) / 8;

        for (int i = 0; i < totalLength; i += 8) {
            int blockSize = Math.min(8, totalLength - i);
            byte[] block = Arrays.copyOfRange(plaintext, i, i + blockSize);
            byte[] paddedBlock = padPlaintext(new String(block, StandardCharsets.UTF_8));

            // Encrypt
            IDEA_Algorithm256 ideaEncrypt = new IDEA_Algorithm256(key, "encrypt");
            ideaEncrypt.convert(paddedBlock);

            // Decrypt
            IDEA_Algorithm256 ideaDecrypt = new IDEA_Algorithm256(key, "decrypt");
            ideaDecrypt.convert(paddedBlock);

            // Copy to ciphertext and decryptedText arrays
            System.arraycopy(paddedBlock, 0, ciphertext, i, blockSize);
            System.arraycopy(paddedBlock, 0, decryptedText, i, blockSize);
        }

        // Convert ciphertext to hex string
        String hexCiphertext = byteArrayToHexString(ciphertext);

        // Convert decrypted text to normal string
        String decryptedString = new String(decryptedText, StandardCharsets.UTF_8).trim();

        // Print Key, Plaintext, Ciphertext, and Decrypted text
        System.out.println("Key:         " + hexKey);
        System.out.println("Plaintext:   " + plaintextString);
        System.out.println("Ciphertext:  " + hexCiphertext);
        System.out.println("Decrypted:   " + decryptedString);
        System.out.println("------------------Finished------------------");
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

    private static byte[] padPlaintext(String plaintext) {
        byte[] padded = new byte[8];
        byte[] plaintextBytes = plaintext.getBytes();

        // Copy the original bytes
        System.arraycopy(plaintextBytes, 0, padded, 0, Math.min(plaintextBytes.length, 8));

        // Pad with 'x' fill char if needed
        for (int i = plaintextBytes.length; i < 8; i++) {
            padded[i] = 'x';
        }

        return padded;
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}