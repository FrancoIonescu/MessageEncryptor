import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.print("Introduceti mesajul de criptat: ");
        Scanner scanner = new Scanner(System.in);
        String mesaj = scanner.nextLine();

        String mesajCriptat = AlgoritmCriptare.cripteazaMesaj(mesaj);
        System.out.println("Mesajul criptat este: " + mesajCriptat);

    }
}