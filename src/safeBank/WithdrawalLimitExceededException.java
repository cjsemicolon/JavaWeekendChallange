package safeBank;

public class WithdrawalLimitExceededException extends Exception {
    public WithdrawalLimitExceededException(String message) {
        super(message);
    }
}
