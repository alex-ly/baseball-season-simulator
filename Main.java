import java.util.*;

public class Main{

  private static int numFiles=9;

  public static void main(String[] args){
    Scanner scan=new Scanner(System.in);
    int numTeams;
    do{
      System.out.print("Enter number of teams: ");
      numTeams=scan.nextInt();
      if(numTeams>numFiles){
        System.out.println("Too many teams");
      }
      if(numTeams<2){
        System.out.println("Too few teams");
      }
    }while(numTeams>numFiles || numTeams<2);
    scan.close();
    Setup setup=new Setup(numTeams);
  }
}
