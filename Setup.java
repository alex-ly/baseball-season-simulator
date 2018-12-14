import java.util.*;
import java.io.*;

public class Setup{

  private int numTeams;
  private Team[] teams;
  private Team[] playoffTeams;
  //private Team[] advancing;

  public Setup(int n){
    this.numTeams=n;
    this.teams=new Team[numTeams];
    initialize();
    displayStandings(teams);
    playoffTeams=makePlayoffs();
  }

  private void initialize(){
    boolean [][] played=new boolean[numTeams][numTeams];
    String teamName, playerName;

    for(int i=0;i<numTeams;i++){
      Player[] players=new Player[9];
      teamName=" ";
      try{
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("./teams/"+i+".txt")));
        teamName=br.readLine();
        for(int j=0;j<9;j++){
          playerName=br.readLine();
          players[j]=new Player(playerName);
        }
      }catch(Exception e){
        e.printStackTrace();
      }

      teams[i]=new Team(teamName, players);

      for(int j=0;j<numTeams;j++){
        if(i==j){
          played[i][j]=true;
        }else{
          played[i][j]=false;
        }
      }

    }

    int num1, num2;
    Random r=new Random();

    while(!finished(played, numTeams)){
      num1=r.nextInt(numTeams);
      num2=r.nextInt(numTeams);
      if(!played[num1][num2]){
        Game game=new Game(teams[num1], teams[num2], false);
        game.play();
        played[num1][num2]=true;
        played[num2][num1]=true;
      }
    }

  }

  private boolean finished(boolean[][] played, int n){
    for(int i=0;i<n;i++){
      for(int j=0;j<n;j++){
        if(!played[i][j]){
          return false;
        }
      }
    }
    return true;
  }

  private void displayStandings(Team[] teams){
    try{
      PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter("./stats/stats.txt")));
      //System.out.printf("%-10s\t%s\t%s\t%s\n","Team","Wins","Losses","Ties");
      writer.printf("%-10s\t%s\t%s\t%s\t%s\t%s","Team","Wins","Losses","Ties","Points","Run Differential");
      writer.println(" ");

      for(int i=0;i<teams.length;i++){
        //System.out.printf("%-10s\t%d\t%d\t%d\n",teams[i].getName(), teams[i].w, teams[i].l, teams[i].t);
        writer.printf("%-10s\t%d\t%d\t%d\t%d\t%d",teams[i].getName(), teams[i].w, teams[i].l, teams[i].t,teams[i].points,teams[i].rd);
        writer.println(" ");
      }
      //System.out.println("\n");
      //System.out.printf("%-10s\t%s\t%s\t%s\n","Name","Avg","HR","RBI");
      writer.println("\n");

      for(int i=0;i<teams.length;i++){
        writer.println(teams[i].getName());
        writer.printf("%-10s\t%s\t%s\t%s\n","Name","Avg","HR","RBI");
        writer.println(" ");

        for(int j=0;j<9;j++){
          teams[i].getPlayers()[j].avg=(double)teams[i].getPlayers()[j].hits/teams[i].getPlayers()[j].ab;
          //System.out.printf("%-10s\t%.3f\t%d\t%d\n",teams[i].getPlayers()[j].getName(),teams[i].getPlayers()[j].avg,teams[i].getPlayers()[j].hr,teams[i].getPlayers()[j].rbi);
          writer.printf("%-10s\t%.3f\t%d\t%d\n",teams[i].getPlayers()[j].getName(),teams[i].getPlayers()[j].avg,teams[i].getPlayers()[j].hr,teams[i].getPlayers()[j].rbi);
          writer.println(" ");
        }
        //System.out.println(" ");
        writer.println(" ");

      }
      writer.close();
    }catch(Exception e){
      e.printStackTrace();
    }

  }

  private Team[] makePlayoffs(){
    int numPlayoffTeams=2;
    while(numPlayoffTeams<numTeams){
      if(numPlayoffTeams*2<numTeams){
        numPlayoffTeams*=2;
      }else{
        break;
      }
    }
    Team[] playoffTeams=sortTeams(teams);
    //sort the teams by points
    playoffTeams=new Team[numPlayoffTeams];
    for(int i=0;i<numPlayoffTeams;i++){
      playoffTeams[i]=teams[i];
      System.out.println(teams[i].getName()+" has made the playoffs");
    }

    return playoffs(playoffTeams);

  }

  private Team[] sortTeams(Team[] teams){
    Team[] playoffTeams=new Team[teams.length];
    for(int i=0;i<teams.length;i++){
      for(int j=i+1;j<teams.length;j++){
        if(teams[i].points<teams[j].points || teams[i].points==teams[j].points && teams[i].rd<teams[j].rd){
          Team t=teams[i];
          teams[i]=teams[j];
          teams[j]=t;
        }
      }
    }
    return playoffTeams;

  }

  private Team[] playoffs(Team[] playoffTeams){
    if(playoffTeams.length==1){
      System.out.println(playoffTeams[0].getName()+" have won the championships!");
      return playoffTeams;
    }

    Team[] advancing=new Team[playoffTeams.length/2];
    System.out.println(playoffTeams.length/2+" are advancing");
    for(int i=0;i<playoffTeams.length/2;i++){
      System.out.println("PLAYOFFS");

      System.out.println(playoffTeams[playoffTeams.length-i-1].getName()+" Vs "+playoffTeams[i].getName());
      Game g=new Game(playoffTeams[playoffTeams.length-i-1],playoffTeams[i],true);
      g.play();
      if(playoffTeams[playoffTeams.length-i-1].runs>playoffTeams[i].runs){
        advancing[i]=playoffTeams[playoffTeams.length-i-1];
        advancing[i].points-=2;

      }else if(playoffTeams[playoffTeams.length-i-1].runs<playoffTeams[i].runs){
        advancing[i]=playoffTeams[i];
        advancing[i].points-=2;

      }else{
        i--;
        playoffTeams[playoffTeams.length-i-1].points--;
        playoffTeams[i].points--;
      }

      playoffTeams[playoffTeams.length-i-1].rd-=playoffTeams[playoffTeams.length-i-1].runs-playoffTeams[i].runs;
      playoffTeams[i].rd-=playoffTeams[i].runs-playoffTeams[playoffTeams.length-i-1].runs;

    }

    return playoffs(advancing);

  }

}
