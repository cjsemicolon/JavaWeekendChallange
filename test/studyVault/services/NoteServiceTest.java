package studyVault.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import studyVault.NoteRepository;
import studyVault.NoteStore;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NoteServiceTest {

    @TempDir
    Path temporaryDirectory;

    private NoteService noteService;

    @BeforeEach
    public void setUp() throws IOException {
        NoteStore noteStore = new NoteStore(temporaryDirectory);
        FileNameService fileNameService = new FileNameService();

        NoteRepository noteRepository =
                new NoteRepository(noteStore, fileNameService);

        noteService = new NoteService(noteRepository);
    }

    @Test
    public void testCreateNote() throws IOException {
        noteService.createNote("Java", "Learning Java", false);

        assertEquals("Learning Java", noteService.readNote("Java"));
    }

    @Test
    public void testGetAllNotes() throws IOException {
        noteService.createNote("Java", "Java content", false);
        noteService.createNote("Python", "Python content", false);

        List<String> notes = noteService.getAllNotes();

        assertEquals(2, notes.size());
        assertTrue(notes.contains("Java.txt"));
        assertTrue(notes.contains("Python.txt"));
    }

    @Test
    public void testReadNote() throws IOException {
        noteService.createNote("Java", "Learning Java", false);

        assertEquals("Learning Java", noteService.readNote("Java"));
    }

    @Test
    public void testAppendToNote() throws IOException {
        noteService.createNote("Java", "First line", false);

        noteService.appendToNote("Java", "Second line");

        assertEquals(
                "First line" + System.lineSeparator() + "Second line",
                noteService.readNote("Java")
        );
    }

    @Test
    public void testSearchNotes() throws IOException {
        noteService.createNote("Java", "Learning Java", false);
        noteService.createNote("Python", "Learning Python", false);

        List<String> results = noteService.searchNotes("Java");

        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("Java"));
    }

    @Test
    public void testDeleteNote() throws IOException {
        noteService.createNote("Java", "Learning Java", false);

        noteService.deleteNote("Java");

        assertTrue(noteService.getAllNotes().isEmpty());
    }

    @Test
    public void testDeleteMissingNote() {
        assertThrows(
                IOException.class,
                () -> noteService.deleteNote("Missing")
        );
    }
}
