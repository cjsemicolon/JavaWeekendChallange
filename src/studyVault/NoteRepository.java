package studyVault;

import studyVault.services.FileNameService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NoteRepository {

    private final NoteStore noteStore;
    private final FileNameService fileNameService;

    public NoteRepository(
            NoteStore noteStore,
            FileNameService fileNameService
    ) {
        this.noteStore = noteStore;
        this.fileNameService = fileNameService;
    }

    private Path notePath(String name) {
        return noteStore.getNotesDirectory()
                .resolve(fileNameService.normalizeName(name))
                .normalize();
    }

    public void create(
            String name,
            String content,
            boolean overwrite
    ) throws IOException {

        Path path = notePath(name);

        if (!overwrite) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    path,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW
            )) {
                writer.write(Objects.requireNonNullElse(content, ""));
            }

            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(Objects.requireNonNullElse(content, ""));
        }
    }

    public List<String> list() throws IOException {
        try (var paths = Files.list(noteStore.getNotesDirectory())) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
        }
    }

    public String read(String name) throws IOException {
        Path path = notePath(name);

        if (!Files.exists(path)) {
            throw new NoSuchFileException(path.toString());
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(
                path,
                StandardCharsets.UTF_8
        )) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (content.length() > 0) {
                    content.append(System.lineSeparator());
                }

                content.append(line);
            }
        }

        return content.toString();
    }

    public void append(String name, String content)
            throws IOException {

        Path path = notePath(name);

        if (!Files.exists(path)) {
            throw new NoSuchFileException(path.toString());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND
        )) {
            if (Files.size(path) > 0) {
                writer.newLine();
            }

            writer.write(Objects.requireNonNullElse(content, ""));
        }
    }

    public List<String> search(String keyword) throws IOException {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException(
                    "Search keyword cannot be blank."
            );
        }

        List<String> matches = new ArrayList<>();

        for (String file : list()) {
            Path path = noteStore.getNotesDirectory().resolve(file);

            try (BufferedReader reader = Files.newBufferedReader(
                    path,
                    StandardCharsets.UTF_8
            )) {
                String line;
                int lineNumber = 0;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    if (line.toLowerCase(Locale.ROOT)
                            .contains(keyword.toLowerCase(Locale.ROOT))) {
                        matches.add(
                                file + ":" + lineNumber + ": " + line
                        );
                    }
                }
            }
        }

        return matches;
    }

    public void delete(String name) throws IOException {
        Path path = notePath(name);

        if (!Files.deleteIfExists(path)) {
            throw new NoSuchFileException(path.toString());
        }
    }
}