package ibf2022.workshop28.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.workshop28.exception.GameReviewException;
import ibf2022.workshop28.model.Review;
import ibf2022.workshop28.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepo;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date timeNow = new Date();
    String timestamp = format.format(timeNow);


    /****************** TASK A *******************/
    public Document createResultDoc(int gid) throws GameReviewException {
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


    /****************** TASK B *******************/
    public String getNameByID(int gid) {
        Document result = gameRepo.findGameByGID(gid);
        return result.getString("name");
    }

    public List<String> getRankedGameList(String highestOrLowest) {
        List<Document> documents = gameRepo.getReviewsRanked(highestOrLowest);

        List<String> games = new ArrayList<>();
        for (Document doc : documents) {
            int gid = (int) doc.get("_id");
            Review review = new Review();
            String gameName = getNameByID(gid);
            review.setGid(gid);
            review.setName(gameName);
            int rating = (int) doc.get("rating");
            String user = doc.get("user").toString();
            String comment = doc.get("comment").toString();
            String reviewId = doc.get("review_id").toString();
            review.setRating(rating);
            review.setUser(user);
            review.setComment(comment);
            review.setReviewid(reviewId);
            games.add(review.toString());
            System.out.println(">>>>>>" + review.toString());
        }
        return games;
    }
}
