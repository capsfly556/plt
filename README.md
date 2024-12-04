
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



# Workflow of scan function

 It scans each character to categorize and group it into recognizable tokens like keywords, identifiers, literals, operators, delimiters, or errors, making it useful in building a simple lexer for parsing programming languages or other structured text. Here's a breakdown of how it works:

1. **Initialize Tokens List**:
   - A list `tokens` is created to store the `Token` objects that represent each recognized part of the input.
   
2. **Loop Through Characters**:
   - A `while` loop iterates through each character in `input` based on its index `i`.

3. **Skip Whitespace**:
   - If the current character is whitespace, it’s ignored, and the loop moves to the next character by incrementing `i`.

4. **Handle Keywords or Identifiers**:
   - If the current character is a letter, the code assumes it could be the start of a keyword or identifier.
   - A `StringBuilder` is used to collect consecutive letter or digit characters.
   - Once the complete word is gathered, it checks if it matches any known keywords (in `KEYWORDS`). If it matches, a keyword token is added. Otherwise, it’s treated as an identifier.

5. **Handle Number Literals**:
   - If the current character is a digit, the code collects all consecutive digits to form a number literal.
   - Once the complete number is gathered, it’s added as a `LITERAL` token.

6. **Handle Operators**:
   - If the current character matches an operator, it checks if it’s recognized (in `OPERATORS`).
   - If recognized, it adds an operator token. Otherwise, it adds an error token indicating an unknown operator.

7. **Handle Delimiters**:
   - If the character is in `DELIMITERS` (a set of known single-character delimiters like parentheses, commas, etc.), it adds a corresponding delimiter token.

8. **Handle Unrecognized Characters**:
   - If a character doesn’t match any known category, it’s treated as an unrecognized token, and an error token is added, identifying it as a lexical error.

9. **End of Input**:
   - After processing all characters, it adds an `EOF` (End of File) token to signify the end of the input stream.

This `scan` function returns a list of `Token` objects, each representing a segment of the input that has been classified. This list can then be used for further parsing or processing by other components of a lexer or parser.



# Workflow of Parser class

The `parse` function in this `Parser` class is a top-down parser that analyzes a list of tokens generated by a lexer, then organizes and translates them into a structured Abstract Syntax Tree (AST) representing a program’s structure. This type of parser is common for processing structured input, such as code or expressions, in a programming language. Here’s a detailed explanation of each part of this parser:

### Main Components of the Parser

1. **Initialization**:
   - The parser is initialized with a list of tokens, and `position` is set to `0` to keep track of the current token being analyzed.
   - The `parseProgram` function is the entry point of the parser, calling it initiates parsing of the entire token list.

2. **parseProgram**:
   - This method creates a list to store statement nodes, calling `parseStatement` repeatedly to parse each statement until the end of the token list.
   - After collecting all statements, it returns a `ProgramNode`, which holds the list of statements in the program.

3. **parseStatement**:
   - This method identifies the type of statement based on the current token type and directs parsing accordingly:
     - **If Statement** (`IF` token): Calls `parseIfStatement`.
     - **Block Statement** (`LBRACE` token): Calls `parseBlock`.
     - **Assignment Statement** (Identifier): Calls `parseAssignmentStatement`.
   - If none of these match, it throws an error indicating an unexpected token.

4. **parseIfStatement**:
   - Parses an `if` statement structure, starting with `IF` and expecting an opening parenthesis `(`.
   - Parses the condition expression inside the parentheses by calling `parseExpression`.
   - Expects a closing parenthesis `)` and then parses the subsequent statement, typically a block or single statement to execute if the condition is true.
   - Returns an `IfStatementNode` containing the condition and the statement(s) to execute.

5. **parseAssignmentStatement**:
   - Parses an assignment statement, expecting an identifier followed by an `=` symbol, an expression, and a semicolon `;`.
   - Calls `parseExpression` to parse the assigned expression, then returns an `AssignmentNode` with the identifier and parsed expression.

6. **parseBlock**:
   - Parses a block of statements enclosed in braces `{}`.
   - Repeatedly calls `parseStatement` until a closing brace `}` is found, indicating the end of the block.
   - Returns a `BlockNode` containing the parsed statements within the block.

