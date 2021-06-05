package banking;

import java.sql.*;

/**
 * class DataBase provides set of useful methods for work with databases.
 */

public class DataBase {

    /**
     * method create() creates a database.
     */

    public static void create(String fileName) {
        String url = "jdbc:sqlite:" + fileName;

        try(Connection con = DriverManager.getConnection(url)) {
            if (con != null) {
                DatabaseMetaData meta = con.getMetaData();
                System.out.println(meta.getDriverName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * method table() creates a table.
     */

    public static void table() {
        String url = "jdbc:sqlite:card.s3db";

        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + " id INTEGER PRIMARY KEY,\n"
                + " number TEXT NOT NULL,\n"
                + " pin TEXT NOT NULL,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method connect() creates a connection.
     */

    private Connection connect() {
        String url = "jdbc:sqlite:card.s3db";
        Connection con = null;

        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    /**
     * method insert() allows to insert data into database.
     */

    public  void insert(String number, String pin) {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setString(1, number);
            prepStmt.setString(2, pin);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method select() provides a select action.
     */

    public void select(String number, String pin) {
        String sql = "SELECT number, pin " + "FROM card WHERE number = ? AND pin = ?";

        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {

            prepStmt.setString(1, number);
            prepStmt.setString(2, pin);

            ResultSet rs = prepStmt.executeQuery();

            if(rs.next()) {
                prepStmt.close();
                rs.close();
                System.out.println("You have succesfully logged in!");
                Main.accountMenu(number, pin);
            } else {
                System.out.println("Wrong number card or PIN!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method checkExistence() checks whether or not the table contains some data.
     */

    public boolean checkExistence(String number) {
        boolean isExist = false;
        String sql = "SELECT number " + "FROM card WHERE number = ?";
        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setString(1, number);

            ResultSet rs = prepStmt.executeQuery();

            if (rs.next()) {
                isExist = true;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return isExist;
    }

    /**
     * method delete() allows to delete some data from database.
     */

    public void delete(String number, String pin) {
        String sql = "DELETE FROM card WHERE number = ? AND pin = ?";

        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {

            prepStmt.setString(1, number);
            prepStmt.setString(2, pin);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method showBalance() allows to check the  balance from given credit card.
     */

    public void showBalance(String number, String pin) {
        String sql = "SELECT balance " + "FROM card WHERE number = ? AND pin = ?";

        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setString(1, number);
            prepStmt.setString(2, pin);


            ResultSet rs = prepStmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt("balance"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method getMoney() allows to take money from chosen account.
     */

    public int getMoney(String number) {
        int money = 0;
        String sql = "SELECT balance " + "FROM card WHERE number = ?";
        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setString(1, number);

            ResultSet rs = prepStmt.executeQuery();

            while(rs.next()) {
                money = rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return money;
    }

    /**
     * method addIncome() allows to renew amount of money on account.
     */

    public void addIncome(String number, String pin, int balance) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ? AND pin = ?";

        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setInt(1, balance);
            prepStmt.setString(2, number);
            prepStmt.setString(3, pin);

            prepStmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * method newOutBalance() shows amount of money on account after transfer.
     */

    public void newOutBalance(String number, int balance) {
        String sql = "UPDATE card SET balance = balance - ? WHERE number = ?";

        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setInt(1, balance);
            prepStmt.setString(2, number);

            prepStmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method newInBalance() shows amount of money on account after transfer.
     */

    public void newInBalance(String number, int balance) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection con = this.connect();
             PreparedStatement prepStmt = con.prepareStatement(sql)) {
            prepStmt.setInt(1, balance);
            prepStmt.setString(2, number);

            prepStmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method doTransfer() allows to transfer money between two accounts.
     */

    public void doTransfer(String number, String receiverCard) {
        if (!BankAccount.checkNumber(receiverCard)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        } else if (number.equals(receiverCard + "")) {
            System.out.println("You can't transfer money to the same account!");
        } else if (!checkExistence(receiverCard)){
            System.out.println("Such a card does not exist");
        } else {
            System.out.println("Enter how much money you want to transfer: ");
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            int moneyToTransfer = scanner.nextInt();
            if (moneyToTransfer > this.getMoney(number)) {
                System.out.println("You dont have enough money!");
            } else {
                this.newOutBalance(number, moneyToTransfer);
                this.newInBalance(receiverCard, moneyToTransfer);
                System.out.println("Success!");
            }
        }
    }
}
