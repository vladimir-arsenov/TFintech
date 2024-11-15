package org.example.service.snapshot;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Getter
@Component
public class SnapshotManager<T> {
    private final Stack<Pair> history = new Stack<>();

    public void saveSnapshot(String command, T snapshot) {
        if (history.isEmpty() || !history.peek().getSnapshot().equals(snapshot)) {
            history.push(new Pair(command, snapshot));
        }
    }

    @Getter
    public class Pair {
        private final String command;
        private final T snapshot;
        Pair(String c, T s) {
            command = c;
            snapshot = s;
        }

    }
}
