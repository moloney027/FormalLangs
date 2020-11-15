package recognition;

import entity.FiniteStateAutomate;
import init.CreateListFSA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

class StateMachineOperationTest {

    static StateMachineOperation smo;
    static CreateListFSA createListFSA;
    static List<FiniteStateAutomate[]> finiteStateAutomates;

    @BeforeAll
    static void start() throws IOException {
        smo = new StateMachineOperation();
        createListFSA = new CreateListFSA();
        finiteStateAutomates = createListFSA.create();
    }

    // для создания автоматов

    @Test
    public void testFunctionForCreateFSA() throws IOException {
        List<FiniteStateAutomate[]> finiteStateAutomates = createListFSA.create();
        Assertions.assertEquals(9, finiteStateAutomates.size());
    }

    @Test
    public void testFunctionForCreateInputs() {
        for (FiniteStateAutomate[] fsa : finiteStateAutomates) {
            for (FiniteStateAutomate automate : fsa) {
                if (smo.forCreateInputs(automate) != null && automate.getInputs() != null) {
                    Map<String, Set<String>> createInp = smo.forCreateInputs(automate);
                    if (createInp.get("digit") != null) {
                        Assertions.assertEquals(Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), createInp.get("digit"));
                    }
                    if (createInp.get("char") != null) {
                        char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                        for (char ch : alphabet) {
                            Assertions.assertTrue(createInp.get("char").contains(String.valueOf(ch)));
                        }
                    }
                    if (createInp.get("exp") != null) {
                        Assertions.assertEquals(Set.of("e", "E"), createInp.get("exp"));
                    }
                    if (createInp.get("ops") != null) {
                        Assertions.assertEquals(Set.of("*", "/", "%"), createInp.get("ops"));
                    }
                    if (createInp.get("ops2") != null) {
                        Assertions.assertEquals(Set.of("&", "|", "+", "-"), createInp.get("ops2"));
                    }
                    if (createInp.get("whitespace") != null) {
                        Assertions.assertEquals(Set.of("\\n", "\\r", "\\t", " "), createInp.get("whitespace"));
                    }
                    if (createInp.get("sign") != null && (automate.getName().equals("integer") || automate.getName().equals("real"))) {
                        Assertions.assertEquals(Set.of("+", "-"), createInp.get("sign"));
                    } else if (createInp.get("sign") != null && automate.getName().equals("id")) {
                        Assertions.assertEquals(Set.of("+", "-", "<", ">", "!", "#", "*", "/", "&", "$", "@", "~"), createInp.get("sign"));
                    } else if (createInp.get("sign") != null && automate.getName().equals("special")) {
                        Assertions.assertEquals(Set.of(".", ",", ":", ";", "!", "?", "(", ")", "{", "}", "[", "]"), createInp.get("sign"));
                    }
                }
            }
        }
    }

    @Test
    public void testFunctionForGetType() {
        for (FiniteStateAutomate[] fsa : finiteStateAutomates) {
            for (FiniteStateAutomate automate : fsa) {
                if (automate.getInputs() != null && automate.getName().equals("integer")) {
                    automate.setInputs(smo.forCreateInputs(automate));
                    Assertions.assertEquals("sign", smo.getType(automate, '-'));
                } else if (automate.getInputs() != null && automate.getName().equals("id")) {
                    automate.setInputs(smo.forCreateInputs(automate));
                    Assertions.assertEquals("sign", smo.getType(automate, '<'));
                } else if (automate.getInputs() != null && automate.getName().equals("whitespace")) {
                    automate.setInputs(smo.forCreateInputs(automate));
                    Assertions.assertEquals("whitespace", smo.getType(automate, '\n'));
                } else if (automate.getInputs() != null && automate.getName().equals("special")) {
                    automate.setInputs(smo.forCreateInputs(automate));
                    Assertions.assertEquals("sign", smo.getType(automate, '?'));
                } else {
                    Assertions.assertEquals("q", smo.getType(automate, 'q'));
                    Assertions.assertEquals("t", smo.getType(automate, 't'));
                    Assertions.assertEquals("h", smo.getType(automate, 'h'));
                }
            }
        }
    }

    // для первой задачи

    @Test
    public void testFunctionMaxForBool() {
        FiniteStateAutomate[] fsaBool = StateMachineOperationTest.finiteStateAutomates.get(0);
        Assertions.assertEquals("bool", fsaBool[0].getName());
        if (fsaBool[0].getInputs() != null) fsaBool[0].setInputs(smo.forCreateInputs(fsaBool[0]));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaBool[0], "true", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaBool[0], "true", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaBool[0], "True", 0));
        Assertions.assertEquals("bool", fsaBool[1].getName());
        if (fsaBool[1].getInputs() != null) fsaBool[1].setInputs(smo.forCreateInputs(fsaBool[1]));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaBool[1], "false", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaBool[1], "false", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaBool[1], "False", 0));
    }

    @Test
    public void testFunctionMaxForDatatype() {
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(1);
        Assertions.assertEquals("datatype", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(smo.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 7), smo.max(fsaType[0], "boolean", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "boolean", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "Boolean", 0));
        Assertions.assertEquals("datatype", fsaType[1].getName());
        if (fsaType[1].getInputs() != null) fsaType[1].setInputs(smo.forCreateInputs(fsaType[1]));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[1], "byte", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[1], "byte", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[1], "ByTe", 0));
        Assertions.assertEquals("datatype", fsaType[2].getName());
        if (fsaType[2].getInputs() != null) fsaType[2].setInputs(smo.forCreateInputs(fsaType[2]));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaType[2], "short", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[2], "short", 4));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[2], "ShorT", 0));
        Assertions.assertEquals("datatype", fsaType[3].getName());
        if (fsaType[3].getInputs() != null) fsaType[3].setInputs(smo.forCreateInputs(fsaType[3]));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[3], "int", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[3], "int", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[3], "Int12", 0));
        Assertions.assertEquals("datatype", fsaType[4].getName());
        if (fsaType[4].getInputs() != null) fsaType[4].setInputs(smo.forCreateInputs(fsaType[4]));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[4], "long", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[4], "long", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[4], "LonG", 0));
        Assertions.assertEquals("datatype", fsaType[5].getName());
        if (fsaType[5].getInputs() != null) fsaType[5].setInputs(smo.forCreateInputs(fsaType[5]));
        Assertions.assertEquals(Pair.createPair(true, 6), smo.max(fsaType[5], "double", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[5], "double", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[5], "doublE", 0));
        Assertions.assertEquals("datatype", fsaType[6].getName());
        if (fsaType[6].getInputs() != null) fsaType[6].setInputs(smo.forCreateInputs(fsaType[6]));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaType[6], "float", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[6], "float", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[6], "fLoAt", 0));
        Assertions.assertEquals("datatype", fsaType[7].getName());
        if (fsaType[7].getInputs() != null) fsaType[7].setInputs(smo.forCreateInputs(fsaType[7]));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[7], "char", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[7], "char", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[7], "5CHAR5", 0));
    }

    @Test
    public void testFunctionMaxForId() {
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(2);
        Assertions.assertEquals("id", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(smo.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 6), smo.max(fsaType[0], "eto_id", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[0], "ID", 0));
        Assertions.assertEquals(Pair.createPair(true, 6), smo.max(fsaType[0], "+item'0", 1));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "a", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "b", 0));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaType[0], "break", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "id,,", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "123", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "11ID_5", 0));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[0], "11ID_5", 2));
    }

    @Test
    public void testFunctionMaxForInteger() {
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(3);
        Assertions.assertEquals("integer", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(smo.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[0], "1234", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[0], "1234", 2));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[0], "+12", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "+-11", 0));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[0], "+-11", 1));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[0], "145-", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "1+2", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "12.9", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "-16.E+2", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "14B5", 2));
    }

    @Test
    public void testFunctionMaxForKeyword() {
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(4);
        Assertions.assertEquals("keyword", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(smo.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaType[0], "begin", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "begin", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "Begin", 0));
        Assertions.assertEquals("keyword", fsaType[1].getName());
        if (fsaType[1].getInputs() != null) fsaType[1].setInputs(smo.forCreateInputs(fsaType[1]));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[1], "else", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[1], "else", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[1], "ElSE", 0));
        Assertions.assertEquals("keyword", fsaType[2].getName());
        if (fsaType[2].getInputs() != null) fsaType[2].setInputs(smo.forCreateInputs(fsaType[2]));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[2], "end", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[2], "end", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[2], "enD", 0));
        Assertions.assertEquals("keyword", fsaType[3].getName());
        if (fsaType[3].getInputs() != null) fsaType[3].setInputs(smo.forCreateInputs(fsaType[3]));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[3], "if", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[3], "if", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[3], "IF", 0));
        Assertions.assertEquals("keyword", fsaType[4].getName());
        if (fsaType[4].getInputs() != null) fsaType[4].setInputs(smo.forCreateInputs(fsaType[4]));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[4], "in", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[4], "in", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[4], "iN", 0));
        Assertions.assertEquals("keyword", fsaType[5].getName());
        if (fsaType[5].getInputs() != null) fsaType[5].setInputs(smo.forCreateInputs(fsaType[5]));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[5], "let", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[5], "let", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[5], "LeT6", 0));
        Assertions.assertEquals("keyword", fsaType[6].getName());
        if (fsaType[6].getInputs() != null) fsaType[6].setInputs(smo.forCreateInputs(fsaType[6]));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[6], "then", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[6], "then", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[6], "THeN", 0));
        Assertions.assertEquals("keyword", fsaType[7].getName());
        if (fsaType[7].getInputs() != null) fsaType[7].setInputs(smo.forCreateInputs(fsaType[7]));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[7], "val", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[7], "val", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[7], "VAL12", 0));
        Assertions.assertEquals("keyword", fsaType[8].getName());
        if (fsaType[8].getInputs() != null) fsaType[8].setInputs(smo.forCreateInputs(fsaType[8]));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaType[8], "while", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[8], "while", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[8], "WHILE", 0));
        Assertions.assertEquals("keyword", fsaType[9].getName());
        if (fsaType[9].getInputs() != null) fsaType[9].setInputs(smo.forCreateInputs(fsaType[9]));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaType[9], "break", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[9], "break", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[9], "BReaK", 0));
    }

    @Test
    public void testFunctionMaxForOperation() {
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(5);
        Assertions.assertEquals("operation", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(smo.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[0], "<=", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[0], ">=", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[0], "==", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[0], "!=", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[0], "&&", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], ">", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "-", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "=", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "/", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "!", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "<5", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "=)", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "#", 0));
    }

    @Test
    public void testFunctionMaxForReal() {
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(6);
        Assertions.assertEquals("real", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(smo.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[0], "1.5", 0));
        Assertions.assertEquals(Pair.createPair(true, 7), smo.max(fsaType[0], "-6.e+33", 0));
        Assertions.assertEquals(Pair.createPair(true, 7), smo.max(fsaType[0], "+11.E-1", 0));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[0], "7E+5", 0));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaType[0], "-88.999", 2));
        Assertions.assertEquals(Pair.createPair(true, 4), smo.max(fsaType[0], "+.57", 0));
        Assertions.assertEquals(Pair.createPair(true, 5), smo.max(fsaType[0], "0.e+9", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), smo.max(fsaType[0], "-67.0", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "+_3", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "E5", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "+e+1", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "111EE", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "0..1", 1));
    }

    @Test
    public void testFunctionMaxForSpecial() {
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(7);
        Assertions.assertEquals("special", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(smo.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], ".", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], ".", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], ":", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], ";", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "??", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], ")(", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "[;", 1));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], ";;", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], ",_", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "@@", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], ".'", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "*a", 1));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "0..1", 1));
    }

    @Test
    public void testFunctionMaxForWhitespace() {
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(8);
        Assertions.assertEquals("whitespace", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(smo.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], " ", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "\n", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "\t", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), smo.max(fsaType[0], "\r", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "n", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "t", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), smo.max(fsaType[0], "r", 1));
        Assertions.assertEquals(Pair.createPair(true, 3), smo.max(fsaType[0], "    ", 1));
    }

    // для второй задачи

    @Test
    public void testFunctionCreatePairs() throws IOException {
        String strCode = "while (b > a && !break)";
        List<Pair<String, String>> listPair = smo.createPairs(strCode);
        Assertions.assertEquals(Pair.createPair("keyword", "while"), listPair.get(0));
        Assertions.assertEquals(Pair.createPair("whitespace", " "), listPair.get(1));
        Assertions.assertEquals(Pair.createPair("special", "("), listPair.get(2));
        Assertions.assertEquals(Pair.createPair("id", "b"), listPair.get(3));
        Assertions.assertEquals(Pair.createPair("whitespace", " "), listPair.get(4));
        Assertions.assertEquals(Pair.createPair("operation", ">"), listPair.get(5));
        Assertions.assertEquals(Pair.createPair("whitespace", " "), listPair.get(6));
        Assertions.assertEquals(Pair.createPair("id", "a"), listPair.get(7));
        Assertions.assertEquals(Pair.createPair("whitespace", " "), listPair.get(8));
        Assertions.assertEquals(Pair.createPair("operation", "&&"), listPair.get(9));
        Assertions.assertEquals(Pair.createPair("whitespace", " "), listPair.get(10));
        Assertions.assertEquals(Pair.createPair("operation", "!"), listPair.get(11));
        Assertions.assertEquals(Pair.createPair("keyword", "break"), listPair.get(12));
        Assertions.assertEquals(Pair.createPair("special", ")"), listPair.get(13));
    }

    @Test
    public void testFunctionParseForBool() throws IOException {
        Assertions.assertEquals(Pair.createPair("bool", "true"), smo.parse("true", 0));
        Assertions.assertEquals(Pair.createPair("bool", "false"), smo.parse("false", 0));
        Assertions.assertNotEquals(Pair.createPair("bool", "true"), smo.parse("TRUE", 0));
        Assertions.assertNotEquals(Pair.createPair("bool", "false"), smo.parse("FALSE", 0));
    }

    @Test
    public void testFunctionParseForDatatype() throws IOException {
        Assertions.assertEquals(Pair.createPair("datatype", "boolean"), smo.parse("boolean", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "byte"), smo.parse("byte", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "short"), smo.parse("short", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "int"), smo.parse("int", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "long"), smo.parse("long", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "double"), smo.parse("double", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "float"), smo.parse("float", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "char"), smo.parse("char", 0));
    }

    @Test
    public void testFunctionParseForId() throws IOException {
        Assertions.assertEquals(Pair.createPair("id", "someid"), smo.parse("someid", 0));
        Assertions.assertEquals(Pair.createPair("id", "a_d_d___"), smo.parse("a_d_d___", 0));
        Assertions.assertEquals(Pair.createPair("id", "@"), smo.parse("@", 0));
        Assertions.assertEquals(Pair.createPair("id", "a"), smo.parse("a", 0));
        Assertions.assertEquals(Pair.createPair("id", "b"), smo.parse("b", 0));
        Assertions.assertEquals(Pair.createPair("id", "~>"), smo.parse("~>", 0));
        Assertions.assertNotEquals(Pair.createPair("id", "1asd"), smo.parse("1asd", 0));
        Assertions.assertNotEquals(Pair.createPair("id", "_asd"), smo.parse("_asd", 0));
        Assertions.assertNotEquals(Pair.createPair("id", "'asd"), smo.parse("'asd", 0));
        Assertions.assertNotEquals(Pair.createPair("id", ""), smo.parse("", 0));
    }

    @Test
    public void testFunctionParseForInteger() throws IOException {
        Assertions.assertEquals(Pair.createPair("integer", "123"), smo.parse("123", 0));
        Assertions.assertEquals(Pair.createPair("integer", "0000"), smo.parse("0000", 0));
        Assertions.assertEquals(Pair.createPair("integer", "3"), smo.parse("+123123", 6));
        Assertions.assertEquals(Pair.createPair("integer", "2"), smo.parse("-100092", 6));
        Assertions.assertNotEquals(Pair.createPair("integer", "oai11"), smo.parse("oai11", 0));
        Assertions.assertNotEquals(Pair.createPair("integer", "--000f##fmin"), smo.parse("--000f##fmin", 0));
    }

    @Test
    public void testFunctionParseForKeyword() throws IOException {
        Assertions.assertEquals(Pair.createPair("keyword", "begin"), smo.parse("begin", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "else"), smo.parse("else", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "end"), smo.parse("end", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "if"), smo.parse("if", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "in"), smo.parse("in", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "let"), smo.parse("let", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "then"), smo.parse("then", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "val"), smo.parse("val", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "while"), smo.parse("while", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "break"), smo.parse("break", 0));
        Assertions.assertNotEquals(Pair.createPair("keyword", "min"), smo.parse("min", 0));
        Assertions.assertNotEquals(Pair.createPair("keyword", "vval"), smo.parse("vval", 0));
    }

    @Test
    public void testFunctionParseForOperation() throws IOException {
        Assertions.assertEquals(Pair.createPair("operation", ">"), smo.parse(">", 0));
        Assertions.assertEquals(Pair.createPair("operation", ">"), smo.parse("<>", 1));
        Assertions.assertEquals(Pair.createPair("operation", "/"), smo.parse("///", 2));
        Assertions.assertNotEquals(Pair.createPair("operation", "123"), smo.parse("123", 0));
        Assertions.assertNotEquals(Pair.createPair("operation", "!!"), smo.parse("!!", 0));
        Assertions.assertNotEquals(Pair.createPair("operation", "@<>"), smo.parse("@<>", 0));
    }

    @Test
    public void testFunctionParseForReal() throws IOException {
        Assertions.assertEquals(Pair.createPair("real", "+1."), smo.parse("+1.", 0));
        Assertions.assertEquals(Pair.createPair("real", "-1."), smo.parse("-1.", 0));
        Assertions.assertEquals(Pair.createPair("real", "2e13"), smo.parse("2e13", 0));
        Assertions.assertEquals(Pair.createPair("real", "10.1e+10"), smo.parse("10.1e+10", 0));
        Assertions.assertEquals(Pair.createPair("real", "+123.e2"), smo.parse("+123.e2", 0));
        Assertions.assertEquals(Pair.createPair("real", "+.1e10"), smo.parse("+.1e10", 0));
        Assertions.assertEquals(Pair.createPair("real", "-23.e2"), smo.parse("-23.e2", 0));
        Assertions.assertNotEquals(Pair.createPair("real", "+1.2-e123"), smo.parse("+1.2-e123", 0));
        Assertions.assertNotEquals(Pair.createPair("real", "+e8"), smo.parse("+e8", 0));
        Assertions.assertNotEquals(Pair.createPair("real", "-.e1"), smo.parse("-.e1", 0));
        Assertions.assertNotEquals(Pair.createPair("real", "+."), smo.parse("+.", 0));
    }

    @Test
    public void testFunctionParseForSpecial() throws IOException {
        Assertions.assertEquals(Pair.createPair("special", ";"), smo.parse(";", 0));
        Assertions.assertEquals(Pair.createPair("special", "("), smo.parse("(", 0));
        Assertions.assertEquals(Pair.createPair("special", "]"), smo.parse("]", 0));
        Assertions.assertEquals(Pair.createPair("special", ","), smo.parse(",", 0));
    }

    @Test
    public void testFunctionParseForWhitespace() throws IOException {
        Assertions.assertEquals(Pair.createPair("whitespace", " "), smo.parse(" ", 0));
        Assertions.assertEquals(Pair.createPair("whitespace", "\n"), smo.parse("\n", 0));
        Assertions.assertEquals(Pair.createPair("whitespace", "\t"), smo.parse("\t", 0));
        Assertions.assertEquals(Pair.createPair("whitespace", "\r"), smo.parse("\r", 0));
        Assertions.assertEquals(Pair.createPair("whitespace", "  "), smo.parse("  ", 0));
        Assertions.assertNotEquals(Pair.createPair("whitespace", " . "), smo.parse(" . ", 1));
    }
}