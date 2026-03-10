public class Lexer
{
    String program;
    int cchar = 0, state = 0, start = 0;
    char c;

    private char getcurchar()
    {
        if(cchar >= program.length()) return '\0';
        return program.charAt(cchar);
    }

    private char peek()
    {
        if(cchar + 1>= program.length()) return '\0';
        return program.charAt(cchar + 1);
    }

    private void advance()
    {
        char temp;
        do
        {
            cchar++;
            temp = getcurchar();
        }while(Character.isWhitespace(temp));
        c = getcurchar();
    }

    Token nextToken()
    {
        if(cchar >= program.length()) return new Token(Token.Token_type.EOF);
        state = 0;
        start = cchar;
        if(c == '\0') return new Token(Token.Token_type.EOF);
        do
        {
            switch (state)
            {
                case 0 :
                {
                    switch (c)
                    {
                        case '+' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Add);

                        case '-' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Minus);

                        case '*' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Multiply);

                        case '/' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Divide);

                        case '=' :
                            if(peek() == '=')
                            {
                                advance();
                                advance();
                                state = 0;
                                return new Token(Token.Token_type.Equals);
                            }
                            advance();
                            return new Token(Token.Token_type.Assign);

                        case ';' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Semicolon);

                        case '(' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Lp);

                        case ')' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Rp);

                        case '{' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.LBrac);

                        case '}' :
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.RBrac);

                        case '!' :
                            if(peek() == '=')
                            {
                                advance();
                                advance();
                                state = 0;
                                return new Token(Token.Token_type.NotEquals);
                            }
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Error);

                        case '>' :
                            if(peek() == '=')
                            {
                                advance();
                                advance();
                                state = 0;
                                return new Token(Token.Token_type.GE);
                            }
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Greater);

                        case'<' :
                            if(peek() == '=')
                            {
                                advance();
                                advance();
                                state = 0;
                                return new Token(Token.Token_type.LE);
                            }
                            advance();
                            state = 0;
                            return new Token(Token.Token_type.Less);

                        case 'l' :
                            state = (peek() == 'e') ? 2 : 4;
                            continue;

                        case 'i' :
                            if(peek() == 'f') state = 5;
                            else if(peek() == 'n') state = 6;
                            else state = 4;
                            continue;

                        case 'e' :
                            state = peek() == 'l' ? 10 : 4;
                            continue;

                        case 'p' :
                            state = peek() == 'r' ? 15 : 4;
                            continue;

                        case 'w' :
                            state = peek() == 'h' ? 19 : 4;
                            continue;

                        case 'c' :
                            state = peek() == 'o' ? 23 : 4;
                            continue;

                        case 'b' :
                            state = peek() == 'r' ? 30 : 4;
                            continue;

                        default :
                            if(c >= '0' && c <= '9')
                            {
                                state = 1;
                                continue;
                            }
                            else if(Character.isLetter(c))
                            {
                                state = 4;
                                continue;
                            }
                            else
                            {
                                advance();
                                state = 0;
                                return new Token(Token.Token_type.Error);
                            }
                    }
                }

                case 1 : // digits , contants
                {
                    while (peek() >= '0' && peek() <= '9') advance();
                    int index = cchar + 1;
                    advance();
                    return new Token(Token.Token_type.Constant, Integer.parseInt(program.substring(start, index)));
                }

                case 2 : //le
                {
                    advance();
                    state = peek() == 't' ? 3 : 4;
                    continue;
                }

                case 3 : // let keyword
                {
                    advance();
                    if (!Character.isLetterOrDigit(peek())) {
                        state = 0;
                        advance();
                        return new Token(Token.Token_type.Let);
                    }
                    state = 4;
                    continue;
                }

                case 4 : // identifier dump
                {
                    char temp = peek();
                    while(Character.isLetterOrDigit(temp))
                    {
                        advance();
                        temp = peek();
                    }
                    int index;
                    index = cchar + 1;
                    advance();
                    return new Token(Token.Token_type.Identifier, program.substring(start, index));
                }

                case 5 : // if keyword
                {
                    advance();
                    if (!Character.isLetterOrDigit(peek()))
                    {
                        state = 0;
                        advance();
                        return new Token(Token.Token_type.If);
                    }
                    state = 4;
                    continue;
                }

                case 6 : // in
                {
                    advance();
                    state = peek() == 'p' ? 7 : 4;
                    continue;
                }

                case 7 : // inp
                {
                    advance();
                    state = peek() == 'u' ? 8 : 4;
                    continue;
                }

                case 8 : //inpu
                {
                    advance();
                    state = peek() == 't' ? 9 : 4;
                    continue;
                }

                case 9 : // input keyword
                {
                    advance();
                    if(!Character.isLetterOrDigit(peek()))
                    {
                        state = 0;
                        advance();
                        return new Token(Token.Token_type.Input);
                    }
                    state = 4;
                    continue;
                }

                case 10 : //el
                {
                    advance();
                    state = peek() == 's' ? 11 : 4;
                    continue;
                }

                case 11 : // els
                {
                    advance();
                    state = peek() == 'e' ? 12 : 4;
                    continue;
                }

                case 12 : //else keyword
                {
                    advance();
                    char temp = peek();
                    if(temp == 'i')
                    {
                        state = 13;
                        continue;
                    }
                    else if(Character.isLetterOrDigit(temp))
                    {
                        state = 4;
                        continue;
                    }
                    else
                    {
                        advance();
                        state = 0;
                        return new Token(Token.Token_type.Else);
                    }
                }

                case 13 : //elsei
                {
                    advance();
                    state = peek() == 'f' ? 14 : 4;
                    continue;
                }

                case 14 : //elseif keyword
                {
                    advance();
                    if(!Character.isLetterOrDigit(peek()))
                    {
                        state = 0;
                        advance();
                        return new Token(Token.Token_type.Elseif);
                    }
                    state = 4;
                    continue;
                }

                case 15 : // pr
                {
                    advance();
                    state = peek() == 'i' ? 16 : 4;
                    continue;
                }

                case 16 : // pri
                {
                    advance();
                    state = peek() == 'n' ? 17 : 4;
                    continue;
                }

                case 17 : //prin
                {
                    advance();
                    state = peek() == 't' ? 18 : 4;
                    continue;
                }

                case 18 : //print keyword
                {
                    advance();
                    if(!Character.isLetterOrDigit(peek()))
                    {
                        advance();
                        state = 0;
                        return new Token(Token.Token_type.Print);
                    }
                    state = 4;
                    continue;
                }

                case 19 : //wh
                {
                    advance();
                    state = peek() == 'i' ? 20 : 4;
                    continue;
                }

                case 20 : // whi
                {
                    advance();
                    state = peek() == 'l' ? 21 : 4;
                    continue;
                }

                case 21 : //whil
                {
                    advance();
                    state = peek() == 'e' ? 22 : 4;
                    continue;
                }

                case 22 : //while keyword
                {
                    advance();
                    if(!Character.isLetterOrDigit(peek()))
                    {
                        advance();
                        state = 0;
                        return new Token(Token.Token_type.While);
                    }
                    state = 4;
                    continue;
                }

                case 23 : //co
                {
                    advance();
                    state = peek() == 'n' ? 24 : 4;
                    continue;
                }

                case 24 : // con
                {
                    advance();
                    state = peek() == 't' ? 25 : 4;
                    continue;
                }

                case 25 : //cont
                {
                    advance();
                    state = peek() == 'i' ? 26 : 4;
                    continue;
                }

                case 26 : //conti
                {
                    advance();
                    state = peek() == 'n' ? 27 : 4;
                    continue;
                }

                case 27 : //contin
                {
                    advance();
                    state = peek() == 'u' ? 28 : 4;
                    continue;
                }

                case 28 : //continu
                {
                    advance();
                    state = peek() == 'e' ? 29 : 4;
                    continue;
                }

                case 29 : //continue keyword
                {
                    advance();
                    if(!Character.isLetterOrDigit(peek()))
                    {
                        advance();
                        state = 0;
                        return new Token(Token.Token_type.Continue);
                    }
                    state = 4;
                    continue;
                }

                case 30 : //br
                {
                    advance();
                    state = peek() == 'e' ? 31 : 4;
                    continue;
                }

                case 31 : //bre
                {
                    advance();
                    state = peek() == 'a' ? 32 : 4;
                    continue;
                }

                case 32 : //brea
                {
                    advance();
                    state = peek() == 'k' ? 33 : 4;
                    continue;
                }

                case 33 : //break keyword
                {
                    advance();
                    if(!Character.isLetterOrDigit(peek()))
                    {
                        advance();
                        state = 0;
                        return new Token(Token.Token_type.Break);
                    }
                    state = 4;
                    continue;
                }
            }

        }while(!Character.isWhitespace(c));
        return new Token(Token.Token_type.Error);
    }

    public Lexer(String s)
    {
        program = s;
        c = getcurchar();
    }
}