package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons in: ";

    public static final String MESSAGE_FAILURE = "Select a class first before attempting to list students!";
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (model.getSelectedClassName() == "No class selected!") {
            return new CommandResult(MESSAGE_FAILURE);
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS + model.getSelectedClassName());
    }
}
