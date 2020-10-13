package com.example.chat_web_app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client2 extends JFrame implements ActionListener {
    String username;
    PrintWriter pw;
    BufferedReader br;
    JTextArea  chatmsg;
    JTextField chatip;
    JButton send, exit;
    Socket chatusers;


    public Client2(String uname,String serverName) throws IOException {
        super(uname);
        this.username = uname;
        chatusers  = new Socket(serverName,80);
        br = new BufferedReader( new InputStreamReader( chatusers.getInputStream()) ) ;
        pw = new PrintWriter(chatusers.getOutputStream(),true);
        pw.println(uname);
        buildInterface();
        MessagesThread t1 = new MessagesThread();
        t1.start();

    }


    class MessagesThread extends  Thread{
        @Override
        public void run(){
            String msg ;
            //Scanner scanner = new Scanner(System.in);
            while(true){
                try {
                    msg = br.readLine();
                    chatmsg.append(msg);
                    chatmsg.append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void buildInterface(){
        send = new JButton("Send");
        exit = new JButton("Exit");
        chatmsg = new JTextArea();
        chatmsg.setRows(30);
        chatmsg.setColumns(50);
        chatmsg.setEditable(false);
        chatip  = new JTextField(50);
        JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel bp = new JPanel(new FlowLayout());
        bp.add(chatip);

        bp.add(send);
        bp.add(exit);
        bp.setBackground(Color.LIGHT_GRAY);
        bp.setName("Instant Messenger");
        send.addActionListener(this);
        exit.addActionListener(this);
        add(bp,"North");
        setSize(500,300);
        setVisible(true);
        pack();

    }





    public static void main(String args[]){
        String uname = JOptionPane.showInputDialog(null,"Write your name","Instant Chat Application",JOptionPane.PLAIN_MESSAGE);
        String serverName = "localhost";
        try{
            new Client2(uname,serverName);
        }catch(Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==exit){
            pw.println("end");
            System.exit(0);
        }
        else{
            String t1 = chatip.getText();
            chatip.setText("");
            pw.println(t1);
        }
    }
}
