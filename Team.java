import java.util.*;

public class Team{

  public int w=0;
  public int l=0;
  public int t=0;
  public int points=0;
  public int rd=0;
  public int runs;
  private Player[] players;
  private String name;

  public Team(String n, Player[] players){
    this.name=n;
    this.players=players;
  }

  public String getName(){
    return this.name;
  }

  public Player[] getPlayers(){
    return this.players;
  }

}
