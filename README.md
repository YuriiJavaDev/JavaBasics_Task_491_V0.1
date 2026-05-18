# Classroom Management: Immediate Stream Processing (JavaBasics_Task_491_V0.1)

## 📖 Description
In many data-processing scenarios, accumulating results into a intermediate collection is redundant and wasteful for system memory. This project demonstrates **Immediate Stream Consumption** using the Java Stream API. We simulate a classroom environment where a teacher filters a student roster to find names starting with the letter 'A'. Instead of collecting these names into a new `List`, the pipeline utilizes the terminal `forEach()` operation combined with a method reference to print each matching student immediately as they pass through the filter.

## 📋 Requirements Compliance
- **Stream Filtering**: Applied a lambda predicate within `.filter()` to isolate names beginning with 'A'.
- **In-Place Consumption**: Used `.forEach(System.out::println)` to output elements without allocating a new collection.
- **Resource Optimization**: Avoided the use of `Collectors`, demonstrating a more memory-efficient pipeline for print-only tasks.

## 🚀 Architectural Stack
- Java 17+ (Stream API, Method References)

## 🏗️ Implementation Details
- **ClassroomApp**: The main orchestrator managing the student roster and streaming the announcement logic.

## 📋 Expected result
```text
=== Students called to the board ===
Anna
Alexey
Andrey
Artur
Alisa
```

## 💻 Code Example

Project Structure:

    JavaBasics_Task_491/
    ├── src/
    │   └── com/yurii/pavlenko/
    │                 └── app/
    │                     └── ClassroomApp.java
    ├── LICENSE
    ├── TASK.md
    ├── THEORY.md
    └── README.md

Code
```java
package com.yurii.pavlenko.app;

import java.util.List;

public class ClassroomApp {

    public static void main(String[] args) {
        List<String> students = List.of(
                "Anna", "Boris", "Alexey", "Maria",
                "Andrey", "Victor", "Artur", "Galina", "Alisa", "Dmitry"
        );

        System.out.println("=== Students called to the board ===");
        students.stream()
                .filter(name -> name.startsWith("A"))
                .forEach(System.out::println);
    }
}
```

## ⚖️ License
This project is licensed under the **MIT License**.

Copyright (c) 2026 Yurii Pavlenko

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files...

License: [MIT](LICENSE)