### Expression Parsing Methods

7. **parseExpression**:
   - Parses expressions by looking for addition (`+`) or subtraction (`-`) operators, which it treats as binary operations.
   - Calls `parseTerm` to parse the left side of the expression, then checks for additional `+` or `-` operators to parse the right side.
   - Constructs and returns a `BinaryExpressionNode` if operators are found, building a left-to-right binary expression tree.

8. **parseTerm**:
   - Parses terms by looking for multiplication (`*`) or division (`/`) operators, again as binary operations.
   - Calls `parseFactor` to parse the left side of the term, then checks for additional `*` or `/` operators to parse the right side.
   - Constructs and returns a `BinaryExpressionNode` for terms involving these operators.

9. **parseFactor**:
   - Parses the most basic units of expressions:
     - **Identifier**: If the token is an identifier, creates an `IdentifierNode`.
     - **Literal**: If the token is a literal value (e.g., a number), creates a `LiteralNode`.
     - **Parentheses**: If the token is an opening parenthesis `(`, it calls `parseExpression` to evaluate the expression within the parentheses, expecting a closing parenthesis `)`.
   - Returns an appropriate node for each type.

### Helper Methods

10. **Helper Functions**:
    - `match`: Checks if the current token matches any provided types and advances if it does.
    - `check`: Checks if the current token matches a specific type without advancing.
    - `advance`: Moves to the next token.
    - `isAtEnd`: Checks if the parser has reached the end of the token list.
    - `peek` and `previous`: Get the current and previous tokens, respectively.
    - `consume`: Ensures the current token matches an expected type and advances; otherwise, throws an error.
    - `error`: Creates a `ParseException` with an error message.

### Error Handling

If unexpected tokens are encountered during parsing, the `error` function generates a `ParseException`, which interrupts parsing and indicates where the error occurred.

### Youtube link
https://youtu.be/1_NT4my9zGs

## Project Structure

```
├── org
│   └── group
│       ├── ASTNode.java
│       ├── CodeGenerator.java
│       ├── Lexer.java
│       ├── Parser.java
│       └── [Other Java files if any]
├── run_compiler.sh
└── README.md
```



## Detailed Description of Each Step

### 1. Lexical Analysis (Scanning)

- **File:** `Lexer.java`
- **Purpose:** Reads the input source code and converts it into a list of tokens.
- Process:
  - Defines token types (keywords, operators, delimiters, identifiers, literals).
  - Uses regular expressions and character checks to identify tokens.
  - Handles errors for unrecognized tokens.
- **Output:** A list of tokens representing the input source code.

### 2. Parsing

- **File:** `Parser.java`
- **Purpose:** Parses the list of tokens to build an Abstract Syntax Tree (AST).
- Process:
  - Implements recursive descent parsing methods for expressions, statements, and program structures.
  - Handles grammatical rules of the language.
  - Throws parse exceptions for syntax errors.
- **Output:** An AST representing the hierarchical structure of the source code.

### 3. Abstract Syntax Tree (AST) Nodes

- **File:** `ASTNode.java`

- **Purpose:** Defines the various node types used in the AST.

- Node Types:

  - `ProgramNode`

  - ```
    StatementNode
    ```

     and its subclasses:

    - `AssignmentNode`
    - `IfStatementNode`
    - `BlockNode`

  - ```
    ExpressionNode
    ```

     and its subclasses:

    - `BinaryExpressionNode`
    - `IdentifierNode`
    - `LiteralNode`

- Functionality:

  - Each node has a `print` method for debugging and visualization.
  - Nodes are used by the parser and code generator.

### 4. Code Generation

- **File:** `CodeGenerator.java`
- **Purpose:** Traverses the AST to generate three-address code.
- Process:
  - Maintains a temporary variable counter and a list of generated code lines.
  - Implements methods to handle each type of AST node.
    - **ProgramNode:** Generates code for each statement.
    - **AssignmentNode:** Generates code for expressions and assigns the result.
    - **IfStatementNode:** Generates conditional branching code with labels.
    - **BinaryExpressionNode:** Generates code for binary operations using temporary variables.
    - **IdentifierNode & LiteralNode:** Handles variables and constants.
  - Simplifies expressions where possible and removes redundant code.
