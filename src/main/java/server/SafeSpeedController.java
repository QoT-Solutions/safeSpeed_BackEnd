
package server;
import org.springframework.web.bind.annotation.*;  // Importing the necessary annotations
import server.Database.DB;

@RestController
@RequestMapping("/api")
public class SafeSpeedController {

    // GET endpoint
    @GetMapping("/healthCheck")
    public String getHello() {
        return "Hello, World!";
    }
}

