package seedu.address.testutil;

import java.io.IOException;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.person.Classes;
import seedu.address.model.person.CourseCode;

/**
 * A utility class to help with building Classes objects.
 */
public class ClassBuilder {

    public static final String DEFAULT_CC = "cs1101s";

    private CourseCode cc;

    public ClassBuilder() {
        cc = new CourseCode(DEFAULT_CC);
    }
    public ClassBuilder(Classes classes) {
        cc = classes.getCourseCode();
    }

    /**
     * Sets the {@code cc} of the {@code Classes} that we are building.
     */
    public ClassBuilder withCC(String cc) {
        this.cc = new CourseCode(cc);
        return this;
    }

    public Classes build() throws DataLoadingException, IOException {
        return new Classes(cc);
    }
}
