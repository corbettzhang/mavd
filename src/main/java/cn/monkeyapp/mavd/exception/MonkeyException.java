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
    private String message;

    /**
     * Construct YoutubeDLException with a message
     *
     * @param message
     */
    public MonkeyException(String message) {
        this.message = message;
    }

    /**
     * Construct YoutubeDLException from another exception
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
