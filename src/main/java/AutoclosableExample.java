import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AutoclosableExample {

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("/dev");
        try {
         fis.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
            }
        }

        try (FileInputStream fis2 = new FileInputStream("/dev")) {
            fis2.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
