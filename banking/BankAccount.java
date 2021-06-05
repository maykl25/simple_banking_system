package banking;

/**
 * class BankAccount provides some basic methods for create bank accounts.
 */

public class BankAccount {
    public long cardNumber;
    public long pinCode;
    public static long balance;

    /**
     * method createAccount() creates an account.
     */

    public void createAccount() {
        cardNumber = createCardNumber();
        pinCode = createPinCode();
    }

    /**
     * method createCardNumber() creates the card number according the Luhn's Algorithm.
     */

    public long createCardNumber() {
        int[] numbers = new int[15];
        int[] primaryNumber = new int[15];
        numbers[0] = 4;
        for (int i = 1; i < 6; i++) {
            numbers[i] = 0;
        }
        int sum = 0;
        int checkSum = 0;
        java.util.Random random = new java.util.Random();
        for (int i = 6; i < 15; i++) {
            numbers[i] = random.nextInt(10);
        }
        System.arraycopy(numbers, 0, primaryNumber, 0, 15);

        for (int i = 0; i < numbers.length; i++) {
            if (i % 2 == 0) {
                numbers[i] = numbers[i] * 2;
            }
        }

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > 9) {
                numbers[i] = numbers[i] - 9;
            }
        }

        for (int everyNumber : numbers) {
            sum = sum + everyNumber;
        }
        while ((sum + checkSum) % 10 != 0) {
            checkSum++;
        }
        String sequence = "";
        for (int everyNumber : primaryNumber) {
            sequence = sequence + everyNumber;
        }
        sequence = sequence + checkSum;
        return Long.parseLong(sequence);
    }

    /**
     * method checkNumber() checks whether or not card number satisfies Luhn's Algorithm.
     */

    public static boolean checkNumber(String number) {
        boolean isRight = false;
        int sum = 0;
        String sequence = number;
        int[] array = new int[15];
        int checkSum = Integer.parseInt(sequence.charAt(15) + "");
        for (int i = 0; i < array.length; i++ ) {
            array[i] = Integer.parseInt(sequence.charAt(i) + "");
            if (i % 2 == 0) {
                array[i] = array[i] * 2;
            }
            if (array[i] > 9) {
                array[i] = array[i] - 9;
            }
        }

        for (int everyNumber : array) {
            sum = sum + everyNumber;
        }
        if ((sum + checkSum) % 10 == 0) {
            isRight = true;
        }

        return isRight;
    }

    /**
     * method createPinCode() creates pin code.
     */

    public long createPinCode() {
        java.util.Random random = new java.util.Random();
        int lower = 1000;
        int upper = 9999;
        int range = (upper - lower) + 1;
        int number = random.nextInt(range) + lower;

        return (long) number;
    }
}
