package studyVault.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileNameServiceTest {

    private FileNameService fileNameService;

    @BeforeEach
    public void setUp() {
        fileNameService = new FileNameService();
    }

    @Test
    public void testNormalizeName() {
        assertEquals(
                "Java Notes.txt",
                fileNameService.normalizeName("Java Notes")
        );
    }

    @Test
    public void testNormalizeNameAlreadyHasTxtExtension() {
        assertEquals(
                "Java Notes.txt",
                fileNameService.normalizeName("Java Notes.txt")
        );
    }

    @Test
    public void testNormalizeNameRemovesExtraSpacesAroundName() {
        assertEquals(
                "Java Notes.txt",
                fileNameService.normalizeName("  Java Notes  ")
        );
    }

    @Test
    public void testNormalizeNameWithUppercaseExtension() {
        assertEquals(
                "Java Notes.TXT",
                fileNameService.normalizeName("Java Notes.TXT")
        );
    }

    @Test
    public void testNormalizeNameWithBlankName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> fileNameService.normalizeName("")
        );
    }

    @Test
    public void testNormalizeNameWithWhitespace() {
        assertThrows(
                IllegalArgumentException.class,
                () -> fileNameService.normalizeName("   ")
        );
    }

    @Test
    public void testNormalizeNameWithNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> fileNameService.normalizeName(null)
        );
    }

    @Test
    public void testNormalizeNameWithUnsupportedCharacter() {
        assertThrows(
                IllegalArgumentException.class,
                () -> fileNameService.normalizeName("Java*Notes")
        );
    }

    @Test
    public void testNormalizeNameWithPathSeparator() {
        assertThrows(
                IllegalArgumentException.class,
                () -> fileNameService.normalizeName("folder/notes")
        );
    }

    @Test
    public void testNormalizeNameWithDoubleDots() {
        assertThrows(
                IllegalArgumentException.class,
                () -> fileNameService.normalizeName("../notes")
        );
    }
}
