package org.example;

import java.util.List;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        // Часть 1
        CustomLinkedList<Integer> a = new CustomLinkedList<>();
        a.add(null);
        a.add(1);
        a.addAll(List.of(2, 3, 4));
        System.out.println("После add() и addAll(): " + a);

        a.remove(null);
        a.remove(1);
        a.remove(Integer.valueOf(1));
        System.out.println("После remove(): " + a);

        System.out.println("contains(3): " + a.contains(3));
        System.out.println("size(): " + a.size());

        System.out.println("get(0): " + a.get(0));
        System.out.println("get(2): " + a.get(1));


        // Часть 2
        CustomLinkedList<Integer> squares1To10 = IntStream.range(1, 11)
                .map(x -> x * x)
                .boxed()
                .reduce(new CustomLinkedList<>(),
                        (linkedList, element) -> {
                            linkedList.add(element);
                            return linkedList;
                        },
                        (ll, ll2) -> {
                            ll.addAll(ll2);
                            return ll;
                        }
                );
        System.out.println(squares1To10);
    }
}