package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_RECORD;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
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
import seedu.address.model.person.Description;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Attendance;

/**
 * Deletes an attendance record of all students in that selected class.
 */
public class DeleteAttendanceRecordCommand extends Command {

    public static final String COMMAND_WORD = "dela";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Delete attendance record. "
            + "Parameters: "
            + PREFIX_ATTENDANCE_RECORD + "DATE\n"
            + "Example: " + COMMAND_WORD + " ar/ 19-03-2024";


    public static final String MESSAGE_SUCCESS = "Attendance deleted for: %1$s";
    public static final String MESSAGE_FAILURE = "Create/Select a class before deleting an attendance record";

    private Attendance date;

    /**
     * Create a DeleteAttendanceRecordCommand Object with the selected date to delete.
     * @param date of attendance to delete.
     */
    public DeleteAttendanceRecordCommand(Attendance date) {
        requireAllNonNull(date);
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireAllNonNull(model);
        if (model.getSelectedClassName() == "No class selected!") {
            return new CommandResult(MESSAGE_FAILURE);
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (lastShownList.size() == 0) {
            throw new CommandException(Messages.MESSAGE_NO_PERSON_IN_THE_CLASS);
        }

        boolean dateExisted = false;
        for (Attendance i : lastShownList.get(0).getAttendances()) {
            if (date.attendanceName.getDate().equals(i.attendanceName.getDate())) {
                dateExisted = true;
                break;
            }
        }

        if (!dateExisted) {
            throw new CommandException(Messages.MESSAGE_DATE_NOT_FOUND);
        }


        for (int i = 0; i < lastShownList.size(); i++) {
            Index index = Index.fromZeroBased(i);

            DeleteAttendanceDescriptor deleteAttendanceDescriptor = new DeleteAttendanceDescriptor();
            deleteAttendanceDescriptor.setName(lastShownList.get(i).getName());
            deleteAttendanceDescriptor.setPhone(lastShownList.get(i).getPhone());
            deleteAttendanceDescriptor.setEmail(lastShownList.get(i).getEmail());
            deleteAttendanceDescriptor.setStudentId(lastShownList.get(i).getStudentId());
            Set<Attendance> set = new HashSet<>(lastShownList.get(i).getAttendances());
            for (Attendance attendance : set) {
                if (attendance.attendanceName.getDate().equals(date.attendanceName.getDate())) {
                    set.remove(attendance);
                    break;
                }
            }
            deleteAttendanceDescriptor.setAttendances(set);

            Person personToEdit = lastShownList.get(index.getZeroBased());
            Person editedPerson = createEditedPerson(personToEdit, deleteAttendanceDescriptor);

            model.setPerson(personToEdit, editedPerson);
            model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, date));
    }

    /**
     * Update the student's info
     * @param personToEdit targeted user to update.
     * @param deleteAttendanceDescriptor the updated information of the person
     * @return the updated student
     */
    private static Person createEditedPerson(Person personToEdit,
                                             DeleteAttendanceDescriptor deleteAttendanceDescriptor) {
        assert personToEdit != null;

        Name updatedName = deleteAttendanceDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = deleteAttendanceDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = deleteAttendanceDescriptor.getEmail().orElse(personToEdit.getEmail());

        StudentId updatedStudentId = deleteAttendanceDescriptor.getStudentId().orElse(personToEdit.getStudentId());
        Set<Attendance> updatedAttendances = deleteAttendanceDescriptor.getTags().orElse(personToEdit.getAttendances());
        Description updatedDescription = deleteAttendanceDescriptor.getDescription()
                        .orElse(personToEdit.getDescription());


        return new Person(updatedName, updatedPhone, updatedEmail, updatedStudentId, updatedAttendances,
                updatedDescription);

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteAttendanceRecordCommand)) {
            return false;
        }

        DeleteAttendanceRecordCommand otherAddAttendanceRecordCommand = (DeleteAttendanceRecordCommand) other;
        return date.equals(otherAddAttendanceRecordCommand.date);
    }


    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class DeleteAttendanceDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private StudentId studentId;
        private Set<Attendance> attendances;
        private Description description;

        public DeleteAttendanceDescriptor() {}


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

        public Optional<StudentId> getStudentId() {
            return Optional.ofNullable(studentId);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setAttendances(Set<Attendance> attendances) {
            this.attendances = (attendances != null) ? new HashSet<>(attendances) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Attendance>> getTags() {
            return (attendances != null) ? Optional.of(Collections.unmodifiableSet(attendances)) : Optional.empty();
        }

        public void setDescription(Description description) {
            this.description = description;
        }
        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof DeleteAttendanceDescriptor)) {
                return false;
            }

            DeleteAttendanceDescriptor otherEditPersonDescriptor = (DeleteAttendanceDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(studentId, otherEditPersonDescriptor.studentId)
                    && Objects.equals(attendances, otherEditPersonDescriptor.attendances);
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
