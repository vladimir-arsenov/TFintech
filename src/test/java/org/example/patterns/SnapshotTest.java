package org.example.patterns;

import org.example.model.Location;
import org.example.service.snapshot.LocationSnapshot;
import org.example.service.snapshot.SnapshotManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SnapshotTest {

    private SnapshotManager<LocationSnapshot> snapshotManager;

    @BeforeEach
    void setUp() {
        snapshotManager = new SnapshotManager<>();
    }

    @Test
    void testSaveSnapshot() {
        var snapshot = new LocationSnapshot(new Location());
        var command = "save";
        snapshotManager.saveSnapshot(command, snapshot);

        assertEquals(1, snapshotManager.getHistory().size());
        assertEquals(command, snapshotManager.getHistory().peek().getCommand());
        assertEquals(snapshot, snapshotManager.getHistory().peek().getSnapshot());
    }

    @Test
    void testSaveIdenticalSnapshot() {
        var snapshot = new LocationSnapshot(new Location());
        var command = "save";
        snapshotManager.saveSnapshot(command, snapshot);
        snapshotManager.saveSnapshot(command, snapshot);

        assertEquals(1, snapshotManager.getHistory().size());
    }
}
