//Jayden Lim
//5-15-22
//PGame.java
//Creates game to be played

import java.awt.*;  
import java.awt.event.*;
import javax.swing.*;


import java.io.*;
import java.util.ArrayList;
import java.lang.Math;
import java.time.Duration;
import java.time.Instant;


//The game panel
//Panel shown when play is pressed
//User can play the game with keys
public class PGame extends JLayeredPane implements KeyListener, MouseListener {
    //Pikachu
    int PIKA_SIZE = 100;
    int PIKA_LOC_X = 350;
    int PIKA_LOC_Y = 350;
    int PIKA_SPEED = 3;
    int throw_chance = 60;
    int start_trainers = 5;
    int spawn_amount = 3;
    int spawn_rate = 8000;

    double PI = Math.PI;

    int pokeballs = 0;
    int laserCount = 0;
    Instant start, stop;

    Timer moving;
    Movement move;
    boolean uppress, downpress, leftpress, rightpress;
    Timer pikaTime;
    Pika pika;
    Image grass;
    Image buttonTemplate;

    Image[] pikapics = new Image[8];
    Image[] tpics = new Image[12];
    Image[] ballpics = new Image[8];
    Image[] attackpics = new Image[5];
    Image[] dashpics = new Image[2];
    Image[] bloodpics = new Image[16];
    Image[] portalpics = new Image[5];
    Image[] laserpics = new Image[3];
    //Image[] lightningpics = new Image[5];
    Image[] light2pics = new Image[8];
    Image[] smokepics = new Image[12];

    Image settingImage;
    Image frontfence, leftfence, rightfence;
    Image excla;
    Image youdied = new ImageIcon("YOU-DIED.png").getImage();
    //Image rocks = new ImageIcon("rocks.png").getImage();
    Image level2 = new ImageIcon("level2.png").getImage();
    Image level3 = new ImageIcon("snow.png").getImage();
    Image redLife = new ImageIcon("redheart.png").getImage();
    Image whiteLife = new ImageIcon("whiteheart.png").getImage();
    Image playButton = new ImageIcon("play.png").getImage();
    Image pauseButton = new ImageIcon("pause.png").getImage();
    Image cooldowndash = new ImageIcon("cooldown/cooldowndash.png").getImage();
    Image cooldownspin = new ImageIcon("cooldown/spinicon2.png").getImage();
    Image cooldownlightning = new ImageIcon("cooldown/cooldownlightning.png").getImage();

    int TRAINER_SIZE_WIDTH = 60;
    int TRAINER_SIZE_HEIGHT = 80;
    Timer trainTime; 
    Trainer trainer;
    
    Timer attackAni;
    Attack attack;

    ArrayList<Timer> trainTimers = new ArrayList<>();
    ArrayList<Trainer> trainers = new ArrayList<>();

    ArrayList<Timer> bloodTimers = new ArrayList<>();
    ArrayList<Blood> bloodMarks = new ArrayList<>();

    ArrayList<Timer> smokeTimers = new ArrayList<>();
    ArrayList<Smoke> smokeMarks = new ArrayList<>();

    ArrayList<Timer> ballTimers = new ArrayList<>();
    ArrayList<Pokeball> balls = new ArrayList<>();

    ArrayList<Timer> laserTimers = new ArrayList<>();
    ArrayList<Laser> lasers = new ArrayList<>();

    ArrayList<Timer> lightningTimers = new ArrayList<>();
    ArrayList<Lightning> lightnings = new ArrayList<>();

    Font font;

    Timer spawner;

    Timer scoreTimer;
    int score = 0;
    int hits = 0;
    int kills = 0;
    int dashCount = 0;
    int spinCount = 0;
    int lightningCount = 0;
    int lives = 3;
    int waves = 0;

    JButton back = new JButton("BACK");
    JLabel scorelab = new JLabel("Score: " + score);
    JLabel time = new JLabel(String.format(" Time: %02d:%02d:%02d\n\n",0,0,0));
    JLabel killcount = new JLabel("Killed: " + kills + "/20");
    JLabel liveLabel = new JLabel("Lives: ");
    JLabel lvl = new JLabel("Lvl: 1");

    JTextArea statsWords = new JTextArea("Score:\n\nKills\n\nTime:\n\nRank:\n\nAttacks:");

    JTextArea statsValue = new JTextArea();


    JButton home = new JButton("Home");
    JButton playagain = new JButton("Play Again");
    JButton leaderboard = new JButton("Leaderboard");
    JButton playpause = new JButton();

    boolean ded = false;
    MainPan rev;
    Leaderboard leader;

    Spin spin = new Spin();
    Timer spinTimer = new Timer(1,spin);
    boolean isSpinning = false;

    boolean infinite = false;
    boolean canTele = false;
    boolean canScore = true;
    int level = 1;

    Portal portal = new Portal();
    Timer portalTimer = new Timer(100, portal);

    boolean ended = false;
    boolean playing = true;

    long dashCooldown = System.currentTimeMillis();
    long spinCooldown = System.currentTimeMillis();
    long lightningCooldown = System.currentTimeMillis();

    boolean canDash = true;
    boolean canSpin = true;
    boolean canLightning = true;
    boolean circle = false;

