//Jayden Lim
//5-15-22
//Settings.java
//Creates settings panel which player can change difficulty or language

//Panel for the setting options

import java.awt.*;  
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*; 
import java.io.*;
import javax.swing.JLayeredPane;

//Panel for the setting options
public class Settings extends JLayeredPane implements MouseListener {
    JButton back = new JButton("BACK");
    MainPan rev;
    Font font;
    JTextField username;
    JLabel currentUser;
    JButton changeName = new JButton("Change");
    boolean userFocus = false;
    JLabel valid = new JLabel("NOT VALID USERNAME!");

    JLabel vulgarLab = new JLabel("Vulgar");
    JCheckBox vulgar = new JCheckBox();



    //Get the main panel so it can access the stuff
    public Settings(MainPan revenge)
    {   
        rev = revenge;
        font = rev.font;
        username = new JTextField();
        currentUser = new JLabel("Current Player: " + rev.currentPlayer);
        addMouseListener(this);
        run();
    }
    //Make all components that user can change
    public void run()
    {
        setOpaque(false);
        
        currentUser.setFont(font.deriveFont(20f));
        currentUser.setBounds(200,250,500,40);
        add(currentUser);

        username.setFont(font.deriveFont(20f));
        username.setBounds(200,300,300,40);
        username.addFocusListener(new NameFocus());
        username.addActionListener(e -> username.setSelectionEnd(0));
        
        add(username);

        changeName.setFont(font.deriveFont(15f));
        changeName.setBounds(530,300,80,40);
        changeName.addActionListener(new UserChange());
        add(changeName);

        back.setFont(font.deriveFont(15f));
        back.setBounds(10,10,100,40);
        back.setVerticalAlignment(JButton.CENTER);
        add(back);

        valid.setFont(font.deriveFont(20f));
        valid.setOpaque(false);
        valid.setBounds(200,350,500,40);
        valid.setForeground(Color.RED);
        valid.setVisible(false);
        add(valid);

        vulgarLab.setFont(font.deriveFont(20f));
        vulgarLab.setBounds(200,400,200,100);
        add(vulgarLab);

        vulgar.setBounds(270,405,80,80);
        vulgar.addItemListener(new VulgarChange());
        add(vulgar);
    }
    //Draw background
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(rev.leaderboard.pokedex, -240, 0, 1280, 800, null);
        g.drawImage(rev.leaderboard.scroll, 25, 150, 750, 625, null);
        //g.drawImage(rev.leaderboard.leadlogo, 100, 60, 600, 70, null);
        g.setFont(font.deriveFont(15f));
        if(userFocus)
        {
            username.requestFocus();
            username.setCaretPosition(username.getText().length());
        }
        

    }

    //Do Nothing
    public void mouseClicked(MouseEvent e) {}

    //Keep focus on username textfield
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
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

    //Change name when button is clicked
    class UserChange implements ActionListener
    {
        //Changes name and reset textfield
        public void actionPerformed(ActionEvent e)
        {
            valid.setVisible(false);
            if(username.getText().contains(" ") || username.getText().equals(""))
            {
                valid.setVisible(true);
            }
            else
            {
                rev.currentPlayer = username.getText();
                currentUser.setText("Current Player: " + rev.currentPlayer);
                username.setText("");
            }
        }
    }

    //Class to change text to be vulgar or not
    class VulgarChange implements ItemListener
    {
        //Change text if vulgar or not
        public void itemStateChanged(ItemEvent e)
        {
            if(vulgar.isSelected())
            {
                rev.instructPan.text = rev.instructPan.text2;
                rev.instructPan.currentLetter = 0;
            }
            else
            {
                rev.instructPan.text = rev.instructPan.text1;
                rev.instructPan.currentLetter = 0;
            }
        }
    }

}

