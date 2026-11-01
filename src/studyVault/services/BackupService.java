package studyVault.services;

import studyVault.NoteStore;

import java.nio.file.FileAlreadyExistsException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class BackupService {

    private final NoteStore noteStore;
    private final FileNameService fileNameService;

    public BackupService(
            NoteStore noteStore,
            FileNameService fileNameService
    ) {
        this.noteStore = noteStore;
        this.fileNameService = fileNameService;
    }

    public void exportNote(String name) throws IOException {
        String normalizedName = fileNameService.normalizeName(name);

        Path source = noteStore.getNotesDirectory()
                .resolve(normalizedName);

        Path target = noteStore.getBackupsDirectory()
                .resolve(normalizedName);

        if (!Files.exists(source)) {
            throw new NoSuchFileException(source.toString());
        }

        if (Files.exists(target)) {
            throw new FileAlreadyExistsException(
                    target.getFileName().toString()
            );
        }

        copyBytes(source, target);
    }

    public void restoreNote(String name) throws IOException {
        String normalizedName = fileNameService.normalizeName(name);

        Path source = noteStore.getBackupsDirectory()
                .resolve(normalizedName);

        Path target = noteStore.getNotesDirectory()
                .resolve(normalizedName);

        if (!Files.exists(source)) {
            throw new NoSuchFileException(source.toString());
        }

        if (Files.exists(target)) {
            throw new FileAlreadyExistsException(
                    target.getFileName().toString()
            );
        }

        copyBytes(source, target);
    }

    private void copyBytes(Path source, Path target)
            throws IOException {

        try (
                InputStream input = new FileInputStream(source.toFile());
                OutputStream output = new FileOutputStream(target.toFile())
        ) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }
}