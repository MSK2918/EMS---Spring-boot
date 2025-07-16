package com.susheel.ems.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @RequestMapping(value = "/", method = RequestMethod.GET, name = "Home greet controller")
    public String home() {
        return "Welcome to Employee Management System";
    }
}
