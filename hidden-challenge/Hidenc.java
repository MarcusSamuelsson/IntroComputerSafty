import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.print.DocFlavor.STRING;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;

public class Hidenc {
    public static SecretKeySpec key;
    public static byte[] keyBytes;
    public static byte[] md5Key;
    public static byte[] md5Plain;
    
    public static void main(String[] args) {
        String k = "", ctr = "", input = "", output = "", offset = "", template = "", size = "";

        String[] temp;
        for(int i = 0; i < args.length; i++) {
            temp = args[i].split("=");

            if(temp[0].equals("--key"))
                k = temp[1];
            else if(temp[0].equals("--ctr"))
                ctr = temp[1];
            else if(temp[0].equals("--input"))
                input = temp[1];
            else if(temp[0].equals("--output"))
                output = temp[1];
            else if(temp[0].equals("--offset"))
                offset = temp[1];
            else if(temp[0].equals("--template"))
                template = temp[1];
            else if(temp[0].equals("--size"))
                size = temp[1];
            else {
                System.out.println("not a command!");
            }
        }

        sKey(k);
        md5Key = md5(keyBytes);
        createHidden(getPlainText(input), Integer.parseInt(size), Integer.parseInt(offset), output);
    }

    public static void sKey(String temp) {

        keyBytes = new byte[temp.length() / 2];
 
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = (byte)Integer.parseInt(temp.substring(i * 2, (i * 2) + 2), 16);
        }

        System.out.println(keyBytes.length);
        System.out.println(hex(keyBytes));

        key = new SecretKeySpec(keyBytes, "AES");
    }

    public static byte[] encrypt(byte[] plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] decryptedBytes = new byte[plainText.length];
            byte[] temp = new byte[16];

            for (int i = 0; i < (plainText.length/16); i++) {
                for (int j = 0; j < temp.length; j++) {
                    temp[j] = plainText[j+(16*i)];
                }
                
                temp = cipher.doFinal(temp);

                for (int j = 0; j < temp.length; j++) {
                    decryptedBytes[j + (16*i)] = temp[j];
                }
            }

            System.out.println(decryptedBytes.length);
            printByteArray(decryptedBytes, true);
            return decryptedBytes;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void createHidden(byte[] encrypted, int size, int offset, String output) {
        byte[] hidden = new byte[size];
        Random rd = new Random();
        rd.nextBytes(hidden);

        for (int i = offset; i < hidden.length; i++) {  
            if(i-offset < md5Key.length) {
                System.out.println("1: " + (i-offset));
                hidden[i] = md5Key[i-offset];
            }
            else if(i-offset-md5Key.length < encrypted.length) {
                System.out.println("2: " + (i-offset-md5Key.length));
                hidden[i] = encrypted[i-offset-md5Key.length];
            }
            else if(i-offset-md5Key.length-encrypted.length < md5Key.length) {
                System.out.println("3: " + (i-offset-md5Key.length-encrypted.length));
                hidden[i] = md5Key[i-offset-md5Key.length-encrypted.length];
            }
            else if(i-offset-md5Key.length-encrypted.length-md5Key.length < md5Plain.length) {
                System.out.println("4: " + (i-offset-md5Key.length-encrypted.length-md5Key.length));
                hidden[i] = md5Plain[i-offset-md5Key.length-encrypted.length-md5Key.length];
            } 
            else {
                break;
            }
        }

        printToFile(encrypt(hidden), output);
    }

    public static byte[] md5 (byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorithm \"MD5\" is not available");
        } catch (Exception e) {
            System.out.println("Exception "+e);
        }

        return null;
    }

    public static byte[] getPlainText(String inpt) {
        try {
            int p;
            FileInputStream file = new FileInputStream(inpt);
            ArrayList<Integer> temp = new ArrayList<Integer>();

            while((p = file.read()) != -1) {
                temp.add(p);
            }

            file.close();

            byte[] arr = new byte[temp.size()];

            for (int i = 0; i < arr.length; i++) {
                arr[i] = (byte)(int)(temp.get(i));
            }
            //System.out.println(txt);

            md5Plain = md5(arr);

            return arr;
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
            System.exit(1);
        } catch (Exception e) {
            
        }
        
        return null;
    }

    public static void printToFile (byte[] ba, String output) {
        try {
            FileOutputStream file = new FileOutputStream(output);
            file.write(ba);
            file.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public static void printByteArray(byte[] ba, boolean hexa) {
        String str = new String(ba);

        if(hexa)    
            System.out.print(hex(ba));
        else
            System.out.print(str);
        
        
        System.out.println();
    }

    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }
}
