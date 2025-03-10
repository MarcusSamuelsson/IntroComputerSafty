package com.iv1013.cryptohash;

import java.security.*;

public class SampleDigest {
    public static void main(String[] args) {

        String digestAlgorithm = "SHA-256";
        String textEncoding = "UTF-8";
        String inputText = "Test message 1";
        String inputText2 = "Test message 2";

        try {
            MessageDigest md = MessageDigest.getInstance(digestAlgorithm);

            byte[] inputBytes = inputText.getBytes(textEncoding);
            md.update(inputBytes);
            byte[] digest = md.digest();

            printDigest(inputText, md.getAlgorithm(), digest);

            byte[] inputBytes2 = inputText2.getBytes(textEncoding);
            md.update(inputBytes2);
            byte[] digest2 = md.digest();

            printDigest(inputText2, md.getAlgorithm(), digest2);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorithm \"" + digestAlgorithm  + "\" is not available");
        } catch (Exception e) {
            System.out.println("Exception "+e);
        }
    }

    public static void printDigest(String inputText, String algorithm, byte[] digest) {
        System.out.println("Digest for the message \"" + inputText +"\", using " + algorithm + " is:");
        for (int i=0; i<digest.length; i++)
            System.out.format("%02x", digest[i]&0xff);
        System.out.println();
    }
}
