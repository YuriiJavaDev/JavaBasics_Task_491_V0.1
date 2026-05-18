### Imagine you're a primary school teacher and need to select all students whose names begin with the letter 'A' for a special assignment at the board.
#### You have a complete list of all students. Your task is to quickly scan this list, select only the relevant students, and, without collecting their names in a new list, immediately read them out one by one so they come to the board.

```java
import java.util.List;

public class ClassroomApp {
    public static void main(String[] args) {
        // List of all students (sample data)
        List<String> students = List.of(
            "Anna", "Boris", "Alexey", "Maria",
            "Andrey", "Victor", "Artur", "Galina", "Alisa", "Dmitry"
            );
        
        // We iterate through the stream of names, retaining only those that begin with 'A',
        // and immediately display each name on the screen.
        // Important: We do not collect the filtered names into a new collection.
    }
}
```
