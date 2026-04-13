## Overview
A Java-based Compiler/Transpiler that converts my languages into an java code and executes it 

## Features
- Lexical analysis (tokenization)
- Syntax parsing
- Basic semantic validation
- Code transformation
- code execution

## Example

Input: (Code in Test.txt file in main directory)
let a = 0;
input a;
while(a <= 100)
{
  if(a == 95)
  {
    a = a + 1;
    continue;
  }
  print a;
  a = a + 1;
}

Output: (if a's value is 91)
91
92
93
94
96
97
98
99
100
