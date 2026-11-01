package studyVault.services;

import studyVault.NoteRepository;

import java.io.IOException;
import java.util.List;

public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void createNote(
            String name,
            String content,
            boolean overwrite
    ) throws IOException {
        noteRepository.create(name, content, overwrite);
    }

    public List<String> getAllNotes() throws IOException {
        return noteRepository.list();
    }

    public String readNote(String name) throws IOException {
        return noteRepository.read(name);
    }

    public void appendToNote(String name, String content)
            throws IOException {
        noteRepository.append(name, content);
    }

    public List<String> searchNotes(String keyword)
            throws IOException {
        return noteRepository.search(keyword);
    }

    public void deleteNote(String name) throws IOException {
        noteRepository.delete(name);
    }
}