//Jayden Lim
//5-15-22
//PGame.java
//Creates leaderboard of top 10 scores
import java.awt.*;  
import javax.swing.*; 
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;

//Leaderboard panel that shows ranks, score, time, kills, and player
public class Leaderboard extends JLayeredPane implements MouseListener {
    Scanner scan;
    Scanner scan2;
    Scanner scan3;
    Scanner scan4;
    Scanner scan5;
    Scanner scan6;

    ArrayList<String> gameNames2 = new ArrayList<>();
    ArrayList<ArrayList<Integer>> gameStats2 = new ArrayList<>();
    HashMap<String,ArrayList<ArrayList<Integer>>> games2 = new HashMap<>();
    String[] name2 = new String[10];
    int[][] stats2 = new int[10][3];
    ArrayList<Integer> rank2 = new ArrayList<>();
    long[] allstats2 = new long[7];

    ArrayList<String> gameNames = new ArrayList<>();
    ArrayList<ArrayList<Integer>> gameStats = new ArrayList<>();

    HashMap<String,ArrayList<ArrayList<Integer>>> games = new HashMap<>();

    String[] name = new String[10];
    int[][] stats = new int[10][3];
    Font font;
    Image grass, pokedex, leadlogo, scroll;
    JButton back = new JButton("BACK");
    JButton next = new JButton("NEXT");
    long[] allstats = new long[10];
    ArrayList<Integer> rank = new ArrayList<>();
    int statPage = 0;

    JButton searchButton = new JButton("Search");
    JTextField search = new JTextField();
    String currentSearch = "";
    boolean userFocus = false;

    boolean dodgeMode = false;

    JButton dodgeChange = new JButton("Dodge");

