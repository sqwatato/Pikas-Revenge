//Jayden Lim
//5-15-22
//PGame2.java
//Creates dodging game to be played

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
public class PGame2 extends JLayeredPane implements KeyListener, MouseListener {
    //Pikachu
    int PIKA_SIZE = 100;
    int PIKA_LOC_X = 350;
    int PIKA_LOC_Y = 350;
    int PIKA_SPEED = 3;
    int throw_chance = 40;
    int start_trainers = 5;
    int spawn_amount = 3;
    int spawn_rate = 5000;

    double PI = Math.PI;

    int pokeballs = 0;
    int laserCount = 0;
    int waves;
    Instant start, stop;
    Instant start2;
    long pause =0;
    boolean paused;
    int tmpPause = 0;

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
    Image[] laserpics = new Image[3];

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


    ArrayList<Timer> ballTimers = new ArrayList<>();
    ArrayList<Pokeball> balls = new ArrayList<>();

    ArrayList<Timer> laserTimers = new ArrayList<>();
    ArrayList<Laser> lasers = new ArrayList<>();


    Font font;

    Timer spawner;

    int hits = 0;
    int dashCount = 0;
    int spinCount = 0;
    int lives = 3;

    JButton back = new JButton("BACK");
    JLabel time = new JLabel(String.format(" Time: %02d:%02d:%02d\n\n",0,0,0));
    JLabel liveLabel = new JLabel("Lives: ");
    JLabel lvl = new JLabel("Lvl: 1");
    JLabel laserLabel = new JLabel("Lasers: " + laserCount);
    JLabel ballLabel = new JLabel("Pokeballs: " + pokeballs);

    JTextArea statsWords = new JTextArea("Time:\n\nRank:\n\nAttacks:");

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


    boolean ended = false;
    boolean playing = true;

    long dashCooldown = System.currentTimeMillis();
    long spinCooldown = System.currentTimeMillis();

    boolean canDash = true;
    boolean canSpin = true;
    boolean canLightning = true;
    boolean circle = false;

    //Set font, set default variables to false, initialize images, make and start timers
    public PGame2(MainPan revenge) {
        spawner = new Timer(spawn_rate,e -> spawnTrainer(spawn_amount));
        try {
            Font tmpfont = Font.createFont(Font.TRUETYPE_FONT, new File("Minecraft.ttf"));
            font = tmpfont.deriveFont(30f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
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
        for(int i = 0; i < 3; i++)
            laserpics[i] = new ImageIcon("laser/laser" + i + ".png").getImage();

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
        

        time.setFont(font.deriveFont(15f));
        time.setBounds(120,12,150,40);

        liveLabel.setFont(font.deriveFont(15f));
        liveLabel.setBounds(430, 12, 100, 40);

        ballLabel.setFont(font.deriveFont(15f));
        ballLabel.setBounds(220,12,150,40);

        laserLabel.setFont(font.deriveFont(15f));
        laserLabel.setBounds(345,12,150,40);

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
        playagain.addActionListener(e -> rev.card.show(rev, "game2"));
        playagain.addActionListener(e -> rev.pikagame2.startGame());
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
        add(time);
        add(liveLabel);
        add(lvl);
        add(playpause);
        add(ballLabel);
        add(laserLabel);
    }

    //Calls the drawing functions and adds back button
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g);
        if(!attack.running && isSpinning == false && lives > 0)
            drawPika(g);
        drawLaser(g2d);
        drawTrainer(g);
        drawFrontFence(g);
        for(int i = 0; i < balls.size(); i++)
            drawPokeball(g, balls.get(i).curx, balls.get(i).cury, 1, balls.get(i).state, 50);
        drawAttack(g2d, 380, 380, attack.state, 180, 40);
        drawSpin(g2d);
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
            //g.drawImage(rev.leaderboard.scroll, 85, 220, 250, 230, null);
            g.drawImage(buttonTemplate,100,450,600,75, null);
            g.drawImage(buttonTemplate, 100,550,600,75, null);
            g.drawImage(buttonTemplate, 100,650,600,75, null);
        }
        else
            stop = Instant.now();

        if(paused)
        {
                //pause = Duration.between(start2, Instant.now()).getSeconds();
            int hello = tmpPause;
            time.setText(String.format("Time: %02d:%02d\n\n",hello/60, hello%60)); 
        }
        else 
        {
            int hello = (int)(Duration.between(start,stop).getSeconds()-pause);
            time.setText(String.format("Time: %02d:%02d\n\n",hello/60, hello%60)); 
        }

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
        if(System.currentTimeMillis() - spinCooldown >= 400)
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
       

        updateLabel();
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
        int curx = 0;
        int cury = 0;
        int var = 0;
        boolean running = false;
        int targetx = 350;
        int targety = 350;
        int speed = 30;
        int throwing = 0;
        int shooting = 0;
        Laser curLaser;
        boolean madeLaser = false;

