package ibf2022.workshop28.controller;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.workshop28.exception.GameReviewException;
import ibf2022.workshop28.service.GameService;

@RestController
@RequestMapping
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    GameService gameSvc;

    @GetMapping(path="/game/{game_id}/reviews")
    public ResponseEntity<String> getGameReviews(@PathVariable("game_id") int gid) {
        
        try {
            Document doc = gameSvc.createResultDoc(gid);
            return ResponseEntity.status(200).body("Review retrieved successfully: " + doc.toJson());
        } catch (GameReviewException e) {
            logger.error("Document not found for gid: {}", gid, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid game id: " + gid);
        }
        
    }
}
