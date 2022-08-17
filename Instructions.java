//Jayden Lim
//5-15-22
//Instructions.java
//Creates instruction panel which player can test out game mechanics

import java.awt.*;  
import java.awt.event.*;
import javax.swing.*; 
import java.io.*;
import java.util.ArrayList;


//JPanel that shows instructions and allows player to test out game mechanics
public class Instructions extends JLayeredPane implements KeyListener, MouseListener {
    //Pikachu
    int PIKA_SIZE = 100;
    int PIKA_LOC_X = 350;
    int PIKA_LOC_Y = 350;
    int PIKA_SPEED = 2;
    int THROW_CHANCE = 0;
    int START_TRAINERS = 1;
    int SPAWN_AMOUNT = 3;
    int SPAWN_RATE = 5000;

    long pokeballs = 0;

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

    Image settingImage;
    Image frontfence, leftfence, rightfence;
    Image excla;
    Image youdied = new ImageIcon("YOU-DIED.png").getImage();
    //Image rocks = new ImageIcon("rocks.png").getImage();
    Image textBubble = new ImageIcon("textbubble.png").getImage();
    Image pikaIcon = new ImageIcon("pikaIcon.png").getImage();

    int TRAINER_SIZE_WIDTH = 60;
    int TRAINER_SIZE_HEIGHT = 80;
    Timer trainTime; 
    Trainer trainer;
    
    Timer typing = new Timer(30,e -> nextLetter());

    Timer attackAni;
    Attack attack;
    

    ArrayList<Timer> trainTimers = new ArrayList<>();
    ArrayList<Trainer> trainers = new ArrayList<>();

    ArrayList<Timer> bloodTimers = new ArrayList<>();
    ArrayList<Blood> bloodMarks = new ArrayList<>();

    ArrayList<Timer> ballTimers = new ArrayList<>();
    ArrayList<Pokeball> balls = new ArrayList<>();

    Font font;

    Timer spawner = new Timer(SPAWN_RATE,e -> spawnTrainer(SPAWN_AMOUNT));

    JButton back = new JButton("BACK");

    int currentInstruction = 0;
    int currentLetter = 0;

    boolean ded = false;
    boolean canSkipText = true;
    boolean isTyping = true;
    MainPan rev;

    JTextArea pikaSpeech = new JTextArea("");
    JLabel enter = new JLabel("Enter >>>");

    ArrayList<Integer> textCheckpoints = new ArrayList<>();
    
    ArrayList<String> text1 = new ArrayList<>() {
        {
            add("Hey! Over here!");
            add("My gf has been taken by the Trainers :(");
            add("I need your help to get her back.");
            add("But first you need to learn the controls!");
            add("To move around, use the arrows keys or WASD.\nTry it yourself!");
            add("Nice!");
            add("You can also click anywhere on the screen to dash!\nTry it yourself!");
            add("Great job!");
            add("Trainers will appear everywhere!\nThere try to catch you or even kill you!");
            add("Touch them or dash into them to kill them!\nTry it yourself!");
            add("");
            add("Amazing!");
            add("Trainers can also throw pokeballs!\nThis will hurt you!");
            add("Make sure to dodge the pokeballs!\nTry it your self!");
            add("Wow!");
            add("That's all you need to know for now!");
            add("I trust you will help me get my revenge!");
            add("And off you go...");
            add("...");
            add(".....");
            add("bro go already...");
        }

        
    };

    ArrayList<String> text2 = new ArrayList<>() {
        {
            add("Hey dipsh*t!");
            add("Those mf trainers took my gf.");
            add("And now I need your dumbass to get my revenge.");
            add("Hope your one incompetent braincell can learn these controls.");
            add("To move around, use the arrows keys or WASD.\nShow me your tiny ass legs can move.");
            add("Wow, didn't think you could use your f*king brain!");
            add("Click on the screen to move your f*cking slow ass!\nShow me u can do something!");
            add("Wow! Didn't know your b*tch ass could move!");
            add("Those humans will come out everywhere, kill'em.");
            add("Touch them or dash into them with fatass to kill them!");
            add("");
            add("bruh.");
            add("They try to kill u with balls");
            add("Don't die!");
            add("bruh");
            add("ok ur gud");
            add("now go kill them");
            add("go...");
            add("...");
            add(".....");
            add("bro go already...");
        }

        
    };
    ArrayList<String> text = new ArrayList<>(text1);

