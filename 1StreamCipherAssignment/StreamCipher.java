import java.util.Random;
import java.io.*;

public class StreamCipher {
    public static void main(String[] args) {
        String key = args[0], infile = args[1], outfile = args[2];
        
        try {
          FileInputStream inpt = new FileInputStream(infile);
          BufferedInputStream reader = new BufferedInputStream(inpt);
          Random rand = new Random(Long.parseLong(key));
          int result;
          int b;

          FileOutputStream myObj = new FileOutputStream(outfile);
          BufferedOutputStream writer = new BufferedOutputStream(myObj);

          while((b = reader.read()) != -1) {
              result = (b^rand.nextInt(256));
              writeToFile(writer, outfile, result);
          }
          writer.close();

          reader.close();

          System.exit(0);
        } catch (FileNotFoundException e) {
          System.out.println(infile + " file does not exist.");
          System.exit(1);
        } catch (IOException e) {
          System.out.println("Input file does not exist.");
          System.exit(1);
        } catch (NumberFormatException e) {
          System.out.println("Key should only contain decimal numbers.");
          System.exit(1);
        }
    }

    public static void writeToFile(BufferedOutputStream writer, String target, int inpt) {
      try {
        writer.write(inpt);
      } catch (FileNotFoundException e) {
        System.out.println("Could not write to " + target);
        System.exit(1);
        return;
      } catch (IOException e) {
        System.out.println("An IO error has occured!");
        System.exit(1);
        return;
      }
    }
}