package banking;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * class Main allows to interact with bank accounts using database.
 */

public class Main {
    public static ArrayList<BankAccount> users = new ArrayList<>();
    public static int result = 2;

    public static void main(String[] args) {
        DataBase.create(args[1]);
        DataBase.table();
        mainMenu();
    }

    public static void mainMenu() {
        String option;
        do {
            System.out.println("1. Create an account" + '\n' + "2. Log into account" + '\n' + "0. Exit");
            Scanner scanner = new Scanner(System.in);
            option = scanner.nextLine();
            mainOption(option);
        } while (result != 0);
    }

    public static void mainOption(String option) {
        switch (option) {
            case "1":
                BankAccount acc = new BankAccount();
                DataBase base = new DataBase();
                acc.createAccount();
                base.insert(acc.cardNumber + "", acc.pinCode + "");
                users.add(acc);
                System.out.println("Your card has been created");
                System.out.println("Your card number:");
                System.out.println(acc.cardNumber);
                System.out.println("Your card PIN:");
                System.out.println(acc.pinCode);
                break;
            case "2":
                openBase();
                break;
            case "0":
                result = 0;
                System.out.println();
                System.out.println("Bye!");
        }
    }


    public static void openBase() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN");
        String pin = scanner.nextLine();

        DataBase base = new DataBase();
        base.select(number, pin);
    }

    public static void accountMenu(String number, String pin) {
        Scanner scanner = new Scanner(System.in);
        String option;
        do {
            System.out.println("1. Balance" + '\n' + "2. Add income" + '\n' + "3. Do transfer" + '\n' + "4. Close account" + '\n' + "5. Log out" + '\n' + "0. Exit");
            option = scanner.nextLine();
            switch (option) {
                case "1":
                    DataBase base = new DataBase();
                    base.showBalance(number, pin);
                    break;
                case "2":
                    System.out.println("Enter income:");
                    int income = scanner.nextInt();
                    DataBase base2 = new DataBase();
                    base2.addIncome(number, pin, income);
                    break;
                case "3":
                    System.out.println("Transfer");
                    System.out.println("Enter card number: ");
                    String receiverCard = scanner.nextLine();
                    DataBase base3 = new DataBase();
                    base3.doTransfer(number, receiverCard);
                    break;
                case "4":
                    DataBase base4 = new DataBase();
                    base4.delete(number, pin);
                    System.out.println("The account has been closed!");
                    Main.mainMenu();
                    break;
                case "5":
                    System.out.println("You have successfully logged out!");
                    Main.mainMenu();
                    break;
                case "0":
                    result = 0;
                    System.out.println();
                    System.out.println("Bye!");
                    break;
            }
        } while (result == 2);
    }
}
