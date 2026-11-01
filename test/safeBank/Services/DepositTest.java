package safeBank.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import safeBank.BankAccount;

import static org.junit.jupiter.api.Assertions.*;

public class DepositTest {

    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService();
    }

    @Test
    public void testCreateAccountWithValidDetails() {
        BankAccount account =
                accountService.createAccount("Praise", 50000);

        assertNotNull(account);
        assertEquals("Praise", account.getAccountHolder());
        assertEquals(50000, account.getBalance(), 0.001);
    }

    @Test
    public void testCreateAccountWithZeroOpeningBalance() {
        BankAccount account =
                accountService.createAccount("Praise", 0);

        assertNotNull(account);
        assertEquals(0, account.getBalance(), 0.001);
    }

    @Test
    public void testCreateAccountWithNegativeOpeningBalance() {
        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.createAccount("Praise", -500)
        );
    }

    @Test
    public void testCreateAccountWithBlankName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.createAccount("", 1000)
        );
    }

    @Test
    public void testCreateAccountWithWhitespaceName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.createAccount("   ", 1000)
        );
    }

    @Test
    public void testCreateAccountWithNullName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.createAccount(null, 1000)
        );
    }
}
