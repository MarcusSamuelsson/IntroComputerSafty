import java.util.Scanner;

public class CompareBit {
    public static void main(String[] args) {
        String old = args[0], changed = args[1];
        int diff = 0, tot = 0;

        String o = "", n = "";

        for(int i = 0; i < old.length(); i++){
            if(old.charAt(i) == changed.charAt(i))
                diff++;

            tot++;

            o += old.charAt(i);
            n += changed.charAt(i);
        }

        System.out.println("Old: " + o);
        System.out.println("New: " + n);
        System.out.println("Difference: " + diff);
        System.out.println("Total: " + tot);
    }
}
