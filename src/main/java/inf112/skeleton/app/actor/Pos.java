package inf112.skeleton.app.actor;

public class Pos implements Comparable<Pos>{
	public int x, y;

	public Pos(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int compareTo(Pos p){
		return (this.x != p.x ? this.x - p.x : this.y - p.y);
	}
}
