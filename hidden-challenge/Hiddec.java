import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.print.DocFlavor.STRING;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;

public class Hiddec {
    public static SecretKeySpec key;
    public static byte[] keyBytes;
    public static byte[] md5Key;

    public static void main(String[] args) {
        String k = "", ctr = "", input = "", output = "";

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
            else {
                System.out.println("not a command!");
            }
        }

        System.out.println("start ctr: " + ctr);

        sKey(k);

        md5Key = md5(keyBytes);
        printByteArray(md5Key, true);
        
        findBlob(decrypt(getEncryptedText(input), ctr), output);
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

    public static byte[] ctrCalc(String temp) {

        byte[] ctr = new byte[temp.length() / 2];
 
        for (int i = 0; i < keyBytes.length; i++) {
            ctr[i] = (byte)Integer.parseInt(temp.substring(i * 2, (i * 2) + 2), 16);
        }

        System.out.println("ctr leng: " + ctr.length);
        System.out.println("ctr hex: " + hex(ctr));

        return ctr;
    }

    public static byte[] decrypt(byte[] encryptedBytes, String ctr) {
        try {
            Cipher cipher;

            if(ctr.equals("")) {
                cipher = Cipher.getInstance("AES/ECB/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                System.out.println("test");
                cipher = Cipher.getInstance("AES/CTR/NoPadding");
                byte[] iv = new byte[128 / 8];
                byte[] nonce = ctrCalc(ctr);
                System.arraycopy(nonce, 0, iv, 0, nonce.length);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            }     

            
            byte[] decryptedBytes = new byte[encryptedBytes.length];
            byte[] temp = new byte[16];

            for (int i = 0; i < (encryptedBytes.length/16); i++) {
                for (int j = 0; j < temp.length; j++) {
                    temp[j] = encryptedBytes[j+(16*i)];
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

    public static void findBlob (byte[] db, String output) {
        int match = 0, tot = 0;
        int[] start = new int[2], end = new int[2];

        for (int i = 0; i < db.length; i++) {
            if(match == md5Key.length) {
                System.out.println("Match at " + (i-md5Key.length) + "\nEnd at " + i);

                start[tot] = i-md5Key.length;
                end[tot] = i;

                if(tot == 1)
                    break;
                    

                tot++;

                match = 0;
                i = i+md5Key.length;
            }

            if(match > 0)
                System.out.println("yes");

            if(db[i] == md5Key[match])
                match++;
            else if(db[i] != md5Key[match] && match > 0) {
                match = 0;
                System.out.println("no");
            }
        }

        byte[] data = new byte[start[1] - end[0]];

        for (int i = 0; i < data.length; i++) {
            data[i] = db[i+end[0]];
        }

        byte[] plainTextMD5 = md5(data);

        for (int i = 0; i < plainTextMD5.length; i++) {
            if(match == plainTextMD5.length) {
                System.out.println("Matching data and data\'");
                break;
            }

            if(plainTextMD5[i] == db[i+end[1]])
                match++;
            else {
                System.out.println("does not match");
                break;
            }
        }

        printByteArray(data, false);
        printToFile(data, output);

        
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

    public static byte[] getEncryptedText(String inpt) {
        try {
            int e;
            FileInputStream file = new FileInputStream(inpt);
            ArrayList<Integer> temp = new ArrayList<Integer>();

            while((e = file.read()) != -1) {
                temp.add(e);
            }

            file.close();

            byte[] arr = new byte[temp.size()];

            for (int i = 0; i < arr.length; i++) {
                arr[i] = (byte)(int)(temp.get(i));
            }
            //System.out.println(txt);

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