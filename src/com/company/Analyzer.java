package com.company;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;


public class Analyzer {
    private String[] Words = { "main", "string", "var", "int", "float", "bool", "begin", "end", "if", "then", "char",
            "else", "while", "do", "scanf", "printf", "true", "false", "switch", "case", "break" };
    private String[] Delimiter = { ".", ";", ",", "(", ")", "\"", "\'", "$", "{", "}", ":", "#" };
    private String[] Operators = { "+", "-", "*", "^", "=", "==", "!=", ">", "<", "&", "||" }; //6
    private String[] sysHEX = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D", "E", "F"};//10
    public ArrayList<Lex> Lexemes = new ArrayList<>();
    private String[] Directives = {"include", "stdio"};

    public String[] TID = { "" }; // lexems identificators
    public String[] TNUM = { "" };// lexems numbers


    private String buff = "";// buffer for storing lexem

    private char[] smb = new char[1]; // current sybmol
    private String dt;//
    private enum States {S, NUM, DLM, FIN, ID, ER, ASGN, COM, STRCONST, CHARCONST, HEXSYS, DIRECT};
        private States state = States.S;//current state
    private StringReader sr;


    private void GetNext(){
        try{
            sr.read(smb, 0, 1);
        }
        catch (Exception e){
            e.getStackTrace();
        }

    }

    private void ClearBuff(){
        buff = "";
    }

    private void AddBuff(char symb){
        buff += symb;
    }

    private ArrayList SearchLex(String[] lexems){
        for(int i = 0; i < lexems.length; i++){
            if(lexems[i].equals(buff)){
                return new ArrayList(Arrays.asList(i, buff));
            }
        }
        return new ArrayList(Arrays.asList(-1, ""));
    }

    private boolean SearchHexSysLex(String[] lexemes, char smb){
        for(String s: lexemes){
            if(s.equals(String.valueOf(smb)))
                return true;
        }
        return false;
    }

    public ArrayList PushLex(String[] lexems, String buff){
        for(int i = 0; i < lexems.length; i++){
            if(lexems[i].equals(buff)){
                return new ArrayList(Arrays.asList(-1, ""));
            }
        }
        lexems = Arrays.copyOf(lexems, lexems.length + 1);
        lexems[lexems.length - 1] = buff;
        return new ArrayList(Arrays.asList(lexems.length - 1, buff));
    }

    private void AddLex(ArrayList<Lex> lexemes, int key, int val, String lex){
        lexemes.add(new Lex(key, val, lex));
    }

