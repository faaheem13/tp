package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Classes;

/**
 * Deletes a class identified using it's displayed index from the class book.
 */
public class RemoveClassCommand extends Command {
    public static final String COMMAND_WORD = "rm";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes class identified by the index number used in the displayed class list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Class: %1$s";

    private final Index targetIndex;

    public RemoveClassCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Classes> lastShownList = model.getFilteredClassList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CLASS_DISPLAYED_INDEX);
        }

        Classes classToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.removeClass(classToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.classFormat(classToDelete))
                + "\nUse the 'view' command to see your remaining classes!"

        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemoveClassCommand)) {
            return false;
        }

        RemoveClassCommand otherCmd = (RemoveClassCommand) other;
        return targetIndex.equals(otherCmd.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
