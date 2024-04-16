package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemoveClassCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the given {@code String} of arguments in the context of the RemoveClassCommand
 * and returns a RemoveClassCommand object for execution.
 * @throws ParseException if the user input does not conform the expected format
 */
public class RemoveClassCommandParser implements Parser<RemoveClassCommand> {

    @Override
    public RemoveClassCommand parse(String userInput) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(userInput);
            return new RemoveClassCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveClassCommand.MESSAGE_USAGE), pe);
        }
    }
}
