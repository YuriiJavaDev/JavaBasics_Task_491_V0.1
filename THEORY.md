## Methods forEach, peek: side effects.

### 1. The forEach Method: Final Action on Elements

Before diving into the details, let's recap what terminal and intermediate stream operations are.

- **Intermediate operations** (e.g., filter, map, distinct, peek) — return a new stream and typically perform no action until the terminal operation is called (**they only describe what needs to be done**, but don't iterate over the data).
- **Terminal operations** (e.g., forEach, collect, count, anyMatch) — start processing the stream's elements and return the result (or return nothing, like forEach).

**💬 Quick Facts**

- **intermediate** = "set up processing"
- **terminal** = "execute and get the result"

After a terminal operation, the stream is considered **closed**, and no further operations can be applied to it. It's like trying to finish an ice cream cone you've already eaten — it won't work: the stream is already "used."

Now let's look at two important methods for working with side effects: forEach and peek.

#### What does forEach do?

forEach is a stream terminal operation that performs a specified action on each element of the stream. It is typically used for displaying data, writing to a log, calculating statistics, and other side effects.

**Method Signature:**

```java
void forEach(Consumer<? super T> action)
```

Consumer<T> is a functional interface that takes one argument and returns nothing (e.g., System.out::println).

#### Example: Print all elements of a list

Let's say we have a list of user names:

```java
List<String> users = List.of("Anna", "Boris", "Alex", "Alina", "Dmitry");
```

Displaying all users on the screen using the Stream API is very simple:

```java
users.stream().forEach(System.out::println);
```

**Result:**

```
Anna
Boris
Alex
Alina
Dmitry
```

You can also use a lambda expression:

```java
users.stream().forEach(name -> System.out.println("User: " + name));
```

**Result:**

```
User: Anna
User: Boris
User: Alex
User: Alina
User: Dmitry
```

#### Important: forEach terminates the stream

After calling forEach, the stream is "closed." Cannot continue the chain:

```java
users.stream()
    .filter(name -> name.startsWith("A"))
    .forEach(System.out::println)
    .map(String::toUpperCase); // Error! Stream is already closed.
```

Attempting to call anything after forEach will result in a compilation error: the terminal operation returns void, not a new stream.

### 2. The peek method: peeking, but not interfering

peek is an **intermediate** operation. It allows you to perform an action on each element at a specific stage of processing without modifying the element itself or terminating the stream.

**Method signature:**

```java
Stream<T> peek(Consumer<? super T> action)
```

- peek returns a new stream in which the action will be performed on each element. - Typically used for debugging, logging, or monitoring stream state.

#### Example: Logging after filtering

```java
List<String> users = List.of("Anna", "Boris", "Alex", "Alina", "Dmitry");

List<Integer> nameLengths = users.stream()
    .filter(name -> name.startsWith("A"))
    .peek(name -> System.out.println("Passed filter: " + name))
    .map(String::length)
    .collect(Collectors.toList());
```

**Result in console:**

```
Passed filter: Anna
Passed filter: Alex
Passed filter: Alina
```

**Contents of nameLengths:**

```java
[4, 4, 5]
```

#### Where is peek useful?

- For debugging a chain of operations: see what happens at each stage.
- For collecting statistics (for example, counting the number of elements).
- For logging data at intermediate stages.

**Important:** peek should not be used to modify stream elements. Map is used for transformations. peek means "look at," not "interfere."

### 3. forEach vs. peek: What's the difference?

| **Method** | **Operation type** | **When is it used** | **Can the chain continue?** | **What is it best suited for** |
| --- | --- | --- | --- | --- |
| `forEach` | Terminal | At the very end of stream processing | No | Final actions (output, logging, writing to the database) |
| `peek` | Intermediate | In the middle of a chain of operations | Yes | Debugging, intermediate logging, counting |

**Example: Differences in Usage**

```java
// Example with forEach
users.stream()
    .filter(name -> name.startsWith("A"))
    .map(String::toUpperCase)
    .forEach(System.out::println); // The stream ends here

// Example with peek
// The chain can be continued
users.stream()
    .filter(name -> name.startsWith("A"))
    .peek(name -> System.out.println("Filter passed: " + name))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

#### Important to remember

- forEach is a point of no return: after it, nothing more can be done with the stream.
- peek does not guarantee execution unless a terminal operation is called. If you write only a chain of intermediate operations, nothing will happen.

### 4. Non-obvious points: forEach isn't always the best choice!

#### Why shouldn't you use forEach to modify collections?

Many beginners try to use forEach to modify collection elements or the collection itself (for example, removing elements). But this is bad practice: streams are not designed to modify the underlying collections.

**Example of incorrect usage:**

```java
List<String> names = new ArrayList<>(List.of("Anna", "Boris", "Alex"));
    names.stream().forEach(name -> {
        if (name.startsWith("A")) {
            names.remove(name); // May result in ConcurrentModificationException!
        }
    });
```

**Result:** runtime error – you can't modify the collection while traversing a stream (ConcurrentModificationException).

#### Why use forEach anyway?

- For display (e.g., printing a report).
- For writing to a log.
- For calling external services (e.g., sending an email).
- For collecting statistics (e.g., incrementing a counter).

### 5. Once again about peek: for debugging purposes only!

I'm really tempted to use peek to modify elements, for example, to increment the user's age:

```java
users.stream()
    .peek(user -> user.setAge(user.getAge() + 1)) // Bad!
    .collect(Collectors.toList());
```

**Why is it bad?**

- This breaks the declarative nature and purity of the Stream API.
- This code becomes difficult to maintain and test.
- Side effects in intermediate operations can lead to unobvious bugs.

It's better to use map to transform data:

```java
List<User> olderUsers = users.stream()
    .map(user -> new User(user.getName(), user.getAge() + 1))
    .collect(Collectors.toList());
```

#### Diagram: the difference between forEach and peek

```
users.stream()
    .filter(...) // intermediate operation
    .peek(...) // intermediate operation, "peeking"
    .map(...) // intermediate operation
    .forEach(...) // terminal operation, "doing the action"
```

**Explanation:**

— Everything before forEach can be combined, rearranged, or added.

— After forEach, the stream is closed.

### 6. Common Mistakes When Working with forEach and peek

**Mistake №1: Using peek to modify data.** peek is intended only for observing, not modifying, stream elements. For transformations, use map.

**Mistake №2: Expecting peek to always execute.** peek executes only if it is followed by a terminal operation (collect, forEach, count, etc.). Without a terminal operation, nothing happens.

**Mistake №3: Trying to continue the stream after forEach.** forEach is a terminal operation. You cannot call other stream methods after it.

**Mistake №4: Modifying a collection inside forEach.** Modifying the original collection (removing or adding elements) while traversing through forEach is a surefire way to get a ConcurrentModificationException.

**Mistake №5: Using forEach instead of collect to collect results.** If you want to collect elements into a new collection, use collect(Collectors.toList()) instead of forEach with manual addition. This breaks declarativity and can lead to errors in multithreaded scenarios.

---

🔥

**Task#0_1 Basic filter + forEach (output)**

```java
List<Integer> numbers = List.of(3, 10, 15, 20, 25, 30);
```

Print **only numbers greater than 15** without creating a new list.

---

💡

**Task#0_2 map + collect (transformation)**

```java
List<String> words = List.of("java", "stream", "api", "code");
```

1. Convert to uppercase
2. Collect into a list
3. Return the result

---

💡

**Task#0_3 peek + filter (thread debugging)**

```java
List<Integer> data = List.of(5, 10, 15, 20, 25);
```

1. Keep multiples of 10
2. Use `peek` to print:

```
"Passed filter: X"
```

3. Collect into a list

---

💡

**Task#0_4 objects + filter + map**

```java
class Student {
String name;
int score;

Student(String name, int score) {
this.name = name;
this.score = score;
}
}
```

```java
List<Student> students = List.of(
new Student("Anna", 90),
new Student("Boris", 45),
new Student("Alex", 75),
new Student("Ivan", 60)
);
```

1. Keep students with a score ≥ 60
2. Convert to strings:

```
"Anna passed"
```

3. Collect into a list

---
