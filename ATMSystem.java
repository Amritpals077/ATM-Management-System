import java.io.*;
import java.util.Scanner;

public class ATMSystem {
    private String username;
    private int pin;
    private double balance;
    private String accountType;
    private StringBuilder transactionHistory;
    private int depositCount;

    public ATMSystem() {
        balance = 0;
        transactionHistory = new StringBuilder();
        depositCount = 0;
    }

    public void setUserData(Scanner sc) {
        System.out.print("Enter customer name: ");
        username = sc.nextLine();
        System.out.print("Enter pin: ");
        pin = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Select account type (Saving/Current): ");
        accountType = sc.nextLine();
        transactionHistory.append("Account created.\n");
    }

    public void displayBalance() {
        System.out.println("Your current balance is: " + balance);
    }

    public boolean verifyPin(int enteredPin) {
        return enteredPin == pin;
    }

    public void deposit(Scanner sc) {
        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();
        balance += amount;
        depositCount++;
        transactionHistory.append("Deposited: ").append(amount).append("\n");
        System.out.println("Amount deposited successfully!");
    }

    public void withdraw(Scanner sc) {
        System.out.println("Note: You can only withdraw amounts in multiples of 100 or 500.");
        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();

        if ((int) amount % 100 == 0 || (int) amount % 500 == 0) {
            if (amount <= balance) {
                balance -= amount;
                transactionHistory.append("Withdrew: ").append(amount).append("\n");
                System.out.println("Amount withdrawn successfully!");
            } else {
                System.out.println("Insufficient balance!");
            }
        } else {
            System.out.println("Please enter amount in multiples of 100 or 500.");
        }
    }

    public void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("atm_data.txt", true))) {
            bw.write(username + "\n" + pin + "\n" + balance + "\n" + accountType + "\n" + transactionHistory + "\n");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader("atm_data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String u = line;
                int p = Integer.parseInt(br.readLine());
                double b = Double.parseDouble(br.readLine());
                String accType = br.readLine();
                String transHist = br.readLine();

                if (u.equals(username) && p == pin) {
                    balance = b;
                    accountType = accType;
                    transactionHistory = new StringBuilder(transHist);
                    break;
                }
            }
        } catch (IOException e) {
            // File might not exist initially
        }
    }

    public void printReceipt(Scanner sc) {
        System.out.print("Do you want a receipt? (y/n): ");
        char choice = sc.next().charAt(0);
        if (choice == 'y' || choice == 'Y') {
            System.out.println("\nReceipt:");
            System.out.println("============================");
            System.out.println("Username: " + username);
            System.out.println("Account Type: " + accountType);
            System.out.println("Current Balance: " + balance);
            System.out.println("Transaction History:");
            System.out.println(transactionHistory);
            System.out.println("----------------------------");
            System.out.println("Total Deposit Transactions: " + depositCount);
            System.out.println("============================");
            System.out.println("Thank you for using ATM!");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ATMSystem atm = new ATMSystem();

        System.out.println("Welcome to ATM!");
        atm.setUserData(sc);
        atm.loadData();

        System.out.print("Enter your pin to continue: ");
        int enteredPin = sc.nextInt();

        if (!atm.verifyPin(enteredPin)) {
            System.out.println("Invalid pin. Access denied.");
            return;
        }

        int choice;
        do {
            System.out.println("\nATM Menu:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    atm.displayBalance();
                    break;
                case 2:
                    atm.deposit(sc);
                    break;
                case 3:
                    atm.withdraw(sc);
                    break;
                case 4:
                    atm.saveData();
                    atm.printReceipt(sc);
                    System.out.println("Thank you for using ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);

        sc.close();
    }
}