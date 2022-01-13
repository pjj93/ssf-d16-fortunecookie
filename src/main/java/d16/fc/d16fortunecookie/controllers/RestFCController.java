package d16.fc.d16fortunecookie.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d16.fc.d16fortunecookie.services.FortuneCookie;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping(path="/cookies", produces=MediaType.APPLICATION_JSON_VALUE)
public class RestFCController {
    
    @Autowired
    private FortuneCookie fc;

    @GetMapping
    public ResponseEntity<String> getCookie(@RequestParam(defaultValue = "1") Integer n) {

        if ((n > 10) || (n < 1)){
            JsonObjectBuilder errBuilder = Json.createObjectBuilder();
            errBuilder.add("error", "count must be between 1 and 10 inclusive");
            JsonObject errObj = errBuilder.build();
            // ResponseEntity
            //     .status(HttpStatus.BAD_REQUEST)
            //     .body("count must be between 1 and 10 inclusive"); // fluent api style
            return ResponseEntity.badRequest().body(errObj.toString());
        }

        List<String> cookies = fc.getCookies(n);

        JsonObjectBuilder cookieObjBuilder = Json.createObjectBuilder();
        JsonArrayBuilder cookieArrBuilder = Json.createArrayBuilder();

        for(String c: cookies) {
            cookieArrBuilder.add(c);
        }

/*         cookies.parallelStream()
            .reduce(
                cookieArrBuilder, //identity
                (ab, item) -> ab.add(item),
                (ab0, ab1) -> {
                    JsonArray a = ab1.build();
                    for (int i=0; i< a.size(); i++)
                        ab0.add(a.get(i));
                    return ab0;
                }
            ); */  //stream to add cookies in parallel execution
        
        JsonArray cookieArr = cookieArrBuilder.build();
        cookieObjBuilder.add("cookies", cookieArr).add("timestamp", System.currentTimeMillis());

        JsonObject cookieObj = cookieObjBuilder.build();
    
        return ResponseEntity.ok(cookieObj.toString());
    }
}
