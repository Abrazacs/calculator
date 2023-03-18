package ru.sergeysemenov.trialcalculator.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sergeysemenov.trialcalculator.dto.Expression;
import ru.sergeysemenov.trialcalculator.dto.Result;
import ru.sergeysemenov.trialcalculator.exceptions.AppError;
import ru.sergeysemenov.trialcalculator.service.CalculatorService;

@Controller
@RequiredArgsConstructor
public class CalculatorController {
    private final CalculatorService service;

    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("expression", new Expression());
        model.addAttribute("appError", new AppError());
        model.addAttribute("result", new Result());
        return "index";
    }

    @PostMapping("/")
    public String calculate(@ModelAttribute ("expression") Expression expression,
                            @ModelAttribute("appError") AppError error, Model model){
        model.addAttribute("result", new Result(service.calculate(expression)));
        return "index";
    }


}
