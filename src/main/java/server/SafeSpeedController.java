
package com.example.SafeSpeed;
import org.springframework.web.bind.annotation.*;  // Importing the necessary annotations
import server.Database.DB;

@RestController
@RequestMapping("/api")
public class SafeSpeedController {

    // GET endpoint
    @GetMapping("/hello")
    public String getHello() {
        return "Hello, World!";
    }

    // POST endpoint
    @PostMapping("/greet")
    public String postGreeting(@RequestBody Greeting greeting) {
        return "Hello, " + greeting.getName() + "!";
    }

    // Class to handle the POST request body
    public static class Greeting {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

