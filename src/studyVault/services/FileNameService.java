package studyVault.services;

import java.util.Locale;

public class FileNameService {

    public String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Filename cannot be blank."
            );
        }

        String trimmedName = name.trim();

        if (trimmedName.equals(".")
                || trimmedName.equals("..")
                || trimmedName.contains("/")
                || trimmedName.contains("\\")
                || trimmedName.contains("..")) {
            throw new IllegalArgumentException(
                    "Use a simple filename without path separators or '..'."
            );
        }

        char[] unsupportedCharacters = {
                '<', '>', ':', '"', '|', '?', '*'
        };

        for (char character : unsupportedCharacters) {
            if (trimmedName.indexOf(character) >= 0) {
                throw new IllegalArgumentException(
                        "Filename contains unsupported characters."
                );
            }
        }

        if (trimmedName.toLowerCase(Locale.ROOT).endsWith(".txt")) {
            return trimmedName;
        }

        return trimmedName + ".txt";
    }
}