- **Output:** A list of three-address code instructions representing the input program.



## Sample Input Programs and Expected Outputs

### Sample Program 1: Simple Assignment Statement

**Input:**

```plaintext
x = a + 5;
```

**Tokens:**

```
<IDENTIFIER, 'x'>
<ASSIGN, '='>
<IDENTIFIER, 'a'>
<PLUS, '+'>
<LITERAL, '5'>
<SEMICOLON, ';'>
<EOF, ''>
```

**AST:**

```
Program
  Assignment
    Identifier: x
    Expression: 
      BinaryExpression (PLUS)
        Left: 
          Identifier: a
        Right: 
          Literal: 5
```

**Generated Code:**

```plaintext
t0 = 5
t1 = a + t0
x = t1
```

### Sample Program 2: Simple If Statement

**Input:**

```plaintext
if (x) { y = x - 1; }
```

**Tokens:**

```
<IF, 'if'>
<LPAREN, '('>
<IDENTIFIER, 'x'>
<RPAREN, ')'>
<LBRACE, '{'>
<IDENTIFIER, 'y'>
<ASSIGN, '='>
<IDENTIFIER, 'x'>
<MINUS, '-'>
<LITERAL, '1'>
<SEMICOLON, ';'>
<RBRACE, '}'>
<EOF, ''>
```

**AST:**

```
Program
  IfStatement
    Condition: 
      Identifier: x
    Then: 
      Block
        Assignment
          Identifier: y
          Expression: 
            BinaryExpression (MINUS)
              Left: 
                Identifier: x
              Right: 
                Literal: 1
```

**Generated Code:**

```plaintext
ifFalse x goto else_0
t1 = 1
t2 = x - t1
y = t2
goto endif_1
else_0:
endif_1:
```

### Sample Program 3: Nested Expressions

**Input:**

```plaintext
z = (x + y) * (a - b);
```

**Tokens:**

```
<IDENTIFIER, 'z'>
<ASSIGN, '='>
<LPAREN, '('>
<IDENTIFIER, 'x'>
<PLUS, '+'>
<IDENTIFIER, 'y'>
<RPAREN, ')'>
<MULTIPLY, '*'>
<LPAREN, '('>
<IDENTIFIER, 'a'>
<MINUS, '-'>
<IDENTIFIER, 'b'>
<RPAREN, ')'>
<SEMICOLON, ';'>
<EOF, ''>
```

**AST:**

```
Program
  Assignment
    Identifier: z
    Expression: 
      BinaryExpression (MULTIPLY)
        Left: 
          BinaryExpression (PLUS)
            Left: 
              Identifier: x
            Right: 
              Identifier: y
        Right: 
          BinaryExpression (MINUS)
            Left: 
              Identifier: a
            Right: 
              Identifier: b
```

**Generated Code:**

```plaintext
t0 = x + y
t1 = a - b
t2 = t0 * t1
z = t2
```

### Sample Program 4: Input with Errors

**Input:**

```plaintext
if x { y = 2; }
```

**Output:**

```
Parse error at token 'x': Expected '(' after 'if'
```

### Sample Program 5: Multiple Statements

**Input:**

```plaintext
{ x = 1; y = x + 2; }
```

**Tokens:**

```
<LBRACE, '{'>
<IDENTIFIER, 'x'>
<ASSIGN, '='>
<LITERAL, '1'>
<SEMICOLON, ';'>
<IDENTIFIER, 'y'>
<ASSIGN, '='>
<IDENTIFIER, 'x'>
<PLUS, '+'>
<LITERAL, '2'>
<SEMICOLON, ';'>
<RBRACE, '}'>
<EOF, ''>
```

**AST:**

```
Program
  Block
    Assignment
      Identifier: x
      Expression: 
        Literal: 1
    Assignment
      Identifier: y
      Expression: 
        BinaryExpression (PLUS)
          Left: 
            Identifier: x
          Right: 
            Literal: 2
```

**Generated Code:**

```plaintext
t0 = 1
x = t0
t1 = 2
t2 = x + t1
y = t2
```



## 



## Contribution and Collaboration

- All team members contributed through collaborative coding sessions.
- Pull requests and code reviews were used to manage contributions.
- GitHub issues and project boards were used for task tracking.

