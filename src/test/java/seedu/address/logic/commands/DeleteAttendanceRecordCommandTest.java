package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_1;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalClassBook;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ClassBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Classes;
import seedu.address.model.person.CourseCode;
import seedu.address.model.person.Description;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Attendance;

class DeleteAttendanceRecordCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), getTypicalClassBook());

    DeleteAttendanceRecordCommandTest() throws DataLoadingException, IOException {
    }

    @BeforeEach
    public void setUp() throws DataLoadingException, IOException {
        model.selectClass(new Classes(new CourseCode("class1")));
    }

    @Test
    public void execute_deleteAttendanceRecord_failure() throws DataLoadingException, IOException, CommandException {
        DeleteAttendanceRecordCommand deleteAttendanceRecordCommand = new DeleteAttendanceRecordCommand(
                new Attendance(new AttendanceStatus(VALID_DATE_1, "1")));
        String expectedMessage = String.format(DeleteAttendanceRecordCommand.MESSAGE_SUCCESS, "[" + VALID_DATE_1 + "]");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new ClassBook(model.getClassBook()));
        model.selectClass(new Classes(new CourseCode("class1")));
        expectedModel.selectClass(new Classes(new CourseCode("class1")));
        model.setAddressBook(getTypicalAddressBook());
        expectedModel.setAddressBook(getTypicalAddressBook());
        List<Person> personList = expectedModel.getFilteredPersonList();
        for (int i = 0; i < personList.size(); i++) {
            Index index = Index.fromZeroBased(i);

            DeleteAttendanceRecordCommand.DeleteAttendanceDescriptor
                    deleteAttendanceDescriptor = new DeleteAttendanceRecordCommand.DeleteAttendanceDescriptor();
            deleteAttendanceDescriptor.setName(personList.get(i).getName());
            deleteAttendanceDescriptor.setPhone(personList.get(i).getPhone());
            deleteAttendanceDescriptor.setEmail(personList.get(i).getEmail());
            deleteAttendanceDescriptor.setStudentId(personList.get(i).getStudentId());
            Set<Attendance> set = new HashSet<>(personList.get(i).getAttendances());
            for (Attendance attendance : set) {
                if (attendance.attendanceName.getDate().equals(VALID_DATE_1)) {
                    set.remove(attendance);
                    break;
                }
            }
            deleteAttendanceDescriptor.setAttendances(set);

            Person personToEdit = personList.get(index.getZeroBased());
            Person editedPerson = createEditedPerson(personToEdit, deleteAttendanceDescriptor);

            expectedModel.setPerson(personToEdit, editedPerson);
            expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        }

        assertCommandSuccess(deleteAttendanceRecordCommand, model, expectedMessage, expectedModel);

    }









    private static Person createEditedPerson(Person personToEdit, DeleteAttendanceRecordCommand
            .DeleteAttendanceDescriptor deleteAttendanceDescriptor) {
        assert personToEdit != null;

        Name updatedName = deleteAttendanceDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = deleteAttendanceDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = deleteAttendanceDescriptor.getEmail().orElse(personToEdit.getEmail());
        StudentId updatedStudentId = deleteAttendanceDescriptor.getStudentId().orElse(personToEdit.getStudentId());
        Description updatedDescription = deleteAttendanceDescriptor.getDescription()
                        .orElse(personToEdit.getDescription());
        Set<Attendance> updatedAttendances = deleteAttendanceDescriptor.getTags().orElse(personToEdit.getAttendances());


        return new Person(updatedName, updatedPhone, updatedEmail, updatedStudentId, updatedAttendances,
                updatedDescription);
    }
}
