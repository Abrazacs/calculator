package ru.sergeysemenov.trialcalculator.service;

import org.springframework.stereotype.Service;
import ru.sergeysemenov.trialcalculator.dto.Expression;
import ru.sergeysemenov.trialcalculator.exceptions.BadExpressionException;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CalculatorService {

    private final Map<Character, Integer> operationPriority = new HashMap<>();
    private final Set<String> operationString = new HashSet<>();
    private final static String BAD_EXPRESSION = "Некорректное выражение";

    public CalculatorService(){
        operationPriority.put('+', 1);
        operationPriority.put('-', 1);
        operationPriority.put('*', 2);
        operationPriority.put('/', 2);
        operationString.add("+");
        operationString.add("-");
        operationString.add("/");
        operationString.add("*");
    }

    public String calculate(Expression expression) {
        List<String> record = postfixRecord(expression.getValue());
        if(record.size()==1){
            return record.get(0);
        }
        Stack<BigDecimal> stack = new Stack<>();
        for (String s:record) {
            if(!operationString.contains(s)){
                try {
                    BigDecimal value = new BigDecimal(s);
                    stack.push(value);
                } catch (NumberFormatException e){
                    throw new BadExpressionException(BAD_EXPRESSION);
                }
            }else{
                try {
                    BigDecimal b = stack.pop();
                    BigDecimal a = stack.pop();
                    switch (s){
                        case "+" ->stack.push(a.add(b));
                        case "-" ->stack.push(a.subtract(b));
                        case "/" ->stack.push(a.divide(b));
                        case "*" ->stack.push(a.multiply(b));
                    }
                } catch (EmptyStackException e){
                    throw new BadExpressionException(BAD_EXPRESSION);
                }
            }
        }
        return stack.pop().toString();
    }
    private List<String> postfixRecord(String expression){
        if(expression.isEmpty()) throw new BadExpressionException(BAD_EXPRESSION);
        List<String> record = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        Set<Character> operations = operationPriority.keySet();
        char[] sequence = expression.toCharArray();
        char prev ='-';
        for (int i=0; i<sequence.length;i++) {
            if((operations.contains(prev) || prev=='(') && sequence[i]=='-'){  // обрабатываем отрицательные числа
                i = addDigitToRecord(record, sequence, i);
            } else if (Character.isDigit(sequence[i])) { // обрабатываем все числа
                i = addDigitToRecord(record, sequence, i);
            } else if (sequence[i]=='(') {
                stack.push(sequence[i]);
            } else if (sequence[i]==')') {
                addOperationsToRecord(record, stack);
            } else if (operations.contains(sequence[i])) { // обрабатываем все операции
                addOperationToStack(record,stack, sequence[i], operations);
            } else { // если не скобки, не числа и не операции, то выкидываем ошибку
                throw new BadExpressionException(BAD_EXPRESSION);
            }
            prev = sequence[i];
        }
        while (!stack.isEmpty()){
            char operation = stack.pop();
            if(!operations.contains(operation)){
                throw new BadExpressionException(BAD_EXPRESSION);
            }else {
                record.add(String.valueOf(operation));
            }
        }
        return record;
    }

    private void addOperationToStack(List<String> record, Stack<Character> stack, char operation, Set<Character> operations) {
        if(stack.isEmpty()) stack.push(operation);
        else{
            while (!stack.isEmpty() && operations.contains(stack.peek()) && operationPriority.get(stack.peek())>=operationPriority.get(operation)){
                record.add(String.valueOf(stack.pop()));
            }
            stack.push(operation);
        }
    }

    private static void addOperationsToRecord(List<String> record, Stack<Character> stack) {
        try{
            while (stack.peek()!='('){
                record.add(String.valueOf(stack.pop()));
            }
            stack.pop();
        }catch (EmptyStackException e){
            throw new BadExpressionException(BAD_EXPRESSION);
        }
    }

    private static int addDigitToRecord(List<String> record, char[] sequence, int i) {
        StringBuilder sb = new StringBuilder();
        do{
            sb.append(sequence[i]);
            i++;
        } while (i < sequence.length && (Character.isDigit(sequence[i]) || sequence[i]=='.'));
        record.add(sb.toString());
        i--;
        return i;
    }
}
