package ibf2022.workshop28.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class GameRepository {

    
    @Autowired
    MongoTemplate mongoTemplate;

    /*
     * db.games.aggregate([
     * { $match: {gid: 5 }},
     * { 
     *  $lookup: {
     *      from: 'comments',
     *      foreignField: "gid",
     *      localField: "gid",
     *      as: 'reviews'
     * } },
     * ])
     */
    public Document findGameByGID(int gid) {
        // 1. Stages
        MatchOperation matchId = Aggregation.match(Criteria.where("gid").is(gid));
        LookupOperation lookupComments = Aggregation.lookup("comment", "gid", "gid", "reviews");
        
        // ProjectionOperation selectFields = Aggregation.project("gid", "name", "year", "rank");

    
        // 2. Pipeline
        Aggregation pipeline = Aggregation.newAggregation(matchId, lookupComments);

        // 3. Query
        Document retrievedGame = mongoTemplate.aggregate(pipeline, "game", Document.class).getUniqueMappedResult(); // Get only the first result

        // 4. Projection
        return retrievedGame;
    }


    /* 
     * db.comments.find(
        { gid : 1},
        { _id : 1})
     */
    public List<Document> getReviewIDs(int gid) {
        Criteria criteria = Criteria.where("gid").is(gid); 
        Query query = Query.query(criteria);
        query.fields().include("_id");
        List<Document> reviewIDList = mongoTemplate.find(query, Document.class, "comment");
        return reviewIDList;
    }

    /*
     * db.comments.aggregate(
        { $match: { gid: 5 } }, 
        { $group: {
            _id: "$gid",   
            average: { $avg: "$rating" }  
        }
      })
     */
    public Document getAverageRating(int gid) {
        MatchOperation matchGID = Aggregation.match(Criteria.where("gid").is(gid));
        GroupOperation group = Aggregation.group("gid")
                                        .avg("rating").as("average");
        Aggregation pipeline = Aggregation.newAggregation(matchGID, group);
        Document retrievedAverage = mongoTemplate.aggregate(pipeline, "comment", Document.class).getUniqueMappedResult();
        return retrievedAverage;
    }




    // Method to print results
    // public void printIt (AggregationResults<Document> results) {
    //     for (Document d: results) {
    //         System.out.printf(">>>> %s\n", d.toJson());
    //     }
    // }
}