    //Set font, set default variables to false, initialize images, make and start timers
    public PGame(MainPan revenge) {
        spawner = new Timer(spawn_rate,e -> spawnTrainer(spawn_amount));
        try {
            Font tmpfont = Font.createFont(Font.TRUETYPE_FONT, new File("Minecraft.ttf"));
            font = tmpfont.deriveFont(30f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        spawner.addActionListener(e -> waves++);
        rev = revenge;
        leader = revenge.leaderboard;
        uppress = downpress = leftpress = rightpress = false;
        for(int i = 0; i < 8; i++)
            pikapics[i] = new ImageIcon("pikapics/00" + i + ".png").getImage();
        for(int i = 0; i < 2; i++)
            dashpics[i] = new ImageIcon("dash/dash" + i + ".png").getImage();
        for(int i = 0; i < 12; i++)
            tpics[i] = new ImageIcon(String.format("blackpics/tile0%02d.png",i)).getImage();   
        for(int i = 0; i < 16; i++)
            bloodpics[i] = new ImageIcon(String.format("blood/tile0%02d.png",i)).getImage();   
        for(int i = 2; i < 10; i++)
            ballpics[i-2] = new ImageIcon("pokeball/tile00" + i + ".png").getImage();
        for(int i = 0; i < 5; i++)
            portalpics[i] = new ImageIcon("portal/portal" + i + ".png").getImage();
        for(int i = 0; i < 3; i++)
            laserpics[i] = new ImageIcon("laser/laser" + i + ".png").getImage();
        //for(int i = 1; i <= 5; i++)
        //    lightningpics[i-1] = new ImageIcon("lightning/lightning" + i + ".png").getImage();
        for(int i = 0; i < 8; i++)
            light2pics[i] = new ImageIcon("light2/light" + i + ".png").getImage();
        for(int i = 0; i < 12; i++)
            smokepics[i] = new ImageIcon(String.format("smokepics/frame_%02d_delay-0.1s.png",i)).getImage();

        grass = new ImageIcon("pikagrass.png").getImage();
        frontfence = new ImageIcon("fences/front.png").getImage();
        leftfence = new ImageIcon("fences/left.png").getImage();
        rightfence = new ImageIcon("fences/right.png").getImage();
        settingImage = new ImageIcon("settings.png").getImage();
        excla = new ImageIcon("exclamation.png").getImage();
        buttonTemplate = new ImageIcon("buttontemplate.png").getImage();

       

        attack = new Attack();
        attackAni = new Timer(3, attack);
        pika = new Pika();
        pikaTime = new Timer(100,pika);
        move = new Movement();
        moving = new Timer(6, move);

        setBackground(Color.GREEN);

        //setLayout(new FlowLayout(FlowLayout.LEFT));
        back.setFont(font.deriveFont(15f));
        back.setBounds(10,10,100,40);
        back.setVerticalAlignment(JButton.CENTER);
        
        scoreTimer = new Timer(1000, e -> updateLabel());
        scoreTimer.addActionListener(e -> score += 10);


        scorelab.setFont(font.deriveFont(15f));
        scorelab.setBounds(130,12,150,40);

        time.setFont(font.deriveFont(15f));
        time.setBounds(240,12,150,40);

        killcount.setFont(font.deriveFont(15f));
        killcount.setBounds(340,12,100,40);

        liveLabel.setFont(font.deriveFont(15f));
        liveLabel.setBounds(430, 12, 100, 40);

        home.setFont(font.deriveFont(40f));
        home.setBounds(100,450,600,75);
        home.setHorizontalAlignment(JLabel.CENTER);
        home.addActionListener(e -> rev.card.show(rev, "start"));
        home.setOpaque(false);
        home.setContentAreaFilled(false);
        home.setBorderPainted(false);

        playagain.setFont(font.deriveFont(40f));
        playagain.setBounds(100,550,600,75);
        playagain.setHorizontalAlignment(JLabel.CENTER);
        playagain.addActionListener(e -> rev.card.show(rev, "game"));
        playagain.addActionListener(e -> rev.pikagame.startGame());
        playagain.setOpaque(false);
        playagain.setContentAreaFilled(false);
        playagain.setBorderPainted(false);

        leaderboard.setFont(font.deriveFont(40f));
        leaderboard.setBounds(100,650,600,75);
        leaderboard.setHorizontalAlignment(JLabel.CENTER);
        leaderboard.addActionListener(e -> rev.card.show(rev, "leader"));
        leaderboard.setOpaque(false);
        leaderboard.setContentAreaFilled(false);
        leaderboard.setBorderPainted(false);

        statsWords.setFont(font.deriveFont(15f));
        statsWords.setForeground(Color.BLACK);
        statsWords.setBounds(150,260,200,300);
        statsWords.setOpaque(false);
        statsWords.setEditable(false);

        statsValue.setFont(font.deriveFont(15f));
        statsValue.setForeground(Color.BLACK);
        statsValue.setBounds(220,260,200,300);
        statsValue.setOpaque(false);
        statsValue.setEditable(false);

        playpause.setOpaque(false);
        playpause.setContentAreaFilled(false);
        playpause.setBorderPainted(false);
        playpause.setBounds(650,10,40,40);
        playpause.addActionListener(e -> {
            playing = !playing;
            if(playing) resumeGame();
            else pauseGame();
        });

        lvl.setFont(font.deriveFont(15f));
        lvl.setBounds(600, 12, 100, 40);

       
        

        add(back);
        add(scorelab);
        add(time);
        add(killcount);
        add(liveLabel);
        add(lvl);
        add(playpause);
    }

    //Calls the drawing functions and adds back button
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g);
        drawPortal(g);
        if(!attack.running && isSpinning == false && lives > 0)
            drawPika(g);
        drawLaser(g2d);
        drawTrainer(g);
        drawFrontFence(g);
        for(int i = 0; i < balls.size(); i++)
            drawPokeball(g, balls.get(i).curx, balls.get(i).cury, 1, balls.get(i).state, 50);
        drawAttack(g2d, 380, 380, attack.state, 180, 40);
        drawSpin(g2d);
        drawSmoke(g);
        drawLightning(g);
        drawBlood(g);
        
        
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(10, 10, 780, 40, 10, 10);
        
        g.drawImage(grass,20,60,100,100, null);
        if(level == 2) g.drawImage(level2,20,60,100,100,null);
        else if (level == 3) g.drawImage(level3,20,60,100,100,null);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRect(20, 60, 100, 100);
        g.setColor(new Color(165,42,42));
        g.drawRect(23, 63, 94, 94);
        g.setColor(Color.YELLOW);
        g.fillOval(pika.curx/25+25, pika.cury/25+65, 10, 10);

        if(circle)
            g.drawOval(200,200,400,400);

        drawLives(g);
        if(playing) g.drawImage(pauseButton,653, 13, 35, 35,null);
        else g.drawImage(playButton,653, 13, 35, 35,null);
        

        if(ded)
        {
            g2d.setColor(new Color(1.0f,0.0f,0.0f,0.5f));
            g2d.fillRect(0, 0, 800, 800);
            g.drawImage(youdied,100,100,600,120,null);
            g.drawImage(rev.leaderboard.scroll, 85, 220, 250, 230, null);
            g.drawImage(buttonTemplate,100,450,600,75, null);
            g.drawImage(buttonTemplate, 100,550,600,75, null);
            g.drawImage(buttonTemplate, 100,650,600,75, null);
        }
        else
            stop = Instant.now();

        int hello = (int)(Duration.between(start,stop).getSeconds());
        time.setText(String.format("Time: %02d:%02d\n\n",hello/60, hello%60)); 

        g.setColor(Color.black);
        g.fillOval(750, 300, 40, 40);
        g.drawImage(cooldowndash, 757, 305, 30, 30, null);
        if(System.currentTimeMillis() - dashCooldown >= 300)
        {
            canDash = true;
            g.setColor(new Color(1f,1f,0f,1f));
            g.drawOval(750, 300, 40, 40);
        }
        else
        {
            g.setColor(new Color(0f,0f,0f,0.5f));
            g.fillArc(750, 300, 40, 40, 90,(int)(((System.currentTimeMillis() - dashCooldown)/300.0)*360));
        }
        
        g.setColor(Color.black);
        g.fillOval(750, 400, 40, 40);
        g.drawImage(cooldownspin, 755, 405, 30, 30, null);
        if(System.currentTimeMillis() - spinCooldown >= 400 && level >= 2)
        {
            canSpin = true;
            g.setColor(new Color(1f,1f,0f,1f));
            g.drawOval(750, 400, 40, 40);
        }
        else
        {
            g.setColor(new Color(0f,0f,0f,0.5f));
            g.fillArc(750, 400, 40, 40, 90, (int)((System.currentTimeMillis() - spinCooldown)/ 400.0*360));
        }
        g.setColor(Color.black);
        g.fillOval(750, 500, 40, 40);
        g.drawImage(cooldownlightning, 755, 505, 30, 30, null);
        if(System.currentTimeMillis() - lightningCooldown >= 1000 && level == 3)
        {
            canLightning = true;
            g.setColor(new Color(1f,1f,0f,1f));
            g.drawOval(750, 500, 40, 40);
        }
        else
        {
            g.setColor(new Color(0f,0f,0f,0.5f));
            g.fillArc(750, 500, 40, 40, 90, (int)((System.currentTimeMillis() - lightningCooldown)/ 1000.0*360));
        }
       

        if(pika.curx > 950 && pika.curx < 1050 && pika.cury > 950 && pika.cury < 1050 && canTele)
        {
            if(level == 1)
            {
                if(lives < 3) lives++;
                level = 2;
                throw_chance = 45;
                spawn_amount = 4;
                spawn_rate = 6500;
                portalTimer.stop();
                canTele = false;
                spawner.start();
                scoreTimer.start();
            }
            else if(level == 2)
            {
                if(lives < 3) lives++;
                level = 3;
                throw_chance = 35;
                spawn_amount = 7;
                portalTimer.stop();
                canTele = false;
                spawner.start();
                scoreTimer.start();
            }
            lvl.setText("Lvl: " + level);
        }
    }

