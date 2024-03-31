package com.RandomProblemSolving.CompilerConstruction;//package com.RandomProblemSolving.CompilerConstruction;
//
//Last edited on 13 jun 2023 by SMS
//}

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyLexicalAnalyzerFinalForNow {

//   public class Token {
//        String tokenType;
//        String tokenValue;
//        int line;
//
//        public Token(String tokenValue, int line) {
//            this.tokenType = "undefined";
//            this.tokenValue = tokenValue;
//            this.line = line;
//        }
//
//        public String displayToken() {
//            String string = "\t" + this.tokenType + ",   \t" + this.tokenValue + ",    \t" + this.line + " ";
//            return string;
//        }
//    }
    //file reading
    static String Filename = "src/com/RandomProblemSolving/CompilerConstruction/Truecode.txt";
//    static String Filename = "src/com/RandomProblemSolving/CompilerConstruction/Errorcode.txt";


    //===============
    public static boolean intConst(String word) {
        String pattern = "(^[+|-]?[0-9]+$)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(word);
        return m.matches();
    }

    public static boolean floatConst(String word) {
        String pattern = "(^[+|-]?[0-9]*[.][0-9]+$)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(word);
        return m.matches();
    }

    public static boolean charConst(String word) {
        String pattern = "(\'[\\sA-Za-z0-9-!@$#%^&*()+=;:<>,.?/{}\\[\\]|]\')";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(word);
        return m.matches();
    }

    public static boolean stringConst(String word) {
        String pattern = "(\".*\")";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(word);
        return m.matches();
    }

    public static boolean isIdentifier(String word) {
        String pattern = "([A-Za-z]\\w*)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(word);
        return m.matches();
    }

    public Token generateToken(String word, int line, int i) {
        Token currentToken = new Token(word, line);
        i++;
        return currentToken;
    }

    public static String readUsingFiles(String FileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(Filename)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

/// BREAKWORDS HERE

    public List<Token> breakWords(String filename) {

        String inputString=readUsingFiles(Filename);

        String word = "";
        int line = 1;
        List<Token> tokenList = new ArrayList<Token>();
        List<String> single_breakers = Arrays.asList(",", ";", "(", ")", "{", "}", "[", "]",
                "-", "+", "*", "/", "%", "=", ">", "<", "@", "!");
        List<String> combo_breakers = Arrays.asList("||", "&&", ">=", "<=", "!=",
                "++", "--", "+=", "-=", "*=", "/=", "==");
        int i = 0;
        while (i < inputString.length()) {
//            char checking = inputString.charAt(i);

            // Checking ASCII of New Line character
            if (inputString.charAt(i) == '\n' || inputString.charAt(i) == '\r') {
                if (!word.equals("")) {
                    Token currentToken = generateToken(word, line, i);
                    currentToken=classifyToken(currentToken);
                    if(!currentToken.getTokenType().equals("INVALID")){

                    tokenList.add(currentToken);
                     }
//                    else{
//
//                    }

                    word = "";
                    i = i + 1;
//                    tokenList.add(currentToken);
                } else {
                    i++;
                }
                if (inputString.charAt(i-1)=='\n')
                   line++; // ek bar /r k lie bhi new line krrha hia aur ek br /n k lie bhi
                continue;
            }

            // Checking ASCII of Space
            if (inputString.charAt(i) == ' ') {
                if (!word.equals("")) {
                    Token currentToken = generateToken(word, line, i);
                    currentToken=classifyToken(currentToken);
                    if(!currentToken.getTokenType().equals("INVALID")){

                    tokenList.add(currentToken);
                }
//                    else{
//
//                    }

                    word = "";
                    i = i + 1;
//                    tokenList.add(currentToken);
                } else {
                    i++;
                }
                continue;
            }

            // Checking for .
            if (inputString.charAt(i) == '.') {
                if (!word.equals("")) {
                    // If word contains only numbers then dot is a decimal
                    if (word.matches("\\d+")) {

                        //if word is digit and dot k baad bhi digit hai then is should move forward
                        //masla h idhr  25 may , check last tokens
                        word += inputString.charAt(i); // dot ko concatenate krlia 99.a lerha h yw
                        i++;
                    }
                    // If not then word and dot are separate
                    else {
                        Token currentToken = generateToken(word, line, i);
                        currentToken=classifyToken(currentToken);
                        if(!currentToken.getTokenType().equals("INVALID")){

                            tokenList.add(currentToken);
                        }
//                    else{
//
//                    }

                        word = "";
                        i = i + 1;
//                        tokenList.add(currentToken);
                        i--;
                    }
                } // shayad ye bracket ghlt lagi hui hai , ye neeche wale iff k baad honi chaiiye thi

                if (i < inputString.length())//length -1
                {
//                    var ab=99.a6d3;
//                    var abc=99.6d3;
                    // If next character of dot is a number then dot is part of next and word continues
//                    checking = inputString.charAt(i + 1);
                    if (Character.isDigit(inputString.charAt(i + 1))) // checking variable gaya hua tha isdigit k andar
                    {
                        System.out.println(inputString.charAt(i+1)+","+ word);
                        System.out.println(inputString.charAt(i)+" , " + word);
                        word += inputString.charAt(i);
                        i++;
                    }
                    // If next character of dot is not a number then dot is a separate word
                    else {
//                        word += inputString.charAt(i);
                        Token currentToken = generateToken(word, line, i);
                        currentToken=classifyToken(currentToken);
                        if(!currentToken.getTokenType().equals("INVALID")){

                            tokenList.add(currentToken);
                        }
//                    else{
//
//                    }

                        word = "";
//                        i = i + 1;//i=i+1  check last test value of float for this
//                        tokenList.add(currentToken);
                    }
                }

                continue;
            }

            // Checking for Starting of a String (with double quote)
            if (inputString.charAt(i) == '\"') {
                if (!word.equals("")) {
                    Token currentToken = generateToken(word, line, i);
                    currentToken=classifyToken(currentToken);
                    if(!currentToken.getTokenType().equals("INVALID")){

                    tokenList.add(currentToken);
                }
//                    else{
//
//                    }

//                    tokenList.add(currentToken);
                    word="";
                    i--;
                }

//                word = Character.toString(inputString.charAt(i));
                word += inputString.charAt(i);
                i++;

                // String continues till File Ends, Next Double Quote or New Line Character
                while (i < inputString.length()) {
//                    checking = inputString.charAt(i);
                    if (i + 1 < inputString.length()) {
                        if (inputString.charAt(i) == '\\') {
//                            checking = inputString.charAt(i)+inputString.charAt(i+1);
                            word += inputString.charAt(i)+inputString.charAt(i+1);
                            i++;
                            if (i + 1 < inputString.length()) {
                                i++;
                                continue;
                            } else {
                                break;
                            }
                        }
                    }

                    if (inputString.charAt(i) != '\"' && inputString.charAt(i) != '\n') {
                        word += inputString.charAt(i);
                        i++;
                    } else {
                        break;
                    }
                }

                if (inputString.charAt(i) == '\n') {
                    line++;
                }

                // Breaking String if Double Quote Appear
                if (i < inputString.length() && inputString.charAt(i) == '\"') {
                    word += inputString.charAt(i);
                }

                Token currentToken = generateToken(word, line, i);
                currentToken=classifyToken(currentToken);
                if(!currentToken.getTokenType().equals("INVALID")){

                    tokenList.add(currentToken);
                }
//                    else{
//
//                    }

                System.out.println(word);
                word="";
//                tokenList.add(currentToken);
                i++;// receltadeed
                continue;
            }

            // Checking for Starting of a Character (with single quote)
            if (inputString.charAt(i) == '\'') {
                if (!word.equals("")) {
                    Token currentToken = generateToken(word, line, i);
                    currentToken=classifyToken(currentToken);
                    if(!currentToken.getTokenType().equals("INVALID")){

                    tokenList.add(currentToken);
                     }
//                    else{
//
//                    }

                    word="";
//                    tokenList.add(currentToken);
//                    i -= 1;
                }
                word += inputString.charAt(i);
                i = i + 1;

                // idhr hai masla
                // Character continues till File Ends, New Line Character or Length (3 or 4)
                int can_be = 3;
                while (i < inputString.length() && inputString.charAt(i) != '\n' && word.length() < can_be) {
//                    checking = inputString.charAt(i);
                    word = word + inputString.charAt(i);
                    if (word.charAt(1) == '\'') {// iske andar // ye tha
                        can_be = 4;
                    }
                    i = i + 1;
                }

                Token currentToken = generateToken(word, line, i);
                currentToken=classifyToken(currentToken);
                if(!currentToken.getTokenType().equals("INVALID")){
                    System.out.println("added as it is not invalid:"+currentToken.getTokenType());
                    tokenList.add(currentToken);
                }
//                    else{
//
//                    }

//                tokenList.add(currentToken);
                word="";
                i++;
                i -= 1;
                continue;
            }
//            // Combo Breaking Word Appear  // chances are k ye neeche bhi likha hua hai
//            if (i + 1 < inputString.length()) {
//                checking = String.valueOf(new char[]{inputString.charAt(i), char[i + 1]});//196
//            }
//            if (i + 1 < inputString.length() && combo_breakers.contains(checking)) {
//                if (!word.equals("")) {
//                    Token currentToken = generateToken(word, line, i);
//            currentToken=classifyToken(currentToken);
//            if(!currentToken){
//
//            }
//            else{
//
//            }

//                    tokenList.add(currentToken);
//                    i = i - 1;
//                }
//                word = String.valueOf(new char[]{inputString.charAt(i), char[i + 1]});
//                Token currentToken = generateToken(word, line, i);
//            currentToken=classifyToken(currentToken);
//            if(!currentToken){
//
//            }
//            else{
//
//            }

//                tokenList.add(currentToken);
//                i = i + 1;
//                continue;
//            }
//
//            // Single Character Breaking Word Appear
//            if (single_breakers.contains(String.valueOf(inputString.charAt(i)))) {
//                if (!word.equals("")) {
//                    Token currentToken = generateToken(word, line, i);
//            currentToken=classifyToken(currentToken);
//            if(!currentToken){
//
//            }
//            else{
//
//            }

//                    tokenList.add(currentToken);
//                    i = i - 1;
//                }
//
//                word = String.valueOf(inputString.charAt(i));
//                Token currentToken = generateToken(word, line, i);
//            currentToken=classifyToken(currentToken);
//            if(!currentToken){
//
//            }
//            else{
//
//            }

//                tokenList.add(currentToken);
//                continue;
//            }
//              ye rha upr jesa likha hua
            // Combo Breaking Word Appear
//            if (i + 1 < inputString.length()) {
//                checking = Character.toString(inputString.charAt(i)) + Character.toString( char[i + 1]);//189
//            }
            if (i + 1 < inputString.length() && combo_breakers.contains(inputString.charAt(i)+inputString.charAt(i+1))) {
                if (!word.equals("")) {
                    Token currentToken = generateToken(word, line, i);
                    currentToken=classifyToken(currentToken);
                    if(!currentToken.getTokenType().equals("INVALID")){

                        tokenList.add(currentToken);
                    }
//                    else{
//
//                    }
//                    tokenList.add(currentToken);
                    word="";
                    i = i - 1;
                }
                word += inputString.charAt(i)+inputString.charAt(i+1);//196
                Token currentToken = generateToken(word, line, i);
                currentToken=classifyToken(currentToken);
                if(currentToken.getTokenType().equals("INVALID")){
                    System.out.println("not adding this as it is invalid:"+currentToken.getTokenType());
                    tokenList.add(currentToken);
                }
//                    else{
//
//                    }
//                    tokenList.add(currentToken);
                word="";
                i = i + 1;
                continue;
            }

            // Single Character Breaking Word Appear
            if (single_breakers.contains(inputString.charAt(i))) {
                if (!word.equals("")) {
                    Token currentToken = generateToken(word, line, i);
                    currentToken=classifyToken(currentToken);
                    if(!currentToken.getTokenType().equals("INVALID")){

                        tokenList.add(currentToken);
                    }
//                    else{
//
//                    }
//                    tokenList.add(currentToken);
                    word="";
                    i = i - 1;
                }
                word += inputString.charAt(i);
                Token currentToken = generateToken(word, line, i);
                currentToken=classifyToken(currentToken);
                if(!currentToken.getTokenType().equals("INVALID")){

                    tokenList.add(currentToken);
                }
//                    else{
//
//                    }
//                    tokenList.add(currentToken);
                word="";
                continue;
            }
//python wale ka
//            // Comment Character Appear
//            if (inputString.charAt(i) == '/' && inputString.charAt(i + 1) == '/' ) {
//                if (!word.equals("")) {
//                    Token currentToken = generateToken(word, line, i);
//            currentToken=classifyToken(currentToken);
//            if(!currentToken){
//
//            }
//            else{
//
//            }

//                    tokenList.add(currentToken);
//                    word="";
//                } else {
////                    i = i + 1;//+2? due to // and not single character
//                    i = i + 2;//+2? due to // and not single character
//                }
//
//
//
//                if (i < inputString.length()) {
////                    String checking = Character.toString(inputString.charAt(i)); //  i dont think ts needed , as it was not used below
//                    // Multi Line Comment Characters Appear
//                    if (inputString.charAt(i) == '/' && inputString.charAt(i+1)=='*') {
////                        i = i + 1;// i+2????
//                        i = i + 2;// i+2????
//                        // Iteration till the Ending Comment character Appear or File Ends
//                        while (i < inputString.length()) {
////                            checking = Character.toString(inputString.charAt(i));
//                            if (inputString.charAt(i) == '\n') {
//                                line += 1;
//                            }
//                            if (inputString.charAt(i) == '*' && inputString.charAt(i+1)=='/'){
////                                i = i + 1;
//                                i = i + 2;
//                                break;
//                            }
//                            i = i + 1;
//                        }
//                        i = i + 1;
//                        continue;
//                        // Single Line Comment Character
//                    } else {
//                        // Iteration till the line ends or File Ends
//                        while (i < inputString.length()) {
////                            checking = Character.toString(inputString.charAt(i));
//                            if (inputString.charAt(i) == '\n') {
//                                line += 1;
//                                break;
//                            }
//                            i = i + 1;
//                        }
//                        i = i + 1;
//                        continue;
//                    }
//                }
//            }
//            //comment wale ka hai

            //my own
            if (inputString.charAt(i) == '/' && inputString.charAt(i + 1) == '/' )

            {

                if (!word.equals("")) {
                    Token currentToken = generateToken(word, line, i);
                    currentToken=classifyToken(currentToken);
                    if(!currentToken.getTokenType().equals("INVALID")){

                       tokenList.add(currentToken);
                    }
//                    else{
//
//                    }
//                    tokenList.add(currentToken);

                    word = "";
                } else {
//                    i = i + 1;//+2? due to // and not single character
                    i = i + 2;//+2? due to // and not single character
                    while (i < inputString.length() && inputString.charAt(i) != '\n') {
                        i++;
                    }
                    if (inputString.charAt(i)=='\n')
                          line++;
                          i++;
                    System.out.println("ignored single line comment");
                    continue;
                }
            }

            // Multi Line Comment Characters Appear
            if (inputString.charAt(i) == '/' && inputString.charAt(i + 1) == '*'){
//                        i = i + 1;// i+2????
                if (!word.equals("")) {
                    Token currentToken = generateToken(word, line, i);
                    currentToken=classifyToken(currentToken);
                    if(!currentToken.getTokenType().equals("INVALID")){
                        tokenList.add(currentToken);

                    }
//                    else{
//
//                    }
//
//                    tokenList.add(currentToken);
                    word = "";
                } else {

                    i = i + 2;// i+2????
                    // Iteration till the Ending Comment character Appear or File Ends
                    while (i < inputString.length() ) {
//                            checking = Character.toString(inputString.charAt(i));
                        if (inputString.charAt(i) == '*' && inputString.charAt(i + 1) == '/'){
                                break;
                        }
                        if (inputString.charAt(i) == '\n') {
                            line += 1;
                        }
                        i++;
                    }
                    i = i + 2;
                    System.out.println("ignored multiline comment");
                    continue;
                    // multi Line Comment Character
                }
            }
            // Character added to word if no breaking occurs
            word += inputString.charAt(i);
            i = i + 1;
        }
            // End of File with last word not Breaked
            if (!word.equals("")) {
                Token currentToken = generateToken(word, line, i);
                currentToken=classifyToken(currentToken);
                if(!currentToken.getTokenType().equals("INVALID")){

                tokenList.add(currentToken);
                }
//                else{
//
//                }

//                tokenList.add(currentToken);
                word="";
            }

            //  End Marker token... to ensure complete tree and complete inputString parsing
//            tokenList.add(new Token("~", line));


            return tokenList;
    }


// CLASSIFY TOKENS

//            public List<Token> classifyToken(List<Token> tokenList) {
            public Token classifyToken(Token token) {
//                keywords = { it is not inclluded (changed)"do": "DO", "while": "WHILE", it is also not inlcuded(changed) "default": "DEFAULT", not inlcuded(changed) "condensed": "CONDENSED",
//                       as interface(changed it) "symbol": "SYMBOL", ham baghair fun k fun banayenge "function": "FUNCTION", as this(changed in MYlex) "self": "CHAIN",
//                        "public": "PUBLIC"should be as access modifier, "private": "PRIVATE"same, as protected(changed) "preserved": "PRESERVED"samw, as extends(changed)"expands": "EXPANDS",
//                       as implementss(changed) "applies": "APPLIES",as final(changed) "concrete": "CONCRETE",
//                       ,not included (changed along with multiarr whihc and its relatives) but 1580 ki trf masla askta hai in SA "dict": "DICT",  we will not use this "Main": "MAIN_METHOD"}
               Map<String,String> KeywordsinMap=new HashMap<>(Map.of("~", "EOF", "false", "BOOL", "true", "BOOL",
                       "if", "IF", "else", "ELSE",/*"do", "DO",*/ "while", "WHILE","for", "FOR",
                       "continue", "CONTINUE",
                       "break", "BREAK"));
               KeywordsinMap.putAll(Map.of("return", "RETURN","void", "VOID", "class", "CLASS",
                       "interface", "INTERFACE", "static", "STATIC", "function", "FUNCTION", "this", "CHAIN",
                       "parent", "CHAIN",
                        "public", "PUBLIC", "private", "PRIVATE"));
               KeywordsinMap.putAll(Map.of( "protected", "PROTECTED", "extends", "EXTENDS","implements", "IMPLEMENTS",
                       "final", "FINAL", "try", "TRY", "catch", "CATCH", "finally", "FINALLY",
                       "throw", "THROW",  "int", "DT", "string", "DT"));
               KeywordsinMap.putAll(Map.of("exception", "EXCEPTION",
                        "bool", "DT", "float", "DT", "char", "DT", "Main", "MAIN_METHOD", "new", "NEW"));
//

//                List keywords = new ArrayList() {
//                    {
//                        add("true");
//                        add("false");
//                        add("if");
//                        add("else");
//                        add("elseif");
//                        add("while");
//                        add("for");
//                        add("continue");
//                        add("break");
//                        add("return");
//                        add("void");
//                        add("class");
//                        add("static");
//                        add("final");
//                        add("this");
//                        add("parent");
//                        add("public");
//                        add("private");
//                        add("protected");
//                        add("extends");
//                        add("interface");
//                        add("implements");
//                        add("try");
//                        add("catch");
//                        add("finally");
//                        add("throw");
//                        add("int");
//                        add("string");
//                        add("exception");
//                        add("bool");
//                        add("float");
//                        add("char");
//                        add("main");
//                        add("new");
//
//                    }
//                };

//                operators = {"+", "P_M", "-", "P_M", "*", "M_D_M", "/", "M_D_M", "%", "M_D_M", "<", "R_OP", "<=", "R_OP", ">", "R_OP",
//                        ">=", "R_OP", "!=", "R_OP", "==", "R_OP", "&&", "AND", "||", "OR", "!", as NOT_OP(have changed it in  MysyntaxAlyzr)"EXCLAIM", "=", "ASSIGN",
//                        "++", "INC_DEC", "--", "INC_DEC", "+=", "COMP_ASSIGN", "-=", "COMP_ASSIGN", "*=", "COMP_ASSIGN",
//                        "/=", "COMP_ASSIGN"}
                Map<String,String> OperatorsinMap=new HashMap<>(Map.of("+", "P_M", "-", "P_M", "*", "M_D_M", "/", "M_D_M", "%", "M_D_M", "<", "R_OP", "<=", "R_OP", ">", "R_OP",
                        ">=", "R_OP","!=", "R_OP"));
                OperatorsinMap.putAll(Map.of("==", "R_OP", "&&", "AND", "||", "OR", "!", "NOT_OP", "=", "ASSIGN",
                        "++", "INC_DEC", "--", "INC_DEC", "+=", "COMP_ASSIGN", "-=", "COMP_ASSIGN", "*=", "COMP_ASSIGN"));
                OperatorsinMap.put( "/=", "COMP_ASSIGN");

//                List operators = new ArrayList()
//                {
//                    {
//                        add("+");
//                        add("-");
//                        add("/");
//                        add("*");
//                        add("%");
//                        add("<");
//                        add(">");
//                        add("<=");
//                        add(">=");
//                        add("!=");
//                        add("==");
//                        add("&&");
//                        add("||");
//                        add("!");
//                        add("=");
//                        add("++");
//                        add("--");
//                        add("+=");
//                        add("-=");
//                        add("*=");
//                        add("/=");
//                    }
//                };
//                punctuators = {";", "SEMI_COL", ",", "COMMA", "(", "O_PARAN", ")", "C_PARAN", "{", "O_BRACE", "}", "C_BRACE",
//                        "[", "O_BRACK", "]", "C_BRACK", ".", "TERMINATOR", ",": "COLON"}
                Map<String,String> PunctuatorsinMap=new HashMap<>(Map.of(";", "SEMI_COL", ",", "COMMA", "(", "O_PARAN", ")", "C_PARAN", "{", "O_BRACE", "}", "C_BRACE",
                        "[", "O_BRACK", "]", "C_BRACK", ".", "TERMINATOR", ":", "COLON"));




//                List punctuators = new ArrayList() {
//                    {
//                        add(";");
//                        add(",");
//                        add(")");
//                        add("(");
//                        add("}");
//                        add("{");
//                        add("[");
//                        add("]");
//                        add(".");
//                        add(":");
//                    }
//                };

//                for (int j = 0; j < tokenList.size(); j++) {
//                    Token token=tokenList.get(j);
//                    if (keywords.contains(token.tokenValue)) {
                    if (KeywordsinMap.containsKey(token.tokenValue)){
//                            token.tokenType="KEYWORD";
                            token.tokenType=KeywordsinMap.get(token.tokenValue);
//                        continue;
                        return token;
                    }

//                    if (operators.contains(token.tokenValue)) {
                    if (OperatorsinMap.containsKey(token.tokenValue)) {
//                            token.tokenType="OPERATOR";
                            token.tokenType=OperatorsinMap.get(token.tokenValue);
//                        continue;
                        return token;
                    }

//                    if (punctuators.contains(token.tokenValue)) {
                    if (PunctuatorsinMap.containsKey(token.tokenValue)) {

//                            token.tokenType="PUNCTUATOR";
                            token.tokenType=PunctuatorsinMap.get(token.tokenValue);
//                        continue;
                        return token;
                    }

                    if (intConst(token.tokenValue)) {
                        token.tokenType=("INT");
//                        continue;
                        return token;
                    }

                    if (floatConst(token.tokenValue)) {
                        token.tokenType=("FLT");
//                        continue;
                        return token;
                    }

                    if (charConst(token.tokenValue)) {
                        token.tokenType=("CHAR");
//                        token.tokenValue=token.tokenValue.substring(1, token.tokenValue.length() - 1));
//                        continue;
                        return token;
                    }

                    if (stringConst(token.tokenValue)) {
                        token.tokenType=("STR");
//                        token.tokenValue=token.tokenValue.substring(1, token.tokenValue.length() - 1));
//                        continue;
                        return token;
                    }

                    if (isIdentifier(token.tokenValue)) {
                        token.tokenType=("ID");
//                        continue;
                        return token;
                    }

                    System.out.println("This is not a correct token at line"+token.line);
                    token.tokenType=("INVALID");

//                    tokenList.remove(token);
//                    if (token.tokenType=="INVALID") tokenList.remove(tokenList.indexOf(token));


//                }

//                return tokenList;
                return token;


            }

// GENERATE OUTPUT

            public String generateOutput (List < Token > tokenList) {
                String output = "TOKENLIST:\n" +
                        "TKN ID \t TKNTYPE \t TKNVAL \t TKNATLINE\n";
                for (int i = 0; i < tokenList.size(); i++) {
                    if (i == tokenList.size() - 1) {
                        output += i + "\t" + tokenList.get(i).displayToken();
//                        System.out.println(output);
                        continue;
                    }
                    output += i + "\t" + tokenList.get(i).displayToken() + "\n";
                }
                System.out.println(output);
                return output;
            }

// TO READ A FILE WE HAVE TO USE MAIN FUNC IN JAVA  I COMMENTED THE CODE

// public static void main(String[] args) throws IOException {
//     BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Sufiyan\\OneDrive\\Desktop\\Python work\\Compiler work\\inputFile.txt"));
//     String input_text = "";
//     String line = reader.readLine();
//     while (line != null) {
//         input_text += line + "\n";
//         line = reader.readLine();
//     }
//     reader.close();

//     ArrayList<Token> generatedTokens = breakWords(input_text);
//     ArrayList<Token> classifiedTokens = classifyToken(generatedTokens);

//     String output_text = generateOutput(classifiedTokens);

//     FileWriter writer = new FileWriter("C:\\Users\\Sufiyan\\OneDrive\\Desktop\\Python work\\Compiler work\\outputFile.txt");
//     writer.write(generateOutput(classifyToken(breakWords(input_text))));
//     writer.close();
// }
        }
//    }
//}
