package io.github.hellomaker.launcher.app;

import java.util.function.Consumer;

public interface StatusListen {

    void addStatusListener(Consumer<StatusEnum> changeListener);

    void setStatus(StatusEnum status);
}