        //Make trainer spawn outside set x,y
        public Trainer()
        {
            if((int)(Math.random()*4+1) == 1)
            {
                curx = 2800;
                cury = (int)(Math.random()*2800);
            }
            else if((int)(Math.random()*4+1) == 1)
            {
                curx = -800;
                cury = (int)(Math.random()*2800);
            }
            else if((int)(Math.random()*4+1) == 1)
            {
                curx = (int)(Math.random()*2800);
                cury = 2800;
            }
            else
            {
                curx = (int)(Math.random()*2800);
                cury = -800;
            }
            
        }

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
                if(((int)(Math.random()*3+1) == 2)) shootLaser();
                else throwPoke();
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
                try
                {
                    laserTimers.get(lasers.indexOf(curLaser)).start();
                    laserCount += 1;
                }
                catch(IndexOutOfBoundsException e)
                {
                    
                }
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
                try
                {
                    int help = lasers.indexOf(this);
                    if(help != -1)
                    {
                        lasers.remove(help);
                        laserTimers.get(help).stop();
                        laserTimers.remove(help);
                    }
                }
                catch(IndexOutOfBoundsException e2)
                {

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
    


    //Spawns a trainer
    public void spawnTrainer(int x)
    {
        updateLabel();
        waves++;
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
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w') uppress = true;
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's') downpress = true;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd') rightpress = true;
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a') leftpress = true;
        if(e.getKeyCode() == KeyEvent.VK_SLASH) circle = !circle;
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
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
        
        pikaTime.start();   
        repaint();
        
    }

    //Reset boolean when moving
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w') uppress = false;
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's') downpress = false;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd') rightpress = false;
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a') leftpress = false;
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
        ballLabel.setText("Pokeballs: " + pokeballs);
        laserLabel.setText("Lasers: " + laserCount);
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
        bloodTimers.clear();
        bloodMarks.clear();
        pikaTime.start();
        attackAni.start();
        
        hits = 0;
        dashCount = 0;
        spinCount = 0;
        pokeballs = 0;
        laserCount = 0;
        lives = 3;
        start_trainers = 5;
        throw_chance = 40;
        spawn_amount = 3;
        waves = 0;
        paused = false;
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
        start2 = Instant.now();
        paused = true;
        tmpPause = (int)(Duration.between(start,stop).getSeconds()-pause);
        pikaTime.stop();
        attackAni.stop();
        for(int i = 0; i < trainTimers.size(); i++)
            trainTimers.get(i).stop();
        for(int i = 0; i < ballTimers.size(); i++)
            ballTimers.get(i).stop();
        for(Timer time : bloodTimers) time.stop();
        for(Timer time : laserTimers) time.stop();
        moving.stop();
        spawner.stop();
        attackAni.stop();
        removeMouseListener(this);
        removeKeyListener(this);

    }

    //Will resume timers when a pause button is pressed
    public void resumeGame()
    {
        paused = false;
        pause = Duration.between(start2, Instant.now()).getSeconds();
        pikaTime.start();
        attackAni.start();
        for(int i = 0; i < trainTimers.size(); i++)
            trainTimers.get(i).start();
        for(int i = 0; i < ballTimers.size(); i++)
            ballTimers.get(i).start();
        for(Timer time : bloodTimers) time.start();
        for(Timer time : laserTimers) time.start();
        moving.start();
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
            bloodTimers.clear();
            bloodMarks.clear();
            pikaTime.stop();
            attackAni.stop();
            spawner.stop();

            stop = Instant.now();
            leader.dodgeGame(rev.currentPlayer, (int)(Duration.between(start, stop).getSeconds())-((int)(pause)),pokeballs, waves,laserCount,dashCount,spinCount);
            leader.update();
            pauseGame();
            setOpaque(false);
            JPanel redpan = new JPanel();
            redpan.setBackground(new Color(1.0f,0.0f,0.0f,0.5f));
            redpan.setBounds(0,0,800,800);
            grabFocus();
            
            ded = true;
            //"Score:\nTime:\nRank:\nAttacks:"
            int hello = (int)(Duration.between(start,stop).getSeconds());
            statsValue.setText(String.format("%02d:%02d\n\n%d\n\n%d",hello/60, (hello%60),leader.rank.indexOf(hello)+1,dashCount));
            hits = 0;
            dashCount = 0;
            spinCount = 0;
            pokeballs = 0;
            laserCount = 0;
            lives = 0;
            start_trainers = 5;
            throw_chance = 80;
            spawn_amount = 3;
            waves = 0;
            pause = 0;
            add(home);
            add(playagain);
            add(leaderboard);
            //add(statsWords);
            //add(statsValue);
            ended = true;
        }
    
    }
    
}
