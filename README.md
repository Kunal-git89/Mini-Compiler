# Mini Compiler / Transpiler (Java)

## Overview
A Java-based compiler/transpiler that converts a custom-designed language into Java code and executes it.  
The project demonstrates core compiler design concepts including lexical analysis, syntax parsing, semantic validation, and code transformation.

---

## Features
- Lexical Analysis (Tokenization)
- Syntax Parsing
- Basic Semantic Validation
- Code Transformation to Java
- Automated Code Execution

---

## Workflow
Custom Source Code → Tokens → Parsed Structure → Transformed Java Code → Execution

---

## Tech Stack
- Java

---

## Example

### Input (from `Test.txt`)
```c
let a = 0;
input a;

while(a <= 100) {
    if(a == 95) {
        a = a + 1;
        continue;
    }
    print a;
    a = a + 1;
}
