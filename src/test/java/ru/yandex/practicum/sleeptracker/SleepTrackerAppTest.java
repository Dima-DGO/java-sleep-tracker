package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    @TempDir
    Path tempDir;

    @Test
    void testMainWithValidFile() {
        Path testFile = tempDir.resolve("sleep_log.txt");
        String content = "01.10.25 22:15;02.10.25 08:00;GOOD\n" +
                "02.10.25 23:00;03.10.25 08:00;BAD\n" +
                "03.10.25 14:30;03.10.25 15:20;NORMAL";
        try {
            Files.write(testFile, content.getBytes());
        } catch (IOException e) {
            fail("Failed to create test file: " + e.getMessage());
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SleepTrackerApp.main(new String[]{testFile.toString()});

        String output = outContent.toString();

        assertTrue(output.contains("Общее количество сессий сна: 3"));
        assertTrue(output.contains("Минимальная продолжительность сессии (мин):"));
        assertTrue(output.contains("Максимальная продолжительность сессии (мин):"));
        assertTrue(output.contains("Средняя продолжительность сессии (мин):"));
        assertTrue(output.contains("Количество сессий с плохим качеством сна: 1"));
        assertTrue(output.contains("Количество бессонных ночей:"));
        assertTrue(output.contains("Хронотип пользователя:"));

        System.setOut(System.out);
    }

    @Test
    void testMainWithoutArguments() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        SleepTrackerApp.main(new String[]{});

        String error = errContent.toString();
        assertTrue(error.contains("Укажите путь к файлу с логом сна"));

        System.setErr(System.err);
    }

    @Test
    void testMainWithNonExistentFile() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SleepTrackerApp.main(new String[]{"non_existent_file.txt"});

        String error = errContent.toString();
        String output = outContent.toString();

        assertTrue(error.contains("Ошибка чтения файла"));
        assertTrue(output.contains("Общее количество сессий сна: 0"));
        assertTrue(output.contains("Количество сессий с плохим качеством сна: 0"));

        System.setErr(System.err);
        System.setOut(System.out);
    }


    @Test
    void testMainWithEmptyFile() {
        Path testFile = tempDir.resolve("empty_sleep_log.txt");
        try {
            Files.createFile(testFile);
        } catch (IOException e) {
            fail("Failed to create empty test file: " + e.getMessage());
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SleepTrackerApp.main(new String[]{testFile.toString()});

        String output = outContent.toString();

        assertTrue(output.contains("Общее количество сессий сна: 0"));
        assertTrue(output.contains("Минимальная продолжительность сессии (мин): 0"));
        assertTrue(output.contains("Максимальная продолжительность сессии (мин): 0"));
        assertTrue(output.contains("Средняя продолжительность сессии (мин): 0.0"));
        assertTrue(output.contains("Количество сессий с плохим качеством сна: 0"));
        assertTrue(output.contains("Количество бессонных ночей: 0"));
        assertTrue(output.contains("Хронотип пользователя: голубь"));


        System.setOut(System.out);
    }

    @Test
    void testMainWithFileContainingOnlyCommentsAndEmptyLines() {
        Path testFile = tempDir.resolve("comments_sleep_log.txt");
        String content = "\n" + "   \n" + "# Комментарий\n" + "\n";

        try {
            Files.write(testFile, content.getBytes());
        } catch (IOException e) {
            fail("Failed to create test file with comments: " + e.getMessage());
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SleepTrackerApp.main(new String[]{testFile.toString()});

        String output = outContent.toString();

        assertTrue(output.contains("Общее количество сессий сна: 0"));
        assertTrue(output.contains("Хронотип пользователя: голубь"));

        System.setOut(System.out);
    }
}

