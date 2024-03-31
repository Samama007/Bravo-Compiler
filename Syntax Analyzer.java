package com.RandomProblemSolving.CompilerConstruction;

import java.util.List;

public class MysyntaxAnalyzer {

        private static int i;
        private static List<Token> tokenList;
        private static int errorAt;
        private static String synError;

        public MysyntaxAnalyzer() {
            i = 0;
            tokenList = null;
            errorAt = 0;
            synError = "";
        }

        public /*Result*/List<Object> syntaxAnalyzer(List<Token> tokens) {
            i = 0;
            errorAt = 0;
            tokenList = tokens;
            boolean result = structure();
            if (!result) {
                synError += "\nTOKEN UNEXPECTED:\n\tValue:\t" + tokenList.get(errorAt).getTokenValue() +
                        "\n\tType:\t" + tokenList.get(errorAt).getTokenType() +
                        "\n\tFile:\t'.\\input.txt' [at line: " + tokenList.get(errorAt).getLine() + "]\n\tToken:\t" + errorAt + "\n\n\n";
                System.out.println("\nTOKEN UNEXPECTED:\n\tValue:\t" + tokenList.get(errorAt).getTokenValue() +
                        "\n\tType:\t" + tokenList.get(errorAt).getTokenType() +
                        "\n\tFile:\t'.\\input.txt' [at line: " + tokenList.get(errorAt).getLine() + "]\n\tToken:\t" + errorAt);
            }
            return List.of(result,synError);
//        return new Result(synError, result);
        }

        private static boolean syntaxError() {
            if (i > errorAt) {
                errorAt = i;
            }
            return false;
        }

