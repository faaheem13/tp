package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalClassBook;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyClassBook_success() throws CommandException {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_FAILURE, expectedModel);
    }

    @Test
    public void execute_nonEmptyClassBook_success() throws DataLoadingException, IOException, CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalClassBook());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalClassBook());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_FAILURE, expectedModel);
    }

}
