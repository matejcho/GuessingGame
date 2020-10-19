import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.io.*;
public class Server extends JPanel implements ActionListener, Runnable  {
    JButton enable, disable;
    JLabel lblPort, lblAction;
    JTextField txtPort;
    static JTextArea log;
    ServerSocket S;
    Thread t;
    ButtonGroup group;

    public int randomInt = (int) (Math.random() * 50);
    public Server() {
        setLayout(null);
        enable = new JButton("Enable Server");
        enable.addActionListener(this);
        log=new JTextArea("");
        disable = new JButton("Disable Server");
        disable.addActionListener(this);
        txtPort=new JTextField("",5);
        lblPort = new JLabel("Port:");
        lblAction = new JLabel("Actions:");
        txtPort.setBounds(40,10,45,20);
        enable.setBounds(90,10,120,20);
        disable.setBounds(215,10,120,20);
        lblPort.setBounds(5,10,200,20);
        lblAction.setBounds(5,35,200,20);
        log.setBounds(5,60,330,330);

        add(txtPort);
        add(lblPort);
        add(disable);
        add(enable);
        add(lblAction);
        disable.setEnabled(false);
        JScrollPane scrollbar = new JScrollPane(log);
        scrollbar.setBounds(5,60,330,330);
//     add(log);
        add(scrollbar);
        enable.setEnabled(true);
    }
    public void run(){
        while(true){
            try{
                Socket connectionSocket = S.accept();
                BufferedReader fromTheClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                PrintWriter x = new PrintWriter( new OutputStreamWriter( connectionSocket.getOutputStream() ), true );
                //x.println(randomInt);
                String messageFromTheClient = fromTheClient.readLine();

                int number=Integer.parseInt(messageFromTheClient);
                if (number == randomInt) {
                    x.println("Congratulations, you have won the game!");
                } else if (number > randomInt) {
                    x.println("Try lower then your previous choice");
                } else if (number < randomInt) {
                    x.println("Try higher then your previous choice");
                }
                t.sleep(1000);
            }
            catch(Exception ex) 	{ }
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enable) {
            try{
                int portS = Integer.parseInt(txtPort.getText());
                S = new ServerSocket(portS);
                disable.setEnabled(true);
                enable.setEnabled(false);
                log.append("The server is enabled on port "+txtPort.getText()+" \n");
                t = new Thread(this);
                t.start();

            } catch (Exception ex){}
        }

        if (e.getSource()== disable){
            try{
                S.close();
                S = null;
                disable.setEnabled(false);
                enable.setEnabled(true);
                log.append("The server is disabled \n");
            }catch(Exception ex){}
        }
    }
    public static void main(String[] arg) {
        JFrame f = new JFrame("Guessing Game");

        f.getContentPane().add(new Server());
        f.setSize(350,450);
        f.setLocation(200,200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.show();
    }
}