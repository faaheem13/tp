package seedu.address.ui;

/**
 * Monitors for changes requiring UI to be updated
 */
public interface UiUpdateListener {
    // void updateUiOnClassSelected(Classes selectedClass);

    /**
     * Updates Ui when a change is made to currently selected class or swapping to new class.
     */
    void updateUi();

}
