package ar.edu.itba.primer;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java.io.Reader;

import static ar.edu.itba.primer.Symbols.*;

%%
/*-*
 * LEXICAL FUNCTIONS:
 */

%cupsym Symbols
%cup
%line
%column
%unicode
%public
%class Lexer

%{
private ComplexSymbolFactory symbolFactory;
private int csline, cscolumn;
private String filename = "unknown";

public Lexer(Reader reader, ComplexSymbolFactory sf) {
    this(reader);
    symbolFactory = sf;
}

public void setFilename(String filename) {
    this.filename = filename;
}

public Symbol symbol(String name, int code) {
    Location left = new Location(filename, yyline + 1, yycolumn + 1 - yylength());
    Location right = new Location(filename, yyline + 1, yycolumn + 1);
    return symbolFactory.newSymbol(name, code, left, right);
}

public Symbol symbol(String name, int code, Object lexem) {
    Location left = new Location(filename, yyline+1, yycolumn +1);
    Location right = new Location(filename, yyline+1, yycolumn+yylength());
    return symbolFactory.newSymbol(name, code, left, right, lexem);
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
char            = [^\r\n]
line_terminator = \r|\n|\r\n
comment_body    = {char}*\n
comment         = "#" {comment_body}
whitespace      = [ \n\t]

/* TODO: Improve
 * See https://code.google.com/p/jsyntaxpane/source/browse/branches/r095/jsyntaxpane/src/main/jflex/jsyntaxpane/lexers/python.flex?r=112 */
string          = "\"" [^\"]* "\""

%%
/**
 * LEXICAL RULES:
 */
"*"                 { return symbol("*", TIMES); }
"+"                 { return symbol("+", PLUS); }
"-"                 { return symbol("-", MINUS); }
"/"                 { return symbol("/", DIVIDE); }
"%"                 { return symbol("%", MODULUS); }
"("                 { return symbol("(", LEFT_PAREN); }
")"                 { return symbol(")", RIGHT_PAREN); }
{integer}           { return symbol(Integer.valueOf(yytext()).toString(), INT, Integer.valueOf(yytext())); }
{comment}           { /* Ignore comment. */ }
{whitespace}        { /* Ignore whitespace. */ }
{line_terminator}   { /* Ignore newlines */ }
{string}            { String s = yytext();
                      return symbol("string", STRING, s.substring(1, s.length() - 1)); }
"def"               { return symbol("def", DEF); }
"while"             { return symbol("while", WHILE); }
"if"                { return symbol("if", IF); }
"else"              { return symbol("else", ELSE); }
"false"             { return symbol("false", FALSE); }
"true"              { return symbol("true", TRUE); }
"return"            { return symbol("return", RETURN); }
"nil"               { return symbol("nil", NIL); }
"continue"          { return symbol("continue", CONTINUE); }
"break"             { return symbol("break", BREAK); }
"{"                 { return symbol("{", OPEN_BRACKET); }
"}"                 { return symbol("}", CLOSE_BRACKET); }
","                 { return symbol(",", COMMA); }
"&&"                { return symbol("&&", AND); }
"||"                { return symbol("||", OR); }
"<"                 { return symbol("<", LESS_THAN); }
"<="                { return symbol("<=", LESS_EQUAL_THAN); }
">"                 { return symbol(">", GREATER_THAN); }
">="                { return symbol(">=", GREATER_EQUAL_THAN); }
"=="                { return symbol("==", EQUAL); }
"!="                { return symbol("!=", NOT_EQUAL); }
"var"               { return symbol("var", VAR); }
"="                 { return symbol("=", ASSIGN); }
"+="                { return symbol("+=", ASSIGN_PLUS); }
"-="                { return symbol("+=", ASSIGN_MINUS); }
"*="                { return symbol("+=", ASSIGN_TIMES); }
"/="                { return symbol("+=", ASSIGN_DIVIDE); }
"%="                { return symbol("%=", ASSIGN_MODULUS); }
{identifier}        { return symbol(yytext(), IDENTIFIER, yytext()); }
