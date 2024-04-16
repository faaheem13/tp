package seedu.address.model.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ClassBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyClassBook;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Classes;
import seedu.address.model.person.Description;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Attendance;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    public static final Description EMPTY_DESCRIPTION = new Description("");
    public static Person[] getSamplePersons() {
        return new Person[] {
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Attendance> getAttendanceSet(Attendance... strings) {

        Set<Attendance> set = new HashSet<>();
        for (Attendance i : strings) {
            set.add(new Attendance(new AttendanceStatus(i.attendanceName.getDate(), "1")));
        }
        return set;
    }


    public static Classes[] getSampleClasses() throws DataLoadingException, IOException {
        return new Classes[] {

        //new Classes(new CourseCode("CS2103T")),
        //new Classes(new CourseCode("CS2101"))
        };
    }
    public static ReadOnlyClassBook getSampleClassBook() {
        ClassBook sampleCb = new ClassBook();
        return sampleCb;
    }

}
