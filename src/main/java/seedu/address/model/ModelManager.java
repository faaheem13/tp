package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.person.Classes;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.ui.UiUpdateListener;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private final List<UiUpdateListener> uiUpdateListeners;
    private final ClassBook classBook;
    private final UserPrefs userPrefs;
    private FilteredList<Person> filteredPersons;
    private final FilteredList<Classes> filteredClasses;
    private Classes selectedClass;
    private AddressBook selectedClassAddressBook;
    private JsonAddressBookStorage storage;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs, ReadOnlyClassBook classBook) {
        requireAllNonNull(addressBook, userPrefs, classBook);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs
                + "and class book: " + classBook);

        this.selectedClassAddressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.classBook = new ClassBook(classBook);

        filteredPersons = new FilteredList<>(this.selectedClassAddressBook.getPersonList());
        filteredClasses = new FilteredList<>(this.classBook.getClassList());

        // Set an initial predicate that always evaluates to false in order to hide people on startup
        Predicate<Person> initialPredicate = person -> false;
        filteredPersons.setPredicate(initialPredicate);
        uiUpdateListeners = new ArrayList<>();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new ClassBook());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public Path getClassBookFilePath() {
        return userPrefs.getClassBookFilePath();
    }

    @Override
    public void setClassBookFilePath(Path classBookFilePath) {
        requireNonNull(classBookFilePath);
        userPrefs.setClassBookFilePath(classBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.selectedClassAddressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return selectedClassAddressBook;
    }

    @Override
    public void setClassBook(ReadOnlyClassBook classBook) {
        this.classBook.resetData(classBook);
    }

    @Override
    public ReadOnlyClassBook getClassBook() {
        return classBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return selectedClassAddressBook.hasPerson(person);
    }

    @Override
    public boolean hasClass(Classes classes) {
        requireNonNull(classes);
        return classBook.hasClass(classes);
    }

    @Override
    public void deletePerson(Person target) {
        selectedClassAddressBook.removePerson(target);

        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // Update the storage with the edited AddressBook
        try {
            storage.saveAddressBook(selectedClassAddressBook, selectedClass.getFilePath());
        } catch (IOException e) {
            logger.warning("Error saving the address book after deleting person: " + e.getMessage());
        }
    }

    /**
     * Clears all data from the currently selected class address book.
     * This operation effectively resets the data to an empty address book.
     * After clearing the data, the empty address book is saved to the storage
     * at the file path associated with the selected class.
     */
    public void clear() {
        selectedClassAddressBook.resetData(new AddressBook());
        try {
            storage.saveAddressBook(selectedClassAddressBook, selectedClass.getFilePath());
        } catch (IOException e) {
            logger.warning("Error saving the address book after deleting person: " + e.getMessage());
        }
    }

    @Override
    public void addPerson(Person person) {
        requireNonNull(person);
        selectedClassAddressBook.addPerson(person);
        filteredPersons = new FilteredList<>(this.selectedClassAddressBook.getPersonList());
        try {
            this.storage.saveAddressBook(selectedClassAddressBook, selectedClass.getFilePath());
        } catch (IOException e) {
            logger.warning("Error adding person to the selected class address book: " + e.getMessage());
        }

        Predicate<Person> predicate = updatedPerson -> selectedClassAddressBook.getPersonList().contains(updatedPerson);
        updateFilteredPersonList(predicate);
    }

    @Override
    public void createClass(Classes classes) {
        classBook.createClass(classes);
        selectClass(classes);
    }

    @Override
    public void removeClass(Classes classes) {
        hideStudentsUi();
        classBook.removeClass(classes);
        userPrefs.setAddressBookFilePath(Paths.get("No class selected!"));
        this.selectedClass = null;
        this.selectedClassAddressBook = new AddressBook();
        notifyUiUpdateListeners();
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        selectedClassAddressBook.setPerson(target, editedPerson);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // Update the storage with the edited AddressBook
        try {
            storage.saveAddressBook(selectedClassAddressBook, selectedClass.getFilePath());
        } catch (IOException e) {
            logger.warning("Error saving the address book after editing person: " + e.getMessage());
            // Consider what action to take if saving fails
            // e.g., throw a new CommandException or runtime exception
        }

    }
    @Override
    public String getSelectedClassName() {
        try {
            return this.selectedClass.getCourseCode().toString();
        } catch (NullPointerException e) {
            logger.warning("No class currently selected!");
            return "No class selected!";
        }
    }
    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Classes> getFilteredClassList() {
        return filteredClasses;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
        notifyUiUpdateListeners();
    }

    @Override
    public String getFormattedClassList() {
        ObservableList<Classes> classesList = this.getFilteredClassList();
        String returnString = "";
        for (int i = 0; i < classesList.size(); i++) {
            returnString += (i + 1) + ": " + classesList.get(i).getCourseCode() + "   ";
        }
        return returnString;
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return selectedClassAddressBook.equals(otherModelManager.selectedClassAddressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

    //=====================ClassBook accessors=========================================================================

    @Override
    public void selectClass(Classes classes) {
        requireNonNull(classes);

        selectedClass = classes;
        this.storage = new JsonAddressBookStorage(selectedClass.getFilePath());
        userPrefs.setAddressBookFilePath(selectedClass.getFilePath());

        try {
            Optional<ReadOnlyAddressBook> optionalAddressBook = storage.readAddressBook();

            if (optionalAddressBook.isPresent()) {
                selectedClassAddressBook = new AddressBook(optionalAddressBook.get());
                filteredPersons = new FilteredList<>(this.selectedClassAddressBook.getPersonList());

                updateFilteredPersonList(updatedPerson -> true);
            } else {
                logger.warning("Failed to load address book data for class: " + selectedClass);
            }
        } catch (DataLoadingException e) {
            logger.warning("Error loading address book data for class: " + selectedClass);
        }

        filteredPersons = new FilteredList<>(this.selectedClassAddressBook.getPersonList());
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        notifyUiUpdateListeners();
    }

    //=============================== Observer functions =============================================================
    public void addUiUpdateListener(UiUpdateListener listener) {
        uiUpdateListeners.add(listener);
    }

    private void notifyUiUpdateListeners() {
        for (UiUpdateListener listener : uiUpdateListeners) {
            listener.updateUi();
        }
    }


    /**
     * Hides all currently viewed students.
     */
    public void hideStudentsUi() {
        updateFilteredPersonList(updatedPerson -> false);
    }
}
