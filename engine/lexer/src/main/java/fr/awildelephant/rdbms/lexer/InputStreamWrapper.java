package fr.awildelephant.rdbms.lexer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class InputStreamWrapper {

    private static final int END_OF_FILE_CODE = -1;

    private final Reader reader;

    private int next;

    private InputStreamWrapper(Reader reader) {
        this.reader = reader;

        storeNextCharacter();
    }

    private static InputStreamWrapper wrap(InputStream inputStream) {
        return new InputStreamWrapper(new BufferedReader(new InputStreamReader(inputStream)));
    }

    public static InputStreamWrapper wrap(String input) {
        return wrap(new ByteArrayInputStream(input.getBytes()));
    }

    boolean isFinished() {
        return next == END_OF_FILE_CODE;
    }

    int get() {
        return next;
    }

    void next() {
        if (next != END_OF_FILE_CODE) {
            storeNextCharacter();
        }
    }

    private void storeNextCharacter() {
        try {
            next = reader.read();
        } catch (IOException e) {
            throw new IllegalStateException(e); // Throw a customized exception
        }
    }
}
