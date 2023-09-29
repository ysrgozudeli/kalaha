package com.bol.kalaha.common.util;

import java.util.Objects;

public class Preconditions {

    private Preconditions() {
        // Private constructor to prevent instantiation
    }

    public static void checkArgument(boolean expression) {

        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, String errorMessage) {

        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {

        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static <T> T checkNotNull(T reference) {

        return Objects.requireNonNull(reference);
    }

    public static <T> T checkNotNull(T reference, String errorMessage) {

        return Objects.requireNonNull(reference, errorMessage);
    }

    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {

        return Objects.requireNonNull(reference, String.format(errorMessageTemplate, errorMessageArgs));
    }
}