/*
 */
package InputGenerating;

import InputGenerating.Exceptions.InputGeneratingException;
import java.io.Reader;

/**
 *
 * @author partizanka
 */
public class InputFromProgramGenerator {

    /**
     *
     * @param testNumber
     * @return
     * @throws InputGeneratingException
     */
    public Reader getReader(int testNumber) throws InputGeneratingException {
        throw new InputGeneratingException("There is no generator.");
    }
}