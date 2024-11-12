
### Team Members
- **Chunyu Sui**, cs4480
- **Huiyuan Li**, hl3700

### Sample Output
Below is a screenshot of the lexer program in action. You can refer to it for a basic understanding of the workflow.

![Lexer Sample Output](img.png)

### Project description
**Custom Lexer for Programming Language**

### Overview
This project implements a custom lexer in Java, designed to process input source code written in a specified language and output a list of tokens. The lexer is built with Spring Boot and can be easily executed using Maven commands or the provided shell script.

### Features
- **Tokenization of Source Code**: The lexer breaks down input source code into distinct token types, such as keywords, identifiers, literals, operators, and delimiters.
- **State Transitions**: Implements finite automata for lexical analysis to manage state transitions.
- **Error Handling**: Detects and reports lexical errors, including unrecognized tokens.

### Prerequisites

#### Installation Requirements
Ensure the following are installed on your system:
1. **Java Development Kit (JDK)**: Version 17 or later.
2. **Maven**: Make sure Maven is installed and properly configured.

Check if Java and Maven are installed by running:

```bash
java -version
mvn -version
```

If they are not installed, follow these steps:

- **Install Java**:
    - **Linux (Ubuntu/Debian)**:
      ```bash
      sudo apt update
      sudo apt install openjdk-17-jdk
      ```
    - **macOS**:
      ```bash
      brew install openjdk@17
      ```
    - **Windows**:
        - Download and install the JDK from the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).

- **Install Maven**:
    - **Linux**:
      ```bash
      sudo apt install maven
      ```
    - **macOS**:
      ```bash
      brew install maven
      ```
    - **Windows**:
        - Download from [Maven](https://maven.apache.org/download.cgi) and follow the installation instructions.

### Running the Lexer Using Spring Boot

1. **Ensure Dependencies are Installed**:
   Run the Maven `clean install` command to download dependencies and compile the project.

    ```bash
    mvn clean install
    ```

2. **Run the Lexer**:
   Execute the lexer using the provided shell script. This script simplifies the process by setting permissions and running the application.

    ```bash
    chmod a=rwx run_lexer.sh
    ./run_lexer.sh
    ```

   Inside the shell script, the following command runs the Spring Boot application:

    ```bash
    mvn spring-boot:run
    ```

3. **Modify the Input Program**:
   The lexer processes a hardcoded input in the `LexerApplication.java` file. You can update this file to include the source code you want the lexer to analyze.

4. **Example Output**:
   For an input program such as:

    ```c
    if (x == 10) return x;
    ```

   The lexer output will be:
    ```
    <KEYWORD, if>
    <DELIMITER, (>
    <IDENTIFIER, x>
    <OPERATOR, ==>
    <LITERAL, 10>
    <DELIMITER, )>
    <KEYWORD, return>
    <IDENTIFIER, x>
    <DELIMITER, ;>
    ```

### Error Handling
If the input code contains unrecognized tokens or other lexical errors, the lexer will print an error message indicating the problem and its position.

For example, for input:
```c
x @ 10;
```

The output will include:
```
<IDENTIFIER, x>
<ERROR, Unrecognized token '@' at index 2>
<LITERAL, 10>
<DELIMITER, ;>
```

### Grammar (CFG) Used by the Lexer and Parser

```
Program         -> StatementList

StatementList   -> Statement StatementList | ε

Statement       -> IfStatement
                | AssignmentStatement
                | Block

IfStatement     -> 'if' '(' Expression ')' Statement

AssignmentStatement -> IDENTIFIER '=' Expression ';'

Block           -> '{' StatementList '}'

Expression      -> Term (('+' | '-') Term)*

Term            -> Factor (('*' | '/') Factor)*

Factor          -> IDENTIFIER
                | LITERAL
                | '(' Expression ')'
```

### Explanation of the Grammar Rules

- **Program**: Represents the entire program, consisting of a `StatementList`.
- **StatementList**: A list of statements, either a single `Statement` followed by another `StatementList` or an empty list (`ε`).
- **Statement**: Represents a single statement, which can be an `IfStatement`, `AssignmentStatement`, or a `Block`.
- **IfStatement**: Represents an `if` statement with the keyword `if`, followed by a condition in parentheses (`Expression`), and then a `Statement` to execute if the condition is true.
- **AssignmentStatement**: An assignment operation where an `IDENTIFIER` is assigned the result of an `Expression`, ending with a semicolon (`;`).
- **Block**: A block of code, enclosed in curly braces (`{}`), containing multiple statements (`StatementList`).
- **Expression**: Consists of terms separated by `+` or `-` operators, allowing for expressions like `a + b - c`.
- **Term**: A term in an expression, containing factors separated by `*` or `/` operators, allowing for multiplication and division.
- **Factor**: Basic elements of an expression, which can be:
    - An `IDENTIFIER` (e.g., variable name),
    - A `LITERAL` (e.g., a number),
    - An expression in parentheses, enabling nested expressions (e.g., `(a + b)`).

### Terminal Symbols

The terminal symbols in this grammar are:

- Keywords: `if`
- Operators: `+`, `-`, `*`, `/`, `=`
- Delimiters: `(`, `)`, `{`, `}`, `;`
- `IDENTIFIER` and `LITERAL` for variable names and numeric literals.

### Non-terminal Symbols

The non-terminal symbols in this grammar are:

- `Program`
- `StatementList`
- `Statement`
- `IfStatement`
- `AssignmentStatement`
- `Block`
- `Expression`
- `Term`
- `Factor`

