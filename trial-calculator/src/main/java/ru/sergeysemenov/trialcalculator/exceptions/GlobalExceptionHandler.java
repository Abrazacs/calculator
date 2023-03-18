package ru.sergeysemenov.trialcalculator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.sergeysemenov.trialcalculator.dto.Expression;
import ru.sergeysemenov.trialcalculator.dto.Result;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public String badExpressionHandler(BadExpressionException e, Model model){
        model.addAttribute("expression", new Expression());
        model.addAttribute("result", new Result());
        model.addAttribute("appError", new AppError(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
        return "index";
    }
}
