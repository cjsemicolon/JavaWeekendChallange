package studyVault;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoteStore {

    private final Path notesDirectory;
    private final Path backupsDirectory;

    public NoteStore(Path baseDirectory) throws IOException {
        notesDirectory = baseDirectory.resolve("notes");
        backupsDirectory = baseDirectory.resolve("backups");

        Files.createDirectories(notesDirectory);
        Files.createDirectories(backupsDirectory);
    }

    public Path getNotesDirectory() {
        return notesDirectory;
    }

    public Path getBackupsDirectory() {
        return backupsDirectory;
    }
}