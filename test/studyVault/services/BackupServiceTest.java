package studyVault.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import studyVault.NoteRepository;
import studyVault.NoteStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class BackupServiceTest {

    @TempDir
    Path temporaryDirectory;

    private NoteStore noteStore;
    private BackupService backupService;
    private NoteRepository noteRepository;

    @BeforeEach
    public void setUp() throws IOException {
        noteStore = new NoteStore(temporaryDirectory);

        FileNameService fileNameService = new FileNameService();

        noteRepository = new NoteRepository(
                noteStore,
                fileNameService
        );

        backupService = new BackupService(
                noteStore,
                fileNameService
        );
    }

    @Test
    public void testExportNote() throws IOException {
        noteRepository.create("Java", "Learning Java", false);

        backupService.exportNote("Java");

        Path backupPath = noteStore.getBackupsDirectory()
                .resolve("Java.txt");

        assertTrue(Files.exists(backupPath));
        assertEquals("Learning Java", Files.readString(backupPath));
    }

    @Test
    public void testExportMissingNote() {
        assertThrows(
                IOException.class,
                () -> backupService.exportNote("Missing")
        );
    }

    @Test
    public void testExportNoteWhenBackupAlreadyExists()
            throws IOException {

        noteRepository.create("Java", "Learning Java", false);

        backupService.exportNote("Java");

        assertThrows(
                IOException.class,
                () -> backupService.exportNote("Java")
        );
    }

    @Test
    public void testRestoreNote() throws IOException {
        noteRepository.create("Java", "Learning Java", false);

        backupService.exportNote("Java");

        noteRepository.delete("Java");

        backupService.restoreNote("Java");

        assertEquals("Learning Java", noteRepository.read("Java"));
    }

    @Test
    public void testRestoreMissingBackup() {
        assertThrows(
                IOException.class,
                () -> backupService.restoreNote("Missing")
        );
    }

    @Test
    public void testRestoreWhenNoteAlreadyExists()
            throws IOException {

        noteRepository.create("Java", "Learning Java", false);

        backupService.exportNote("Java");

        assertThrows(
                IOException.class,
                () -> backupService.restoreNote("Java")
        );
    }
}