    //Set font, set default variables to false, initialize images, make and start timers
    public Instructions(MainPan revenge) {
        try {
            Font tmpfont = Font.createFont(Font.TRUETYPE_FONT, new File("Minecraft.ttf"));
            font = tmpfont.deriveFont(30f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        rev = revenge;
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
        moving = new Timer(4, move);

        setBackground(Color.GREEN);

        back.setFont(font.deriveFont(15f));
        back.setBounds(695,730,100,40);
        back.setVerticalAlignment(JButton.CENTER);

        pikaSpeech.setFont(font.deriveFont(20f));
        pikaSpeech.setOpaque(false);
        pikaSpeech.setEditable(false);
        pikaSpeech.setLineWrap(true);
        pikaSpeech.setBounds(130,30,670,100);

        enter.setBounds(650,60,130,50);
        enter.setFont(font.deriveFont(20f));

        add(back);
        add(pikaSpeech);

        textCheckpoints.add(5);
        textCheckpoints.add(7);
        textCheckpoints.add(10);
        textCheckpoints.add(14);
    }

    //Calls the drawing functions and adds back button
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g);
        drawBlood(g);
        if(currentInstruction == 10 && trainers.size() > 0)
        {
            Graphics2D g3d = (Graphics2D) g.create();
            Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0, new float[]{9}, 0);
            g3d.setStroke(dashed);
            g3d.drawLine(400, 400, trainers.get(0).curx-pika.curx+350+40, trainers.get(0).cury-pika.cury+350+40);
            g3d.dispose();
        }
        if(!attack.running)
            drawPika(g);
        drawTrainer(g);
        drawFrontFence(g);
        for(int i = 0; i < balls.size(); i++)
            drawPokeball(g, balls.get(i).curx, balls.get(i).cury, 1, balls.get(i).state, 50);
        drawAttack(g2d, 380, 380, attack.state, 180, 40);
        
        g2d.setColor(Color.WHITE);
        g.drawImage(textBubble, 0, 0, 800, 120, null);
        g.drawImage(pikaIcon,30,20,80,80,null);
        
