package org.noear.grit.client;

/**
 * @author noear
 * @since 1.0
 */
public class GritException extends RuntimeException {
    public GritException(Throwable cause) {
        super(cause);
    }

    public GritException(String message) {
        super(message);
    }
}
