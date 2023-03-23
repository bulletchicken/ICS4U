import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class GUI extends JFrame {
	
	double num1, num2, AnswerNum;
	
	JButton PlusButton = new JButton("+");
	JButton MinusButton = new JButton("-");
	JButton TimesButton = new JButton("*");
	JButton DivideButton = new JButton("/");
	
	JTextField FirstInput = new JTextField();
	JTextField SecondInput = new JTextField();
	JTextField DisplayOutput = new JTextField();
	
	JLabel NumberLabel1 = new JLabel();
	JLabel NumberLabel2 = new JLabel();
	JLabel AnswerLabel1 = new JLabel();
	
	JPanel Number1Panel = new JPanel();
	JPanel Number2Panel = new JPanel();
	JPanel AnswerPanel = new JPanel();
	JPanel ButtonPanel = new JPanel();
	
	public GUI(){
		setSize(400, 400);
		setTitle("Simple Calculator");
		
		
		setVisible(true);
	}
	
	GridLayout layout2 = new GridLayout(5, 2);
	
	public void actionPerformed(ActionEvent event){
		String command = event.getActionCommand();
		num1 = Double.parseDouble(FirstInput.getText());
		num2 = Double.parseDouble(SecondInput.getText());
		AnswerNum = 0;
		if(command.equals("+")){
			AnswerNum = num1+num2;
		}
		else if(command.equals("-")){
			AnswerNum = num1-num2;
		}
		else if(command.equals("*")){
			AnswerNum = num1*num2;
		}
		else if(command.equals("/")){
			AnswerNum = num1/num2;
		}
		
		
		
	}
	
	public static void main(String[]args){
		GUI frame = new GUI();
	}
	
	static void add(){
		
	}
	static void subtract(){
		
	}
	static void multiply(){
		
	}
	static void divide(){
		
	}
}
