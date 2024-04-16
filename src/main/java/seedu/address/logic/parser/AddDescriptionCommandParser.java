package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddDescriptionCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Description;


/**
 * Parses input arguments and creates a new {@code AddDescriptionCommand} object
 */
public class AddDescriptionCommandParser implements Parser<AddDescriptionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code AddDescriptionCommand}
     * and returns a {@code AddDescriptionCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddDescriptionCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DESCRIPTION);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DESCRIPTION);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddDescriptionCommand.MESSAGE_USAGE), ive);
        }

        String description = argMultimap.getValue(PREFIX_DESCRIPTION).orElse("");

        return new AddDescriptionCommand(index, new Description(description));
    }
}
