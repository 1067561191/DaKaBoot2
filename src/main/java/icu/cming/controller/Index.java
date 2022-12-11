package icu.cming.controller;

import icu.cming.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class Index {

    @Resource(name = "studentService")
    private StudentService studentService;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("success", studentService.getSuccess());
        mav.addObject("fail", studentService.getFail());
        mav.setViewName("index");
        return mav;
    }

}
