package ibf2022.workshop28.controller;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.workshop28.exception.GameReviewException;
import ibf2022.workshop28.service.GameService;

@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    @Autowired
    GameService gameSvc;

     /****************** TASK A *******************/
    @GetMapping(path="/game/{game_id}/reviews")
    public ResponseEntity<String> getGameReviews(@PathVariable("game_id") int gid) {
        try {
            Document doc = gameSvc.createResultDoc(gid);
            return ResponseEntity.status(200).body("Review retrieved successfully: " + doc.toJson());
        } catch (GameReviewException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid game id: " + gid);
        }
    }

    
     /****************** TASK B *******************/
    @GetMapping(path="/games/{direction}")
    public ResponseEntity<String> showRanked(@PathVariable("direction") String highestOrLowest)  {
        if (!highestOrLowest.equals("highest") && !highestOrLowest.equals("lowest")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Wrong input, please indicate either 'highest' or 'lowest' in query");
        }
        
        List<String> games = gameSvc.getRankedGameList(highestOrLowest);
    
        return ResponseEntity.status(200)
                             .body("Rating: " + highestOrLowest + ", Games: " + games.toString() + "TimeStamp: " + new Date());
    }
}
