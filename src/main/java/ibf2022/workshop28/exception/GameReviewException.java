package ibf2022.workshop28.exception;

import ibf2022.workshop28.model.Game;

public class GameReviewException extends Exception {
    
    private Game game;

    
    public GameReviewException(String message) {
        super(message);
    }


    public Game getGame() {return this.game;}
    public void setGame(Game game) {this.game = game;}

}