    public void Analysis(String text){
        sr = new StringReader(text);
        while (state != States.FIN){
            switch(state){

                case S:
                    if(smb[0] == ' ' || smb[0] == '\n' || smb[0] == '\t' || smb[0] == '\0' || smb[0] == '\r'){
                        GetNext();
                    }
                    else if(Character.isLetter(smb[0])){
                        ClearBuff();
                        AddBuff(smb[0]);
                        state = States.ID;
                        GetNext();
                    }
                    else if(Character.isDigit(smb[0])){
                        dt = String.valueOf(smb[0]);
                        GetNext();
                        state = States.NUM;
                    }
//                    else if(smb[0] == '['){
//                        AddLex(Lexemes, 5, 0, String.valueOf(smb[0]));
//                        state = States.COM;
//                        GetNext();
//                    }
                    else if(smb[0] == ':'){
                        state  = States.ASGN;
                        ClearBuff();
                        AddBuff(smb[0]);
                        GetNext();

                    }
                    else if(smb[0] == '='){
                        state  = States.ASGN;
                        ClearBuff();
                    }
                    else if(smb[0] == '.'){
                        state = States.FIN;
                    }
                    else{
                        state = States.DLM;
                    }
                    break;

                case ID:
                    if(Character.isLetterOrDigit(smb[0])){
                        AddBuff(smb[0]);
                        GetNext();
                    }
                    else{

                        ArrayList srch = SearchLex(Words);
                        if ((int)srch.get(0) != -1) {
                            AddLex(Lexemes, 1, (int)srch.get(0), (String)srch.get(1));
                        }
                        else {
                            srch = SearchLex(Directives);
                            if((int)srch.get(0) != -1){
                                state = States.DIRECT;
                                AddBuff(smb[0]);
                                break;
                            }
                            else{
                                ArrayList j = PushLex(TID, buff);
                                AddLex(Lexemes, 4, (int) j.get(0), (String) j.get(1));
                            }
                        }
                        state = States.S;
                    }
                    break;

                case DIRECT:
                    GetNext();
                    if(smb[0] == '\n' || smb[0] == '\0' || smb[0] == '\r' ||
                            smb[0] ==';' || smb[0] == '#'){
                        AddLex(Lexemes, 11, 0, buff);
                        ClearBuff();
                        state = States.S;
                        GetNext();
                        break;
                    }
                    AddBuff(smb[0]);
                    break;

                    case NUM:
                    if(Character.isDigit(smb[0])){
                        dt += smb[0];
                        GetNext();
                    }
                    else if(smb[0] == '.'){
                        dt += '.';
                        GetNext();
                    }
                    else{
                        ArrayList j  = PushLex(TNUM, String.valueOf(dt));
                        AddLex(Lexemes, 3, (int)j.get(0), (String)j.get(1));
                        state = States.S;
                    }
                    break;

                case CHARCONST:
                    if(smb[0] == '\''){
                        AddBuff(smb[0]);
                        AddLex(Lexemes, 8, 0, buff);
                        state = States.S;
                        ClearBuff();

                    }
                    AddBuff(smb[0]);
                    GetNext();
                    break;

                case STRCONST:
                    if(smb[0] == '\"'){
                        AddBuff(smb[0]);
                        AddLex(Lexemes, 7, 0, buff);
                        state = States.S;
                        ClearBuff();

                    }
                    AddBuff(smb[0]);
                    GetNext();
                    break;

                case HEXSYS:
                    if(SearchHexSysLex(sysHEX, smb[0])){
                        AddBuff(smb[0]);
                        GetNext();
                    }
                    else{
                        if(Character.isLetterOrDigit(smb[0])){
                            AddBuff(smb[0]);
                            state = States.ER;
                        }
                        else{
                            AddLex(Lexemes, 10, 0, buff);
                            ClearBuff();
                            state = States.S;
                        }
                    }
                break;

                case DLM:
                    ClearBuff();
                    AddBuff(smb[0]);

                    ArrayList r  = SearchLex(Delimiter);
                    if((int)r.get(0) != -1){
                        if(r.get(1).equals("\"")){
                            state = States.STRCONST;
                            GetNext();
                        }
                        else if(r.get(1).equals("\'")){
                            state = States.CHARCONST;
                            GetNext();
                        }
                        else if(r.get(1).equals("$")){
                            state = States.HEXSYS;
                            GetNext();
                        }
                        else{
                            AddLex(Lexemes, 2, (int)r.get(0), (String)r.get(1));
                            state = States.S;
                            GetNext();
                        }
                    }
                    else{
                        r = SearchLex(Operators);
                        if(((int)r.get(0) != -1)){
                            AddLex(Lexemes, 6, (int)r.get(0), (String)r.get(1));
                            state = States.S;
                            GetNext();
                        }
                        else{
                            state = States.ER;
                        }
                    }

                    break;

                case ASGN:
                    if(smb[0] == '='){
                        GetNext();
                        if(smb[0] == '='){
                            AddLex(Lexemes, 6, 4, "==");
                            state = States.S;
                            GetNext();
                            break;
                        } else {
                            AddLex(Lexemes, 6, 4, "=");
                        }
                    }
                    else{
                        AddLex(Lexemes, 2, 3, buff);
                    }
                    state = States.S;
                    break;

                case ER:
                    GetNext();
                    if(smb[0] == ' ' || smb[0] == '\n' || smb[0] == '\t' || smb[0] == '\0' || smb[0] == '\r'){
                        AddLex(Lexemes, 9, 0, buff);
                        ClearBuff();
                        state = States.S;
                        break;
                    }
                    AddBuff(smb[0]);

                case FIN:
                    break;
            }
        }
    }
}
