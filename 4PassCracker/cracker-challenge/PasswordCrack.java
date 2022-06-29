import java.io.*;
import java.util.ArrayList;

public class PasswordCrack {
    static ArrayList<String> passwords = new ArrayList<String>();
    static ArrayList<String> salt = new ArrayList<String>();
    static ArrayList<String> name = new ArrayList<String>();
    static ArrayList<String> mangleNames = new ArrayList<String>();
    static ArrayList<String> dictonary = new ArrayList<String>();

    public static void main(String[] args) {

        readDictionary(args[0]);
        readPasswords(args[1]);

        checkNames(0);
        checkDictionary(0);

        //System.out.println("first done");

        checkNames(1);
        checkDictionary(1);
        
        //System.out.println("sec done");

        checkNames(2);
        checkDictionary(2);

        //System.out.println("third done");

        checkNames(3);
        checkDictionary(3);
        
        //wordListReader.readLine();
    }

    private static void checkNames(int mangle) {
        String[] nameSplit;

        if(mangle == 3){
            for(int i = 0; i < passwords.size(); i++) {
                for(int j = 0; j < name.size(); j++) {
                    for(int m = 0; m < 136; m++) {
                        String word = mangle(m, name.get(j));

                        if(word.length() >= 8 && m >= 23 && m < 34) {m = 33; continue;}
                        if(word.length() >= 8 && m >= 85) {break;}

                        if(i >= passwords.size()) i = 0;
                        
                        if(jcrypt.crypt(salt.get(i), word).equals(passwords.get(i))) {
                            System.out.println(word);
                                passwords.remove(i);
                                salt.remove(i);
                                name.remove(i);
                                break;
                        }
                    }
                }
            }
        } else if(mangle == 2){
            name = new ArrayList<String>();
            for(int i = 0; i < passwords.size(); i++) {
                for(int j = 0; j < mangleNames.size(); j++) {
                    for(int m = 0; m < 136; m++) {
                        String word = mangle(m, mangleNames.get(j));

                        if(word.length() >= 8 && m >= 23 && m < 34) {m = 33; continue;}
                        if(word.length() >= 8 && m >= 85) {break;}

                        name.add(word);

                        if(i >= passwords.size()) i = 0;
                        
                        if(jcrypt.crypt(salt.get(i), word).equals(passwords.get(i))) {
                            System.out.println(word);
                                passwords.remove(i);
                                salt.remove(i);
                                name.remove(i);
                                break;
                        }
                    }
                }
            }
        } else if(mangle == 1){
            for(int i = 0; i < passwords.size(); i++) {
                nameSplit = name.get(i).split(" ");

                for(int j = 0; j < nameSplit.length; j++) {
                    for(int m = 0; m < 136; m++) {
                        String word = mangle(m, nameSplit[j]);

                        if(word.length() >= 8 && m >= 23 && m < 34) {m = 33; continue;}
                        if(word.length() >= 8 && m >= 85) {break;}

                        mangleNames.add(word);

                        if(i >= passwords.size()) i = 0;
                        
                        if(jcrypt.crypt(salt.get(i), word).equals(passwords.get(i))) {
                            System.out.println(word);
                                passwords.remove(i);
                                salt.remove(i);
                                name.remove(i);
                                break;
                        }
                    }
                }
            }
        } else {
            for(int i = 0; i < passwords.size(); i++) {
                nameSplit = name.get(i).split(" ");

                for(int j = 0; j < nameSplit.length; j++) {
                    if(i >= passwords.size()) i = 0;
                    if(jcrypt.crypt(salt.get(i), nameSplit[j]).equals(passwords.get(i))) {
                        System.out.println(nameSplit[j]);
                            passwords.remove(i);
                            salt.remove(i);
                            name.remove(i);
                            break;
                    }
                }
            }
        }
    }

