package inf112.skeleton.app;

public class Options {

    private int totalPlayers;
    private int players;

    Options(){
        totalPlayers = 8;
        players = 8;
    }


    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }
}
