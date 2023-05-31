import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class MainMenu extends JFrame implements ActionListener {
    JPanel mainUI = new JPanel();
    JButton goToUserGuessing = new JButton();
    
    public MainMenu(){
        setTitle("Maze");
		setSize(800, 1000);
		setResizable(true);
		setVisible(true);

        goToUserGuessing.setSize(900, 100);
        goToUserGuessing.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                dispose();
                Merge merge = new Merge();
			}
		});


        add(mainUI);
        mainUI.add(goToUserGuessing);
    }


    public static void main(String[]args){
        //Merge merge = new Merge();
        //merge.genCode();
        MainMenu mainMenu = new MainMenu();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}