    //Draw pikachu of user's pikachu at location, direction, and state
    public void drawPika(Graphics g) {
        if(pika.direction == 3)
        {
            g.drawImage(pikapics[pika.state],PIKA_LOC_X,PIKA_LOC_Y,PIKA_SIZE,PIKA_SIZE,null);
        }
        else if(pika.direction == 4)
        {
            g.drawImage(pikapics[pika.state+2],PIKA_LOC_X,PIKA_LOC_Y,PIKA_SIZE,PIKA_SIZE,null);
        }
        else if(pika.direction == 2)
        {
            g.drawImage(pikapics[pika.state+4],PIKA_LOC_X,PIKA_LOC_Y,PIKA_SIZE,PIKA_SIZE,null);
        }
        else
        {
            g.drawImage(pikapics[pika.state+6],PIKA_LOC_X,PIKA_LOC_Y,PIKA_SIZE,PIKA_SIZE,null);
        }
        repaint();
    }

    //Draws trainer given trainer
    public void drawTrainer(Graphics g) {
        for(int i = 0; i < trainers.size(); i++)
        {
            Trainer trainer1 = trainers.get(i);
            if(trainer1.direction == 3)
            {
                g.drawImage(tpics[trainer1.state],trainer1.curx-pika.curx+350,trainer1.cury-pika.cury+350,TRAINER_SIZE_WIDTH,TRAINER_SIZE_HEIGHT,null);
            }
            else if(trainer1.direction == 4)
            {
                g.drawImage(tpics[trainer1.state+3],trainer1.curx-pika.curx+350,trainer1.cury-pika.cury+350,TRAINER_SIZE_WIDTH,TRAINER_SIZE_HEIGHT,null);
            }
            else if(trainer1.direction == 1)
            {
                g.drawImage(tpics[trainer1.state+6],trainer1.curx-pika.curx+350,trainer1.cury-pika.cury+350,TRAINER_SIZE_WIDTH,TRAINER_SIZE_HEIGHT,null);
            }
            else
            {
                g.drawImage(tpics[trainer1.state+9],trainer1.curx-pika.curx+350,trainer1.cury-pika.cury+350,TRAINER_SIZE_WIDTH,TRAINER_SIZE_HEIGHT,null);
            }
            if(trainer1.throwing > 0 || trainer1.shooting > 0)
                g.drawImage(excla, trainer1.curx-pika.curx+350+25,trainer1.cury-pika.cury+350-50,10,40,null);
        }
       
        repaint();
    }
    
    //Draws blood given trainer death position
    public void drawBlood(Graphics g) {
        for(int i = 0; i < bloodMarks.size(); i++)
        {
            Blood blood1 = bloodMarks.get(i);
            g.drawImage(bloodpics[blood1.state],blood1.curx-pika.curx+350-80,blood1.cury-pika.cury+350-100,300,300,null);
            if(blood1.state >= 15)
            {
                int tmpInt = bloodMarks.indexOf(blood1);
                bloodMarks.remove(tmpInt);
                bloodTimers.get(tmpInt).stop();
                bloodTimers.remove(tmpInt);
            }
        }
        
    
        repaint();
    }

    //Draws smoke given trainer death position
    public void drawSmoke(Graphics g) {
        for(int i = 0; i < smokeMarks.size(); i++)
        {
            Smoke smoke1 = smokeMarks.get(i);
            g.drawImage(smokepics[smoke1.state],smoke1.curx-pika.curx+350-80,smoke1.cury-pika.cury+350-100,200,200,null);
            if(smoke1.state >= 11)
            {
                int tmpInt = smokeMarks.indexOf(smoke1);
                smokeMarks.remove(tmpInt);
                smokeTimers.get(tmpInt).stop();
                smokeTimers.remove(tmpInt);
            }
        }
        

        repaint();
    }

