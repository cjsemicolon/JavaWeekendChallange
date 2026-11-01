package safeBank.Services;

import safeBank.BankAccount;

public class AccountService {

    public BankAccount createAccount(String name, double openingBalance) {
        return new BankAccount(name, openingBalance);
    }
}
