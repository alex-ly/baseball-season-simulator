import java.util.*;

public class Player{

  public int ab = 0;
  public int hits = 0;
  public double avg;
  public int hr = 0;
  public int rbi = 0;
  private String name;

  public Player(String n){
    this.name=n;
  }

  public String getName(){
    return this.name;
  }

}
