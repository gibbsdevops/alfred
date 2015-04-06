package com.gibbsdevops.alfred.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RootController extends ApiController {

    @RequestMapping(method = RequestMethod.GET)
    public Object status() {
        return generateStatus("api", "OK");
    }

}
