package ibf2022.workshop28.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    /****************** TASK A *******************/
    /*
     * db.games.aggregate([
     * { $match: {gid: 5 }},
     * { $lookup: {
     *      from: 'comments',
     *      foreignField: "gid",
     *      localField: "gid",
     *      as: 'reviews'
     * } },
     * ])
     */
    public Document findGameByGID(int gid) {
        // 1. Stages (Match, Lookup)
        MatchOperation matchId = Aggregation.match(Criteria.where("gid").is(gid));
        LookupOperation lookupComments = Aggregation.lookup("comment", "gid", "gid", "reviews");

        // 2. Pipeline
        Aggregation pipeline = Aggregation.newAggregation(matchId, lookupComments);

        // 3. Query
        Document retrievedGame = mongoTemplate.aggregate(pipeline, "game", Document.class)
                                              .getUniqueMappedResult(); // To retrieve only the first result

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



    /****************** TASK B *******************/
    /*
     * db.comment.aggregate([
        { $sort: { gid: 1, rating: -1 }},
        { $group: {
            _id: "$gid",
            rating: { $first: "$rating" },
            user: { $first: "$user"},
            comment: {$first: "$c_text"},
            review_id: {$first: "$_id"}
        } },
        { $sort: { _id: 1 } },
        { $limit: 10 }])
     */
    public List<Document> getReviewsRanked(String highestOrLowest) {
        // 1. Stages (Sort, Sort, Group, Sort, Limit)
        SortOperation sortByGID = Aggregation.sort(Direction.ASC, "gid");

        SortOperation sortByRating = null;
        if (highestOrLowest.contains("highest")) {
            sortByRating = Aggregation.sort(Direction.DESC, "rating");
        } else if (highestOrLowest.contains("lowest")) {
            sortByRating = Aggregation.sort(Direction.ASC, "rating");
        }

        GroupOperation groupOp = Aggregation.group("gid")
                                    .first("rating").as("rating")
                                    .first("user").as("user")
                                    .first("c_text").as("comment")
                                    .first("_id").as("review_id");
        SortOperation sortByID = Aggregation.sort(Direction.ASC, "_id");
        LimitOperation limit = Aggregation.limit(10); // limit to 10 results to improve query speed
    
        // 2. Pipeline
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build(); // Add allowDiskUse to address exceeded memory error
        Aggregation pipeline = Aggregation.newAggregation(sortByGID, sortByRating, groupOp, sortByID, limit).withOptions(options); 
        // Aggregation pipeline = Aggregation.newAggregation(sortByGID, sortByRating, groupOp, sortByID, limit);

        // 3. Query
        AggregationResults<Document> reviews = mongoTemplate.aggregate(pipeline, "comment", Document.class);

        // 4. Projection
        return reviews.getMappedResults();
    }

    /*
     * db.game.find(
        {gid : 1},
        {_id:0, name:1}
     */
    public Document getNameByGID(int gid) {
        Query query = Query.query(Criteria.where("gid").is(gid));
        query.fields().exclude("_id").include("name");
        Document result = mongoTemplate.findOne(query, Document.class, "game");
        return result;
    }
}



    // Another aggregate method
    /*
     * db.game.aggregate([
        { $lookup: {
            from: 'comment',
            foreignField: "gid",
            localField: "gid",
            as: 'reviews',
            pipeline: [
                { $sort: { "rating": -1} },
                { $limit: 1},
            ] } },
        { $sort: { gid : 1 } }
        ])
    */
    // public List<Document> findHighestRatedReview() {
    //     // 1. Stages
    //     LookupOperation lookupComments = Aggregation.lookup("comment", "gid", "gid", "reviews");
    //     SortOperation sortByGID = Aggregation.sort(Sort.by(Direction.DESC, "gid"));
    //     // 2. Pipeline
    //     Aggregation pipeline = Aggregation.newAggregation(lookupComments, sortByGID);
    //     // 3. Query
    //     AggregationResults<Document> reviews = mongoTemplate.aggregate(pipeline, "game", Document.class);
    //     // 4. Projection
    //     return reviews.getMappedResults();
    // }