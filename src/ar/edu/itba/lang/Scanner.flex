package ar.edu.itba.lang;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java.io.Reader;


%%
/*-*
 * LEXICAL FUNCTIONS:
 */

%cupsym Symbols
%cup
%line
%column
%unicode
%class Lexer

%{
private StringBuffer sb;
private ComplexSymbolFactory symbolFactory;
private int csline, cscolumn;

public Lexer(Reader reader, ComplexSymbolFactory sf) {
    this(reader);
    symbolFactory = sf;
}

public Symbol symbol(String name, int code) {
    return symbolFactory.newSymbol(name, code, new Location(yyline+1,yycolumn+1-yylength()), new Location(yyline+1,yycolumn+1));
}

public Symbol symbol(String name, int code, Object lexem) {
    return symbolFactory.newSymbol(name, code, new Location(yyline+1, yycolumn +1), new Location(yyline+1,yycolumn+yylength()), lexem);
}

protected void emit_warning(String message) {
    // ErrorManager.getManager().emit_warning("Scanner at " + (yyline+1) + "(" + (yycolumn+1) + "): " + message);
}

protected void emit_error(String message) {
    // ErrorManager.getManager().emit_error("Scanner at " + (yyline+1) + "(" + (yycolumn+1) +  "): " + message);
}

%}

/*-*
 * PATTERN DEFINITIONS:
 */
letter          = [A-Za-z]
digit           = [0-9]
alphanumeric    = {letter}|{digit}
other_id_char   = [_]
identifier      = {letter}({alphanumeric}|{other_id_char})*
integer         = {digit}+
real            = {integer}\.{integer}
char            = [^\r\n]*
line_terminator = \r|\n|\r\n
comment_body    = {char}*\n
comment         = "#" {comment_body}* {line_terminator}?
whitespace      = [ \n\t]

%%
/**
 * LEXICAL RULES:
 */
"*"             { return symbol("*", Symbols.TIMES); }
"+"             { return symbol("+", Symbols.PLUS); }
"-"             { return symbol("-", Symbols.MINUS); }
"/"             { return symbol("/", Symbols.DIVIDE); }
"("             { return symbol("(", Symbols.LEFT_PAREN); }
")"             { return symbol(")", Symbols.RIGHT_PAREN); }
{integer}       { return symbol("int", Symbols.INT, new Integer(yytext())); }
{real}          { return symbol("double", Symbols.REAL, new Double(yytext())); }
{comment}       { /* Ignore comment. */ }
{whitespace}    { /* Ignore whitespace. */ }