    //Set font and initialize scanner and images
    public Leaderboard()
    {
        try {
            Font tmpfont = Font.createFont(Font.TRUETYPE_FONT, new File("Minecraft.ttf"));
            font = tmpfont.deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        tryCatchIt();
        for(int i = 0; i < 10; i++)
        {
            name[i] = scan.next();
            stats[i][0] = scan.nextInt();
            stats[i][1] = scan.nextInt();
            stats[i][2] = scan.nextInt();
        }

        for(int i = 0; i < 10; i++)
        {
            name2[i] = scan4.next();
            stats2[i][0] = scan4.nextInt();
            stats2[i][1] = scan4.nextInt();
            stats2[i][2] = scan4.nextInt();
        }

        for(int i = 0; i < 10; i++)
        {
            allstats[i] = scan2.nextLong();
        }
        for(int i = 0; i < 7; i++)
        {
            allstats2[i] = scan5.nextLong();
        }
        
        while(scan3.hasNextLine() && scan3.hasNext())
        {
            String name = scan3.next();
            int tmpScore = scan3.nextInt();
            int kills = scan3.nextInt();
            int time = scan3.nextInt();
            ArrayList<Integer> tmpList = new ArrayList<>();
            
            tmpList.add(tmpScore);
            tmpList.add(kills);
            tmpList.add(time);

            if(games.containsKey(name))
                games.get(name).add(tmpList);
            else
            {
                ArrayList<ArrayList<Integer>> lol = new ArrayList<>();
                lol.add(tmpList);
                games.put(name, lol);
            }

            gameNames.add(name);
            rank.add(tmpScore);
            gameStats.add(tmpList);
            
        }

        while(scan6.hasNextLine() && scan6.hasNext())
        {
            String name = scan6.next();
            int tmpScore = scan6.nextInt();
            int kills = scan6.nextInt();
            int time = scan6.nextInt();
            ArrayList<Integer> tmpList = new ArrayList<>();
            
            tmpList.add(tmpScore);
            tmpList.add(kills);
            tmpList.add(time);

            if(games2.containsKey(name))
                games2.get(name).add(tmpList);
            else
            {
                ArrayList<ArrayList<Integer>> lol = new ArrayList<>();
                lol.add(tmpList);
                games2.put(name, lol);
            }

            gameNames2.add(name);
            rank2.add(tmpScore);
            gameStats2.add(tmpList);
            
        }

        Collections.sort(rank,Collections.reverseOrder());
        Collections.sort(rank2,Collections.reverseOrder());

        grass = new ImageIcon("pikagrass.png").getImage();
        pokedex = new ImageIcon("leaderbackground.png").getImage();
        leadlogo = new ImageIcon("leaderlogo.png").getImage();
        scroll = new ImageIcon("scroll.png").getImage();

        addMouseListener(this);
        run();
    }
    //Make back button
    public void run()
    {
        setOpaque(false);
        back.setFont(font.deriveFont(15f));
        back.setBounds(10,10,100,40);
        back.setVerticalAlignment(JButton.CENTER);
        add(back);
        
        next.setFont(font.deriveFont(15f));
        next.setBounds(680,10,100,40);
        next.addActionListener(e -> {
            statPage = (statPage+1)%3;
            search.setVisible(false);
            searchButton.setVisible(false);
            currentSearch = "";
            repaint();
        });
        add(next);

        dodgeChange.setFont(font.deriveFont(15f));
        dodgeChange.setBounds(580,10,100,40);
        dodgeChange.addActionListener(e -> {
            dodgeMode = !dodgeMode;
            search.setVisible(false);
            searchButton.setVisible(false);
            currentSearch = "";
            repaint();
        });
        add(dodgeChange);


        search.setFont(font.deriveFont(20f));
        search.setBounds(195,270,320,40);
        search.setVisible(false);
        search.addFocusListener(new NameFocus());
        search.addActionListener(e -> search.setSelectionEnd(0));
        add(search);

        searchButton.setFont(font.deriveFont(15f));
        searchButton.setBounds(530,270,80,40);
        searchButton.setVisible(false);
        searchButton.addActionListener(e -> {currentSearch = search.getText(); repaint();});
        add(searchButton);

        
    }
    //Draw background and scroll and the leaderboard text
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(pokedex, -240, 0, 1280, 800, null);
        g.drawImage(scroll, 25, 150, 750, 625, null);
        g.drawImage(leadlogo, 100, 60, 600, 70, null);
        if(dodgeMode)
        {
            g.setFont(font.deriveFont(30f));
            g.drawString("Mode: Dodge",300,50);
            if(statPage == 1)
            {
                g.setFont(font.deriveFont(25f));
                String[] statNames = {"Total Games:","Total Waves:","Total Time:","Pokeballs Thrown:","Lasers Shot:","Total Dashes:","Total Spins:"};
                for(int i = 0; i < 7; i++)
                {
                    g.drawString(statNames[i],215,260 + (i * 65));
                    g.drawString(""+allstats2[i],500,260 + (i * 65));
                }
                
            }
            else if(statPage == 0)
            {
                g.setFont(font.deriveFont(15f));
                for(int i = 0; i < 10; i++)
                {
                    g.drawString(String.format("%7s%-4s %-20s","",(i+1)+".", name2[i]),170,300+(i*45)-40);
                    g.drawString(String.format("Time: %02d:%02d\n\n",stats2[i][0]/60, (stats2[i][0]%60)),280,300+(i*45)-20);
                    g.drawString(String.format("Pokeballs: %-5d",stats2[i][1]),400,300+(i*45)-20);
                    g.drawString(String.format(" Lasers: %d\n\n",stats2[i][2]),520,300+(i*45)-20);
                }
            }
            else
            {
                
                if(userFocus)
                {
                    search.requestFocus();
                    search.setCaretPosition(search.getText().length());
                }
                g.setFont(font.deriveFont(20f));
                g.drawString("Search for your top plays!",200,250);
                search.setVisible(true);
                searchButton.setVisible(true);
                if(!currentSearch.equals(""))
                {
                    if(games2.containsKey(currentSearch))
                    {
                        bubbleSortArrayList(games2.get(currentSearch));
                        ArrayList<ArrayList<Integer>> tmpList = games2.get(currentSearch);
                        for(int i = 0; i < Math.min(10,tmpList.size()); i++)
                        {
                            g.drawString(String.format("%-3s Time: %02d:%02d",i+1+".",tmpList.get(i).get(0)/60, (tmpList.get(i).get(0)%60)),200,300+(i*35)+50);
                            g.drawString(String.format("  Pokeballs: %-5d",tmpList.get(i).get(1)),340,300+(i*35)+50);
                            g.drawString(String.format("         Lasers: %d",tmpList.get(i).get(2)),440,300+(i*35)+50);
                        }
                    }
    
                    else
                    {
                        g.setColor(Color.RED);
                        g.drawString("NO USER FOUND!",200,360);
                        g.setColor(Color.BLACK);
                    }
                }
            }
        }
        else
        {
            g.setFont(font.deriveFont(30f));
            g.drawString("Mode: Revenge",300,50);
            if(statPage == 1)
            {
                g.setFont(font.deriveFont(15f));
                String[] statNames = {"Total Games:","Total Kills:","Total Waves:","Total Score:","Total Time:","Pokeballs Thrown:","Lasers Shot:","Total Dashes:","Total Spins:","Total Lightnings:"};
                for(int i = 0; i < 10; i++)
                {
                    g.drawString(statNames[i],215,260 + (i * 45));
                    g.drawString(""+allstats[i],400,260 + (i * 45));
                }
                
            }
            else if(statPage == 0)
            {
                g.setFont(font.deriveFont(15f));
                for(int i = 0; i < 10; i++)
                {
                    g.drawString(String.format("%7s%-4s %-20s","",(i+1)+".", name[i]),170,300+(i*45)-40);
                    g.drawString(String.format(" Score: %-11d",stats[i][0]),320,300+(i*45)-20);
                    g.drawString(String.format("  Kills: %-5d",stats[i][1]),440,300+(i*45)-20);
                    g.drawString(String.format(" Time: %02d:%02d\n\n",stats[i][2]/60, (stats[i][2]%60)),520,300+(i*45)-20);
                }
            }
            else
            {
                if(userFocus)
                {
                    search.requestFocus();
                    search.setCaretPosition(search.getText().length());
                }
                g.setFont(font.deriveFont(20f));
                g.drawString("Search for your top plays!",200,250);
                search.setVisible(true);
                searchButton.setVisible(true);
                if(!currentSearch.equals(""))
                {
                    if(games.containsKey(currentSearch))
                    {
                        bubbleSortArrayList(games.get(currentSearch));
                        ArrayList<ArrayList<Integer>> tmpList = games.get(currentSearch);
                        for(int i = 0; i < Math.min(10,tmpList.size()); i++)
                        {
                            g.drawString(String.format("%-3s   Score: %-11d",i+1+".",tmpList.get(i).get(0)),200,300+(i*35)+50);
                            g.drawString(String.format("     Kills: %-5d",tmpList.get(i).get(1)),360,300+(i*35)+50);
                            g.drawString(String.format("       Time: %02d:%02d\n\n",tmpList.get(i).get(2)/60, (tmpList.get(i).get(2)%60)),440,300+(i*35)+50);
                        }
                    }
    
                    else
                    {
                        g.setColor(Color.RED);
                        g.drawString("NO USER FOUND!",200,360);
                        g.setColor(Color.BLACK);
                    }
                }
            }
        }
    }
    //Make scanner and read leaderboard.txt
    public void tryCatchIt()
	{
	   File inFile = new File ("leaderboard.txt"); 
       File inFile2 = new File("stats.txt");
       File inFile3 = new File("games.txt");
       File inFile4 = new File ("dodgeLeader.txt"); 
       File inFile5 = new File("stats2.txt");
       File inFile6 = new File("dodge.txt");

	   String inFileName = "leaderboard.txt"; 
	   scan = null; 
	   
	   try
	   { 
		 scan = new Scanner ( inFile );
	   }
	   catch ( FileNotFoundException e ) 
	   { 
		 System.err.println("Cannot find " + inFileName + " file.");  
		 System.exit(1);
	   }

       try
	   { 
		 scan2 = new Scanner ( inFile2 );
	   }
	   catch ( FileNotFoundException e ) 
	   { 
		 System.err.println("Cannot find " + inFileName + " file.");  
		 System.exit(1);
	   }

       try
	   { 
		 scan3 = new Scanner ( inFile3 );
	   }
	   catch ( FileNotFoundException e ) 
	   { 
		 System.err.println("Cannot find " + inFileName + " file.");  
		 System.exit(1);
	   }

       try
	   { 
		 scan6 = new Scanner ( inFile6 );
	   }
	   catch ( FileNotFoundException e ) 
	   { 
		 System.err.println("Cannot find " + inFileName + " file.");  
		 System.exit(1);
	   }

       try
	   { 
		 scan5 = new Scanner ( inFile5 );
	   }
	   catch ( FileNotFoundException e ) 
	   { 
		 System.err.println("Cannot find " + inFileName + " file.");  
		 System.exit(1);
	   }

       try
	   { 
		 scan4 = new Scanner ( inFile4 );
	   }
	   catch ( FileNotFoundException e ) 
	   { 
		 System.err.println("Cannot find " + inFileName + " file.");  
		 System.exit(1);
	   }
	
	}
    //Write new leaderboard changes to the text file
    public void update()
    {
        PrintWriter pw = null; 
		File outFile = new File("leaderboard.txt");
		try
		{  
		  pw = new PrintWriter( new FileWriter(outFile, false) ); 
		}
		catch ( IOException e)
		{ 
		  System.err.println("Cannot append to " +  
							 " leaderboard.txt ");
		  System.exit(1);
		}

        for(int i = 0; i < 10; i++)
        {
            pw.printf("%s %d %d %d\n",name[i],stats[i][0],stats[i][1],stats[i][2]);
        }
        pw.close();

        PrintWriter pw2 = null; 
		File outFile2 = new File("stats.txt");
		try
		{  
		  pw2 = new PrintWriter( new FileWriter(outFile2, false) ); 
		}
		catch ( IOException e)
		{ 
		  System.err.println("Cannot append to " +  
							 " stats.txt ");
		  System.exit(1);
		}

        for(int i = 0; i < 10; i++)
        {
            pw2.println(allstats[i]);
        }
        pw2.close();

        PrintWriter pw3 = null; 
		File outFile3 = new File("games.txt");
		try
		{  
		  pw3 = new PrintWriter( new FileWriter(outFile3, false) ); 
		}
		catch ( IOException e)
		{ 
		  System.err.println("Cannot append to " +  
							 " games.txt ");
		  System.exit(1);
		}

        for(int i = 0; i < gameNames.size(); i++)
        {
            pw3.printf("%s %d %d %d\n",gameNames.get(i),gameStats.get(i).get(0),gameStats.get(i).get(1),gameStats.get(i).get(2));
        }
        pw3.close();

        PrintWriter pw4 = null; 
		File outFile4 = new File("dodgeLeader.txt");
		try
		{  
		  pw4 = new PrintWriter( new FileWriter(outFile4, false) ); 
		}
		catch ( IOException e)
		{ 
		  System.err.println("Cannot append to " +  
							 " leaderboard.txt ");
		  System.exit(1);
		}

        for(int i = 0; i < 10; i++)
        {
            pw4.printf("%s %d %d %d\n",name2[i],stats2[i][0],stats2[i][1],stats2[i][2]);
        }
        pw4.close();

        PrintWriter pw5 = null; 
		File outFile5 = new File("stats2.txt");
		try
		{  
		  pw5 = new PrintWriter( new FileWriter(outFile5, false) ); 
		}
		catch ( IOException e)
		{ 
		  System.err.println("Cannot append to " +  
							 " stats.txt ");
		  System.exit(1);
		}

        for(int i = 0; i < 7; i++)
        {
            pw5.println(allstats2[i]);
        }
        pw5.close();

        PrintWriter pw6 = null; 
		File outFile6 = new File("dodge.txt");
		try
		{  
		  pw6 = new PrintWriter( new FileWriter(outFile6, false) ); 
		}
		catch ( IOException e)
		{ 
		  System.err.println("Cannot append to " +  
							 " games.txt ");
		  System.exit(1);
		}

        for(int i = 0; i < gameNames2.size(); i++)
        {
            pw6.printf("%s %d %d %d\n",gameNames2.get(i),gameStats2.get(i).get(0),gameStats2.get(i).get(1),gameStats2.get(i).get(2));
        }
        pw6.close();
    }
    //Compare stats of current game and add to leaderboard if needed
    public void newGame(String player, int score, int kills, int time, long balls, int waves, int laserCount, int dashCount, int spinCount, int lightningCount)
    {
        /*
        Games: 0
        Kills: 0
        Total_Waves: 0
        Total_Scores: 0
        Total_Time: 0
        Pokeballs_Thrown: 0
        Lasers: 0
        Dashes: 0
        Spins: 0
        Lightning: 0
        */
        allstats[0] += 1;
        allstats[1] += kills;
        allstats[2] += waves;
        allstats[3] += score;
        allstats[5] += balls;
        allstats[4] += time;
        allstats[6] += laserCount;
        allstats[7] += dashCount;
        allstats[8] += spinCount;
        allstats[9] += lightningCount;

        rank.add(score);
        Collections.sort(rank,Collections.reverseOrder());

        gameNames.add(player);
        ArrayList<Integer> tmpList = new ArrayList<>();
        tmpList.add(score);
        tmpList.add(kills);
        tmpList.add(time);
        gameStats.add(tmpList);

        for(int i = 8; i >= 0; i--)
        {
            if(stats[i][0] > score && stats[i+1][0] < score)
            {
                shift(i+1);
                name[i+1] = player;
                stats[i+1][0] = score;
                stats[i+1][1] = kills;
                stats[i+1][2] = time;
                return;
            }
            else if(score == stats[i+1][0])
            {
                if(kills == stats[i+1][1])
                {
                    if(time < stats[i+1][2])
                    {
                        shift(i+1);
                        name[i+1] = player;
                        stats[i+1][0] = score;
                        stats[i+1][1] = kills;
                        stats[i+1][2] = time;
                        return;
                    }
                }
                else if(kills > stats[i+1][1])
                {
                    shift(i+1);
                    name[i+1] = player;
                    stats[i+1][0] = score;
                    stats[i+1][1] = kills;
                    stats[i+1][2] = time;
                    return;
                }
            }
        }
        if(score == stats[0][0])
        {
            if(kills == stats[0][1])
            {
                if(time < stats[0][2])
                {
                    shift(0);
                    name[0] = player;
                    stats[0][0] = score;
                    stats[0][1] = kills;
                    stats[0][2] = time;
                    return;
                }
            }
            else if(kills > stats[0][1])
            {
                shift(0);
                name[0] = player;
                stats[0][0] = score;
                stats[0][1] = kills;
                stats[0][2] = time;
                return;
            }
        }
        else if(score > stats[0][0])
        {
            shift(0);
            name[0] = player;
            stats[0][0] = score;
            stats[0][1] = kills;
            stats[0][2] = time;
            return;
        }
    }
    
