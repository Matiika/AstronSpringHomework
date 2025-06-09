package com.AstronSpringHomework.App;

import org.springframework.web.bind.annotation.*;

@RestController // Аннотация на уровне класса
@RequestMapping("/api") // Аннотация на уровне класса
public class Test1 {

    @GetMapping("/hello")  // Аннотация на уровне метода
    public String sayHello(@RequestParam(name = "name", required = false, defaultValue = "Guest") String name) { // Аннотация на уровне параметров
        return "Hello, " + name + "!";
    }

    @PostMapping("/greet") // Аннотация на уровне метода
    public String greet(@RequestBody String name) {  // Аннотация на уровне параметров
        return "Greetings, " + name + "!";
    }
}
