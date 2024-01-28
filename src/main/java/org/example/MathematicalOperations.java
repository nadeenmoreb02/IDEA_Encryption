package org.example;

public class MathematicalOperations {

    // 16-byte Addition
    public static int add(int a, int b) {
        int result = a + b;
        return result & 0xFFFF;
    }

    // 16-byte Multiplication
    public static int multiply(int a, int b) {
        long result = (long) a * b;
        if (result != 0) {
            result = result % 0x10001;
            return (int) result & 0xFFFF; }
        else {
            result = (1 - a - b);
            return (int) result & 0xFFFF;
        }
    }

    // 16-byte Additive Inverse
    public static int  additiveInverse(int a) {
        // convert a to 0xFFFF format
        a = a & 0xFFFF;

        // If a is 0, its additive inverse is also 0
        if (a == 0) {
            return 0;
        }

        // The additive inverse of a is 0x10000 - a
        return 0x10000 - a;
    }

    // 16-byte Multiplicative Inverse
    public static int multiplicativeInverse(int a) {
        if (a <= 1) {
            return a;
        }

        int modulus = 0x10001;
        //coefficients
        int c0 = 1;
        int c1 = 0;

        while (a > 1) {
            // quotient
            int quotient = a / modulus;

            // remainder
            int temp = modulus;
            modulus = a % modulus;
            a = temp;

            temp = c1;
            c1 = c0 - quotient * c1;
            c0 = temp;
        }

        if (c0 < 0) {
            c0 += 0x10001;
        }

        return c0;
    }


}

