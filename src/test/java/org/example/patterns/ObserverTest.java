package org.example.patterns;

import org.example.model.Location;
import org.example.repository.LocationRepository;
import org.example.repository.observers.SaveLocationObserver;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ObserverTest {

    @Test
    public void update_shouldCallSaveOnRepository() {
        var repositoryMock = mock(LocationRepository.class);
        var observer = new SaveLocationObserver(repositoryMock);

        var location  = new Location();
        observer.update(location);

        var captor = ArgumentCaptor.forClass(Location.class);
        verify(repositoryMock).save(captor.capture());
        assertEquals(location, captor.getValue());
    }


}
