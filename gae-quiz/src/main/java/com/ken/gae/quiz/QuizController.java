package com.ken.gae.quiz;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuizController {
    
    @RequestMapping("/list")
    public String list() {
        return "List Add!";
    }
    
    @RequestMapping("/{id}")
    public String getQuiz(@PathVariable("id") String id) {
    	return "Quiz ID=" + id;
    }
    
    @RequestMapping("/{id}/delete")
    public String deleteQuiz(@PathVariable	("id") String id) {
    	return "Delete: Quiz ID=" + id;
    }
    
}
