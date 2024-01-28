package org.example;

public class IDEA_Algorithm256 {

    private static final int roundsNum = 12; // or 16 for a stronger variant

    // (roundsNum * 6 + 4) sub-keys generated from the user 256-bit key
    private int[] sub_key;

    // constructor
    public IDEA_Algorithm256(byte[] key, String option) {
        int[] tempSubKey = key_expansion(key);
        if (option.equals("encrypt"))
            sub_key = tempSubKey;
        else if (option.equals("decrypt")) {
            assert tempSubKey != null;
            sub_key = inverted_sub_key(tempSubKey);
        }
    }

    private static int[] inverted_sub_key (int[] key) {
        int[] new_key = new int[key.length];

        int counter = 0;

        //roundsNum * 6 = 8 * 6 = 48
        int i = 72;

        new_key[i] = MathematicalOperations.multiplicativeInverse(key[counter++]);
        new_key[i + 1] = MathematicalOperations.additiveInverse(key[counter++]);
        new_key[i + 2] = MathematicalOperations.additiveInverse(key[counter++]);
        new_key[i + 3] = MathematicalOperations.multiplicativeInverse(key[counter++]);

        int r = roundsNum - 1;
        while(r>=0){
            i = r * 6;
            int m = r > 0 ? 2 : 1;
            int n = r > 0 ? 1 : 2;
            new_key[i + 4] = key[counter++];
            new_key[i + 5] = key[counter++];
            new_key[i] = MathematicalOperations.multiplicativeInverse(key[counter++]);
            new_key[i + m] = MathematicalOperations.additiveInverse(key[counter++]);
            new_key[i + n] = MathematicalOperations.additiveInverse(key[counter++]);
            new_key[i + 3] = MathematicalOperations.multiplicativeInverse(key[counter++]);
            r--;
        }
        return new_key;
    }

    private static int[] key_expansion(byte[] original_key) {
        // Check if the key length is 32 bytes.
        if (original_key.length == 32) {
            // Create a new array to store the expanded key.
            int[] new_key = new int[76]; // 256-bit key: roundsNum * 6 + 4 = 12 * 6 + 4 = 76
            int new_length = original_key.length / 2;

            // Convert the 32-byte user key to 16 16-bit integers.
            for (int i = 0; i < new_length; i++) {
                new_key[i] = ((original_key[2 * i] & 0xFF) << 8) | (original_key[2 * i + 1] & 0xFF);
            }

            // Generate the remaining 60 sub-keys from the user key.
            for (int i = new_length; i < new_key.length; i++) {
                int previousKeyIndex = (i - 7);
                int nextKeyIndex = (i - 6);

                // If the index is a multiple of 8, then use the previous key index.
                if ((i + 1) % 8 == 0) {
                    previousKeyIndex = i - 15;
                }
                // If the index is 2 more than a multiple of 8, then use the next key index.
                if ((i + 2) % 8 < 2) {
                    nextKeyIndex = i - 14;
                }

                // Generate the sub-key by :
                // shift left 9 bits for previous key index which is a multiplication by 2^9
                // shift right 7 bits for next key index which is a division by 2^7
                new_key[i] = ((new_key[previousKeyIndex] << 9) | (new_key[nextKeyIndex] >>> 7)) & 0xFFFF;
            }

            // Return the expanded key.
            return new_key;
        }

        // If the key length is not 32 bytes, return null.
        System.out.println("Key length is not 32 bytes.");
        return null;
    }

    // encryption or decryption of buffer of 8 bytes / data block
    public void convert(byte[] buffer) {
        int[] x = new int[4];

        // First operation:
        // Combining/OR-ing each two consecutive bytes in the buffer array
        x[0] = ((buffer[0] & 0xFF) << 8) | (buffer[1] & 0xFF);
        x[1] = ((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF);
        x[2] = ((buffer[4] & 0xFF) << 8) | (buffer[5] & 0xFF);
        x[3] = ((buffer[6] & 0xFF) << 8) | (buffer[7] & 0xFF);

        int count = 0;
        // Rounds iterating
        for (int i = 0; i < roundsNum; i++) {
            int[] y = new int[4];
            int[] z = new int[4];

            // Second operation:
            // Multiplying first and last element with corresponding sub-key
            // Adding second and third element with corresponding sub-key
            y[0] = MathematicalOperations.multiply(x[0], sub_key[count++]);
            y[1] = MathematicalOperations.add(x[1], sub_key[count++]);
            y[2] = MathematicalOperations.add(x[2], sub_key[count++]);
            y[3] = MathematicalOperations.multiply(x[3], sub_key[count++]);

            // Third operation:
            // Internally XOR-ing and multiplying or adding
            z[0] = MathematicalOperations.multiply(y[0] ^ y[2], sub_key[count++]);
            z[1] = MathematicalOperations.add(y[1] ^ y[3], z[0]);
            z[2] = MathematicalOperations.multiply(z[1], sub_key[count++]);
            z[3] = MathematicalOperations.add(z[0], z[2]);

            // Fourth operation:
            // XOR-ing
            x[0] = y[0] ^ z[2];
            x[1] = y[2] ^ z[2];
            x[2] = y[1] ^ z[3];
            x[3] = y[3] ^ z[3];
        }

        int[] s = new int[4];

        // Last operation:
        // Multiplying and adding operations
        s[0] = MathematicalOperations.multiply(x[0], sub_key[count++]);
        s[1] = MathematicalOperations.add(x[2], sub_key[count++]);
        s[2] = MathematicalOperations.add(x[1], sub_key[count++]);
        s[3] = MathematicalOperations.multiply(x[3], sub_key[count++]);

        // Final block ciphertext
        buffer[0] = (byte) (s[0] >> 8);
        buffer[1] = (byte) s[0];
        buffer[2] = (byte) (s[1] >> 8);
        buffer[3] = (byte) s[1];
        buffer[4] = (byte) (s[2] >> 8);
        buffer[5] = (byte) s[2];
        buffer[6] = (byte) (s[3] >> 8);
        buffer[7] = (byte) s[3];
    }
}
