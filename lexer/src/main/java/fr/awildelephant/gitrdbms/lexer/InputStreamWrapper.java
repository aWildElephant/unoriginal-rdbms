package fr.awildelephant.gitrdbms.lexer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class InputStreamWrapper {

    private static final int END_OF_FILE_CODE = -1;

    private final InputStream inputStream;

    private int next;

    private InputStreamWrapper(InputStream inputStream) {
        this.inputStream = inputStream;

        storeNextCharacter();
    }

    public static InputStreamWrapper wrap(InputStream inputStream) {
        return new InputStreamWrapper(inputStream);
    }

    public static InputStreamWrapper wrap(String input) {
        return wrap(new ByteArrayInputStream(input.getBytes()));
    }

    public boolean isFinished() {
        return next == END_OF_FILE_CODE;
    }

    public int get() {
        return next;
    }

    public void next() {
        if (next != END_OF_FILE_CODE) {
            storeNextCharacter();
        }
    }

    private void storeNextCharacter() {
        try {
            next = inputStream.read();
        } catch (IOException e) {
            throw new IllegalStateException(e); // Throw a customized exception
        }
    }
}
