package ibf2022.workshop28.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.workshop28.exception.GameReviewException;
import ibf2022.workshop28.repository.GameRepository;


@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    GameRepository gameRepo;

    public Document createResultDoc(int gid) throws GameReviewException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timeNow = new Date();
        String timestamp = format.format(timeNow);
        Document doc = gameRepo.findGameByGID(gid);
        Document docAvg = gameRepo.getAverageRating(gid);

        List<Document> reviewIDs = gameRepo.getReviewIDs(gid);
        List<String> reviewArr = new ArrayList<String>();
        for (Document review : reviewIDs) {
            String rid = review.get("_id").toString();
            String resultStr = "/review/" + rid;
            reviewArr.add(resultStr);
        }

        if (doc == null) {
            throw new GameReviewException("Document not found");
        }

        Document newDoc = new Document();
        newDoc.put("game_id", gid);
        newDoc.put("name", doc.get("name"));
        newDoc.put("year", doc.get("year"));
        newDoc.put("rank", doc.get("ranking"));
        newDoc.put("average rating", docAvg.get("average"));
        newDoc.put("users_rated", doc.get("users_rated"));
        newDoc.put("url", doc.get("url"));
        newDoc.put("thumbnail", doc.get("image"));
        newDoc.put("reviews", reviewArr.toString());
        newDoc.put("timestamp", timestamp); 
    
        return newDoc;
    }

}
