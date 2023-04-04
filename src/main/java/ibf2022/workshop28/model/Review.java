package ibf2022.workshop28.model;

public class Review {
    private int gid;
    private String name;
    private int rating;
    private String user;
    private String comment;
    private String reviewid;

    
    public int getGid() {return this.gid;}
    public void setGid(int gid) {this.gid = gid;}

    public String getName() {return this.name;}  // Retrieved from Game Collections
    public void setName(String name) {this.name = name;}

    public int getRating() {return this.rating;}
    public void setRating(int rating) {this.rating = rating;}

    public String getUser() {return this.user;}
    public void setUser(String user) {this.user = user;}

    public String getComment() {return this.comment;}
    public void setComment(String comment) {this.comment = comment;}

    public String getReviewid() {return this.reviewid;}
    public void setReviewid(String reviewid) {this.reviewid = reviewid;}


    @Override
    public String toString() {
        return "{" +
            " gid='" + getGid() + "'" +
            ", name='" + getName() + "'" +
            ", rating='" + getRating() + "'" +
            ", user='" + getUser() + "'" +
            ", comment='" + getComment() + "'" +
            ", reviewid='" + getReviewid() + "'" +
            "}";
    }

}
