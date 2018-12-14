import java.util.*;
import java.io.*;

public class Game{

  private Team t1, t2;
  private PrintWriter writer;

  private int [] hits1;//number of hits each player gets in the game
  private int [] hits2;

  private int [] hr1;//number of homers each player gets in the game
  private int [] hr2;

  private int [] rbi1;//number of hits each player gets in the game
  private int [] rbi2;

  private int [] ab1;//number of at bats each player gets in the game
  private int [] ab2;

  private int [] bb1;//number of walks each player gets in the game
  private int [] bb2;


  private int [] runs1;//number of runs each team scores each inning
  private int [] runs2;

  public Game(Team t1, Team t2, boolean playoff){
    this.t1=t1;
    this.t2=t2;
    hits1=new int[9];
    hits2=new int[9];
    hr1=new int[9];
    hr2=new int[9];
    rbi1=new int[9];
    rbi2=new int[9];
    ab1=new int[9];
    ab2=new int[9];
    bb1=new int[9];
    bb2=new int[9];

    runs1=new int[9];
    runs2=new int[9];

    try{
      if(playoff){
        writer=new PrintWriter(new BufferedWriter(new FileWriter("./playoffLogs/"+t1.getName()+"Vs"+t2.getName()+".txt")));
      }else{
        writer=new PrintWriter(new BufferedWriter(new FileWriter("./gameLogs/"+t1.getName()+"Vs"+t2.getName()+".txt")));
      }

    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void play(){
    t1.runs=0;
    t2.runs=0;
    boolean top=true;
    int position1=0;
    int position2=0;

    try{
      writer.println("Away: "+t1.getName()+" vs "+t2.getName()+" :Home");
      for(int i=0;i<18;i++){
        if(i==17 && t2.runs>t1.runs){
          break;
        }
        //System.out.println(" ");
        writer.println(" ");
        if(top){
          //System.out.println("Top of inning "+((i/2)+1));
          writer.println("Top of inning "+((i/2)+1));

          position1=playInning(i, position1, t1, t2, hits1, ab1, hr1, rbi1, runs1,bb1);
          top=false;
        }else{
          //System.out.println("Bottom of inning "+((i/2)+1));
          writer.println("Bottom of inning "+((i/2)+1));

          position2=playInning(i, position2, t2, t1, hits2, ab2, hr2, rbi2, runs2,bb2);
          top=true;
        }
      }
      if(t1.runs>t2.runs){
        System.out.println("The "+t1.getName()+" win by a score of "+t1.runs+"-"+t2.runs+" over the "+t2.getName());
        writer.println("The "+t1.getName()+" win by a score of "+t1.runs+"-"+t2.runs+" over the "+t2.getName());

        t1.w++;
        t1.points+=2;
        t2.l++;
      }else if(t2.runs>t1.runs){
        System.out.println("The "+t2.getName()+" win by a score of "+t2.runs+"-"+t1.runs+" over the "+t1.getName());
        writer.println("The "+t2.getName()+" win by a score of "+t2.runs+"-"+t1.runs+" over the "+t1.getName());

        t2.w++;
        t2.points+=2;
        t1.l++;
      }else{

        System.out.println("It's a tie!");
        writer.println("It's a tie!");
        printScore(t1,t2);
        t1.points++;
        t2.points++;
        t1.t++;
        t2.t++;


      }
      t1.rd+=t1.runs-t2.runs;
      t2.rd+=t2.runs-t1.runs;
      printStats();
      writer.close();

    }catch(Exception e){
      e.printStackTrace();
    }

  }

  private int playInning(int inning, int position, Team t1, Team t2, int [] hits1, int[]ab1, int[]hr1, int[]rbi1, int[]runs1,int[]bb1){
    int outs=0;
    boolean first=false;
    boolean second=false;
    boolean third=false;

    while(outs<3){
      if(inning==17 && t1.runs>t2.runs){//walkoff
        break;
      }
      Object []stat=atBat(t1.getPlayers()[position], t1, t2, outs, first, second, third, position, (inning/2)+1, hits1, ab1, hr1, rbi1, runs1,bb1);

      outs=(int)stat[0];
      first=(boolean)stat[1];
      second=(boolean)stat[2];
      third=(boolean)stat[3];
      position=(int)stat[4];

      //t1.getPlayers()[position].ab++;

    }
    return position;

  }

  private Object[] atBat(Player p, Team t1, Team t2, int outs, boolean first, boolean second, boolean third, int position, int inning, int [] hits1, int[]ab1, int[]hr1, int[]rbi1, int[]runs1,int[]bb1){
    int hit;
    Object []stat=new Object[11];
    Random r=new Random();
    hit=r.nextInt(1000);

    if(hit<250){//a hit
      hits1[position]++;
      Object[] bases=new Object[5];
      if(hit<200){//single
        bases=single(p,t1,t2,inning-1,first,second,third, rbi1[position], runs1[inning-1]);
      }else if(hit<225){//double
        bases=double2(p,t1,t2,inning-1,first,second,third, rbi1[position], runs1[inning-1]);
      }else if(hit<245){//homer
        hr1[position]++;
        bases=homer(p,t1,t2,inning-1,first,second,third, rbi1[position], runs1[inning-1]);
      }else{//triple
        bases=triple(p,t1,t2,inning-1,first,second,third, rbi1[position], runs1[inning-1]);
      }
      first=(boolean)bases[0];
      second=(boolean)bases[1];
      third=(boolean)bases[2];

      //this.hr1[position]=(int)bases[3];
      rbi1[position]=(int)bases[3];
      runs1[inning-1]=(int)bases[4];

    }else{//no hit
      hit=r.nextInt(6);
      switch(hit){
        case 0://walk
          p.ab--;
          bb1[position]++;
          ab1[position]--;
          //System.out.print(p.getName()+" walks. ");
          writer.print(p.getName()+" walks. ");
          if(!first){
            first=true;

          }else{

            if(second && third){
              p.rbi++;
              t1.runs++;
              rbi1[position]++;
              runs1[inning-1]++;
              //System.out.println("A run scores. ");
              writer.println("A run scores. ");

              printScore(t1, t2);

            }
            if(second && !third){
              third=true;
            }
            second=true;

          }
          if(second){
            //System.out.print("Man on 2nd. ");
            writer.print("Man on 2nd. ");

          }
          if(third){
            //System.out.print("Man on 3rd. ");
            writer.print("Man on 3rd. ");

          }
          //System.out.println("Man on 1st");
          writer.println("Man on 1st");

          break;

        case 1://strikeout
          outs++;
          //System.out.println(p.getName()+" strikes out. Outs: "+outs);
          writer.println(p.getName()+" strikes out. Outs: "+outs);

          break;

        case 2://groundout
          outs++;
          //System.out.println(p.getName()+" grounds out. Outs: "+outs);
          writer.println(p.getName()+" grounds out. Outs: "+outs);

          break;

        case 3://popout
          outs++;
          //System.out.println(p.getName()+" pops out. Outs: "+outs);
          writer.println(p.getName()+" pops out. Outs: "+outs);

          break;

        case 4://flyout
          outs++;
          //System.out.println(p.getName()+" flies out. Outs: "+outs);
          writer.println(p.getName()+" flies out. Outs: "+outs);

          break;

        case 5://foulout
          outs++;
          //System.out.println(p.getName()+" fouls out. Outs: "+outs);
          writer.println(p.getName()+" fouls out. Outs: "+outs);

          break;
      }
    }
    p.ab++;
    ab1[position]++;
    position++;
    if(position>8){
      position=0;
    }

    stat[0]=outs;
    stat[1]=first;
    stat[2]=second;
    stat[3]=third;
    stat[4]=position;

    stat[5]=this.hits1;
    stat[6]=this.ab1;
    stat[7]=this.hr1;
    stat[8]=this.rbi1;
    stat[9]=this.runs1;
    stat[10]=this.bb1;
    return stat;

  }

  private Object[] single(Player p, Team t1, Team t2, int inning, boolean first, boolean second, boolean third, int rbi, int runs){
    p.hits++;
    int runsScored=0;
    Object[] bases=new Object[5];
    if(third){
      third=false;
      p.rbi++;
      rbi++;
      t1.runs++;
      runsScored++;
    }
    if(second){
      third=true;
      second=false;
      //System.out.println("Man on 3rd");
    }
    if(first){
      second=true;
      //System.out.println("Man on 2nd");
    }else{
      first=true;
    }
    runs+=runsScored;

    if(runsScored>0){
      //System.out.println(p.getName()+" singles. A run scores. ");
      writer.println(p.getName()+" singles. A run scores. ");

      printScore(t1,t2);
    }else{
      //System.out.print(p.getName()+" singles. ");
      writer.print(p.getName()+" singles. ");

    }
    if(second){
      //System.out.print("Man on 2nd. ");
      writer.print("Man on 2nd. ");
    }
    if(third){
      //System.out.print("Man on 3rd. ");
      writer.print("Man on 3rd. ");
    }
    //System.out.println("Man on 1st");
    writer.println("Man on 1st");

    bases[0]=first;
    bases[1]=second;
    bases[2]=third;

    bases[3]=rbi;
    bases[4]=runs;

    return bases;

  }

  private Object[] double2(Player p, Team t1, Team t2, int inning, boolean first, boolean second, boolean third, int rbi, int runs){
    p.hits++;
    int runsScored=0;
    Object[] bases=new Object[5];

    if(third){
      p.rbi++;
      rbi++;
      t1.runs++;
      runsScored++;
      third=false;
    }
    if(second){
      p.rbi++;
      rbi++;
      t1.runs++;
      runsScored++;
    }else{
      second=true;
    }
    if(first){
      third=true;
      first=false;
    }
    runs+=runsScored;

    if(runsScored>0){
      //System.out.println(p.getName()+" doubles. "+runsScored+" runs score. ");
      writer.println(p.getName()+" doubles. "+runsScored+" runs score. ");

      printScore(t1,t2);
    }else{
      //System.out.print(p.getName()+" doubles. ");
      writer.print(p.getName()+" doubles. ");

    }
    if(third){
      //System.out.print("Man on 3rd. ");
      writer.print("Man on 3rd. ");

    }
    //System.out.println("Man on 2nd");
    writer.println("Man on 2nd");

    bases[0]=first;
    bases[1]=second;
    bases[2]=third;

    bases[3]=rbi;
    bases[4]=runs;

    return bases;

  }

  private Object[] triple(Player p, Team t1, Team t2, int inning, boolean first, boolean second, boolean third, int rbi, int runs){
    Object[] bases=new Object[5];

    p.hits++;
    int runsScored=0;
    for(int i=0;i<menOnBase(first,second,third);i++){
      t1.runs++;
      p.rbi++;
      rbi++;
      runsScored++;
    }
    if(runsScored>0){
      //System.out.println(p.getName()+" triples. "+runsScored+" runs score");
      writer.println(p.getName()+" triples. "+runsScored+" runs score");

      printScore(t1,t2);
    }else{
      //System.out.print(p.getName()+" triples. ");
      writer.print(p.getName()+" triples. ");

    }
    //System.out.println("Man on 3rd");
    writer.println("Man on 3rd");
    runs+=runsScored;

    first=false;
    second=false;
    third=true;

    bases[0]=first;
    bases[1]=second;
    bases[2]=third;

    bases[3]=rbi;
    bases[4]=runs;

    return bases;

  }

  private Object[] homer(Player p, Team t1, Team t2, int inning, boolean first, boolean second, boolean third, int rbi, int runs){
    p.rbi++;
    p.hr++;
    p.hits++;
    t1.runs++;
    rbi++;
    Object[] bases=new Object[5];
    int runsScored=1;
    for(int i=0;i<menOnBase(first,second, third);i++){
      p.rbi++;
      rbi++;
      t1.runs++;
      runsScored++;
    }
    //System.out.println(p.getName()+" homers. "+runsScored+" runs score");
    writer.println(p.getName()+" homers. "+runsScored+" runs score");
    runs+=runsScored;

    printScore(t1,t2);

    first=false;
    second=false;
    third=false;

    bases[0]=first;
    bases[1]=second;
    bases[2]=third;

    bases[3]=rbi;
    bases[4]=runs;

    return bases;

  }

  private int menOnBase(boolean first, boolean second, boolean third){
    if(first && !second && !third
    || !first && second && !third
    || !first && !second && third){
      return 1;

    }else if (first && second && third){
      return 3;
    }else if(!first && !second && !third){
      return 0;
    }else{
      return 2;
    }
  }

  private void printScore(Team t1, Team t2){
    //System.out.println("Score: "+t1.getName()+" "+t1.runs+"-"+t2.runs+" "+t2.getName());
    writer.println("Score: "+t1.getName()+" "+t1.runs+"-"+t2.runs+" "+t2.getName());
  }

  private void printStats(){
    int totalHits1=0;
    int totalHits2=0;
    int totalAb1=0;
    int totalAb2=0;
    int totalHr1=0;
    int totalHr2=0;
    int totalBB1=0;
    int totalBB2=0;

    for(int i=0;i<9;i++){
      totalHits1+=hits1[i];
      totalHits2+=hits2[i];
      totalAb1+=ab1[i];
      totalAb2+=ab2[i];
      totalHr1+=hr1[i];
      totalHr2+=hr2[i];
      totalBB1+=bb1[i];
      totalBB2+=bb2[i];
    }
    writer.println(" ");
    writer.printf("%-10s|\t1\t|\t2\t|\t3\t|\t4\t|\t5\t|\t6\t|\t7\t|\t8\t|\t9\t|Runs\t|Hits", "Inning");
    writer.println(" ");
    writer.printf("%-10s|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|%d\t|%d",t1.getName(),runs1[0],runs1[1],runs1[2],runs1[3],runs1[4],runs1[5],runs1[6],runs1[7],runs1[8],t1.runs,totalHits1);
    writer.println(" ");
    writer.printf("%-10s|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|\t%d\t|%d\t|%d",t2.getName(),runs2[0],runs2[1],runs2[2],runs2[3],runs2[4],runs2[5],runs2[6],runs2[7],runs2[8],t2.runs,totalHits2);
    writer.println(" ");
    writer.println("Stats");
    writer.println(t1.getName());
    writer.printf("%-10s|AB\t|H\t|HR\t|RBI\t|BB","Name");
    writer.println(" ");

    for(int i=0;i<9;i++){
      writer.printf("%-10s|%d\t|%d\t|%d\t|%d\t|%d",t1.getPlayers()[i].getName(),ab1[i],hits1[i],hr1[i],rbi1[i],bb1[i]);
      writer.println(" ");
    }
    writer.printf("%-10s|%d\t|%d\t|%d\t|%d\t|%d","Total",totalAb1,totalHits1,totalHr1,t1.runs,totalBB1);
    writer.println(" ");
    writer.println(" ");
    writer.println(t2.getName());
    writer.printf("%-10s|AB\t|H\t|HR\t|RBI\t|BB","Name");
    writer.println(" ");
    for(int i=0;i<9;i++){
      writer.printf("%-10s|%d\t|%d\t|%d\t|%d\t|%d",t2.getPlayers()[i].getName(),ab2[i],hits2[i],hr2[i],rbi2[i],bb2[i]);
      writer.println(" ");
    }
    writer.printf("%-10s|%d\t|%d\t|%d\t|%d\t|%d","Total",totalAb2,totalHits2,totalHr2,t2.runs,totalBB2);

  }

}