    //Draw Trainer given location, direction, and state of animation
    public void drawTrainer(Graphics g, int x, int y, int d, int state, int size1, int size2) {
        if(d == 3)
        {
            if(state == 0) g.drawImage(tpics[state],x,y,size1,size2,null);
            else g.drawImage(tpics[state+1],x,y,size1,size2,null);
        }
        else if(d == 4)
        {
            g.drawImage(tpics[state+3],x,y,size1,size2,null);
        }
        else if(d == 2)
        {
            g.drawImage(tpics[state+6],x,y,size1,size2,null);
        }
        else
        {
            g.drawImage(tpics[state+9],x,y,size1,size2,null);
        }
        repaint();
    }

    //Draw Pikachu given location, direction, and state of animation
    public void drawPika(Graphics g, int x, int y, int d, int state, int size) {
        if(d == 3)
        {
            g.drawImage(pikapics[state],x,y,size,size,null);
        }
        else if(d == 4)
        {
            g.drawImage(pikapics[state+2],x,y,size,size,null);
        }
        else if(d == 2)
        {
            g.drawImage(pikapics[state+4],x,y,size,size,null);
        }
        else
        {
            g.drawImage(pikapics[state+6],x,y,size,size,null);
        }
        repaint();
    }

    //Draw pokeball given location, size, and state of animation
    public void drawPokeball(Graphics g, int x, int y, int d, int state, int size) {
        if(d == 1)
            g.drawImage(ballpics[state],x-pika.curx+350,y-pika.cury+350,size,size,null);
        else
            g.drawImage(ballpics[state],x,y,size,size,null);
        repaint();
    }

    //Draws attack given location, size, and state of animation
    public void drawAttack(Graphics2D g, int x, int y, int state, int size1, int size2) {
        if(attack.running)
        {
            g.rotate(PI+PI-attack.direction,400,400);
            g.drawImage(dashpics[state%2],x,y,size1,size2,null);
            repaint();
            if(state >= 31)
            {
                attackAni.stop();
                attack.running = false;
                pika.update();

            }
            repaint();
            g.rotate(PI-(PI-attack.direction),400,400);
        }
    }

    //Draws grass background
    public void drawBackground(Graphics g) {
        int tmpx = -pika.curx;
        int tmpy = -pika.cury;
        for(int i = 0; i < 5; i++)
            for(int l = 0; l < 5; l++)
                g.drawImage(grass,tmpx+(i*640),tmpy+(l*640),640,640,null);
        if(level == 2)
            g.drawImage(level2,tmpx,tmpy-20,2810,2800,null);
        else if(level == 3)
            g.drawImage(level3,tmpx,tmpy-20,2810,2800,null);
        for(int i = 0; i < 2030; i += 80)
            g.drawImage(frontfence,tmpx+360+i,tmpy+320,80,80,null);
        for(int i = 0; i <= 2000; i += 80)
        {
            g.drawImage(leftfence,tmpx+360,tmpy+i+340,15,80,null);
            g.drawImage(rightfence,tmpx+2430,tmpy+i+340,15,80,null);
        }
        //g.drawImage(rocks, 400+tmpx+350, 400+tmpy+350, 250, 250, null);
        
    }

    //Draws Bottom front fence to prevent pikachu overlap
    public void drawFrontFence(Graphics g)
    {
        for(int i = 0; i < 2030; i += 80)
            g.drawImage(frontfence,(-pika.curx)+360+i,(-pika.cury)+2350,80,80,null);
    }

    //Draw Spinning pikachu
    public void drawSpin(Graphics2D g2d)
    {
        if(isSpinning)
        {
            g2d.rotate(spin.direction%(2*PI),400,400);
            g2d.drawImage(pikapics[5], 360, 360, 100, 100, null);
            g2d.rotate(-(spin.direction%(2*PI)),400,400);
        }
    }

    //Draws the current lives
    public void drawLives(Graphics g)
    {
        for(int i = 0; i < 3; i++)
        {
            if(lives > i) g.drawImage(redLife, 500+(i*30), 20, 20, 20, null);
            else g.drawImage(whiteLife, 500+(i*30), 20, 20, 20, null);
        }
    }

    //Draw Portal
    public void drawPortal(Graphics g)
    {
        g.drawImage(portalpics[4],1000-pika.curx+350, 1000-pika.cury+350, 150, 150, null);
        if(canTele) g.drawImage(portalpics[portal.state],1000-pika.curx+350, 1000-pika.cury+350, 150, 150, null);
    }

    //Draw Laser
    public void drawLaser(Graphics2D g2d)
    {
        for(Laser laser : lasers)
        {
            g2d.rotate(laser.direction,laser.curx-pika.curx+350,laser.cury-pika.cury+350+7);
            g2d.setColor(Color.RED);
            g2d.drawRect(laser.curx-pika.curx+350, laser.cury-pika.cury+350, 6000, 15);
            g2d.drawImage(laserpics[laser.state], laser.curx-pika.curx+350, laser.cury-pika.cury+350, 6000, 15, null);
            g2d.rotate(-laser.direction,laser.curx-pika.curx+350,laser.cury-pika.cury+350+7);
        }
        
    }

    //Draw Lightning
    public void drawLightning(Graphics g)
    {
        for(Lightning light : lightnings)
            g.drawImage(light2pics[light.state],light.tarx-320-pika.curx+350+50+20,light.tary-340-pika.cury+350+20,320,340,null);
            //g.drawImage(lightningpics[light.state],light.tarx-120-pika.curx+350,light.tary-440-pika.cury+350,120,440,null);
    }

    //Pikachu class
    //Keeps track of direction, location, and animation state
    class Pika implements ActionListener {
        int state = 0;
        int direction = 3;
        int curx = 350;
        int cury = 350;
        int atkChangeX = 0;
        int atkChangeY = 0;
        Rectangle pikaRect = new Rectangle(curx+30, cury+30, 40, 40);

        //Changes animation state of pikachu
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state%2;
            repaint();
            grabFocus();
            pikaRect.setBounds(curx+30, cury+30, 40, 40);
        }

