package com.dianpoint.summer.test.web;

import com.dianpoint.summer.stereotype.Controller;
import com.dianpoint.summer.web.RequestMapping;
import com.dianpoint.summer.web.annotation.RequestParam;
import com.dianpoint.summer.web.annotation.ResponseBody;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(@RequestParam("name") String name) {
        return "Hello, " + name;
    }

    @RequestMapping("/json")
    @ResponseBody
    public User json(@RequestParam(value = "id", defaultValue = "0") int id) {
        User user = new User();
        user.setId(id);
        user.setName("Test");
        return user;
    }

    public static class User {
        private int id;
        private String name;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
