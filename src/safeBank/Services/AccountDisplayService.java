package safeBank.Services;

import safeBank.BankAccount;

public class AccountDisplayService {

    public String showAccount(BankAccount account) {
        StringBuilder result = new StringBuilder();

        result.append(String.format(
                "Balance: NGN %,.2f%n",
                account.getBalance()
        ));

        result.append("Successful transactions: ")
                .append(account.getTransactionHistory().size())
                .append(System.lineSeparator());

        for (String transaction : account.getTransactionHistory()) {
            result.append("- ")
                    .append(transaction)
                    .append(System.lineSeparator());
        }

        return result.toString();
    }
}