        private static boolean structure() {
            if (tokenList.get(i).getTokenType().equals("STATIC")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("CLASS")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        i++;
                        if (inherit()) {
                            if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                                i++;
                                if (sCst()) {
                                    if (tokenList.get(i).getTokenType().equals("EOF")) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            } /* //not included in our lang
            else if (tokenList.get(i).getTokenType().equals("CONDENSED")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("CLASS")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        i++;
                        if (inherit()) {
                            if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                                i++;
                                if (cCst()) {
                                    if (tokenList.get(i).getTokenType().equals("EOF")) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            } */else if (final_/*was concrete_*/() && tokenList.get(i).getTokenType().equals("CLASS")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (inherit()) {
                        if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                            i++;
                            if (gCst()) {
                                if (tokenList.get(i).getTokenType().equals("EOF")) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("INTERFACE"/*was called symbol before*/)) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                        i++;
                        if (intSt()) {
                            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                                i++;
                                if (structure()) {
                                    if (tokenList.get(i).getTokenType().equals("EOF")) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return syntaxError();
        }

        private static boolean sCst() {
            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                i++;
                return structure();
            } else if (tokenList.get(i).getTokenType().equals("FUNCTION")) {
                i++;
                if (pubPriv_()) {
                    if (tokenList.get(i).getTokenType().equals("STATIC")) {
                        i++;
                        if (functionSig()) {
                            return sCst();
                        }
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("PRIVATE")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("STATIC")) {
                    i++;
                }
                if (classVars()) {
                    return sCst();
                }
            } else if (public_() && tokenList.get(i).getTokenType().equals("STATIC")) {
                i++;
                return sCst_();
            }
            return syntaxError();
        }

        private static boolean public_() {
            if (tokenList.get(i).getTokenType().equals("PUBLIC")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("STATIC") ||
                    tokenList.get(i).getTokenType().equals("FINAL"/*was concrete*/) ||
                    tokenList.get(i).getTokenType().equals("DT") ||
                    tokenList.get(i).getTokenType().equals("ID") ||
                    tokenList.get(i).getTokenType().equals("VOID") /*||
                   tokenList.get(i).getTokenType().equals("DICT") //not included in our language */) {
                return true;
            }
            return syntaxError();
        }

        private static boolean inherit() {
            if (extends_()/*was expands*/ && implements_/*was applies*/()) {
                return true;
            }
            return syntaxError();
        }

        private static boolean extends_() {
            if (tokenList.get(i).getTokenType().equals("EXTENDS"/*was expands*/)) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("IMPLEMENTS"/*was applies*/) ||
                    tokenList.get(i).getTokenType().equals("O_BRACE")) {
                return true;
            }
            return syntaxError();
        }

        private static boolean implements_() {
            if (tokenList.get(i).getTokenType().equals("IMPLEMENTS"/*was applies*/)) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return implements__();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                return true;
            }
            return syntaxError();
        }

        private static boolean implements__() {
            if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return implements__();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                return true;
            }
            return syntaxError();
        }


        public static boolean pubPriv_() {
            if (tokenList.get(i).getTokenType().equals("PUBLIC") || tokenList.get(i).getTokenType().equals("PRIVATE")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("STATIC")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean functionSig() {
            if (returnType() && tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                    i++;
                    if (argumentList() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                        i++;
                        if (bodyMST()) {
                            return true;
                        }
                    }
                }
            }
            return syntaxError();
        }

        public static boolean returnType() {
            if (tokenList.get(i).getTokenType().equals("DT") || tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return returnType_();
            } else if (tokenList.get(i).getTokenType().equals("VOID")) {
                i++;
                return true;
            } /*else if (tokenList.get(i).getTokenType().equals("DICT")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                        i++;
                        return true;
                    }
                }
                // not included in our lang
            }*/
            return syntaxError();
        }

        public static boolean returnType_() {
            if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean argumentList() {
            if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("DT") || tokenList.get(i).getTokenType().equals("ID") /*|| tokenList.get(i).getTokenType().equals("DICT") //not included in our langauge*/) {
                return argumentList_();
            }
            return syntaxError();
        }

        public static boolean argumentList_() {
            if (tokenList.get(i).getTokenType().equals("DT") || tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (argumentList___() && tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return argumentList__();
                }
            }
            /*
            if (tokenList.get(i).getTokenType().equals("DICT")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                        i++;
                        if (tokenList.get(i).getTokenType().equals("ID")) {
                            i++;
                            return argumentList__();
                        }
                    }
                } //not included in our language
            }*/
            return syntaxError();
        }

        public static boolean argumentList__() {
            if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                return argumentList_();
            }
            return syntaxError();
        }

        public static boolean argumentList___() {

            if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean bodyMST() {
            if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                i++;
                if (mst()) {
                    tokenList.get(i).getTokenType().equals("C_BRACE");
                    i++;
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean mst() {
            if (tokenList.get(i).getTokenType().equals("C_BRACE") /*|| tokenList.get(i).getTokenType().equals("CASE") || tokenList.get(i).getTokenType().equals("DEFAULT")//not included in our lang*/) {
                return true;
            }
            if (/*tokenList.get(i).getTokenType().equals("SWITCH") || not included in our lang*/ tokenList.get(i).getTokenType().equals("IF") || tokenList.get(i).getTokenType().equals("FOR") || tokenList.get(i).getTokenType().equals("WHILE") /*|| tokenList.get(i).getTokenType().equals("DO") //not inlcuded in our lange*/
                    || tokenList.get(i).getTokenType().equals("RETURN") || tokenList.get(i).getTokenType().equals("CONTINUE") || tokenList.get(i).getTokenType().equals("BREAK") || tokenList.get(i).getTokenType().equals("TRY") || tokenList.get(i).getTokenType().equals("INC_DEC")
                    || tokenList.get(i).getTokenType().equals("CHAIN") || tokenList.get(i).getTokenType().equals("DT") ||/* tokenList.get(i).getTokenType().equals("DICT")//not included in our lang ||*/ tokenList.get(i).getTokenType().equals("ID") || tokenList.get(i).getTokenType().equals("THROW")) {
                if (sst()) {
                    return mst();
                }
            }
            return syntaxError();
        }

        public static boolean sst() {
            /*if (tokenList.get(i).getTokenType().equals("SWITCH")) {
                return switchSt(); //not included in our lang
            } else*/ if (tokenList.get(i).getTokenType().equals("IF")) {
                return ifSt();
            } else if (tokenList.get(i).getTokenType().equals("FOR")) {
                return forSt();
            } else if (tokenList.get(i).getTokenType().equals("WHILE")) {
                return whileSt();
            } /*else if (tokenList.get(i).getTokenType().equals("DO")) {
                return doWhileSt();  //not included in our language
            } */
            else if (tokenList.get(i).getTokenType().equals("RETURN")) {
                return returnSt();
            } else if (tokenList.get(i).getTokenType().equals("CONTINUE")) {
                return continueSt();
            } else if (tokenList.get(i).getTokenType().equals("BREAK")) {
                return breakSt();
            } else if (tokenList.get(i).getTokenType().equals("TRY")) {
                return trySt();
            } else if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                if (sp_() && tokenList.get(i).getTokenType().equals("ID")) {
                    return ref();
                }
            } else if (tokenList.get(i).getTokenType().equals("CHAIN")) {//ye change hoga
                i++;
                if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        boolean check = ref();
                        if (check) {
                            check = assignOp();
                            if (check) {
                                check = expression();
                                if (check && tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                                    i++;
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("DT")) {
                i++;
                return nDec();
            } /*else if (tokenList.get(i).getTokenType().equals("DICT")) { //ye hange hoga
                i++;
                return multiArr(); //not inlcuded in our lang
            }*/
            else if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return sstID();
            } else if (tokenList.get(i).getTokenType().equals("THROW")) {
                i++;
                if (throw_() && tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    i++;
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean throw_() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("NEW")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("EXCEPTION")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        i++;
                        if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                            i++;
                            if (throw__() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                                return true;
                            }
                        }
                    }
                }
            }
            return syntaxError();
        }

        public static boolean throw__() {
            if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("STR")) {
                i++;
                return true;
            }
            return syntaxError();
        }
//not included in our lang
//        public static boolean switchSt() {
//            if (tokenList.get(i).getTokenType().equals("SWITCH")) {
//                i++;
//                if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
//                    i++;
//                    if (tokenList.get(i).getTokenType().equals("ID")) {
//                        i++;
//                        if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
//                            i++;
//                            if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
//                                i++;
//                                if (switchBody() && tokenList.get(i).getTokenType().equals("C_BRACE")) {
//                                    i++;
//                                    return true;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            return syntaxError();
//        }
//not included in our lang
//        public static boolean switchBody() {
//            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
//                return true;
//            } /*else if (tokenList.get(i).getTokenType().equals("CASE") || tokenList.get(i).getTokenType().equals("DEFAULT")) {
//                if (case_() && default_()) {
//                    return true; //not included in our lang
//                }
//            }*/
//            return syntaxError();
//        }
//
//        public static boolean case_() {
//            if (tokenList.get(i).getTokenType().equals("CASE")) {
//                i++;
//                if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
//                    i++;
//                    if (const_() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
//                        i++;
//                        if (tokenList.get(i).getTokenType().equals("COLON")) {
//                            i += 2;
//                            if (mst()) {
//                                return case_();
//                            }
//                        }
//                    }
//                }
//            } else if (/*tokenList.get(i).getTokenType().equals("DEFAULT") ||*/ tokenList.get(i).getTokenType().equals("C_BRACE")) {
//                return true;
//            }
//            return syntaxError();
//        }

        //not inlcuded in our language
//        public static boolean default_() {
//            if (tokenList.get(i).getTokenType().equals("DEFAULT")) {
//                i++;
//                if (tokenList.get(i).getTokenType().equals("COLON")) {
//                    i++;
//                    if (mst()) {
//                        return case_();
//                    }
//                }
//            } else if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
//                return true;
//            }
//            return syntaxError();
//        }

        public static boolean const_() {
            if (tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("BOOL")) {
                i++;
                return true;
            }
            return syntaxError();
        }

        public static boolean ifSt() {
            if (tokenList.get(i).getTokenType().equals("IF")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                    i++;
                    if (expression()) {
                        if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                            i++;
                            if (bodyMST() && oElse()) {
                                return true;
                            }
                        }
                    }
                }
            }
            return syntaxError();
        }

        public static boolean oElse() {
            if (tokenList.get(i).getTokenType().equals("ELSE")) {
                i++;
                return oElse_();
            } else if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                return true;
            }
            return syntaxError();
        }


        public static boolean oElse_() {
            if (tokenList.get(i).getTokenType().equals("IF")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                    i++;
                    if (expression()) {
                        if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                            i++;
                            if (bodyMST()) {
                                return oElse();
                            }
                        }
                    }
                }
            } else if (bodyMST()) {
                return true;
            }
            return syntaxError();
        }

        public static boolean expression() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("BOOL") || tokenList.get(i).getTokenType().equals("CHAIN") || tokenList.get(i).getTokenType().equals("ID")) {
                if (b() && a_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean a_() {
            if (tokenList.get(i).getTokenType().equals("OR")) {
                i++;
                if (b() && a_()) {
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("SEMI_COL") || tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("CHAIN") || tokenList.get(i).getTokenType().equals("ID") || tokenList.get(i).getTokenType().equals("COMMA") || tokenList.get(i).getTokenType().equals("C_BRACK") || tokenList.get(i).getTokenType().equals("C_PARAN") || tokenList.get(i).getTokenType().equals("C_BRACE") || tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("BOOL")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean b() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("BOOL") || tokenList.get(i).getTokenType().equals("CHAIN") || tokenList.get(i).getTokenType().equals("ID")) {
                if (c() && b_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean b_() {
            if (tokenList.get(i).getTokenType().equals("AND")) {
                i++;
                if (c() && b_()) {
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("SEMI_COL") || tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("CHAIN") || tokenList.get(i).getTokenType().equals("ID") || tokenList.get(i).getTokenType().equals("COMMA") || tokenList.get(i).getTokenType().equals("C_BRACK") || tokenList.get(i).getTokenType().equals("C_PARAN") || tokenList.get(i).getTokenType().equals("C_BRACE") || tokenList.get(i).getTokenType().equals("OR") || tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("BOOL")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean c() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ ||
                    tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("CHAR") ||
                    tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("STR") ||
                    tokenList.get(i).getTokenType().equals("BOOL") || tokenList.get(i).getTokenType().equals("CHAIN") ||
                    tokenList.get(i).getTokenType().equals("ID")) {
                if (e() && c_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean c_() {
            if (tokenList.get(i).getTokenType().equals("R_OP")) {
                i++;
                if (e() && c_()) {
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("SEMI_COL") || tokenList.get(i).getTokenType().equals("O_PARAN") ||
                    tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("CHAIN") ||
                    tokenList.get(i).getTokenType().equals("ID") || tokenList.get(i).getTokenType().equals("COMMA") ||
                    tokenList.get(i).getTokenType().equals("C_BRACK") || tokenList.get(i).getTokenType().equals("C_PARAN") ||
                    tokenList.get(i).getTokenType().equals("C_BRACE") || tokenList.get(i).getTokenType().equals("AND") ||
                    tokenList.get(i).getTokenType().equals("OR") || tokenList.get(i).getTokenType().equals("INT") ||
                    tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("CHAR") ||
                    tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("BOOL")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean e() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ ||
                    tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("CHAR") ||
                    tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("STR") ||
                    tokenList.get(i).getTokenType().equals("BOOL") || tokenList.get(i).getTokenType().equals("CHAIN") ||
                    tokenList.get(i).getTokenType().equals("ID")) {
                if (t() && e_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean e_() {
            if (tokenList.get(i).getTokenType().equals("P_M")) {
                i++;
                if (t() && e_()) {
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("SEMI_COL") || tokenList.get(i).getTokenType().equals("O_PARAN") ||
                    tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("CHAIN") ||
                    tokenList.get(i).getTokenType().equals("ID") || tokenList.get(i).getTokenType().equals("COMMA") ||
                    tokenList.get(i).getTokenType().equals("C_BRACK") || tokenList.get(i).getTokenType().equals("C_PARAN") ||
                    tokenList.get(i).getTokenType().equals("C_BRACE") || tokenList.get(i).getTokenType().equals("R_OP") ||
                    tokenList.get(i).getTokenType().equals("AND") || tokenList.get(i).getTokenType().equals("OR") ||
                    tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("FLT") ||
                    tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("STR") ||
                    tokenList.get(i).getTokenType().equals("BOOL")) {
                return true;
            }
            return syntaxError();
        }


        public static boolean t() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN") ||
                    tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ ||
                    tokenList.get(i).getTokenType().equals("INT") ||
                    tokenList.get(i).getTokenType().equals("CHAR") ||
                    tokenList.get(i).getTokenType().equals("FLT") ||
                    tokenList.get(i).getTokenType().equals("STR") ||
                    tokenList.get(i).getTokenType().equals("BOOL") ||
                    tokenList.get(i).getTokenType().equals("CHAIN") ||
                    tokenList.get(i).getTokenType().equals("ID")) {
                if (f() && t_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean t_() {
            if (tokenList.get(i).getTokenType().equals("M_D_M")) {
                i++;
                if (f() && t_()) {
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("SEMI_COL") ||
                    tokenList.get(i).getTokenType().equals("O_PARAN") ||
                    tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ ||
                    tokenList.get(i).getTokenType().equals("CHAIN") ||
                    tokenList.get(i).getTokenType().equals("ID") ||
                    tokenList.get(i).getTokenType().equals("COMMA") ||
                    tokenList.get(i).getTokenType().equals("C_BRACK") ||
                    tokenList.get(i).getTokenType().equals("C_PARAN") ||
                    tokenList.get(i).getTokenType().equals("C_BRACE") ||
                    tokenList.get(i).getTokenType().equals("P_M") ||
                    tokenList.get(i).getTokenType().equals("R_OP") ||
                    tokenList.get(i).getTokenType().equals("INT") ||
                    tokenList.get(i).getTokenType().equals("FLT") ||
                    tokenList.get(i).getTokenType().equals("CHAR") ||
                    tokenList.get(i).getTokenType().equals("STR") ||
                    tokenList.get(i).getTokenType().equals("BOOL")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean f() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    return true;
                }
            } else if (const_()) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/) {
                i++;
                return f();
            } else if (sp_() && tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return optF();
            }
            return syntaxError();
        }

        public static boolean sp_() {
            if (tokenList.get(i).getTokenType().equals("CHAIN")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                    i++;
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                return true;
            }
            return syntaxError();
        }


        public static boolean optF() {
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return optF();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return optF1();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (pl() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    return optF_();
                }
            } else if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("SEMI_COL") || tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("CHAIN") || tokenList.get(i).getTokenType().equals("ID") || tokenList.get(i).getTokenType().equals("COMMA") || tokenList.get(i).getTokenType().equals("C_BRACK") || tokenList.get(i).getTokenType().equals("C_PARAN") || tokenList.get(i).getTokenType().equals("C_BRACE") || tokenList.get(i).getTokenType().equals("M_D_M") || tokenList.get(i).getTokenType().equals("P_M") || tokenList.get(i).getTokenType().equals("R_OP") || tokenList.get(i).getTokenType().equals("AND") || tokenList.get(i).getTokenType().equals("OR") || tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("BOOL")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean optF1() {
            if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                return true;
            }
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return optF();
                }
            }
            return syntaxError();
        }

        public static boolean optF_() {
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return optF();
                }
            } else if (tokenList.get(i).getTokenType().equals("SEMI_COL") || tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("CHAIN") || tokenList.get(i).getTokenType().equals("ID") || tokenList.get(i).getTokenType().equals("COMMA") || tokenList.get(i).getTokenType().equals("C_BRACK") || tokenList.get(i).getTokenType().equals("C_PARAN") || tokenList.get(i).getTokenType().equals("C_BRACE") || tokenList.get(i).getTokenType().equals("M_D_M") || tokenList.get(i).getTokenType().equals("P_M") || tokenList.get(i).getTokenType().equals("R_OP") || tokenList.get(i).getTokenType().equals("AND") || tokenList.get(i).getTokenType().equals("OR") || tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("BOOL")) {
                return true;
            }
            return syntaxError();
        }


        public static boolean pl() {
            if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/ || tokenList.get(i).getTokenType().equals("INT") || tokenList.get(i).getTokenType().equals("CHAR") || tokenList.get(i).getTokenType().equals("FLT") || tokenList.get(i).getTokenType().equals("STR") || tokenList.get(i).getTokenType().equals("BOOL") || tokenList.get(i).getTokenType().equals("CHAIN") || tokenList.get(i).getTokenType().equals("ID")) {
                if (expression()) {
                    return pl_();
                }
            }
            return syntaxError();
        }


        public static boolean pl_() {
            if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (expression()) {
                    return pl_();
                }
            } else if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean forSt() {
            if (tokenList.get(i).getTokenType().equals("FOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                    i++;
                    if (st1() && expression() && tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                        i++;
                        if (st3() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                            return body();
                        }
                    }
                }
            }
            return syntaxError();
        }

        public static boolean body() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                return bodyMST();
            }
            return syntaxError();
        }

        public static boolean st1() {
            if (tokenList.get(i).getTokenType().equals("DT")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return declare_();
                }
            } else if (sp_() && tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (ref() && assignOp() && expression() && tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    i++;
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean ref() {
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return ref();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return ref_();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (pl() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                        i++;
                        if (tokenList.get(i).getTokenType().equals("ID")) {
                            i++;
                            return ref();
                        }
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("ASSIGN") || tokenList.get(i).getTokenType().equals("COMP_ASSIGN")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean ref_() {
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return ref();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return ref_();
                }
            } else if (tokenList.get(i).getTokenType().equals("ASSIGN") || tokenList.get(i).getTokenType().equals("COMP_ASSIGN")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean assignOp() {
            if (tokenList.get(i).getTokenType().equals("ASSIGN") || tokenList.get(i).getTokenType().equals("COMP_ASSIGN")) {
                i++;
                return true;
            }
            return syntaxError();
        }

        public static boolean st3() {
            if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                if (sp_() && tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return ref();
                }
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return forOpt();
            } else if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean forOpt() {
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return forOpt();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    return forOpt1();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (pl() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        i++;
                        return forOpt();
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                return forOpt_();
            } else if (assignOp() && expression()) {
                return forOpt_();
            }
            return syntaxError();
        }

        public static boolean forOpt1() {
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return forOpt();
                }
            } else if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                return forOpt_();
            } else if (assignOp() && expression()) {
                return forOpt_();
            }
            return syntaxError();
        }

        public static boolean forOpt_() {
            if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return forOpt();
                }
            }
            return syntaxError();
        }

        public static boolean whileSt() {
            if (tokenList.get(i).getTokenType().equals("WHILE")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                    i++;
                    if (expression() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                        i++;
                        return body();
                    }
                }
            }
            return syntaxError();
        }

//not included in our lang
//        public static boolean doWhileSt() {
//            if (tokenList.get(i).getTokenType().equals("DO")) {
//                i++;
//                if (bodyMST() && tokenList.get(i).getTokenType().equals("WHILE")) {
//                    i++;
//                    if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
//                        i++;
//                        if (expression() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
//                            i++;
//                            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
//                                i++;
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
//            return syntaxError();
//        }

        public static boolean returnSt() {
            if (tokenList.get(i).getTokenType().equals("RETURN")) {
                i++;
                if (return_() && tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    i++;
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean return_() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                return true;
            } else if (expression()) {
                return true;
            }
            return syntaxError();
        }

        public static boolean continueSt() {
            if (tokenList.get(i).getTokenType().equals("CONTNIUE")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    i++;
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean breakSt() {
            if (tokenList.get(i).getTokenType().equals("BREAK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    i++;
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean trySt() {
            if (tokenList.get(i).getTokenType().equals("TRY")) {
                i++;
                if (bodyMST() && catchFinally()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean catchFinally() {
            if (tokenList.get(i).getTokenType().equals("FINALLY")) {
                return finallyC();
            } else if (tokenList.get(i).getTokenType().equals("CATCH")) {
                if (catch_() && finallyC_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean finallyC() {
            if (tokenList.get(i).getTokenType().equals("FINALLY")) {
                i++;
                return bodyMST();
            }
            return syntaxError();
        }

        public static boolean catch_() {
            if (tokenList.get(i).getTokenType().equals("FINALLY") || tokenList.get(i).getTokenType().equals("C_BRACE")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("CATCH")) {
                return catch_();
            }
            return syntaxError();
        }

        public static boolean finallyC_() {
            if (tokenList.get(i).getTokenType().equals("FINALLY")) {
                i++;
                return bodyMST();
            } else if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean classVars() {
            if (tokenList.get(i).getTokenType().equals("DT")) {
                i++;
                return nDec();
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return oDec();
            }/* else if (tokenList.get(i).getTokenType().equals("dict")) {
                i++;
                return multiArr(); // not inlcuded in our lang
            }*/
            return syntaxError();
        }

        public static boolean consVar() {
            if (tokenList.get(i).getTokenType().equals("DT")) {
                i++;
                return nDec();
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return consVar_();
            }/* else if (tokenList.get(i).getTokenType().equals("dict")) {
                i++;
                return multiArr(); // not inlcuded in our lang
            }*/
            return syntaxError();
        }

        public static boolean consVar_() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (argumentList() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    return bodyMST();
                }
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return object_();
            } else if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                    i++;
                    return oArr_();
                }
            }
            return syntaxError();
        }

        public static boolean nDec() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return declare_();
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        i++;
                        return gArr_();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean declare_() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return declare_();
                }
            } else if (tokenList.get(i).getTokenType().equals("ASSIGN")) {
                i++;
                return initList();
            }
            return syntaxError();
        }

        public static boolean initList() {
            if (sp_() && tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return list1();
            } else if (const_()) {
                return list2();
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    return list2();
                }
            } else if (tokenList.get(i).getTokenType().equals("NOT_OP")/*was EXClaim before*/) {
                i++;
                if (f()) {
                    return initList_();
                }
            }
            return syntaxError();
        }

        public static boolean initList_() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return declare_();
                }
            }
            return syntaxError();
        }

        public static boolean list1() {
            if (tokenList.get(i).getTokenType().equals("ASSIGN")) {
                i++;
                return initList();
            } else if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return list1();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return list3();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (pl() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    return list2();
                }
            } else if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                return list2();
            } else if (list2()) {
                return true;
            }
            return syntaxError();
        }

        public static boolean list2() {
            if (tokenList.get(i).getTokenType().equals("M_D_M") || tokenList.get(i).getTokenType().equals("P_M") || tokenList.get(i).getTokenType().equals("R_OP") || tokenList.get(i).getTokenType().equals("AND") || tokenList.get(i).getTokenType().equals("OR") || tokenList.get(i).getTokenType().equals("SEMI_COL") || tokenList.get(i).getTokenType().equals("COMMA")) {
                if (t_() && e_() && c_() && b_() && a_() && initList_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean list3() {
            if (tokenList.get(i).getTokenType().equals("ASSIGN")) {
                i++;
                return initList();
            } else if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return list1();
                }
            } else if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                return list2();
            } else if (list2()) {
                return true;
            }
            return syntaxError();
        }

        public static boolean gArr_() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i += 2;
                    return gArr_();
                }
            } else if (tokenList.get(i).getTokenType().equals("ASSIGN")) {
                i++;
                if (initGArr() && gArr_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean initGArr() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("NEW")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("DT")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                        i++;
                        return initGArr_();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean initGArr_() {
            if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                    i++;
                    if (valGArr() && tokenList.get(i).getTokenType().equals("C_BRACE")) {
                        i++;
                        return true;
                    }
                }
            } else if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
                i++;
                return true;
            }
            return syntaxError();
        }

        public static boolean valGArr() {
            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                return true;
            } else if (const_() && valGArr_()) {
                return true;
            }
            return syntaxError();
        }

        public static boolean valGArr_() {
            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (const_() && valGArr_()) {
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean oDec() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return object_();
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        i++;
                        return oArr_();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean object_() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return object_();
                }
            } else if (tokenList.get(i).getTokenType().equals("ASSIGN")) {
                i++;
                if (initObject()) {
                    return object_();
                }
            }
            return syntaxError();
        }

        public static boolean initObject() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("NEW")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                        i++;
                        if (pl() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                            i++;
                            return true;
                        }
                    }
                }
            }
            return syntaxError();
        }

        public static boolean oArr_() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return oArr_();
                }
            } else if (tokenList.get(i).getTokenType().equals("ASSIGN")) {
                i++;
                if (initOArr()) {
                    return oArr_();
                }
            }
            return syntaxError();
        }

        public static boolean initOArr() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("NEW")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                        i++;
                        return initOArr_();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean initOArr_() {
            if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                    i++;
                    if (valOArr() && tokenList.get(i).getTokenType().equals("C_BRACE")) {
                        i++;
                        return true;
                    }
                }
            } else if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
                i++;
                return true;
            }
            return syntaxError();
        }

        public static boolean valOArr() {
            if (tokenList.get(i).getTokenType().equals("NEW")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                        i++;
                        if (pl() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                            i++;
                            return valOArr_();
                        }
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean valOArr_() {
            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("NEW")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        i++;
                        if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                            i++;
                            if (pl() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                                i++;
                                return valOArr_();
                            }
                        }
                    }
                }
            }
            return syntaxError();
        }
