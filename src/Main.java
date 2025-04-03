import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.print("Enter the message to encrypt: ");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();

        System.out.print("Enter the password: ");
        String password = scanner.nextLine();
        String encryptedPassword = EncryptionAlgorithm.encryptMessage(password);

        String encryptedMessage = EncryptionAlgorithm.encryptMessage(message);

        creareFisier(encryptedPassword, encryptedMessage);

    }

    public static void creareFisier(String parolaCriptata, String mesajCriptat) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("EncryptedMessage.txt"));
            writer.write(parolaCriptata + ' ');
            writer.write(mesajCriptat);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}