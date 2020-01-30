package inf112.skeleton.app;

public class Options {

    private int totalPlayers, players, boardX, boardY;

    Options(){
        totalPlayers = 8;
        players = 8;
        boardX = 1;
        boardY = 1;
    }


    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        assert 0 < totalPlayers;
        this.players = Math.min(players, totalPlayers);
        this.totalPlayers = totalPlayers;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        assert 0 < players;
        this.players = Math.min(players, getTotalPlayers());
    }
}
