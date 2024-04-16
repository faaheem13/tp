package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

public class RemoveClassCommandTest {

    @Test
    public void equals() {
        RemoveClassCommand rmFirstCommand = new RemoveClassCommand(INDEX_FIRST_PERSON);
        RemoveClassCommand rmSecondCommand = new RemoveClassCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(rmFirstCommand.equals(rmFirstCommand));

        // same values -> returns true
        RemoveClassCommand rmCopy = new RemoveClassCommand(INDEX_FIRST_PERSON);
        assertTrue(rmFirstCommand.equals(rmCopy));

        // different types -> returns false
        assertFalse(rmFirstCommand.equals(1));

        // null -> returns false
        assertFalse(rmFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(rmFirstCommand.equals(rmSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        RemoveClassCommand rm = new RemoveClassCommand(targetIndex);
        String expected = RemoveClassCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, rm.toString());
    }
}
