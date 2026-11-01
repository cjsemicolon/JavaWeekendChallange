package studyVault;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import studyVault.services.FileNameService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NoteRepositoryTest {

    @TempDir
    Path temporaryDirectory;

    private NoteRepository noteRepository;

    @BeforeEach
    public void setUp() throws IOException {
        NoteStore noteStore = new NoteStore(temporaryDirectory);
        FileNameService fileNameService = new FileNameService();

        noteRepository = new NoteRepository(
                noteStore,
                fileNameService
        );
    }

    @Test
    public void testCreateNote() throws IOException {
        noteRepository.create("Java", "Learning Java", false);

        assertEquals("Learning Java", noteRepository.read("Java"));
    }

    @Test
    public void testCreateNoteWithoutOverwrite() throws IOException {
        noteRepository.create("Java", "Original content", false);

        assertThrows(
                IOException.class,
                () -> noteRepository.create(
                        "Java", "New content", false
                )
        );

        assertEquals("Original content", noteRepository.read("Java"));
    }

    @Test
    public void testCreateNoteWithOverwrite() throws IOException {
        noteRepository.create("Java", "Original content", false);

        noteRepository.create("Java", "New content", true);

        assertEquals("New content", noteRepository.read("Java"));
    }

    @Test
    public void testListNotes() throws IOException {
        noteRepository.create("Java", "Java content", false);
        noteRepository.create("Python", "Python content", false);

        List<String> notes = noteRepository.list();

        assertEquals(2, notes.size());
        assertTrue(notes.contains("Java.txt"));
        assertTrue(notes.contains("Python.txt"));
    }

    @Test
    public void testListNotesWhenEmpty() throws IOException {
        assertTrue(noteRepository.list().isEmpty());
    }

    @Test
    public void testReadNote() throws IOException {
        noteRepository.create("Java", "Learning Java", false);

        assertEquals("Learning Java", noteRepository.read("Java"));
    }

    @Test
    public void testReadMissingNote() {
        assertThrows(
                IOException.class,
                () -> noteRepository.read("Missing")
        );
    }

    @Test
    public void testAppendToNote() throws IOException {
        noteRepository.create("Java", "First line", false);

        noteRepository.append("Java", "Second line");

        assertEquals(
                "First line" + System.lineSeparator() + "Second line",
                noteRepository.read("Java")
        );
    }

    @Test
    public void testAppendToMissingNote() {
        assertThrows(
                IOException.class,
                () -> noteRepository.append("Missing", "New content")
        );
    }

    @Test
    public void testSearchNotes() throws IOException {
        noteRepository.create(
                "Java", "Learning Java programming", false
        );

        noteRepository.create(
                "Python", "Learning Python programming", false
        );

        List<String> results = noteRepository.search("Java");

        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("Java.txt:1:"));
    }

    @Test
    public void testSearchIsCaseInsensitive() throws IOException {
        noteRepository.create("Java", "Learning JAVA", false);

        List<String> results = noteRepository.search("java");

        assertEquals(1, results.size());
    }

    @Test
    public void testSearchWithBlankKeyword() {
        assertThrows(
                IllegalArgumentException.class,
                () -> noteRepository.search(" ")
        );
    }

    @Test
    public void testDeleteNote() throws IOException {
        noteRepository.create("Java", "Learning Java", false);

        noteRepository.delete("Java");

        assertTrue(noteRepository.list().isEmpty());
    }

    @Test
    public void testDeleteMissingNote() {
        assertThrows(
                IOException.class,
                () -> noteRepository.delete("Missing")
        );
    }
}
