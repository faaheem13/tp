package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_RECORD;

import seedu.address.logic.commands.EditCommand;

class DeleteAttendanceRecordCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_ATTENDANCE_RECORD;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private DeleteAttendanceRecordCommandParser parser = new DeleteAttendanceRecordCommandParser();

    //    @Test
    //    public void parse_missingParts_failure() {
    //        // no index specified
    //        assertParseFailure(parser, VALID_DATE_1, DeleteAttendanceRecordCommand.MESSAGE_USAGE);
    //
    //        // no field specified
    //        assertParseFailure(parser, "ar/", DeleteAttendanceRecordCommand.MESSAGE_USAGE);
    //
    //        // no index and no field specified
    //        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    //    }

}
