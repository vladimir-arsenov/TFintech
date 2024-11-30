package org.example;

import java.util.ArrayList;

public class OutOfMemoryExample {
    public static void main(String[] args) {
        var a = new ArrayList<int[]>();
        while (true) {
            a.add(new int[1_000_000_000]);
        }
    }
}
