package seedu.address.logic.commands;

import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalClassBook;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() throws DataLoadingException, IOException {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalClassBook());
    }

}