        if(ded)
        {
            g2d.setColor(new Color(1.0f,0.0f,0.0f,0.5f));
            g2d.fillRect(0, 0, 800, 800);
            g.drawImage(youdied,100,100,600,120,null);
            // g.drawImage(rev.leaderboard.scroll, 85, 220, 250, 230, null);
            // g.drawImage(buttonTemplate,100,450,600,75, null);
            // g.drawImage(buttonTemplate, 100,550,600,75, null);
            // g.drawImage(buttonTemplate, 100,650,600,75, null);
        }
        if(currentInstruction == 14 && trainers.size() == 0)
        {
            canSkipText = true;
            typing.start();
            remove(enter);
        }


        
        
        
    }

    //Makes next letter of instructions
    public void nextLetter()
    {
        try
        {
            if(canSkipText == true)
            {
                if(currentLetter <= text.get(currentInstruction).length())
                {
                    pikaSpeech.setText(text.get(currentInstruction).substring(0,currentLetter));
                    currentLetter++;
                    isTyping = true;
                }
                else
                {
                    currentInstruction++;
                    currentLetter = 0;
                    typing.stop();
                    if(textCheckpoints.contains(currentInstruction))
                        canSkipText = false;
                    isTyping = false;
                    add(enter);
                }
            }
        }
        catch(IllegalArgumentException e)
        {

        }
        
        
    }

    //Skips dialogue typing
    public void skipDialogue()
    {
        try
        {
            pikaSpeech.setText(text.get(currentInstruction));
            currentInstruction++;
            currentLetter = 0;
            typing.stop();
            if(textCheckpoints.contains(currentInstruction))
                canSkipText = false;
            isTyping = false;
            add(enter);
        }
        catch(IllegalArgumentException e)
        {

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
            if(trainer1.throwing > 0)
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
            g.rotate(Math.PI+Math.PI-attack.direction,400,400);
            g.drawImage(dashpics[state%2],x,y,size1,size2,null);
            repaint();
            if(state >= 31)
            {
                attackAni.stop();
                attack.running = false;
                pika.update();

            }
            repaint();
            g.rotate(Math.PI-(Math.PI-attack.direction),400,400);
        }
    }

    //Draws grass background
    public void drawBackground(Graphics g) {
        int tmpx = -pika.curx;
        int tmpy = -pika.cury;
        for(int i = 0; i < 5; i++)
            for(int l = 0; l < 5; l++)
                g.drawImage(grass,tmpx+(i*640),tmpy+(l*640),640,640,null);
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

    //Pikachu class
    //Keeps track of direction, location, and animation state
    class Pika implements ActionListener {
        int state = 0;
        int direction = 3;
        int curx = 350;
        int cury = 350;
        int atkChangeX = 0;
        int atkChangeY = 0;

        //Changes animation state of pikachu
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state%2;
            repaint();
            grabFocus();
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
        }
    }

    //Class for the pokemon trainer
    //Roams around randomly and has animation
    //Will be used multiple times to create multiple trainers
    class Trainer implements ActionListener {
        int state = 0;
        int direction = 3;
        int curx = 1600;
        int cury = 1600;
        int var = 0;
        boolean running = false;
        int targetx = 350;
        int targety = 350;
        int speed = 30;
        int throwing = 0;

        //Change Trainer state
        //Track down pikachu and goes in it's direction
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state%3;

            if(throwing > 0)
                throwPoke();
            else if((int)(Math.random()*THROW_CHANCE) == 1)
                throwPoke();
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
                    trainers.remove(temp);
                    trainTimers.get(temp).stop();
                    trainTimers.remove(temp);
                    if(currentInstruction == 10)
                    {
                        currentInstruction++;
                        canSkipText = true;
                        typing.start();
                        remove(enter);
                    }
                    else if(currentInstruction >= 13)
                    {
                        spawnTrainer(1);
                    }
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
                        bloodMarks.add(new Blood(trainers.get(temp).curx,trainers.get(temp).cury));
                        bloodTimers.add(new Timer(30,bloodMarks.get(bloodMarks.size()-1)));
                        bloodTimers.get(bloodMarks.size()-1).start();
                        trainers.remove(temp);
                        trainTimers.get(temp).stop();
                        trainTimers.remove(temp);
                        if(currentInstruction == 10)
                        {
                            currentInstruction++;
                            canSkipText = true;
                            typing.start();
                            remove(enter);
                        }
                        if(currentInstruction >= 13) spawnTrainer(1);
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

        //Set blood cordinates
        public Blood(int x, int y)
        {
            curx = x;
            cury = y;
        }

        //Change attack animation state and change pikachu x and y
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
                    if(currentInstruction >= 13 && currentInstruction <= 14)
                    {
                        currentInstruction++;
                        canSkipText = true;
                        typing.start();
                        remove(enter);
                        trainers.clear();
                        if(trainTimers.size() > 0)
                            trainTimers.get(0).stop();
                        trainTimers.clear();
                        balls.clear();
                        for(int i = 0; i < ballTimers.size(); i++)
                            ballTimers.get(i).stop();
                        ballTimers.clear();
                    }
                }
            }
            else if (!hitBefore && Math.abs((curx+25)-(pika.curx+50)) <= 40 && Math.abs((cury+25)-(pika.cury+50)) <= 40)
            {
                int help = balls.indexOf(this);
                if(help != -1)
                {
                    balls.remove(help);
                    ballTimers.get(help).stop();
                    ballTimers.remove(help);
                    
                }
                
            }
            repaint();
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
            attack.running = true;
            attack.state = 0;
            attackAni.start();
            attack.direction = angle(400,400,e.getX(),800-e.getY());
            pika.atkChangeY = (int)(180 * Math.sin(attack.direction))/32;
            pika.atkChangeX = (int)(180 * Math.cos(attack.direction))/32;
            pika.atkChangeY *= -1;
            if(currentInstruction == 7)
            {
                canSkipText = true;
                typing.start();
                remove(enter);
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
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) downpress = true;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) rightpress = true;
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) leftpress = true;
        if(currentInstruction >= 21 && e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            moving.stop();
            attackAni.stop();
            pikaTime.stop();
            rev.card.show(rev,"game");
            rev.pikagame.startGame();
        }
        else
        {
            if(e.getKeyCode() == KeyEvent.VK_ENTER && canSkipText && !isTyping ) 
            {
                typing.start();
                remove(enter);
            }
            else if(e.getKeyCode() == KeyEvent.VK_ENTER && canSkipText) skipDialogue();
            pikaTime.start();
            if(currentInstruction == 5 && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) )
            {
                canSkipText = true;
                typing.start();
                remove(enter);
            }
            if(currentInstruction == 9 && e.getKeyCode() == KeyEvent.VK_ENTER && trainers.size() < 1 && !isTyping) spawnTrainer(1);
            if(currentInstruction == 13 && e.getKeyCode() == KeyEvent.VK_ENTER && trainers.size() < 1 && !isTyping)
            {
                THROW_CHANCE = 30;
                spawnTrainer(1);
            }
        }
        
        

        repaint();
        
    }

    //Reset boolean when moving
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) uppress = false;
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) downpress = false;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) rightpress = false;
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) leftpress = false;
        pikaTime.stop();
        repaint();
    }

    //Return angle given two points
    public double angle(int p1x, int p1y, int p2x, int p2y) {

        double tmp = -90.0;
        if((p2x > p1x))
            tmp +=  (Math.atan2((p2x - p1x), (p1y - p2y)) * 180 / Math.PI);
        else if((p2x < p1x)) 
            tmp += 360 - (Math.atan2((p1x - p2x), (p1y - p2y)) * 180 / Math.PI);
        else
            tmp = Math.atan2(0 ,0);
        if(tmp < 0) tmp = 360 + tmp;

        return Math.toRadians(tmp);

    }



    //Starts timers when the game starts
    public void startGame()
    {
        trainTimers.clear();
        trainers.clear();
        balls.clear();
        ballTimers.clear();
        //attackAni.start();
        moving.start();
        ded = false;
        downpress = uppress = leftpress = rightpress = false;
        addKeyListener(this);
        addMouseListener(this);  
        pika.direction = 3;  
        typing.start();
    }

    //Will pause timers when a pause button is pressed
    //Not used right now
    public void pauseGame()
    {
        pikaTime.stop();
        attackAni.stop();
        for(int i = 0; i < trainTimers.size(); i++)
            trainTimers.get(i).stop();
        for(int i = 0; i < ballTimers.size(); i++)
            ballTimers.get(i).stop();
        
        moving.stop();
        spawner.stop();
        attackAni.stop();
        removeMouseListener(this);
        removeKeyListener(this);

    }

    //Will be called when player dies
    //Score will be saved with username
    //Sent back to home page
    //Not used yet
    public void endGame()
    {
        pauseGame();
        setOpaque(false);
        //Update scores stuff
        //-------------------
        JPanel redpan = new JPanel();
        redpan.setBackground(new Color(1.0f,0.0f,0.0f,0.5f));
        redpan.setBounds(0,0,800,800);
        grabFocus();
        
        ded = true;

    
    }
    
}