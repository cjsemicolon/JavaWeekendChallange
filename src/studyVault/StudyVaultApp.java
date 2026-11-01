package studyvault;

import studyVault.NoteRepository;
import studyVault.NoteStore;
import studyVault.services.BackupService;
import studyVault.services.FileNameService;
import studyVault.services.NoteService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class StudyVaultApp {

    public static void main(String[] args) throws IOException {

        NoteStore noteStore = new NoteStore(Path.of("studyvault-data"));

        FileNameService fileNameService = new FileNameService();

        NoteRepository noteRepository =
                new NoteRepository(noteStore, fileNameService);

        NoteService noteService = new NoteService(noteRepository);

        BackupService backupService =
                new BackupService(noteStore, fileNameService);

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n===== STUDY VAULT =====");
            System.out.println("1. Create note");
            System.out.println("2. List notes");
            System.out.println("3. Read note");
            System.out.println("4. Append to note");
            System.out.println("5. Search notes");
            System.out.println("6. Delete note");
            System.out.println("7. Export note to backup");
            System.out.println("8. Restore note from backup");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {

                    case "1": {
                        System.out.print("Enter note filename: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter note content: ");
                        String content = scanner.nextLine();

                        System.out.print("Overwrite if the note exists? (yes/no): ");
                        String answer = scanner.nextLine();

                        boolean overwrite = answer.equalsIgnoreCase("yes");

                        noteService.createNote(name, content, overwrite);

                        System.out.println("Note created successfully.");
                        break;
                    }

                    case "2": {
                        List<String> notes = noteService.getAllNotes();

                        if (notes.isEmpty()) {
                            System.out.println("No notes found.");
                        } else {
                            System.out.println("\nYour notes:");

                            for (String note : notes) {
                                System.out.println("- " + note);
                            }
                        }

                        break;
                    }

                    case "3": {
                        System.out.print("Enter note filename: ");
                        String name = scanner.nextLine();

                        String content = noteService.readNote(name);

                        System.out.println("\n----- NOTE CONTENT -----");
                        System.out.println(content);
                        System.out.println("------------------------");

                        break;
                    }

                    case "4": {
                        System.out.print("Enter note filename: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter content to append: ");
                        String content = scanner.nextLine();

                        noteService.appendToNote(name, content);

                        System.out.println("Content appended successfully.");
                        break;
                    }

                    case "5": {
                        System.out.print("Enter keyword to search for: ");
                        String keyword = scanner.nextLine();

                        List<String> matches =
                                noteService.searchNotes(keyword);

                        if (matches.isEmpty()) {
                            System.out.println("No matches found.");
                        } else {
                            System.out.println("\nSearch results:");

                            for (String match : matches) {
                                System.out.println(match);
                            }
                        }

                        break;
                    }

                    case "6": {
                        System.out.print("Enter note filename to delete: ");
                        String name = scanner.nextLine();

                        noteService.deleteNote(name);

                        System.out.println("Note deleted successfully.");
                        break;
                    }

                    case "7": {
                        System.out.print("Enter note filename to back up: ");
                        String name = scanner.nextLine();

                        backupService.exportNote(name);

                        System.out.println("Note backed up successfully.");
                        break;
                    }

                    case "8": {
                        System.out.print("Enter backup filename to restore: ");
                        String name = scanner.nextLine();

                        backupService.restoreNote(name);

                        System.out.println("Note restored successfully.");
                        break;
                    }

                    case "0":
                        running = false;
                        System.out.println("Thank you for using Study Vault.");
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Please select an option from the menu."
                        );
                }

            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}