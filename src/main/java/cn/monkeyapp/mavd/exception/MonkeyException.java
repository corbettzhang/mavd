package cn.monkeyapp.mavd.exception;

/**
 * 自定义异常
 *
 * @author Corbett Zhang
 */
public class MonkeyException extends Exception {

    /**
     * Exception message
     */
    private final String message;

    /**
     * Construct MonkeyException with a message
     *
     * @param message message
     */
    public MonkeyException(String message) {
        this.message = message;
    }

    /**
     * Construct MonkeyException from another exception
     *
     * @param e Any exception
     */
    public MonkeyException(Exception e) {
        message = e.getMessage();
    }

    /**
     * Get exception message
     *
     * @return exception message
     */
    @Override
    public String getMessage() {
        return message;
    }
}
