import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.Socket;
import java.util.*;
import java.io.*;
import java.lang.Math;
public class Client extends JPanel implements ActionListener {
    JButton send;
    JLabel lblPort, lblServer, lblNumber, lblAction, countDownLabel,lblCount;
    JTextField  txtNumber;
    JTextField tfCount;
    Socket S;
    ButtonGroup group;
    public int countDown = 30;
    final static  String txtPort="8080";
    final static  String txtServer="127.0.0.1";
    private Countdown begin = new Countdown();
    public int count=0;
    public Client() {
        setLayout(null);
        send = new JButton("Send to the Server");
        send.addActionListener(this);


        countDownLabel = new JLabel("Time left:" +countDown);

        txtNumber=new JTextField("",5);
        lblNumber = new JLabel("Input a number:");
        lblAction = new JLabel("Actions:");
        lblCount = new JLabel("Number of tries :");
        tfCount = new JTextField(""+ count + "", 5);

        countDownLabel.setBounds(5,10,200,20);
        tfCount.setBounds(120,100,20,20);
        tfCount.setEditable(false);
        lblCount.setBounds(5,100,200,20);

        lblNumber.setBounds(5,40,200,20);
        txtNumber.setBounds(120,40,50,20);
        send.setBounds(5,70,150,20);

        lblAction.setBounds(5,135,300,20);






        add(lblNumber);
        add(txtNumber);
        add(send);
        add(lblAction);
        add(countDownLabel);
        add(tfCount);
        add(lblCount);
        begin.execute();

    }
    private class Countdown extends SwingWorker<Void, Integer> {
        public Void doInBackground() throws InterruptedException {
            try {
                while (countDown > 0 || count <= 30) {
                    countDownLabel.setText("" + countDown);
                    publish(countDown--);
                    Thread.sleep(1000);
                    if(countDown==0)
                        System.exit(0);
                }
            } catch (InterruptedException e){
            }
            System.out.println("It's over, you have run out of time!");
            //System.exit(0);
            begin.cancel(true);

            return null;
        }
        public void process(ArrayList<Integer> counter) {
            int currentValue = counter.get(counter.size() - 1);

            countDownLabel.setText("" + currentValue + "  seconds left");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == send) {
            try{
                ++count;
                tfCount.setText(count +"");
                int portS = Integer.parseInt(txtPort);
                String whereTo=txtServer;
                S = new Socket(whereTo, portS);
                BufferedReader fromTheServer = new BufferedReader(new InputStreamReader(S.getInputStream()));
                PrintWriter toTheServer = new PrintWriter( new OutputStreamWriter( S.getOutputStream() ), true );
                int number=Integer.parseInt(txtNumber.getText());
                toTheServer.println(number);
                String reply=fromTheServer.readLine();
                lblAction.setText(reply);


            } catch (Exception ex){
                lblAction.setText("The server does not work");
            }
        }


    }
    public static void main(String[] arg) {
        JFrame f = new JFrame("Guessing Game");
        f.getContentPane().add(new Client());
        f.setSize(400,300);
        f.setLocation(200,200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.show();

    }
}
