package org.example.service.snapshot;

import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class SnapshotManager<T> {
    private final Stack<Pair> history = new Stack<>();

    public void saveSnapshot(String command, T snapshot) {
        history.push(new Pair(command, snapshot));
    }

    public Stack<Pair> getHistory() {
        return history;
    }

    public class Pair {
        String command;
        T snapshot;
        Pair(String c, T s) {
            command = c;
            snapshot = s;
        }

        private String getCommand() {
            return command;
        }

        private T getSnapshot() {
            return snapshot;
        }
    }
}
