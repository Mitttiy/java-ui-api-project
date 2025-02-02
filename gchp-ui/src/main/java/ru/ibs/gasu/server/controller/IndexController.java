package ru.ibs.gasu.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @Value("${buildIdentifier}")
    private String buildIdentifier;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("buildIdentifier", buildIdentifier);
        return "index";
    }
}