    //Compare stats of current game and add to leaderboard if needed
    public void dodgeGame(String player, int time, int balls, int waves, int laserCount, int dashCount, int spinCount)
    {
        /*
        Games: 0
        Total_Waves: 0
        Total_Time: 0
        Pokeballs_Thrown: 0
        Lasers: 0
        Dashes: 0
        Spins: 0
        Lightning: 0
        */
        allstats2[0] += 1;
        allstats2[1] += waves;
        allstats2[3] += balls;
        allstats2[2] += time;
        allstats2[4] += laserCount;
        allstats2[5] += dashCount;
        allstats2[6] += spinCount;

        rank2.add(time);
        Collections.sort(rank2,Collections.reverseOrder());

        gameNames2.add(player);
        ArrayList<Integer> tmpList = new ArrayList<>();
        tmpList.add(time);
        tmpList.add(balls);
        tmpList.add(laserCount);
        gameStats2.add(tmpList);

        for(int i = 8; i >= 0; i--)
        {
            if(stats2[i][0] > time && stats2[i+1][0] < time)
            {
                shift2(i+1);
                name2[i+1] = player;
                stats2[i+1][0] = time;
                stats2[i+1][1] = balls;
                stats2[i+1][2] = laserCount;
                return;
            }
            else if(time == stats2[i+1][0])
            {
                if(balls == stats2[i+1][1])
                {
                    if(laserCount < stats2[i+1][2])
                    {
                        shift2(i+1);
                        name2[i+1] = player;
                        stats2[i+1][0] = time;
                        stats2[i+1][1] = balls;
                        stats2[i+1][2] = laserCount;
                        return;
                    }
                }
                else if(balls > stats2[i+1][1])
                {
                    shift2(i+1);
                    name2[i+1] = player;
                    stats2[i+1][0] = time;
                    stats2[i+1][1] = balls;
                    stats2[i+1][2] = laserCount;
                    return;
                }
            }
        }
        if(time == stats2[0][0])
        {
            if(balls == stats2[0][1])
            {
                if(laserCount < stats2[0][2])
                {
                    shift2(0);
                    name2[0] = player;
                    stats2[0][0] = time;
                    stats2[0][1] = balls;
                    stats2[0][2] = laserCount;
                    return;
                }
            }
            else if(balls > stats2[0][1])
            {
                shift2(0);
                name2[0] = player;
                stats2[0][0] = time;
                stats2[0][1] = balls;
                stats2[0][2] = laserCount;
                return;
            }
        }
        else if(time > stats2[0][0])
        {
            shift2(0);
            name2[0] = player;
            stats2[0][0] = time;
            stats2[0][1] = balls;
            stats2[0][2] = laserCount;
            return;
        }
    }

