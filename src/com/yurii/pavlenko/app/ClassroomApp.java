package com.yurii.pavlenko.app;

import java.util.List;

/**
 * Application for scanning a student roster and immediately printing matching names using Streams.
 */
public class ClassroomApp {

    public static void main(String[] args) {
        List<String> students = List.of(
                "Anna", "Boris", "Alexey", "Maria",
                "Andrey", "Victor", "Artur", "Galina", "Alisa", "Dmitry"
        );

        System.out.println("=== Students called to the board ===");

        // The stream filters students by the first letter and immediately prints them out without creating a new list
        students.stream()
                .filter(name -> name.startsWith("A"))
                .forEach(System.out::println);
    }
}