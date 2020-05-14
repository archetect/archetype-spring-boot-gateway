package {{ root_package }}.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RootController {

    @RequestMapping("/")
    public Mono<String> root() {
        return Mono.just("{{ RootName }}");
    }
}
