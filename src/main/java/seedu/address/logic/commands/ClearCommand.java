package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_FAILURE = "Create/Select a class first before attempting to clear!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        try {
            model.clear();
        } catch (NullPointerException e) {
            return new CommandResult(MESSAGE_FAILURE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
