package graph;

/** An unchecked exception that is intended to signal that a particular
 *  Edge or Vertex is not to be traversed.
 *  @author P. N. Hilfinger
 */
public class RejectException extends RuntimeException {

    /** A RejectException with no message. */
    public RejectException() {
    }

    /** A RejectException with MSG as its message. */
    public RejectException(String msg) {
        super(msg);
    }

}
