package seedu.address.logic.commands;

import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalClassBook;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
public class ViewClassesCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() throws DataLoadingException, IOException {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalClassBook());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), model.getClassBook());
    }
}
