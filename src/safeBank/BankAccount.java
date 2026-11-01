package safeBank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccount {
    public static final double WITHDRAWAL_LIMIT = 100_000.00;
    private final String accountHolder;
    private double balance;
    private final List<String> transactionHistory = new ArrayList<>();

    public BankAccount(String accountHolder, double openingBalance) {
        if (accountHolder == null || accountHolder.isBlank()) throw new IllegalArgumentException("Account holder name cannot be blank.");
        if (!Double.isFinite(openingBalance) || openingBalance < 0) throw new IllegalArgumentException("Opening balance cannot be negative.");
        this.accountHolder = accountHolder.trim();
        this.balance = openingBalance;
    }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }
    public List<String> getTransactionHistory() { return Collections.unmodifiableList(transactionHistory); }

    public void deposit(double amount) throws InvalidAmountException {
        validateAmount(amount);
        balance += amount;
        transactionHistory.add(String.format("Deposit: NGN %.2f", amount));
    }
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException, WithdrawalLimitExceededException {
        validateAmount(amount);
        if (amount > WITHDRAWAL_LIMIT) throw new WithdrawalLimitExceededException("Withdrawal exceeds the single-withdrawal limit of NGN 100,000.00.");
        if (amount > balance) throw new InsufficientFundsException("Insufficient funds. Available balance: NGN " + String.format("%.2f", balance));
        balance -= amount;
        transactionHistory.add(String.format("Withdrawal: NGN %.2f", amount));
    }
    private void validateAmount(double amount) throws InvalidAmountException {
        if (!Double.isFinite(amount) || amount <= 0) throw new InvalidAmountException("Amount must be a number greater than zero.");
    }
}