    //Shift the players that are beat down the leaderboard
    public void shift(int index)
    {
        String tmpname1 = name[index];
        int[] tmp1 = stats[index].clone();
        String tmpname2 = "";
        int[] tmp2 = new int[3];
        if(index != 9)
        {
            tmpname2 = name[index+1];
            tmp2 = stats[index+1].clone();
        }
        for(int i = index+1; i < 10; i++)
        {
            name[i] = tmpname1;
            stats[i] = tmp1;
            tmpname1 = tmpname2;
            tmp1 = tmp2.clone();
            if(i != 9)
            {
                tmpname2 = name[i+1];
                tmp2 = stats[i+1];
            }
            
        }
    }

    public void shift2(int index)
    {
        String tmpname1 = name2[index];
        int[] tmp1 = stats2[index].clone();
        String tmpname2 = "";
        int[] tmp2 = new int[3];
        if(index != 9)
        {
            tmpname2 = name2[index+1];
            tmp2 = stats2[index+1].clone();
        }
        for(int i = index+1; i < 10; i++)
        {
            name2[i] = tmpname1;
            stats2[i] = tmp1;
            tmpname1 = tmpname2;
            tmp1 = tmp2.clone();
            if(i != 9)
            {
                tmpname2 = name2[i+1];
                tmp2 = stats2[i+1];
            }
        }
    }

