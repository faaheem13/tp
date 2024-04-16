package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_STUDENTID = new Prefix("s/");
    public static final Prefix PREFIX_CLASS = new Prefix("c/");
    public static final Prefix PREFIX_ATTENDANCE_RECORD = new Prefix("ar/");
    public static final Prefix PREFIX_ATTENDANCE_STATUS = new Prefix("st/");
    public static final Prefix PREFIX_MULTIPLE_STUDENTS = new Prefix("m/");

    public static final Prefix PREFIX_DESCRIPTION = new Prefix("desc/");

}
