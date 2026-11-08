package com.Elanco.Tick_Sighting.BackEnd;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Just here to clean up the urls
@Controller
public class PageController {

    @GetMapping("/map")
    public String map() {
        return "forward:/map.html";
    }

    @GetMapping("/identification")
    public String identification() {
        return "forward:/identification.html";
    }
}
