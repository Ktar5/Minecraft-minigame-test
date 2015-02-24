package ktar.five.TurfWars.Game.Info;

public enum GameStatus {

    RESTARTING(1),
    IN_PROGRESS(2),
    WAITING_FOR_PLAYERS(3),
    LOBBY_COUNTDOWN(4),
    STARTING(5),
    ENDING(6),
    ERROR(7);

    public int id;

    private GameStatus(int id){
        this.id = id;
    }

    public static GameStatus getById(int id){
        for(GameStatus status : GameStatus.values()){
            if(status.id == id){
                return status;
            }
        }
        return GameStatus.ERROR;
    }

}