        //Updates the x,y when called by adding atkChangeX/Y
        public void update()
        {
            atkChangeX = 0;
            atkChangeY = 0;
            double check = Math.toDegrees(attack.direction);
            if(check > 45 && check < 135) direction = 1;
            else if(check > 45+180 && check < 135+180) direction = 3;
            else if(check > 135 && check < 45+180) direction = 4;
            else direction = 2;
            if(curx < 0) curx = 0;
            else if(curx > 2010) curx = 2010;
            if(cury < 0) cury = 0;
            else if(cury > 2000) cury = 2000;
            pikaRect.setBounds(curx+30, cury+30, 40, 40);
        }
    }

    //Class for the pokemon trainer
    //Roams around randomly and has animation
    //Will be used multiple times to create multiple trainers
    class Trainer implements ActionListener {
        int state = 0;
        int direction = 3;
        int curx = (int)(Math.random()*2000);
        int cury = (int)(Math.random()*2000);
        int var = 0;
        boolean running = false;
        int targetx = 350;
        int targety = 350;
        int speed = 30;
        int throwing = 0;
        int shooting = 0;
        Laser curLaser;
        boolean madeLaser = false;

        //Change Trainer state
        //Track down pikachu and goes in it's direction
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state%3;

            if(throwing > 0)
                throwPoke();
            else if(shooting > 0)
                shootLaser();
            else if((int)(Math.random()*throw_chance+1) == 1)
            {
                if(((int)(Math.random()*3+1) == 2) && level > 1) shootLaser();
                else throwPoke();
            }
            else if(!running)
            {
                if(pika.curx > curx)
                    targetx = curx + ((int)(Math.random()*30+3) * 10);
                else
                    targetx = curx - ((int)(Math.random()*30+3) * 10);
                if(pika.cury > cury)
                    targety = cury + ((int)(Math.random()*30+3) * 10);
                else
                    targety = cury - ((int)(Math.random()*30+3) * 10);
                running = true;
            }
            else
            {
                if(targetx == curx && targety == cury)
                    running = false;
                else if((Math.abs(targetx-curx))>(Math.abs(targety-cury))) 
                {
                    if(targetx < curx)
                    {
                        curx -= speed;
                        direction = 4;
                    }
                    else
                    {
                        curx += speed;
                        direction = 2;
                    }
                }
                else
                {
                    if(targety < cury)
                    {
                        cury -= speed;
                        direction = 1;
                    }
                    else
                    {
                        cury += speed;
                        direction = 3;
                    }
                }
                running = false;
            } 
            if(curx < 0) curx = 0;
            else if(curx > 2000) curx = 2000;
            if(cury < 0) cury = 0;
            else if(cury > 2000) cury = 2000;
            if(Math.abs((curx+30)-(pika.curx+50)) <= 40 && Math.abs((cury+40)-(pika.cury+50)) <= 60)
            {
                int temp = trainers.indexOf(this);
                if(temp != -1)
                {
                    bloodMarks.add(new Blood(curx,cury));
                    bloodTimers.add(new Timer(30,bloodMarks.get(bloodMarks.size()-1)));
                    bloodTimers.get(bloodMarks.size()-1).start();
                    if(lasers.indexOf(trainers.get(temp).curLaser) != -1)
                    {
                        int tmp = lasers.indexOf(trainers.get(temp).curLaser);
                        lasers.remove(tmp);
                        laserTimers.get(tmp).stop();
                        laserTimers.remove(tmp);
                    }
                    trainers.remove(temp);
                    trainTimers.get(temp).stop();
                    trainTimers.remove(temp);
                    kills++;
                    checkKills();
                    score += 100;
                    updateLabel();
                }
            }
            repaint();
        }

        //Randomly throws pokeball at pikachu direction
        public void throwPoke()
        {
            throwing += 1;
            double check = (Math.toDegrees(angle(curx,cury,pika.curx,pika.cury)));
            if(check > 45 && check < 135) direction = 3;
            else if(check > 45+180 && check < 135+180) direction = 1;
            else if(check > 135 && check < 45+180) direction = 4;
            else direction = 2;
            if(throwing >= 5)
                throwing = 0;
            else if(throwing == 4)
            {
                balls.add(new Pokeball((Math.random()*0.3-0.15)+angle(curx,cury,pika.curx,pika.cury), 5, curx, cury));
                ballTimers.add(new Timer(50,balls.get(balls.size()-1)));
                ballTimers.get(ballTimers.size()-1).start();
                pokeballs += 1;
            }

        }

        //Shoots laser at pikachu
        public void shootLaser()
        {
            shooting += 1;
            double check = (Math.toDegrees(angle(curx,cury,pika.curx,pika.cury)));
            if(check > 45 && check < 135) direction = 3;
            else if(check > 45+180 && check < 135+180) direction = 1;
            else if(check > 135 && check < 45+180) direction = 4;
            else direction = 2;
            if(shooting >= 15)
            {
                shooting = 0;
                madeLaser = false;
            }
            else if(shooting == 10)
            {
                laserTimers.get(lasers.indexOf(curLaser)).start();
                laserCount += 1;
            }
            else if(shooting < 10 && !madeLaser)
            {
                lasers.add(new Laser((Math.random()*0.3-0.15)+angle(curx,cury,pika.curx,pika.cury), curx+40, cury+40));
                laserTimers.add(new Timer(50,lasers.get(lasers.size()-1)));
                curLaser = lasers.get(lasers.size()-1);
                madeLaser = true;
            }
        }
    }

    //Attack animation 
    //Draws attack according to where user clicks
    class Attack implements ActionListener {
        int state = 33;
        boolean running = true;
        double direction = 0.0;

        //Change attack animation state and change pikachu x and y
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state%32;
            pika.curx += pika.atkChangeX;
            pika.cury += pika.atkChangeY;
            for(int i = 0; i < trainers.size(); i++)
            {
                Trainer tTrain = trainers.get(i);
                if(Math.abs((tTrain.curx+30)-(pika.curx+50)) <= 40 && Math.abs((tTrain.cury+40)-(pika.cury+50)) <= 60)
                {
                    int temp = trainers.indexOf(tTrain);
                    if(temp != -1)
                    {
                        if(lasers.indexOf(trainers.get(temp).curLaser) != -1)
                        {
                            int tmp = lasers.indexOf(trainers.get(temp).curLaser);
                            lasers.remove(tmp);
                            laserTimers.get(tmp).stop();
                            laserTimers.remove(tmp);
                        }
                        bloodMarks.add(new Blood(trainers.get(temp).curx,trainers.get(temp).cury));
                        bloodTimers.add(new Timer(30,bloodMarks.get(bloodMarks.size()-1)));
                        bloodTimers.get(bloodMarks.size()-1).start();
                        trainers.remove(temp);
                        trainTimers.get(temp).stop();
                        trainTimers.remove(temp);
                        kills++;
                        checkKills();
                        score += 100;
                        updateLabel();
                    }
                }
            }
            
            repaint();
            grabFocus();
        }
    }

    //Blood animation 
    //Draws blood according to trainer/pikachu death
    class Blood implements ActionListener {
        int state = 0;
        int curx;
        int cury;

        //Set cordinates of blood animation
        public Blood(int x, int y)
        {
            curx = x;
            cury = y;
        }

        //Change attack animation state
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state%16;
            
            repaint();
            grabFocus();
        }
    }

    //Smoke animation 
    //Draws smoke according to lightning
    class Smoke implements ActionListener {
        int state = 0;
        int curx;
        int cury;

        //Set cordinates of smoke animation
        public Smoke(int x, int y)
        {
            curx = x;
            cury = y;
        }

        //Change attack animation state
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state%12;
            
            repaint();
            grabFocus();
        }
    }

    //Checks movement booleans if true to move to make moving smoother
    class Movement implements ActionListener {
        //Changes direction of pikachu and changes x,y when keys are pressed
        public void actionPerformed(ActionEvent e) {
            if(uppress){
                pika.direction = 1;
                pika.cury -= PIKA_SPEED;
            }
            if(downpress){
                pika.direction = 3;
                pika.cury += PIKA_SPEED;
            }
            if(leftpress){
                pika.direction = 4;
                pika.curx -= PIKA_SPEED;
            }
            if(rightpress){
                pika.direction = 2;
                pika.curx += PIKA_SPEED;
            }
            if(pika.curx < 0) pika.curx = 0;
            else if(pika.curx > 2010) pika.curx = 2010;
            if(pika.cury < 0) pika.cury = 0;
            else if(pika.cury > 2000) pika.cury = 2000;
            repaint();
            grabFocus();
            pika.pikaRect.setBounds(pika.curx+30, pika.cury+30, 40, 40);
        }
    }

    //Class for the pokeball
    //Rolls in one direction until out of bounds or hit pikachu
    class Pokeball implements ActionListener {
        int state = 0;
        double direction = 0;
        int curx = 0;
        int cury = 0;
        int var = 0;
        int speed = 30;
        boolean hitBefore = false;
        boolean fromTrainer = true;


        //Constructor that initializes direction of ball
        public Pokeball(double dir, int sped, int x, int y)
        {
            direction = dir;
            sped = speed;
            curx = x;
            cury = y;
        }
        
        //Change Ball state
        //Rolls in one directions continuously
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state % 8;
            cury += (int)(speed * Math.sin(direction));
            curx += (int)(speed * Math.cos(direction));
            if(curx < -500 || curx > 2800 || cury < -500 || cury > 2800)
            {
                int help = balls.indexOf(this);
                if(help != -1)
                {
                    balls.remove(help);
                    ballTimers.get(help).stop();
                    ballTimers.remove(help);
                    score += 10;
                }
            }
            else if (Math.abs((curx+25)-(pika.curx+50)) <= 40 && Math.abs((cury+25)-(pika.cury+50)) <= 40 && fromTrainer)
            {
                int help = balls.indexOf(this);
                if(help != -1)
                {
                    if(isSpinning == true)
                    {
                        direction = (direction + PI)%(2*PI);
                        fromTrainer = false;
                    }
                    else
                    {
                        lives--;
                        balls.remove(help);
                        ballTimers.get(help).stop();
                        ballTimers.remove(help);
                        bloodMarks.add(new Blood(pika.curx,pika.cury));
                        bloodTimers.add(new Timer(30,bloodMarks.get(bloodMarks.size()-1)));
                        bloodTimers.get(bloodMarks.size()-1).start();
                        if(lives <= 0) endGame();
                    }
                }
                //hitBefore = true;
                
            }
            else
            {
                for(int i = 0; i < trainers.size(); i++)
                {
                    Trainer temp = trainers.get(i);
                    if(!fromTrainer && Math.abs((curx+25)-(temp.curx+50)) <= 40 && Math.abs((cury+25)-(temp.cury+50)) <= 40)
                    {
                        int help = balls.indexOf(this);
                        if(help != -1)
                        {
                            balls.remove(help);
                            ballTimers.get(help).stop();
                            ballTimers.remove(help);
                            bloodMarks.add(new Blood(trainers.get(i).curx,trainers.get(i).cury));
                            bloodTimers.add(new Timer(30,bloodMarks.get(bloodMarks.size()-1)));
                            bloodTimers.get(bloodMarks.size()-1).start();
                            trainers.remove(i);
                            trainTimers.get(i).stop();
                            trainTimers.remove(i);
                            kills++;
                            checkKills();
                            score += 100;
                            updateLabel();
                            i = trainers.size() + 10;
                            
                        }
                    }
                }
            }
            

            repaint();
        }

    }

    //Reflect attack animation
    //Deflect pokeballs
    class Spin implements ActionListener
    {
        double direction = 0;

        //Increase states of spin
        public void actionPerformed(ActionEvent e)
        {
            direction += 0.1;
            if(direction >= (4*PI))
            {
                direction = 0.0;
                isSpinning = false;
                spinTimer.stop();
            }
        }
    }

    //Portal to go to next level
    class Portal implements ActionListener
    {
        int state = 0;
        //Make animation
        public void actionPerformed(ActionEvent e)
        {
            state++;
            state = state%4;
        }
    }

    //Laser attack for trainers
    class Laser implements ActionListener
    {
        int state = 0;
        double direction = 0;
        int curx = 0;
        int cury = 0;
        int total = 0;
        boolean killed = false;
        //Set direction and coordinates of attack
        public Laser(double dir, int x, int y)
        {
            curx = x;
            cury = y;
            direction = dir;
        }

        //Increment state of animation
        public void actionPerformed(ActionEvent e)
        {
            state++;
            state %= 3;
            total++;
            if(total >= 15)
            {
                int help = lasers.indexOf(this);
                if(help != -1)
                {
                    lasers.remove(help);
                    laserTimers.get(help).stop();
                    laserTimers.remove(help);
                    score += 20;
                }
            }
            else 
            {
                double slope = Math.tan(direction);
                int help = lasers.indexOf(this);
                if(help != -1 && !killed)
                {
                    if(Math.toDegrees(direction) <= 90 || Math.toDegrees(direction) >= 270)
                    {
                        for(int i = curx; i < 2000; i += 20)
                        {
                            if(pika.pikaRect.contains(i,slope * (i-curx) + cury) && !killed)
                            {
                                lives--;
                                bloodMarks.add(new Blood(pika.curx,pika.cury));
                                bloodTimers.add(new Timer(30,bloodMarks.get(bloodMarks.size()-1)));
                                bloodTimers.get(bloodMarks.size()-1).start();
                                if(lives <= 0) endGame();
                                killed = true;
                            }
                        }
                    }
                    else
                    {
                        for(int i = curx; i > 0; i -= 10)
                        {
                            if(pika.pikaRect.contains(i,slope * (i-curx) + cury) && !killed)
                            {
                                lives--;
                                bloodMarks.add(new Blood(pika.curx,pika.cury));
                                bloodTimers.add(new Timer(30,bloodMarks.get(bloodMarks.size()-1)));
                                bloodTimers.get(bloodMarks.size()-1).start();
                                if(lives <= 0) endGame();
                                killed = true;
                            }
                        }
                    }
                }

            }
        }
    }
    
    //Lightning Strike attack
    class Lightning implements ActionListener
    {
        int state = 0;
        int tarx = 0;
        int tary = 0;
        int tmp2 = 0;
        Trainer track;
        //Set coordinates of strike
        public Lightning(int x, int y, Trainer target)
        {
            tarx = x;
            tary = y;
            track = target;
            int temp = trainers.indexOf(track);
            if(temp != -1)
            {
                bloodMarks.add(new Blood(track.curx,track.cury));
                bloodTimers.add(new Timer(30,bloodMarks.get(bloodMarks.size()-1)));
                bloodTimers.get(bloodMarks.size()-1).start();
                if(lasers.indexOf(trainers.get(temp).curLaser) != -1)
                {
                    int tmp = lasers.indexOf(trainers.get(temp).curLaser);
                    lasers.remove(tmp);
                    laserTimers.get(tmp).stop();
                    laserTimers.remove(tmp);
                }
                trainers.remove(temp);
                trainTimers.get(temp).stop();
                trainTimers.remove(temp);
                kills++;
                checkKills();
                score += 100;
                updateLabel();
            }
        }

        //Increment attack animation state
        public void actionPerformed(ActionEvent e)
        {
            state++;
            tmp2++;
            state %= 8;

            if(tmp2 == 4)
            {
                Smoke smokey = new Smoke(tarx-120,tary-120);
                Timer timey = new Timer(50,smokey);
                smokeMarks.add(smokey);
                smokeTimers.add(timey);
                timey.start();
            }
            else if(tmp2 == 9)
            {
                int tmp3 = lightnings.indexOf(this);
                lightningTimers.get(tmp3).stop();
                lightningTimers.remove(tmp3);
                lightnings.remove(this);
            }
        }
    }

    //Changes level if certain amount of kills hit
    public void checkKills()
    {
        if(level == 1 && kills >= 20)
        {
            spawner.stop();
            canTele = true;
            portalTimer.start();
            for(Timer time : trainTimers) time.stop();
            trainTimers.clear();
            trainers.clear();
            balls.clear();
            for(Timer time : ballTimers) time.stop();
            ballTimers.clear();
            canScore = false;
            scoreTimer.stop();

        }
        if(level == 2 && kills >= 40)
        {
            spawner.stop();
            canTele = true;
            portalTimer.start();
            for(Timer time : trainTimers) time.stop();
            trainTimers.clear();
            trainers.clear();
            balls.clear();
            for(Timer time : ballTimers) time.stop();
            ballTimers.clear();
            canScore = false;
            scoreTimer.stop();
        }
    }

    //Start lightning attack
    public void lightningAttack()
    {
        int count = trainers.size();
        for(int i = 0; i < trainers.size(); i++)
        {
            Trainer train = trainers.get(i);
            if(Math.abs((pika.curx+50)-(train.curx+40)) <= 200 && Math.abs((pika.cury+50)-(train.cury+40)) <= 200)
            {
                Lightning tmp = new Lightning(train.curx+40+40,train.cury+40+30,train);
                Timer tmp2 = new Timer(20,tmp);
                tmp2.start();
                lightnings.add(tmp);
                lightningTimers.add(tmp2);
                count--;
            }
            if(i >= count) break;
            
        }
        
    }

    //Spawns a trainer
    public void spawnTrainer(int x)
    {
        for(int i = 0; i < x; i++)
        {
            trainers.add(new Trainer());
            trainTimers.add(new Timer(100, trainers.get(trainers.size()-1)));
            trainTimers.get(trainTimers.size()-1).start();
        }
    }

    //Does Nothing
    public void mouseClicked(MouseEvent e) {}
    
    //Starts dash animation and dashes in direction of click
    public void mousePressed(MouseEvent e) {
        if(e.getX() <= 800 && e.getX() >= 0 && e.getY() <= 800 && e.getY() >= 0)
        {
            if(canDash)
            {
                attack.running = true;
                attack.state = 0;
                attackAni.start();
                attack.direction = angle(400,400,e.getX(),800-e.getY());
                pika.atkChangeY = (int)(180 * Math.sin(attack.direction))/32;
                pika.atkChangeX = (int)(180 * Math.cos(attack.direction))/32;
                pika.atkChangeY *= -1;
                dashCooldown = System.currentTimeMillis();
                canDash = false;
                if(canScore) score++;
                dashCount++;
            }
           
        }

        
    }
    //Nothing
    public void mouseReleased(MouseEvent e) {}

    //Nothing
    public void mouseEntered(MouseEvent e) {}

    //Nothing
    public void mouseExited(MouseEvent e) {}

    //Does nothing
    public void keyTyped(KeyEvent e) {}

    //Will update boolean of moving
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) uppress = true;
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() ==  KeyEvent.VK_S) downpress = true;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() ==  KeyEvent.VK_D) rightpress = true;
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() ==  KeyEvent.VK_A) leftpress = true;
        if(e.getKeyCode() == KeyEvent.VK_SLASH) circle = !circle;
        if(e.getKeyCode() == KeyEvent.VK_SPACE && level >= 2)
        {
            if(canSpin)
            {
                spinCount++;
                isSpinning = true;
                spinTimer.start();
                spinCooldown = System.currentTimeMillis();
                canSpin = false;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT && level >= 3)
        {
            if(canLightning)
            {
                lightningCount++;
                lightningAttack();
                lightningCooldown = System.currentTimeMillis();
                canLightning = false; 
            }
        }
        pikaTime.start();   
        repaint();
        
    }

    //Reset boolean when moving
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w') uppress = false;
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's') downpress = false;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd') rightpress = false;
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a') leftpress = false;
        if(canScore) score++; 
        pikaTime.stop();
        repaint();
    }

    //Return angle given two points
    public double angle(int p1x, int p1y, int p2x, int p2y) {

        double tmp = -90.0;
        if((p2x > p1x))
            tmp +=  (Math.atan2((p2x - p1x), (p1y - p2y)) * 180 / PI);
        else if((p2x < p1x)) 
            tmp += 360 - (Math.atan2((p1x - p2x), (p1y - p2y)) * 180 / PI);
        else
            tmp = Math.atan2(0 ,0);
        if(tmp < 0) tmp = 360 + tmp;

        return Math.toRadians(tmp);

    }

    //Resets/Updates text of jlabels
    public void updateLabel()
    {
        //time.setText("Time: " + hits);
        scorelab.setText("Score: " + score);
        if(level == 3)
        {
            killcount.setBounds(350,12,100,40);
            killcount.setText("Kills: " + kills);
        }
        else
            killcount.setText("Kills: " + kills + "/" + level*20);
    }

    //Starts timers when the game starts
    public void startGame()
    {
        for(Timer time : trainTimers) time.stop();
        trainTimers.clear();
        trainers.clear();
        for(Timer time : laserTimers) time.stop();
        laserTimers.clear();
        lasers.clear();
        balls.clear();
        for(Timer time : ballTimers) time.stop();
        ballTimers.clear();
        for(Timer time : bloodTimers) time.stop();
        for(Timer time : smokeTimers) time.stop();
        for(Timer time : lightningTimers) time.stop();
        bloodTimers.clear();
        smokeTimers.clear();
        lightningTimers.clear();
        bloodMarks.clear();
        smokeMarks.clear();
        lightnings.clear();
        pikaTime.start();
        attackAni.start();
        
        hits = 0;
        score = 0;
        kills = 0;
        dashCount = 0;
        spinCount = 0;
        lightningCount = 0;
        laserCount = 0;
        waves = 0;
        lives = 3;
        start_trainers = 5;
        throw_chance = 80;
        spawn_amount = 3;
        for(int i = 0; i < start_trainers; i++)
        {
            trainers.add(new Trainer());
            trainTimers.add(new Timer(100, trainers.get(i)));
        }
        for(int i = 0; i < trainTimers.size(); i++)
            trainTimers.get(i).start();
        ded = false;
        level = 1;
        lvl.setText("Lvl: 1");
        downpress = uppress = leftpress = rightpress = false;
        moving.start();
        scoreTimer.start();
        spawner.setDelay(spawn_rate);
        spawner.start();
        ended = false;
        updateLabel();
        addKeyListener(this);
        addMouseListener(this);
        remove(home);
        remove(playagain);
        remove(leaderboard);
        remove(statsValue);
        remove(statsWords);
        start = Instant.now();
    }

    //Will pause timers when a pause button is pressed
    public void pauseGame()
    {
        pikaTime.stop();
        attackAni.stop();
        for(int i = 0; i < trainTimers.size(); i++)
            trainTimers.get(i).stop();
        for(int i = 0; i < ballTimers.size(); i++)
            ballTimers.get(i).stop();
        for(Timer time : bloodTimers) time.stop();
        for(Timer time : smokeTimers) time.stop();
        for(Timer time : lightningTimers) time.stop();
        for(Timer time : laserTimers) time.stop();
        moving.stop();
        scoreTimer.stop();
        spawner.stop();
        attackAni.stop();
        removeMouseListener(this);
        removeKeyListener(this);

    }

    //Will resume timers when a pause button is pressed
    public void resumeGame()
    {
        pikaTime.start();
        attackAni.start();
        for(int i = 0; i < trainTimers.size(); i++)
            trainTimers.get(i).start();
        for(int i = 0; i < ballTimers.size(); i++)
            ballTimers.get(i).start();
        for(Timer time : bloodTimers) time.start();
        for(Timer time : smokeTimers) time.start();
        for(Timer time : lightningTimers) time.start();
        for(Timer time : laserTimers) time.start();
        moving.start();
        scoreTimer.start();
        spawner.start();
        attackAni.start();
        addMouseListener(this);
        addKeyListener(this);

    }

    //Will be called when player dies
    //Score will be saved with username
    //Sent back to home page
    //Not used yet
    public void endGame()
    {
        if(!ended)
        {
            for(Timer time : trainTimers) time.stop();
            trainTimers.clear();
            trainers.clear();
            for(Timer time : laserTimers) time.stop();
            laserTimers.clear();
            lasers.clear();
            balls.clear();
            for(Timer time : ballTimers) time.stop();
            ballTimers.clear();
            for(Timer time : bloodTimers) time.stop();
            for(Timer time : smokeTimers) time.stop();
            for(Timer time : lightningTimers) time.stop();
            bloodTimers.clear();
            smokeTimers.clear();
            lightningTimers.clear();
            bloodMarks.clear();
            smokeMarks.clear();
            lightnings.clear();
            pikaTime.stop();
            attackAni.stop();
            spawner.stop();

            stop = Instant.now();
            leader.newGame(rev.currentPlayer, score, kills, (int)(Duration.between(start, stop).getSeconds()),pokeballs, waves,laserCount,dashCount,spinCount,lightningCount);
            leader.update();
            pauseGame();
            setOpaque(false);
            JPanel redpan = new JPanel();
            redpan.setBackground(new Color(1.0f,0.0f,0.0f,0.5f));
            redpan.setBounds(0,0,800,800);
            grabFocus();
            
            ded = true;
            //"Score:\nKills\nTime:\nRank:\nAttacks:"
            int hello = (int)(Duration.between(start,stop).getSeconds());
            statsValue.setText(String.format("%d\n\n%d\n\n%02d:%02d\n\n%d\n\n%d",score,kills,hello/60, (hello%60),leader.rank.indexOf(score)+1,dashCount));
            hits = 0;
            score = 0;
            kills = 0;
            dashCount = 0;
            spinCount = 0;
            lightningCount = 0;
            laserCount = 0;
            waves = 0;
            lives = 0;
            start_trainers = 5;
            throw_chance = 80;
            spawn_amount = 3;
            add(home);
            add(playagain);
            add(leaderboard);
            add(statsWords);
            add(statsValue);
            ended = true;
        }
    
    }
    
}
