package safeBank.Services;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import safeBank.BankAccount;
import safeBank.InsufficientFundsException;
import safeBank.InvalidAmountException;
import safeBank.WithdrawalLimitExceededException;

public class WithdrawalTest {

    private Withdrawal withdrawalService;
    private BankAccount account;

    @BeforeEach
    public void setUp() {
        withdrawalService = new Withdrawal();
        account = new BankAccount("Praise", 200000);
    }

    @Test
    public void testSuccessfulWithdrawal()
            throws InvalidAmountException,
            InsufficientFundsException,
            WithdrawalLimitExceededException {

        String result =
                withdrawalService.performWithdrawal(account, 50000);

        assertEquals("Withdrawal successful.", result);
        assertEquals(150000, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactionHistory().size());
    }

    @Test
    public void testWithdrawalOfExactLimit()
            throws InvalidAmountException,
            InsufficientFundsException,
            WithdrawalLimitExceededException {

        withdrawalService.performWithdrawal(account, 100000);

        assertEquals(100000, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawalOfZero() {
        assertThrows(
                InvalidAmountException.class,
                () -> withdrawalService.performWithdrawal(account, 0)
        );

        assertEquals(200000, account.getBalance(), 0.001);
        assertTrue(account.getTransactionHistory().isEmpty());
    }

    @Test
    public void testWithdrawalOfNegativeAmount() {
        assertThrows(
                InvalidAmountException.class,
                () -> withdrawalService.performWithdrawal(account, -1000)
        );

        assertEquals(200000, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawalExceedingBalance() {
        BankAccount smallAccount = new BankAccount("Praise", 5000);

        assertThrows(
                InsufficientFundsException.class,
                () -> withdrawalService.performWithdrawal(
                        smallAccount, 6000
                )
        );

        assertEquals(5000, smallAccount.getBalance(), 0.001);
        assertTrue(smallAccount.getTransactionHistory().isEmpty());
    }

    @Test
    public void testWithdrawalExceedingSingleWithdrawalLimit() {
        assertThrows(
                WithdrawalLimitExceededException.class,
                () -> withdrawalService.performWithdrawal(account, 120000)
        );

        assertEquals(200000, account.getBalance(), 0.001);
        assertTrue(account.getTransactionHistory().isEmpty());
    }

    @Test
    public void testWithdrawalWhenBalanceEqualsAmount()
            throws InvalidAmountException,
            InsufficientFundsException,
            WithdrawalLimitExceededException {

        BankAccount smallAccount = new BankAccount("Praise", 5000);

        withdrawalService.performWithdrawal(smallAccount, 5000);

        assertEquals(0, smallAccount.getBalance(), 0.001);
        assertEquals(1, smallAccount.getTransactionHistory().size());
    }

    @Test
    public void testWithdrawalOfOneKobo()
            throws InvalidAmountException,
            InsufficientFundsException,
            WithdrawalLimitExceededException {

        BankAccount smallAccount = new BankAccount("Praise", 1);

        withdrawalService.performWithdrawal(smallAccount, 0.01);

        assertEquals(0.99, smallAccount.getBalance(), 0.001);
    }

    @Test
    public void testSuccessfulWithdrawalAfterFailedWithdrawal()
            throws InvalidAmountException,
            InsufficientFundsException,
            WithdrawalLimitExceededException {

        assertThrows(
                WithdrawalLimitExceededException.class,
                () -> withdrawalService.performWithdrawal(account, 120000)
        );

        String result =
                withdrawalService.performWithdrawal(account, 50000);

        assertEquals("Withdrawal successful.", result);
        assertEquals(150000, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactionHistory().size());
    }
}
