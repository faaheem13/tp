package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Classes;

/**
 * Selects the Class to be viewed or modified currently.
 */
public class SelectClassCommand extends Command {
    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_SUCCESS = "Here is your class: ";

    public static final String ADD_INSTRUCTION = "Please use the 'add' command to add students to the class."

            + "\nYou may type 'add' in the box to get help on how to use it!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Enters individual class view, "
            + "displaying all students in the class"
            + "Parameters: view [index of class]...\n"
            + "Example: " + COMMAND_WORD + " 2";

    private final Index targetIndex;

    /**
     *  Create a SelectClassCommand Object
     * @param index
     */
    public SelectClassCommand(Index index) {
        requireNonNull(index);
        this.targetIndex = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        ObservableList<Classes> lastShownList = model.getFilteredClassList();

        if (targetIndex.getOneBased() < 1 || targetIndex.getOneBased() > model.getFilteredClassList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CLASS_DISPLAYED_INDEX);
        }
        Classes selectedClass = lastShownList.get(targetIndex.getZeroBased());
        model.selectClass(selectedClass);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(MESSAGE_SUCCESS + selectedClass.getCourseCode()
        + "\n" + ADD_INSTRUCTION);
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof SelectClassCommand)) {
            return false;
        }

        SelectClassCommand otherCommand = (SelectClassCommand) other;
        return targetIndex.equals(otherCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
