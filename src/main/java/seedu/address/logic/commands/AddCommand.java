package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
 * Adds a person to the currently selected class' address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_STUDENTID + "STUDENTID "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION" + "] \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_STUDENTID + "A0255333B "
            + PREFIX_DESCRIPTION + "wants to explore BioInformatics";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_FAILURE = "Create/Select a class first before adding a student!";

    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in the class";

    public static final String MESSAGE_DUPLICATE_EMAIL = "This email already exits in the address book.";

    public static final String MESSAGE_DUPLICATE_STUDENT_ID = "This student id already exists in the address book.";

    public static final String MESSAGE_DUPLICATE_PHONE = "This phone already exists in the address book.";

    private Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.getSelectedClassName() == "No class selected!") {
            return new CommandResult(MESSAGE_FAILURE);
        }
        List<Person> lastShownList = model.getFilteredPersonList();
        for (Person p : lastShownList) {
            if (toAdd.getPhone().equals(p.getPhone())) {
                throw new CommandException(MESSAGE_DUPLICATE_PHONE);
            }

            if (toAdd.getEmail().equals(p.getEmail())) {
                throw new CommandException(MESSAGE_DUPLICATE_EMAIL);
            }

            if (toAdd.getStudentId().equals(p.getStudentId())) {
                throw new CommandException(MESSAGE_DUPLICATE_STUDENT_ID);
            }
        }

        if (lastShownList != null && lastShownList.size() >= 1) {
            Set<Attendance> allDates = lastShownList.get(0).getAttendances();
            Set<Attendance> newDates = new HashSet<>();
            for (Attendance i : allDates) {
                newDates.add(new Attendance(new AttendanceStatus(i.attendanceName.getDate(), "2")));
            }
            EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
            editPersonDescriptor.setAttendances(newDates);
            toAdd = createEditedPerson(toAdd, editPersonDescriptor);
        }
        try {
            model.addPerson(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
        } catch (NullPointerException e) {
            return new CommandResult(MESSAGE_FAILURE);
        }
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        StudentId updatedStudentId = editPersonDescriptor.getStudentId().orElse(personToEdit.getStudentId());
        Set<Attendance> updatedAttendances = editPersonDescriptor.getTags().orElse(personToEdit.getAttendances());
        Description updatedDescription = editPersonDescriptor.getDescription().orElse(personToEdit.getDescription());



        return new Person(updatedName, updatedPhone, updatedEmail, updatedStudentId, updatedAttendances,
                updatedDescription);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
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
        private Set<Attendance> attendances;
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
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
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
                    .add("description", description)
                    .toString();
        }
    }
}
