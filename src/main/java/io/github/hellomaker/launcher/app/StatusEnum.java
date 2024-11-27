package io.github.hellomaker.launcher.app;

/**
 * @author hellomaker
 */

public enum StatusEnum {

    NOT_RUNNING(0),
    IN_RUNNING(1),
    RUNNING(2),
    RUNNING_FAILED(3);
    private final int value;

    StatusEnum(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

}
