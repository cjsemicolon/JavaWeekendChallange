package safeBank.Services;

import safeBank.BankAccount;
import safeBank.InvalidAmountException;

public class Deposit {

    public String performDeposit(BankAccount account, double amount)
            throws InvalidAmountException {

        account.deposit(amount);

        return "Deposit successful.";

    }
}

