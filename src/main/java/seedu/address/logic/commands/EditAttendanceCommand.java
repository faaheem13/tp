package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_RECORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_STATUS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Description;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Attendance;

/**
 * Edits the details of an attendance record of an existing person in the address book.
 */
public class EditAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "edita";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the attendance record of the person identified "
            + "by the index number used in the displayed person list. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_ATTENDANCE_RECORD + "Attendance "
            + PREFIX_ATTENDANCE_STATUS + "Status\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ATTENDANCE_RECORD + "20-03-2024 "
            + PREFIX_ATTENDANCE_STATUS + "0";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_FAILURE = "Create/Select a class before editing an attendance record";

    private final ArrayList<Index> indexs;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param indexs of the people in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditAttendanceCommand(ArrayList<Index> indexs, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(indexs);
        requireNonNull(editPersonDescriptor);

        this.indexs = indexs;
        this.editPersonDescriptor = editPersonDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.getSelectedClassName() == "No class selected!") {
            return new CommandResult(MESSAGE_FAILURE);
        }
        List<Person> lastShownList = model.getFilteredPersonList();

        for (Index i : indexs) {
            if (i.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        ArrayList<Person> listOfPerson = new ArrayList<>();
        for (Index i : indexs) {
            if (!listOfPerson.contains(lastShownList.get(i.getZeroBased()))) {
                listOfPerson.add(lastShownList.get(i.getZeroBased()));
            }
        }

        StringBuilder names = new StringBuilder();
        for (Person i : listOfPerson) {

            Person personToEdit = i;
            Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor, model);

            if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            model.setPerson(personToEdit, editedPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            names.append(editedPerson.getName());
            names.append(", ");
        }
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, names.substring(0, names.length() - 2)));
    }

    private static Person createEditedPerson(Person personToEdit,
                                             EditPersonDescriptor editPersonDescriptor,
                                             Model model) throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        StudentId updatedStudentId = editPersonDescriptor.getAddress().orElse(personToEdit.getStudentId());
        boolean found = false;
        Set<Attendance> newAttendances = new HashSet<>();
        for (Attendance a : personToEdit.getAttendances()) {
            if (a.attendanceName.getDate().equals(editPersonDescriptor.getAttendances().attendanceName.getDate())) {
                found = true;
                newAttendances.add(new Attendance(new AttendanceStatus(editPersonDescriptor
                        .getAttendances()
                        .attendanceName.getDate(), editPersonDescriptor.getAttendances().attendanceName.getStatus())));
            } else {
                newAttendances.add(a);
            }
        }
        if (!found) {
            throw new CommandException(Messages.MESSAGE_DATE_NOT_FOUND);
        }
        Description updatedDescription = editPersonDescriptor.getDescription().orElse(personToEdit.getDescription());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedStudentId, newAttendances,
                updatedDescription);

    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private StudentId studentId;
        private Attendance attendances;

        private Description description;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setStudentId(toCopy.studentId);
            setAttendances(toCopy.attendances);
            setDescription(toCopy.description);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, studentId, attendances);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setStudentId(StudentId studentId) {
            this.studentId = studentId;
        }

        public Optional<StudentId> getAddress() {
            return Optional.ofNullable(studentId);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setAttendances(Attendance attendances) {
            this.attendances = attendances;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Attendance getAttendances() {
            return attendances;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(studentId, otherEditPersonDescriptor.studentId)
                    && Objects.equals(attendances, otherEditPersonDescriptor.attendances)
                    && Objects.equals(description, otherEditPersonDescriptor.description);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("studentid", studentId)
                    .toString();
        }
    }
}

