package ibf2022.workshop28.model;

public class Game {
    
    private int gid;
    private String name;
    private int year;
    private int rank;
    private int users_rated;
    private String url;
 

    public int getGid() {return this.gid;}
    public void setGid(int gid) {this.gid = gid;}

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public int getYear() {return this.year;}
    public void setYear(int year) {this.year = year;}

    public int getRank() {return this.rank;}
    public void setRank(int rank) {this.rank = rank;}

    public int getUsers_rated() {return this.users_rated;}
    public void setUsers_rated(int users_rated) {this.users_rated = users_rated;}

    public String getUrl() {return this.url;}
    public void setUrl(String url) {this.url = url;}

}
