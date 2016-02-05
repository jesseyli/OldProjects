package tex61;

import java.util.List;

/** A PageAssembler that collects its lines into a designated List.
 *  @author Jesse Li
 */
class PageCollector extends PageAssembler {

    /** A new PageCollector that stores lines in OUT. */
    PageCollector(List<String> out) {
        super();
        _out = out;
    }

    /** Add LINE to my List. */
    @Override
    void write(String line) {
        _out.add(line);
    }

    /** A list that stores all the lines that are passed to this
     *  PageAssembler. */
    private List<String> _out;
}