    private static void checkDictionary(int mangle) {
        if(mangle == 3){
            for(int j = 0; j < dictonary.size(); j++) {
                for(int i = 0; i < passwords.size(); i++) {
                    for(int m = 0; m < 136; m++) {
                        for(int n = 0; n < 136; n++) {
                            for(int b = 0; b < 136; b++) {
                                String word = mangle(m, mangle(n, mangle(b, dictonary.get(j))));
                                
                                if(word.length() >= 8 && m >= 23 && m < 34) {m = 33; continue;}
                                if(word.length() >= 8 && m >= 85) {break;}

                                if(i >= passwords.size()) i = 0;
                                
                                if(jcrypt.crypt(salt.get(i), word).equals(passwords.get(i))) {
                                    System.out.println(word);
                                        passwords.remove(i);
                                        salt.remove(i);
                                        name.remove(i);
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        } else if(mangle == 2){
            for(int j = 0; j < dictonary.size(); j++) {
                for(int i = 0; i < passwords.size(); i++) {
                    for(int m = 0; m < 136; m++) {
                        for(int n = 0; n < 136; n++) {
                            String word = mangle(m, mangle(n, dictonary.get(j)));
                            
                            if(word.length() >= 8 && m >= 23 && m < 34) {m = 33; continue;}
                            if(word.length() >= 8 && m >= 85) {break;}

                            if(i >= passwords.size()) i = 0;
                            
                            if(jcrypt.crypt(salt.get(i), word).equals(passwords.get(i))) {
                                System.out.println(word);
                                    passwords.remove(i);
                                    salt.remove(i);
                                    name.remove(i);
                                    break;
                            }
                        }
                    }
                }
            }
        } else if(mangle == 1){
            for(int j = 0; j < dictonary.size(); j++) {
                for(int i = 0; i < passwords.size(); i++) {
                    for(int m = 0; m < 136; m++) {
                        String word = mangle(m, dictonary.get(j));

                        if(word.length() >= 8 && m >= 23 && m < 34) {m = 33; continue;}
                        if(word.length() >= 8 && m >= 85) {break;}
                        
                        if(i >= passwords.size()) i = 0;

                        if(jcrypt.crypt(salt.get(i), word).equals(passwords.get(i))) {
                            System.out.println(word);
                                passwords.remove(i);
                                salt.remove(i);
                                name.remove(i);
                                break;
                        }
                    }
                }
            }
        } else {
            for(int j = 0; j < dictonary.size(); j++) {
                for(int i = 0; i < passwords.size(); i++) {
                    if(i >= passwords.size()) i = 0;
                    if(jcrypt.crypt(salt.get(i), dictonary.get(j)).equals(passwords.get(i))) {
                        System.out.println(dictonary.get(j));
                            passwords.remove(i);
                            salt.remove(i);
                            name.remove(i);
                            break;
                    }
                }
            }
        }
    }

    private static String mangle(int id, String word) {
        StringBuilder sb = new StringBuilder();

        sb.append(word);

        if(id == 0  && word.length() > 0)
            return word.substring(1);
        else if(id == 1 && word.length() > 0)
            return word.substring(0, word.length()-1);
        else if(id == 2)
            return sb.reverse().toString();
        else if(id == 3)
            return word + word;
        else if(id == 4)
            return word + sb.reverse().toString();
        else if(id == 5)
            return sb.reverse().toString() + word;
        else if(id == 6)
            return word.toUpperCase();
        else if(id == 7)
            return word.toLowerCase();
        else if(id == 8 && word.length() > 0)
            return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
        else if(id == 9 && word.length() > 0)
            return Character.toLowerCase(word.charAt(0)) + word.substring(1).toUpperCase();
        else if(id == 10) {
            String newWord = "";
            for(int i = 0; i < word.length(); i++) {
                if(i % 2 == 0)
                    newWord += Character.toUpperCase(word.charAt(i));
                else
                    newWord += Character.toLowerCase(word.charAt(i));
            }
            return newWord;
        } else if(id == 11) {
            String newWord = "";
            for(int i = 0; i < word.length(); i++) {
                if(i % 2 == 0)
                    newWord += Character.toLowerCase(word.charAt(i));
                else
                    newWord += Character.toUpperCase(word.charAt(i));
            }
            return newWord;
        } else if(id >= 12 && id <= 22) {
                return (id-12) + word;
        } else if(id >= 23 && id <= 33) {
            return word + (id-23);
        } else if(id >= 34 && id <= 85) {
            if(id < 60)
                return ((char)(id+31)) + word;
            else
                return ((char)(id+37)) + word;
        } else if(id >= 85 && id <= 135) {
            if(id < 110)
                return  word + ((char)(id+31));
            else
                return  word + ((char)(id+37));
        }

        return "";
    }

    private static void readDictionary (String fileToRead) {
        try{    
            FileReader fileReader = new FileReader(fileToRead);
            BufferedReader reader = new BufferedReader(fileReader);

            String word;

            while((word = reader.readLine()) != null) {
                dictonary.add(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Input file does not exist.");
            System.exit(1);
        }
    }

    private static void readPasswords (String fileToRead) {
        try{
            FileReader fileReader = new FileReader(fileToRead);
            BufferedReader reader = new BufferedReader(fileReader);

            String pass;

            while((pass = reader.readLine()) != null) {
                passwords.add(pass.split(":")[1]);
                salt.add(pass.split(":")[1].substring(0, 2));
                name.add(pass.split(":")[4]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Input file does not exist.");
            System.exit(1);
        }
    }
}