//not included in our lang as it is related with dict

//        public static boolean multiArr() {
//            if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
//                i++;
//                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
//                    i++;
//                    if (tokenList.get(i).getTokenType().equals("ID")) {
//                        i++;
//                        return multiArr_();
//                    }
//                }
//            }
//            return syntaxError();
//        }
//not included in our lang as it is related with dict
//        public static boolean multiArr_() {
//            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
//                i++;
//                return true;
//            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
//                i++;
//                if (tokenList.get(i).getTokenType().equals("ID")) {
//                    i++;
//                    return multiArr_();
//                }
//            } else if (tokenList.get(i).getTokenType().equals("ASSIGN")) {
//                i++;
//                if (initMultidim()) {
//                    return multiArr_();
//                }
//            }
//            return syntaxError();
//        }
//not included in our lang as it is related with multiarr which is related with dict

//        public static boolean initMultidim() {
//            if (tokenList.get(i).getTokenType().equals("ID")) {
//                i++;
//                return true;
//            } else if (tokenList.get(i).getTokenType().equals("NEW")) {
//                i++;
//                /*if (tokenList.get(i).getTokenType().equals("DICT")) { //hoskta h yahan ID aye
//                    i++;*/ // not included in our lang , shayd dict ka kuch relation how new se
//                    if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
//                        i++;
//                        return initMultidim_();
//                    }
//                //}
//            }
//            return syntaxError();
//        }
//not included in our lang as it is related with initmultidm which is related with multiarr which is related with dict

