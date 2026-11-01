package safeBank.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import safeBank.BankAccount;
import safeBank.InsufficientFundsException;
import safeBank.InvalidAmountException;
import safeBank.WithdrawalLimitExceededException;

import static org.junit.jupiter.api.Assertions.*;

public class AccountDisplayServiceTest {

    private AccountDisplayService displayService;
    private BankAccount account;

    @BeforeEach
    public void setUp() {
        displayService = new AccountDisplayService();
        account = new BankAccount("Praise", 50000);
    }

    @Test
    public void testShowAccountWithNoTransactions() {
        String result = displayService.showAccount(account);

        assertTrue(result.contains("Balance: NGN 50,000.00"));
        assertTrue(result.contains("Successful transactions: 0"));
    }

    @Test
    public void testShowAccountWithOneTransaction()
            throws InvalidAmountException {

        account.deposit(10000);

        String result = displayService.showAccount(account);

        assertTrue(result.contains("Balance: NGN 60,000.00"));
        assertTrue(result.contains("Successful transactions: 1"));
        assertTrue(result.contains(account.getTransactionHistory().get(0)));
    }

    @Test
    public void testShowAccountWithMultipleTransactions()
            throws InvalidAmountException,
            InsufficientFundsException,
            WithdrawalLimitExceededException {

        account.deposit(10000);
        account.withdraw(5000);
        account.deposit(2000);

        String result = displayService.showAccount(account);

        assertTrue(result.contains("Balance: NGN 57,000.00"));
        assertTrue(result.contains("Successful transactions: 3"));

        for (String transaction : account.getTransactionHistory()) {
            assertTrue(result.contains("- " + transaction));
        }
    }

    @Test
    public void testShowAccountDoesNotChangeBalanceOrHistory() {
        double originalBalance = account.getBalance();
        int originalHistorySize = account.getTransactionHistory().size();

        displayService.showAccount(account);

        assertEquals(originalBalance, account.getBalance(), 0.001);
        assertEquals(originalHistorySize,
                account.getTransactionHistory().size());
    }

    @Test
    public void testShowAccountFormatsZeroBalance() {
        BankAccount emptyAccount = new BankAccount("Praise", 0);

        String result = displayService.showAccount(emptyAccount);

        assertTrue(result.contains("Balance: NGN 0.00"));
        assertTrue(result.contains("Successful transactions: 0"));
    }
}
