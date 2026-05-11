import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckInputTest {

    @Test
    void areCharactersValidValidVariableNames() {
        assertTrue(CheckInput.areCharactersValid("cossin123456789abcewhekwfhdlreeury$_"));
    }

    @Test
    void areCharactersValidValidActions() {
        assertTrue(CheckInput.areCharactersValid("+-*/^"));
    }

    @Test
    void areCharactersValidValidDecemalComa() {
        assertFalse(CheckInput.areCharactersValid("0,9"));
        assertTrue(CheckInput.areCharactersValid("0.9"));
    }

    @Test
    void bracketsHaveSense() {
        assertTrue(CheckInput.areBracketsHaveSense("1 + b + c "));
        assertTrue(CheckInput.areBracketsHaveSense("1 + (b + c) "));
        assertTrue(CheckInput.areBracketsHaveSense("(1 + (b + c)) "));
        assertFalse(CheckInput.areBracketsHaveSense("1 + (b + c"));
        assertFalse(CheckInput.areBracketsHaveSense("1 + ((b + c)"));
    }

    @Test
    void missingSymbolsPresentNoAdditionals() {
        CheckInput.additionalActionsFound = new HashSet<>();

        assertTrue(CheckInput.areMissingSymbolsPresent("a+b+c"));
        assertFalse(CheckInput.areMissingSymbolsPresent("a++c"));
        assertFalse(CheckInput.areMissingSymbolsPresent("a+4b+c"));
    }

    @Test
    void missingSymbolsPresentWithAdditionals() {
        String formula = "cos(40*a+b*sin(50))";
        CheckInput.additionalActionsFound = CheckInput.getAdditionalActions(formula);
        System.out.println(CheckInput.additionalActionsFound);

        assertTrue(CheckInput.areMissingSymbolsPresent(formula));

        formula = "cos(40*a+b*sin(50)+cos(30))";
        assertFalse(CheckInput.areMissingSymbolsPresent(formula));

        formula = "cos(40*a+b*aacos(50))";
        CheckInput.additionalActionsFound = CheckInput.getAdditionalActions(formula);
        System.out.println(CheckInput.additionalActionsFound);
        assertFalse(CheckInput.areMissingSymbolsPresent(formula));
    }


}