package com.RandomProblemSolving.CompilerConstruction;

public class Token {

    String tokenType;
    String tokenValue;
    int line;

    public Token(String tokenValue, int line) {
        this.tokenType = "undefined";
        this.tokenValue = tokenValue;
        this.line = line;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String displayToken() {
        String string = "\t" + this.tokenType + ",   \t" + this.tokenValue + ",    \t" + this.line + " ";
        return string;
    }

}
