package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import seedu.address.model.person.AttendanceStatus;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidDate(String)} (String)}
 */
public class Attendance {

    public static final String MESSAGE_CONSTRAINTS = "A valid attendance date should be in dd-MM-yyyy";

    public static final String MESSAGE_CONSTRAINTS_STATUS = "Status must be from 0 to 2 where 0 means 'Absent'"
            + ", 1 means 'Present' and 2 means 'Absent with valid reason'";

    public static final String VALIDATION_REGEX_DATE = "\\d{2}-\\d{2}-\\d{4}";
    public static final String VALIDATION_REGEX_STATUS = "[012]";


    public final AttendanceStatus attendanceName;

    /**
     * Constructs a {@code aDate}.
     *
     * @param attendanceName A valid date.
     */
    public Attendance(AttendanceStatus attendanceName) {
        requireNonNull(attendanceName);
        checkArgument(isValidDate(attendanceName.getDate()), MESSAGE_CONSTRAINTS);
        this.attendanceName = attendanceName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidDate(String test) {
        if (test.trim().equals("")) {
            return false;
        }

        if (!test.matches(VALIDATION_REGEX_DATE)) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            Date date = dateFormat.parse(test);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidStatus(String test) {
        return test.matches(VALIDATION_REGEX_STATUS);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Attendance)) {
            return false;
        }

        Attendance otherAttendance = (Attendance) other;
        return attendanceName.equals(otherAttendance.attendanceName);
    }

    @Override
    public int hashCode() {
        return attendanceName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + attendanceName.getDate() + ']';
    }

}
