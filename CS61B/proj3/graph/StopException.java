package graph;

/** An unchecked exception that is intended to terminate a traversal
 *  prematurely.
 *  @author P. N. Hilfinger
 */
public class StopException extends RuntimeException {

    /** A StopException with no message. */
    public StopException() {
    }

    /** A StopException with MSG as its message. */
    public StopException(String msg) {
        super(msg);
    }

}