//        public static boolean initMultidim_() {
//            if (tokenList.get(i).getTokenType().equals("C_BRACK")) { //hoskta hai ye ghlt ho
//                i++;
//                if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
//                    i++;
//                    if (valMultidim() && tokenList.get(i).getTokenType().equals("C_BRACE")) {
//                        i++;
//                        return true;
//                    }
//                }
//            } else if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
//                i++;
//                return true;
//            }
//            return syntaxError();
//        }

//not included in our lang as it is related with initmultidm which is related with multiarr which is related with dict

//        public static boolean valMultidim() {
//            if (tokenList.get(i).getTokenType().equals("ID")) {
//                i++;
//                return valMultidim_();
//            } else if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
//                return true;
//            }
//            return syntaxError();
//        }
//not included in our lang as it is related with valMultidm which is related with initmultidm which is related with multiarr which is related with dict

//        public static boolean valMultidim_() {
//            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
//                return true;
//            } else if (tokenList.get(i).getTokenType().equals("COMMA")) {
//                i++;
//                if (tokenList.get(i).getTokenType().equals("ID")) {
//                    i++;
//                    return valMultidim_();
//                }
//            }
//            return syntaxError();
//        }

        public static boolean sstID() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                return object_();
            } else if (tokenList.get(i).getTokenType().equals("TERMINATOR") || tokenList.get(i).getTokenType().equals("O_BRACK") ||
                    tokenList.get(i).getTokenType().equals("O_PARAN") || tokenList.get(i).getTokenType().equals("INC_DEC") ||
                    tokenList.get(i).getTokenType().equals("ASSIGN") || tokenList.get(i).getTokenType().equals("COMP_ASSIGN")) {
                return sstID_();
            }
            return syntaxError();
        }

        public static boolean sstID_() {
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return sstID_();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return sst1();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (pl() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    return sst2();
                }
            } else if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    i++;
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("ASSIGN") || tokenList.get(i).getTokenType().equals("COMP_ASSIGN")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    i++;
                    return true;
                }
            }
            return syntaxError();
        }

        public static boolean sst1() {
            if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return sstID_();
                }
            } else if (tokenList.get(i).getTokenType().equals("INC_DEC")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    i++;
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("ASSIGN") || tokenList.get(i).getTokenType().equals("COMP_ASSIGN")) {
                i++;
                if (expression() && tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                    return true;
                }
            } else if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                i++;
                return true;
            }
            return syntaxError();
        }

        public static boolean sst2() {
            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("TERMINATOR")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    return sstID_();
                }
            }
            return syntaxError();
        }

        public static boolean intSt() {
            if (public_() && returnType()) {
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                        i++;
                        if (argumentList() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                            i++;
                            if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                                i++;
                                if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                                    i++;
                                    if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                                        i++;
                                        return intSt();
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean sCst_() {
            if (tokenList.get(i).getTokenType().equals("DT")) {
                i++;
                return sStDT();
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return sStID();
            } /*else if (tokenList.get(i).getTokenType().equals("DICT")) { // not inlcuded in our lang
                i++;
                if (multiArr()) {
                    return sCst();
                }
            }*/
            else if (tokenList.get(i).getTokenType().equals("VOID")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                    i++;
                    return sMain();
                }
            }
            return syntaxError();
        }

        public static boolean sStDT() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (declare_()) {
                    return sCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return sStDT_();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return sMain();
            }
            return syntaxError();
        }

        public static boolean sStDT_() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (gArr_()) {
                    return sCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return sMain();
            }
            return syntaxError();
        }

        public static boolean sStID() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (object_()) {
                    return sCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return sStID_();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return sMain();
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    if (bodyMST()) {
                        return sCst();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean sStID_() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (oArr_()) {
                    return sCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return sMain();
            }
            return syntaxError();
        }

        public static boolean sMain() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (argumentList() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    if (bodyMST()) {
                        return sCstNM();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean sCstNM() {
            if (tokenList.get(i).getTokenType().equals("FUNCTION")) {
                i++;
                if (pubPriv_() && tokenList.get(i).getTokenType().equals("STATIC")) {
                    i++;
                    if (functionSig()) {
                        return sCstNM();
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("PRIVATE")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("STATIC")) {
                    i++;
                    if (classVars()) {
                        return sCstNM();
                    }
                }
            } else if (public_()) {
                if (tokenList.get(i).getTokenType().equals("STATIC")) {
                    i++;
                    if (consVar()) {
                        return sCstNM();
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                i++;
                return mainDone();
            }
            return syntaxError();
        }

        public static boolean mainDone() {
            if (tokenList.get(i).getTokenType().equals("EOF")) {
                return true;
            } else if (tokenList.get(i).getTokenType().equals("STATIC")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("CLASS")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("ID")) {
                        i++;
                        if (inherit() && tokenList.get(i).getTokenType().equals("O_BRACE")) {
                            i++;
                            if (sCstNM()) {
                                return mainDone();
                            }
                        }
                    }
                }
            } else if (tokenList.get(i).getTokenType().equals("INTERFACE"/*was called symbol before*/)) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("O_BRACE")) {
                        i++;
                        if (intSt() && tokenList.get(i).getTokenType().equals("C_BRACE")) {
                            i++;
                            return mainDone();
                        }
                    }
                }
            } else if (concCond_() && tokenList.get(i).getTokenType().equals("CLASS")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (inherit() && tokenList.get(i).getTokenType().equals("O_BRACE")) {
                        i++;
                        if (gcCstNM()) {
                            return mainDone();
                        }
                    }
                }
            }
            return syntaxError();
        }

        public static boolean concCond_() {
            if (tokenList.get(i).getTokenType().equals("FINAL"/*was concrete*/)) {
                i++;
                return true;
            }/* else if (tokenList.get(i).getTokenType().equals("CONDENSED")) {
                i++;
                return true;
                //not included in our lang
            }*/
            else if (tokenList.get(i).getTokenType().equals("CLASS")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean final_/*was concrete_*/() {
            if (tokenList.get(i).getTokenType().equals("FINAL"/*was concrete*/)) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("CLASS")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean cCst() {
            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                i++;
                return structure();
            } else if (tokenList.get(i).getTokenType().equals("FUNCTION")) {
                i++;
                if (access() && types()) {
                    return cCst();
                }
            } else if (protecPriv() && statConc()) {
                if (classVars()) {
                    return cCst();
                }
            } else if (classVars()) {
                return cCst();
            } else if (public_()) {
                return cCst_();
            }
            return syntaxError();
        }

        public static boolean access() {
            if (tokenList.get(i).getTokenType().equals("PUBLIC")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("PRIVATE")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("PROTECTED"/*was preserved*/)) {
                i++;
                return true;
            } else if (/*tokenList.get(i).getTokenType().equals("CONDENSED")//not inlcuded in our lang ||  */tokenList.get(i).getTokenType().equals("DT") || tokenList.get(i).getTokenType().equals("ID") /*|| tokenList.get(i).getTokenType().equals("DICT") // not inlcuded in our lang*/|| tokenList.get(i).getTokenType().equals("VOID") || tokenList.get(i).getTokenType().equals("STATIC") || tokenList.get(i).getTokenType().equals("FINAL"/*was concrete*/)) {
                return true;
            }
            return syntaxError();
        }

        public static boolean types() {
           /*// not included in our lang
            if (tokenList.get(i).getTokenType().equals("CONDENSED")) {
                i++;
                if (returnType() && tokenList.get(i).getTokenType().equals("ID")) {
                    i++;
                    if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                        i++;
                        if (argumentList() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                            i++;
                            if (tokenList.get(i).getTokenType().equals("SEMI_COL")) {
                                i++;
                                return true;
                            }
                        }
                    }
                }
            } else */if (statConc() && functionSig()) {
                return true;
            }
            return syntaxError();
        }

        public static boolean statConc() {
            if (tokenList.get(i).getTokenType().equals("STATIC")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("FINAL"/*was concrete*/)) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("DT") || tokenList.get(i).getTokenType().equals("ID") /*|| tokenList.get(i).getTokenType().equals("DICT") // not inlcuded in our lang*/|| tokenList.get(i).getTokenType().equals("VOID")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean protecPriv() {
            if (tokenList.get(i).getTokenType().equals("PRIVATE")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("PROTECTED"/*was preserved*/)) {
                i++;
                return true;
            }
            return syntaxError();
        }

        public static boolean cCst_() {
            if (tokenList.get(i).getTokenType().equals("STATIC")) {
                i++;
                return cCst__();
            } else if (tokenList.get(i).getTokenType().equals("FINAL"/*was concrete*/)) {
                i++;
                if (classVars()) {
                    return cCst();
                }
            }
            return syntaxError();
        }

        public static boolean cCst__() {
            if (tokenList.get(i).getTokenType().equals("DT")) {
                i++;
                return cStDT();
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return cStID();
            } /*else if (tokenList.get(i).getTokenType().equals("DICT")) {
                i++;
                if (multiArr()) {
                    return cCst();
                } // not inlcuded in our lang
            }*/
            else if (tokenList.get(i).getTokenType().equals("VOID")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                    i++;
                    return gcMain();
                }
            }
            return syntaxError();
        }

        public static boolean cStDT() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (declare_()) {
                    return cCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return cStDT_();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return gcMain();
            }
            return syntaxError();
        }

        public static boolean cStDT_() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (gArr_()) {
                    return cCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return gcMain();
            }
            return syntaxError();
        }

        public static boolean cStID() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (object_()) {
                    return cCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return cStID_();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return gcMain();
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (argumentList() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    if (bodyMST()) {
                        return cCst();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean cStID_() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (oArr_()) {
                    return cCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return gcMain();
            }
            return syntaxError();
        }

        public static boolean gcMain() {
            if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (argumentList() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    if (bodyMST()) {
                        return gcCstNM();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean gcCstNM() {
            if (tokenList.get(i).getTokenType().equals("FUNCTION")) {
                i++;
                if (access() && types()) {
                    return gcCstNM();
                }
            } else if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                i++;
                return mainDone();
            } else if (protecPriv() && statConc()) {
                if (classVars()) {
                    return gcCstNM();
                }
            } else if (public_()) {
                if (gcConsVar()) {
                    return gcCstNM();
                }
            }
            return syntaxError();
        }

        public static boolean gcConsVar() {
            if (tokenList.get(i).getTokenType().equals("FINAL"/*was concrete*/)) {
                return classVars();
            } else if (static_()) {
                return consVar();
            }
            return syntaxError();
        }

        public static boolean gCst() {
            if (tokenList.get(i).getTokenType().equals("C_BRACE")) {
                i++;
                return structure();
            } else if (tokenList.get(i).getTokenType().equals("FUNCTION")) {
                i++;
                if (access() && types()) {
                    return gCst();
                }
            } else if (protecPriv() && statConc()) {
                if (classVars()) {
                    return gCst();
                }
            } else if (public_()) {
                return gCst_();
            }
            return syntaxError();
        }

        public static boolean gCst_() {
            if (tokenList.get(i).getTokenType().equals("FINAL"/*was concrete*/)) {
                i++;
                if (classVars()) {
                    return gCst();
                }
            } else if (static_()) {
                return gCst__();
            }
            return syntaxError();
        }

        public static boolean static_() {
            if (tokenList.get(i).getTokenType().equals("STATIC")) {
                i++;
                return true;
            } else if (tokenList.get(i).getTokenType().equals("DT") || tokenList.get(i).getTokenType().equals("ID")/* || tokenList.get(i).getTokenType().equals("DICT")// not inlcuded in our lang */|| tokenList.get(i).getTokenType().equals("VOID")) {
                return true;
            }
            return syntaxError();
        }

        public static boolean gCst__() {
            if (tokenList.get(i).getTokenType().equals("DT")) {
                i++;
                return gStDT();
            } else if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                return gStID();
            }/* else if (tokenList.get(i).getTokenType().equals("DICT")) {
                i++;
                if (multiArr()) {
                    return gCst();
                }
            }*/
            else if (tokenList.get(i).getTokenType().equals("VOID")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                    i++;
                    return gcMain();
                }
            }
            return syntaxError();
        }

        public static boolean gStDT() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (declare_()) {
                    return gCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return gStDT_();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return gcMain();
            }
            return syntaxError();
        }

        public static boolean gStDT_() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (gArr_()) {
                    return gCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return gcMain();
            }
            return syntaxError();
        }

        public static boolean gStID() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (object_()) {
                    return gCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("O_BRACK")) {
                i++;
                if (tokenList.get(i).getTokenType().equals("C_BRACK")) {
                    i++;
                    return gStID_();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return gcMain();
            } else if (tokenList.get(i).getTokenType().equals("O_PARAN")) {
                i++;
                if (argumentList() && tokenList.get(i).getTokenType().equals("C_PARAN")) {
                    i++;
                    if (bodyMST()) {
                        return gCst();
                    }
                }
            }
            return syntaxError();
        }

        public static boolean gStID_() {
            if (tokenList.get(i).getTokenType().equals("ID")) {
                i++;
                if (oArr_()) {
                    return gCst();
                }
            } else if (tokenList.get(i).getTokenType().equals("MAIN_METHOD")) {
                i++;
                return gcMain();
            }
            return syntaxError();
        }

        public static boolean z() {
            return syntaxError();

        }

    }//everything in single class



//    public static void main(String[] args) {
//        try {
//            // Call the starting function here
//            gCst__();
//        } catch (LookupError e) {
//            System.out.println("Tree Incomplete... Input Completely Parsed");
//        }
//    }


    ////--------------------------------------------------DONE---------------------------- NEECHAY WALA FUNCTION GPT KI TARAF SE HA COMMENTS KRDY NA HALKY PHULKY    4:51 PM 6/12/2023

    // Rest of the functions and Token class implementation goes here

//    public static boolean syntaxError() {
//        // Implement syntax error handling logic here
//        return false;
//    }

