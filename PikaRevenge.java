//Jayden Lim
//5-15-22
//PikaRevenge.java
//Creates frame that the game is played on


import java.awt.*;  
import java.awt.event.*;
import javax.swing.*; 
import java.io.*;


//Frame that contains the whole game
public class PikaRevenge extends JFrame {

    //Set frame settings
    public PikaRevenge() {
        super("Pika's Revenge");
        setSize(800,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        MainPan frm = new MainPan();
        setContentPane( frm );
        setVisible(true);
    }
    //Create Frame
    public static void main(String[] args) {
        new PikaRevenge();
    }
    
}

//Main panel containing all buttons that can access game
//Card Layout
class MainPan extends JPanel
{
    String currentPlayer = "Bob";
    Image grass = new ImageIcon("pikagrass.png").getImage();
    Image logo = new ImageIcon("pikalogo.png").getImage();
    
    JButton play = new JButton("PLAY");
    JButton instruction = new JButton("INSTRUCTIONS");;
    JButton leader = new JButton("LEADER");
    JButton settings = new JButton();

    JButton dodge = new JButton("Dodge");
    
    Font font;

    JLayeredPane startBorder = new JLayeredPane();
    //JPanel startMenu = new JPanel(new GridLayout(4,1,50,50));
    Leaderboard leaderboard = new Leaderboard();
    PGame pikagame = new PGame(this);
    PGame2 pikagame2 = new PGame2(this);
    Settings setting;
    
    Instructions instructPan = new Instructions(this);

    StartTimer spt;
    StartTimer spt2;
    //Timer for start page pikachu
    //Changes state of pikachu every 0.5 seconds
    Timer startPika;
    Image icon = new ImageIcon("settings.png").getImage();
    CardLayout card = new CardLayout();
    

    //Run runIt() to create panel
    //Initialize some variables
    public MainPan() 
    {
        //Set Minecraft font
        try {
            Font tmpfont = Font.createFont(Font.TRUETYPE_FONT, new File("Minecraft.ttf"));
            font = tmpfont.deriveFont(25f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        
        
        spt = new StartTimer(2);
        spt2 = new StartTimer(8);
        startPika = new Timer(500, spt);
        startPika.addActionListener(spt2);
        startPika.start();
        
        
        
        runIt();

        
    }
    
    //Draws the background and logo images
    public void paintComponent(Graphics g) 
    {
        super.paintComponent (g);
        for(int i = 0; i < 2; i++)
            for(int l = 0; l < 2; l++)
                g.drawImage(grass,(i*640),(l*640),640,640,null);
        g.drawImage(logo, 70, 80, 657, 93, null);
        g.setColor(Color.WHITE);

        Graphics2D g2d = (Graphics2D) g;

        g.drawImage(pikagame.buttonTemplate, 65, 240, 670, 115, null);
        g.drawImage(pikagame.buttonTemplate, 65, 415, 670, 115, null);
        g.drawImage(pikagame.buttonTemplate, 65, 590, 670, 115, null);


        g2d.fillRoundRect(725,13,60,62, 15, 15);
        g.drawImage(icon, 728,16,53,56, null);

        pikagame.drawPika(g,100,255,3,spt.state,80);
        pikagame.drawPika(g,215,255,3,spt.state,80);
        pikagame.drawPika(g,510,255,3,(spt.state+1)%2,80);
        pikagame.drawPika(g,625,255,3,(spt.state+1)%2,80);

        pikagame.drawTrainer(g,110,445,3,spt.state,45,60);
        pikagame.drawTrainer(g,225,445,3,spt.state,45,60);
        pikagame.drawTrainer(g,525,445,3,(spt.state+1)%2,45,60);
        pikagame.drawTrainer(g,640,445,3,(spt.state+1)%2,45,60);

        pikagame.drawPokeball(g,105,617,2,spt2.state,60);
        pikagame.drawPokeball(g,220,617,2,spt2.state,60);
        pikagame.drawPokeball(g,515,617,2,spt2.state,60);
        pikagame.drawPokeball(g,630,617,2,spt2.state,60);
        
        
    }
    
    //Make buttons in cardlayout to toggle between panels
    public void runIt()
    {
        
        setLayout(card);
        setting = new Settings(this);
        play.setFont(font);
        instruction.setFont(font);
        leader.setFont(font);

        play.setBounds(60, 236, 680, 123);
        instruction.setBounds(60, 411, 680, 123);
        leader.setBounds(60, 586, 680, 123);

        
        settings.setBounds(720,10,70,70);
        settings.setOpaque(false);
        settings.setContentAreaFilled(false);
        settings.setBorderPainted(false);
        
        startBorder.add(play);
        startBorder.add(instruction);
        startBorder.add(leader);
        startBorder.add(settings);

        // startMenu.setOpaque(false);
        startBorder.setOpaque(false);
        add(startBorder, "start");
        add(pikagame,"game");
        add(instructPan,"instruct");
        add(leaderboard, "leader");
        add(setting,"settings");
        add(pikagame2,"game2");

        play.setOpaque(false);
        play.setContentAreaFilled(false);
        play.setBorderPainted(false);

        instruction.setOpaque(false);
        instruction.setContentAreaFilled(false);
        instruction.setBorderPainted(false);

        leader.setOpaque(false);
        leader.setContentAreaFilled(false);
        leader.setBorderPainted(false);

        dodge.setBounds(10,10,100,50);
        dodge.setFont(font.deriveFont(15f));
        dodge.addActionListener(e -> card.show(this,"game2"));
        dodge.addActionListener(e -> pikagame2.startGame());
        startBorder.add(dodge);
        
        play.addActionListener(e -> card.show(this,"game"));
        play.addActionListener(e -> pikagame.startGame());
        instruction.addActionListener(e -> card.show(this,"instruct"));
        instruction.addActionListener(e -> instructPan.startGame());
        instructPan.back.addActionListener(e -> card.show(this,"start"));
        pikagame.back.addActionListener(e -> {pikagame.endGame(); card.show(this,"start");});
        pikagame2.back.addActionListener(e -> {pikagame2.endGame(); card.show(this,"start");});
        leader.addActionListener(e -> card.show(this,"leader"));
        leaderboard.back.addActionListener(e -> card.show(this,"start"));
        setting.back.addActionListener(e -> card.show(this,"start"));
        settings.addActionListener(e -> card.show(this,"settings"));
        
        
    }

    //Starting timer for pikachu for animation
    class StartTimer implements ActionListener {
        int state;
        int length;

        //Gets frame amount
        public StartTimer(int num)
        {
            length = num;
        }
            
        //Change frame of animation
        public void actionPerformed(ActionEvent e) {
            state++;
            state = state%length;
            repaint();
            grabFocus();
        }
        
    }
 
}


//Empty panel that's transparent
class tmpPanel extends JPanel {
    //Makes panel clear
    public tmpPanel()
    {
        setOpaque(false);
    }
}

