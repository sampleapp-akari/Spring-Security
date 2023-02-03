package com.example.authclient.web.controller;

import com.example.authclient.service.TodoService;
import com.example.authclient.service.dto.Todo;
import com.example.authclient.web.form.TodoForm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        logger.debug("{}", authentication);
        List<Todo> todoList = todoService.findAll();
        model.addAttribute("todoList", todoList);
        return "index";
    }

    @PostMapping("/add")
    public String add(TodoForm todoForm) {
        Todo todo = todoForm.convertToDto();
        todoService.save(todo);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer id) {
        todoService.updateDoneById(id);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Integer id) {
        todoService.deleteById(id);
        return "redirect:/";
    }
    
}
