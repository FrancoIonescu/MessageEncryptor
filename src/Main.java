import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.print("Enter the message to encrypt: ");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();

        System.out.print("Enter the password for encryption: ");
        String passwordEncrypt = scanner.nextLine();
        String encryptedPassword = EncryptionAlgorithm.encryptMessage(passwordEncrypt);

        String encryptedMessage = EncryptionAlgorithm.encryptMessage(message);

        EncryptionAlgorithm.createFile(encryptedPassword, encryptedMessage);

        System.out.print("Enter the password for decryption: ");
        String passwordDecrypt = scanner.nextLine();
        EncryptionAlgorithm.requestDecryptMessage(passwordDecrypt, "EncryptedMessage.txt");

    }
}