
package safeBank;

import safeBank.BankAccount;
import safeBank.InsufficientFundsException;
import safeBank.InvalidAmountException;
import safeBank.Services.AccountDisplayService;
import safeBank.Services.AccountService;
import safeBank.Services.Deposit;
import safeBank.Services.Withdrawal;
import safeBank.WithdrawalLimitExceededException;

import java.util.Scanner;

public class SafeBankApp {

    public static void main(String[] args) {

        AccountService accountService = new AccountService();
        Deposit depositService = new Deposit();
        Withdrawal withdrawalService = new Withdrawal();
        AccountDisplayService displayService = new AccountDisplayService();

        try (Scanner scanner = new Scanner(System.in)) {

            BankAccount account = null;

            try {
                System.out.print("Account holder name: ");
                String name = scanner.nextLine();

                System.out.print("Opening balance (NGN): ");
                double balance = Double.parseDouble(scanner.nextLine().trim());

                account = accountService.createAccount(name, balance);

                System.out.println(
                        "Account created for " + account.getAccountHolder() + "."
                );

            } catch (NumberFormatException e) {
                System.out.println("Opening balance must be numeric.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

            if (account == null) {
                return;
            }

            boolean running = true;

            while (running) {
                System.out.println(
                        "\n1. Deposit  2. Withdraw  3. Balance/history  0. Exit"
                );

                System.out.print("Choice: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> {
                        try {
                            System.out.print("Amount: ");
                            double amount = Double.parseDouble(
                                    scanner.nextLine().trim()
                            );

                            System.out.println(
                                    depositService.performDeposit(account, amount)
                            );

                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a numeric amount.");
                        } catch (InvalidAmountException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    case "2" -> {
                        try {
                            System.out.print("Amount: ");
                            double amount = Double.parseDouble(
                                    scanner.nextLine().trim()
                            );

                            System.out.println(
                                    withdrawalService.performWithdrawal(account, amount)
                            );

                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a numeric amount.");
                        } catch (
                                InvalidAmountException |
                                InsufficientFundsException |
                                WithdrawalLimitExceededException e
                        ) {
                            System.out.println(e.getMessage());
                        }
                    }

                    case "3" ->
                            System.out.print(displayService.showAccount(account));

                    case "0" -> running = false;

                    default ->
                            System.out.println(
                                    "Invalid menu selection. Choose 0, 1, 2, or 3."
                            );
                }
            }

            System.out.println("Thank you for using SafeBank.");
        }
    }
}
