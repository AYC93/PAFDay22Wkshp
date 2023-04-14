package prac2.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import prac2.app.model.RSVP;
import prac2.app.repository.RSVPRepo;

@RestController
@RequestMapping("/api")
public class RSVPRestController {
    
    @Autowired
    RSVPRepo rsvpRepo;

    @GetMapping(path ="/rsvps", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllRSVP(){
        List<RSVP> rsvp = rsvpRepo.listAllRSVP();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (RSVP r : rsvp)
            arrayBuilder.add(r.toJson());

        JsonArray result = arrayBuilder.build();
        System.out.println("Valid customer list");
        
        return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.toString());
    }

    @GetMapping(path ="/rsvp")
    public ResponseEntity<String> getSpecificNameInRSVP(@RequestParam String q){
        List<RSVP> rsvp = rsvpRepo.checkNameInRSVP(q);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (RSVP r : rsvp)
            arrayBuilder.add(r.toJson());

        JsonArray result = arrayBuilder.build();
        if(!result.isEmpty()){
        System.out.println("Valid customer");
        
        return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result.toString());
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body("RSVP does not exist!!");
    }

    @PostMapping(path="/rsvp")
    public ResponseEntity<String> addOrUpdateRSVP(@RequestBody String json){
        RSVP rsvp = RSVP.create(json);
        if (rsvpRepo.checkEmailInRSVP(json)==null){
            rsvpRepo.newRSVP(rsvp);
            return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Json.createObjectBuilder().add("rsvpId",rsvp.getId()).build().toString());
            }
             return ResponseEntity.status(HttpStatusCode.valueOf(401))
            .contentType(MediaType.APPLICATION_JSON)
            .body("Exists");
    }

    @PutMapping(path="/rsvp/{email}")
    public ResponseEntity<String> updateRSVP(@PathVariable String email, @RequestBody String json){
        RSVP rsvp = new RSVP();
        rsvp = RSVP.create(json);
        if(rsvpRepo.checkEmailInRSVP(email)==null)
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("No email found");
        else{
            RSVP result = rsvpRepo.updateRSVP(rsvp);
            return ResponseEntity.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(result.toString());
        }
    }

    @GetMapping(path="rsvps/count")
    public ResponseEntity<String> numberOfPplInRSVP(){
        RSVP rsvp = new RSVP();
        int result = rsvpRepo.countRSVP(rsvp);
        return ResponseEntity.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(String.valueOf(result));
    }

}
