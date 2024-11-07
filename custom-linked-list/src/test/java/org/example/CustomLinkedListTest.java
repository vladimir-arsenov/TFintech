package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListTest {

    private CustomLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    public void shouldAddAndGetElement() {
        Integer element1 = 6, element2 = null;

        list.add(element1);
        list.add(element2);

        assertEquals(element1, list.get(0));
        assertEquals(element2, list.get(1));
    }

    @Test
    public void getShouldThrowExceptionIfIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    public void shouldRemoveElement() {
        list.addAll(Arrays.asList(1, 2, null, 5, 3));

        assertTrue(list.remove(null));
        assertEquals(4, list.size());
        assertTrue(list.remove(Integer.valueOf(1)));
        assertFalse(list.remove(Integer.valueOf(7)));
        assertEquals(3, list.size());
    }

    @Test
    public void shouldRemoveElementByIndex() {
        list.addAll(Arrays.asList(1, 2, null, 5, 3));

        assertNull(list.remove(2));
        assertEquals(3, list.remove(3));
        assertEquals(1, list.remove(0));
        assertEquals(2, list.size());
    }

    @Test
    public void removeShouldThrowExceptionIfIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    public void shouldAddAllElements() {
        list.addAll(Arrays.asList(1, 2, null));

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertNull(list.get(2));
    }

    @Test
    public void shouldContainElement() {
        assertFalse(list.contains(1));
        assertFalse(list.contains(null));

        list.addAll(Arrays.asList(1, 2, null));

        assertTrue(list.contains(1));
        assertTrue(list.contains(null));
    }

    @Test
    public void shouldReverse() {
        list.addAll(Arrays.asList(1, 2, null, 4, 5));

        list.reverse();

        assertEquals("[5, 4, null, 2, 1]", list.toString());
    }
}