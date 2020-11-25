package recognition;

import entity.FiniteStateAutomate;
import init.CreateListFSA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

class StateMachineOperationTest {

    static CreateListFSA createListFSA;
    static List<FiniteStateAutomate[]> finiteStateAutomates;

    // для создания автоматов

    @Test
    public void testFunctionForCreateFSA() throws IOException {
        List<FiniteStateAutomate[]> finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(9, finiteStateAutomates.size());
    }

    @Test
    public void testFunctionForCreateInputs() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        for (FiniteStateAutomate[] fsa : finiteStateAutomates) {
            for (FiniteStateAutomate automate : fsa) {
                if (automate.getInputs() != null) {
                    Map<String, Set<String>> createInp = StateMachineOperation.forCreateInputs(automate);
                    if (createInp != null) {
                        if (createInp.get("digit") != null) {
                            automate.setInputs(createInp);
                            Assertions.assertEquals(Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), createInp.get("digit"));
                        }
                        if (createInp.get("char") != null) {
                            automate.setInputs(createInp);
                            char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                            for (char ch : alphabet) {
                                Assertions.assertTrue(createInp.get("char").contains(String.valueOf(ch)));
                            }
                        }
                        if (createInp.get("exp") != null) {
                            automate.setInputs(createInp);
                            Assertions.assertEquals(Set.of("e", "E"), createInp.get("exp"));
                        }
                        if (createInp.get("ops") != null) {
                            automate.setInputs(createInp);
                            Assertions.assertEquals(Set.of("*", "/", "%"), createInp.get("ops"));
                        }
                        if (createInp.get("ops2") != null) {
                            automate.setInputs(createInp);
                            Assertions.assertEquals(Set.of("&", "|", "+", "-"), createInp.get("ops2"));
                        }
                        if (createInp.get("whitespace") != null) {
                            automate.setInputs(createInp);
                            Assertions.assertEquals(Set.of("\n", "\r", "\t", " "), createInp.get("whitespace"));
                        }
                        if (createInp.get("sign") != null && (automate.getName().equals("integer") || automate.getName().equals("real"))) {
                            automate.setInputs(createInp);
                            Assertions.assertEquals(Set.of("+", "-"), createInp.get("sign"));
                        } else if (createInp.get("sign") != null && automate.getName().equals("id")) {
                            automate.setInputs(createInp);
                            Assertions.assertEquals(Set.of("+", "-", "<", ">", "!", "#", "*", "/", "&", "$", "@", "~"), createInp.get("sign"));
                        } else if (createInp.get("sign") != null && automate.getName().equals("special")) {
                            automate.setInputs(createInp);
                            Assertions.assertEquals(Set.of(".", ",", ":", ";", "!", "?", "(", ")", "{", "}", "[", "]"), createInp.get("sign"));
                        }

                    }
                }
            }
        }
    }

    @Test
    public void testFunctionForGetType() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        for (FiniteStateAutomate[] fsa : finiteStateAutomates) {
            for (FiniteStateAutomate automate : fsa) {
                if (automate.getInputs() != null && automate.getName().equals("integer")) {
                    automate.setInputs(StateMachineOperation.forCreateInputs(automate));
                    Assertions.assertEquals("sign", StateMachineOperation.getType(automate, '-'));
                } else if (automate.getInputs() != null && automate.getName().equals("id")) {
                    automate.setInputs(StateMachineOperation.forCreateInputs(automate));
                    Assertions.assertEquals("sign", StateMachineOperation.getType(automate, '<'));
                } else if (automate.getInputs() != null && automate.getName().equals("whitespace")) {
                    automate.setInputs(StateMachineOperation.forCreateInputs(automate));
                    Assertions.assertEquals("whitespace", StateMachineOperation.getType(automate, '\n'));
                } else if (automate.getInputs() != null && automate.getName().equals("special")) {
                    automate.setInputs(StateMachineOperation.forCreateInputs(automate));
                    Assertions.assertEquals("sign", StateMachineOperation.getType(automate, '?'));
                } else {
                    Assertions.assertEquals("q", StateMachineOperation.getType(automate, 'q'));
                    Assertions.assertEquals("t", StateMachineOperation.getType(automate, 't'));
                    Assertions.assertEquals("h", StateMachineOperation.getType(automate, 'h'));
                }
            }
        }
    }

    // для первой задачи

    @Test
    public void testFunctionMaxForBool() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaBool = StateMachineOperationTest.finiteStateAutomates.get(0);
        Assertions.assertEquals("bool", fsaBool[0].getName());
        if (fsaBool[0].getInputs() != null) fsaBool[0].setInputs(StateMachineOperation.forCreateInputs(fsaBool[0]));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaBool[0], "true", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaBool[0], "true", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaBool[0], "True", 0));
        Assertions.assertEquals("bool", fsaBool[1].getName());
        if (fsaBool[1].getInputs() != null) fsaBool[1].setInputs(StateMachineOperation.forCreateInputs(fsaBool[1]));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaBool[1], "false", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaBool[1], "false", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaBool[1], "False", 0));
    }

    @Test
    public void testFunctionMaxForDatatype() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(1);
        Assertions.assertEquals("datatype", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(StateMachineOperation.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 7), StateMachineOperation.max(fsaType[0], "boolean", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "boolean", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "Boolean", 0));
        Assertions.assertEquals("datatype", fsaType[1].getName());
        if (fsaType[1].getInputs() != null) fsaType[1].setInputs(StateMachineOperation.forCreateInputs(fsaType[1]));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[1], "byte", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[1], "byte", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[1], "ByTe", 0));
        Assertions.assertEquals("datatype", fsaType[2].getName());
        if (fsaType[2].getInputs() != null) fsaType[2].setInputs(StateMachineOperation.forCreateInputs(fsaType[2]));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaType[2], "short", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[2], "short", 4));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[2], "ShorT", 0));
        Assertions.assertEquals("datatype", fsaType[3].getName());
        if (fsaType[3].getInputs() != null) fsaType[3].setInputs(StateMachineOperation.forCreateInputs(fsaType[3]));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[3], "int", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[3], "int", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[3], "Int12", 0));
        Assertions.assertEquals("datatype", fsaType[4].getName());
        if (fsaType[4].getInputs() != null) fsaType[4].setInputs(StateMachineOperation.forCreateInputs(fsaType[4]));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[4], "long", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[4], "long", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[4], "LonG", 0));
        Assertions.assertEquals("datatype", fsaType[5].getName());
        if (fsaType[5].getInputs() != null) fsaType[5].setInputs(StateMachineOperation.forCreateInputs(fsaType[5]));
        Assertions.assertEquals(Pair.createPair(true, 6), StateMachineOperation.max(fsaType[5], "double", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[5], "double", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[5], "doublE", 0));
        Assertions.assertEquals("datatype", fsaType[6].getName());
        if (fsaType[6].getInputs() != null) fsaType[6].setInputs(StateMachineOperation.forCreateInputs(fsaType[6]));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaType[6], "float", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[6], "float", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[6], "fLoAt", 0));
        Assertions.assertEquals("datatype", fsaType[7].getName());
        if (fsaType[7].getInputs() != null) fsaType[7].setInputs(StateMachineOperation.forCreateInputs(fsaType[7]));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[7], "char", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[7], "char", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[7], "5CHAR5", 0));
    }

    @Test
    public void testFunctionMaxForId() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(2);
        Assertions.assertEquals("id", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(StateMachineOperation.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 6), StateMachineOperation.max(fsaType[0], "eto_id", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[0], "ID", 0));
        Assertions.assertEquals(Pair.createPair(true, 6), StateMachineOperation.max(fsaType[0], "+item'0", 1));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "a", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "b", 0));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaType[0], "break", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "id,,", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "123", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "11ID_5", 0));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[0], "11ID_5", 2));
    }

    @Test
    public void testFunctionMaxForInteger() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(3);
        Assertions.assertEquals("integer", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(StateMachineOperation.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[0], "1234", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[0], "1234", 2));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[0], "+12", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "+-11", 0));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[0], "+-11", 1));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[0], "145-", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "1+2", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "12.9", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "-16.E+2", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "14B5", 2));
    }

    @Test
    public void testFunctionMaxForKeyword() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(4);
        Assertions.assertEquals("keyword", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(StateMachineOperation.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaType[0], "begin", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "begin", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "Begin", 0));
        Assertions.assertEquals("keyword", fsaType[1].getName());
        if (fsaType[1].getInputs() != null) fsaType[1].setInputs(StateMachineOperation.forCreateInputs(fsaType[1]));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[1], "else", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[1], "else", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[1], "ElSE", 0));
        Assertions.assertEquals("keyword", fsaType[2].getName());
        if (fsaType[2].getInputs() != null) fsaType[2].setInputs(StateMachineOperation.forCreateInputs(fsaType[2]));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[2], "end", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[2], "end", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[2], "enD", 0));
        Assertions.assertEquals("keyword", fsaType[3].getName());
        if (fsaType[3].getInputs() != null) fsaType[3].setInputs(StateMachineOperation.forCreateInputs(fsaType[3]));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[3], "if", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[3], "if", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[3], "IF", 0));
        Assertions.assertEquals("keyword", fsaType[4].getName());
        if (fsaType[4].getInputs() != null) fsaType[4].setInputs(StateMachineOperation.forCreateInputs(fsaType[4]));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[4], "in", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[4], "in", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[4], "iN", 0));
        Assertions.assertEquals("keyword", fsaType[5].getName());
        if (fsaType[5].getInputs() != null) fsaType[5].setInputs(StateMachineOperation.forCreateInputs(fsaType[5]));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[5], "let", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[5], "let", 2));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[5], "LeT6", 0));
        Assertions.assertEquals("keyword", fsaType[6].getName());
        if (fsaType[6].getInputs() != null) fsaType[6].setInputs(StateMachineOperation.forCreateInputs(fsaType[6]));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[6], "then", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[6], "then", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[6], "THeN", 0));
        Assertions.assertEquals("keyword", fsaType[7].getName());
        if (fsaType[7].getInputs() != null) fsaType[7].setInputs(StateMachineOperation.forCreateInputs(fsaType[7]));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[7], "val", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[7], "val", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[7], "VAL12", 0));
        Assertions.assertEquals("keyword", fsaType[8].getName());
        if (fsaType[8].getInputs() != null) fsaType[8].setInputs(StateMachineOperation.forCreateInputs(fsaType[8]));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaType[8], "while", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[8], "while", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[8], "WHILE", 0));
        Assertions.assertEquals("keyword", fsaType[9].getName());
        if (fsaType[9].getInputs() != null) fsaType[9].setInputs(StateMachineOperation.forCreateInputs(fsaType[9]));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaType[9], "break", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[9], "break", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[9], "BReaK", 0));
    }

    @Test
    public void testFunctionMaxForOperation() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(5);
        Assertions.assertEquals("operation", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(StateMachineOperation.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[0], "<=", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[0], ">=", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[0], "==", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[0], "!=", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[0], "&&", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], ">", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "-", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "=", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "/", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "!", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "<5", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "=)", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "#", 0));
    }

    @Test
    public void testFunctionMaxForReal() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(6);
        Assertions.assertEquals("real", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(StateMachineOperation.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[0], "1.5", 0));
        Assertions.assertEquals(Pair.createPair(true, 7), StateMachineOperation.max(fsaType[0], "-6.e+33", 0));
        Assertions.assertEquals(Pair.createPair(true, 7), StateMachineOperation.max(fsaType[0], "+11.E-1", 0));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[0], "7E+5", 0));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaType[0], "-88.999", 2));
        Assertions.assertEquals(Pair.createPair(true, 4), StateMachineOperation.max(fsaType[0], "+.57", 0));
        Assertions.assertEquals(Pair.createPair(true, 5), StateMachineOperation.max(fsaType[0], "0.e+9", 0));
        Assertions.assertEquals(Pair.createPair(true, 2), StateMachineOperation.max(fsaType[0], "-67.0", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "+_3", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "E5", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "+e+1", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "111EE", 3));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "0..1", 1));
    }

    @Test
    public void testFunctionMaxForSpecial() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(7);
        Assertions.assertEquals("special", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(StateMachineOperation.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], ".", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], ".", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], ":", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], ";", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "??", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], ")(", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "[;", 1));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], ";;", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], ",_", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "@@", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], ".'", 1));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "*a", 1));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "0..1", 1));
    }

    @Test
    public void testFunctionMaxForWhitespace() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        FiniteStateAutomate[] fsaType = StateMachineOperationTest.finiteStateAutomates.get(8);
        Assertions.assertEquals("whitespace", fsaType[0].getName());
        if (fsaType[0].getInputs() != null) fsaType[0].setInputs(StateMachineOperation.forCreateInputs(fsaType[0]));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], " ", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "\n", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "\t", 0));
        Assertions.assertEquals(Pair.createPair(true, 1), StateMachineOperation.max(fsaType[0], "\r", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "n", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "t", 0));
        Assertions.assertEquals(Pair.createPair(false, 0), StateMachineOperation.max(fsaType[0], "r", 1));
        Assertions.assertEquals(Pair.createPair(true, 3), StateMachineOperation.max(fsaType[0], "    ", 1));
    }

    // для второй задачи

    @Test
    public void testFunctionCreatePairs() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        String strCode = "while (b > a && !break)";
        List<Pair<String, String>> listPair = StateMachineOperation.createPairs(strCode);
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
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("bool", "true"), StateMachineOperation.parse("true", 0));
        Assertions.assertEquals(Pair.createPair("bool", "false"), StateMachineOperation.parse("false", 0));
        Assertions.assertNotEquals(Pair.createPair("bool", "true"), StateMachineOperation.parse("TRUE", 0));
        Assertions.assertNotEquals(Pair.createPair("bool", "false"), StateMachineOperation.parse("FALSE", 0));
    }

    @Test
    public void testFunctionParseForDatatype() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("datatype", "boolean"), StateMachineOperation.parse("boolean", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "byte"), StateMachineOperation.parse("byte", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "short"), StateMachineOperation.parse("short", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "int"), StateMachineOperation.parse("int", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "long"), StateMachineOperation.parse("long", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "double"), StateMachineOperation.parse("double", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "float"), StateMachineOperation.parse("float", 0));
        Assertions.assertEquals(Pair.createPair("datatype", "char"), StateMachineOperation.parse("char", 0));
    }

    @Test
    public void testFunctionParseForId() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("id", "someid"), StateMachineOperation.parse("someid", 0));
        Assertions.assertEquals(Pair.createPair("id", "a_d_d___"), StateMachineOperation.parse("a_d_d___", 0));
        Assertions.assertEquals(Pair.createPair("id", "@"), StateMachineOperation.parse("@", 0));
        Assertions.assertEquals(Pair.createPair("id", "a"), StateMachineOperation.parse("a", 0));
        Assertions.assertEquals(Pair.createPair("id", "b"), StateMachineOperation.parse("b", 0));
        Assertions.assertEquals(Pair.createPair("id", "~>"), StateMachineOperation.parse("~>", 0));
        Assertions.assertNotEquals(Pair.createPair("id", "1asd"), StateMachineOperation.parse("1asd", 0));
        Assertions.assertNotEquals(Pair.createPair("id", "_asd"), StateMachineOperation.parse("_asd", 0));
        Assertions.assertNotEquals(Pair.createPair("id", "'asd"), StateMachineOperation.parse("'asd", 0));
        Assertions.assertNotEquals(Pair.createPair("id", ""), StateMachineOperation.parse("", 0));
    }

    @Test
    public void testFunctionParseForInteger() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("integer", "123"), StateMachineOperation.parse("123", 0));
        Assertions.assertEquals(Pair.createPair("integer", "0000"), StateMachineOperation.parse("0000", 0));
        Assertions.assertEquals(Pair.createPair("integer", "3"), StateMachineOperation.parse("+123123", 6));
        Assertions.assertEquals(Pair.createPair("integer", "2"), StateMachineOperation.parse("-100092", 6));
        Assertions.assertNotEquals(Pair.createPair("integer", "oai11"), StateMachineOperation.parse("oai11", 0));
        Assertions.assertNotEquals(Pair.createPair("integer", "--000f##fmin"), StateMachineOperation.parse("--000f##fmin", 0));
    }

    @Test
    public void testFunctionParseForKeyword() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("keyword", "begin"), StateMachineOperation.parse("begin", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "else"), StateMachineOperation.parse("else", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "end"), StateMachineOperation.parse("end", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "if"), StateMachineOperation.parse("if", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "in"), StateMachineOperation.parse("in", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "let"), StateMachineOperation.parse("let", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "then"), StateMachineOperation.parse("then", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "val"), StateMachineOperation.parse("val", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "while"), StateMachineOperation.parse("while", 0));
        Assertions.assertEquals(Pair.createPair("keyword", "break"), StateMachineOperation.parse("break", 0));
        Assertions.assertNotEquals(Pair.createPair("keyword", "min"), StateMachineOperation.parse("min", 0));
        Assertions.assertNotEquals(Pair.createPair("keyword", "vval"), StateMachineOperation.parse("vval", 0));
    }

    @Test
    public void testFunctionParseForOperation() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("operation", ">"), StateMachineOperation.parse(">", 0));
        Assertions.assertEquals(Pair.createPair("operation", ">"), StateMachineOperation.parse("<>", 1));
        Assertions.assertEquals(Pair.createPair("operation", "/"), StateMachineOperation.parse("///", 2));
        Assertions.assertNotEquals(Pair.createPair("operation", "123"), StateMachineOperation.parse("123", 0));
        Assertions.assertNotEquals(Pair.createPair("operation", "!!"), StateMachineOperation.parse("!!", 0));
        Assertions.assertNotEquals(Pair.createPair("operation", "@<>"), StateMachineOperation.parse("@<>", 0));
    }

    @Test
    public void testFunctionParseForReal() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("real", "+1."), StateMachineOperation.parse("+1.", 0));
        Assertions.assertEquals(Pair.createPair("real", "-1."), StateMachineOperation.parse("-1.", 0));
        Assertions.assertEquals(Pair.createPair("real", "2e13"), StateMachineOperation.parse("2e13", 0));
        Assertions.assertEquals(Pair.createPair("real", "10.1e+10"), StateMachineOperation.parse("10.1e+10", 0));
        Assertions.assertEquals(Pair.createPair("real", "+123.e2"), StateMachineOperation.parse("+123.e2", 0));
        Assertions.assertEquals(Pair.createPair("real", "+.1e10"), StateMachineOperation.parse("+.1e10", 0));
        Assertions.assertEquals(Pair.createPair("real", "-23.e2"), StateMachineOperation.parse("-23.e2", 0));
        Assertions.assertNotEquals(Pair.createPair("real", "+1.2-e123"), StateMachineOperation.parse("+1.2-e123", 0));
        Assertions.assertNotEquals(Pair.createPair("real", "+e8"), StateMachineOperation.parse("+e8", 0));
        Assertions.assertNotEquals(Pair.createPair("real", "-.e1"), StateMachineOperation.parse("-.e1", 0));
        Assertions.assertNotEquals(Pair.createPair("real", "+."), StateMachineOperation.parse("+.", 0));
    }

    @Test
    public void testFunctionParseForSpecial() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("special", ";"), StateMachineOperation.parse(";", 0));
        Assertions.assertEquals(Pair.createPair("special", "("), StateMachineOperation.parse("(", 0));
        Assertions.assertEquals(Pair.createPair("special", "]"), StateMachineOperation.parse("]", 0));
        Assertions.assertEquals(Pair.createPair("special", ","), StateMachineOperation.parse(",", 0));
    }

    @Test
    public void testFunctionParseForWhitespace() throws IOException {
        createListFSA = new CreateListFSA();
        finiteStateAutomates = CreateListFSA.create();
        Assertions.assertEquals(Pair.createPair("whitespace", " "), StateMachineOperation.parse(" ", 0));
        Assertions.assertEquals(Pair.createPair("whitespace", "\n"), StateMachineOperation.parse("\n", 0));
        Assertions.assertEquals(Pair.createPair("whitespace", "\t"), StateMachineOperation.parse("\t", 0));
        Assertions.assertEquals(Pair.createPair("whitespace", "\r"), StateMachineOperation.parse("\r", 0));
        Assertions.assertEquals(Pair.createPair("whitespace", "  "), StateMachineOperation.parse("  ", 0));
        Assertions.assertNotEquals(Pair.createPair("whitespace", " . "), StateMachineOperation.parse(" . ", 1));
    }
}