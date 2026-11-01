# StudyVault

## About the Project
StudyVault is a simple Java application that allows users to create and manage text notes. Users can save notes, read them, update them, search for information, delete notes, and create backups.

## Features
- Create notes.
- View all saved notes.
- Read note contents.
- Append new content to existing notes.
- Search notes using keywords.
- Delete notes.
- Export notes as backups.
- Restore notes from backups.
- Handle errors when files are missing or already exist.

## Technologies Used
- Java
- JUnit 5
- Java File I/O

## How to Run
1. Open the project in IntelliJ IDEA.
2. Locate `StudyVaultApp.java`.
3. Run the `main` method.
4. Follow the instructions displayed in the console.

## Project Structure
- `StudyVaultApp` – Handles user input and console output.
- `NoteStore` – Creates and manages note and backup directories.
- `FileNameService` – Validates note filenames.
- `NoteRepository` – Handles note file operations.
- `NoteService` – Handles note-related operations.
- `BackupService` – Handles exporting and restoring notes.

## What I Learned
This project helped me practise Java file handling, exception handling, classes, services, and unit testing.