    //Resets leaderboard
    public void reset()
    {
        PrintWriter pw = null; 
		File outFile = new File("leaderboard.txt");
		try
		{  
		  pw = new PrintWriter( new FileWriter(outFile, false) ); 
		}
		catch ( IOException e)
		{ 
		  System.err.println("Cannot append to " +  
							 " leaderboard.txt ");
		  System.exit(1);
		}

        for(int i = 0; i < 10; i++)
        {
            pw.printf("%s %d %d %d\n","---",0,0,0);
        }
        pw.close();
    }

    //Sort arraylist based on kills
    public void bubbleSortArrayList(ArrayList<ArrayList<Integer>> list) {
        ArrayList<Integer> temp;
        boolean sorted = false;
    
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < list.size()-1; i++) {
                if (list.get(i).get(0) < list.get(i+1).get(0)) {
                    temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    sorted = false;
                }
            }
        }
    }

    //Keep Jtextfield focused
    class NameFocus implements FocusListener
    {
 
        //Set boolean so username textfield is focused
        public void focusGained(FocusEvent e) {
            userFocus = true;
        }
        //Does nothing
        public void focusLost(FocusEvent e) {
            
        }
 
    }

    //Do Nothing
    public void mouseClicked(MouseEvent e) {}

    //Keep focus on username textfield
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println(x + " " + y);
        if(x >= 200 && x <= 500 && y >= 300 && y <= 340)
            userFocus = true;
        else
            userFocus = false;
    }

    //Does nothing
    public void mouseReleased(MouseEvent e) {}

    //Does Nothing
    public void mouseEntered(MouseEvent e) {}

    //Does Nothing
    public void mouseExited(MouseEvent e) {}

}
