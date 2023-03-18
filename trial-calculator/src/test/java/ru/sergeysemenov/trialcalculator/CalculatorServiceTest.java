package ru.sergeysemenov.trialcalculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sergeysemenov.trialcalculator.dto.Expression;
import ru.sergeysemenov.trialcalculator.exceptions.BadExpressionException;
import ru.sergeysemenov.trialcalculator.service.CalculatorService;

@SpringBootTest(classes = CalculatorService.class)
public class CalculatorServiceTest {
    @Autowired
    CalculatorService calculatorService;

    @Test
    public void simpleCalculation(){
        Expression expression = new Expression("1+2");
        Assertions.assertEquals("3",calculatorService.calculate(expression));
    }

    @Test
    public void badExpressionStartsWithClosingBrace(){
        Expression expression = new Expression(")12*3");
        Assertions.assertThrows(BadExpressionException.class, ()->calculatorService.calculate(expression));
    }

    @Test
    public void badExpressionEmptyString(){
        Expression expression = new Expression("");
        Assertions.assertThrows(BadExpressionException.class, ()->calculatorService.calculate(expression));
    }

    @Test
    public void badExpressionWithLetters(){
        Expression expression = new Expression("a+b");
        Assertions.assertThrows(BadExpressionException.class, ()->calculatorService.calculate(expression));
    }

    @Test
    public void badExpressionDigitsWithComma(){
        Expression expression = new Expression("1,5+4");
        Assertions.assertThrows(BadExpressionException.class, ()->calculatorService.calculate(expression));
    }

    @Test
    public void complexCalculation(){
        Expression expression = new Expression("-1*2+(4*-5)");
        Assertions.assertEquals("-22",calculatorService.calculate(expression));
    }
    @Test
    public void complexCalculationsAndDigitsWithDot(){
        Expression expression = new Expression("(-1.50*2+3.45)/3");
        Assertions.assertEquals("0.15", calculatorService.calculate(expression));
    }


}
