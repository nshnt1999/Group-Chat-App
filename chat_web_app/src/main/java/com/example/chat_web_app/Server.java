package com.example.chat_web_app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;


public class Server {

    private static ArrayList<String> users = new ArrayList<>();
    private static ArrayList<MessagingThread> clients = new ArrayList<MessagingThread>();



    public static void main(String args[]) throws  Exception{

        ServerSocket server = new ServerSocket(80,10);
        System.out.println("Now server is running");
        while(true){
            Socket client = server.accept();
            MessagingThread thread = new MessagingThread(client);
            clients.add(thread);
            thread.start();
        }
    }




    static class MessagingThread extends Thread{

        String user;
        BufferedReader input;
        PrintWriter output;

        public MessagingThread(Socket client) throws Exception{
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(),true);
            user = input.readLine();
            users.add(user);

            DbOperations.addUserInDb(user);

        }


        public String getUser(){
            return user;
        }

        public void sendMessage(String user,String msg){
            output.println(user+": "+msg);
        }

        public void sendToMe(String user,String msg){
            output.println("You: "+msg);
        }

        public void sendToAllUser(String user,String msg){

            for(MessagingThread c:clients){
                if(!c.getUser().equals(user)){
                    c.sendMessage(user,msg);
                }else{
                    c.sendToMe(user,msg);
                }
            }
        }

        public void saveInDb(String user,String msg) throws SQLException {
            String msg_id = user + "_" + System.currentTimeMillis();
            DbOperations.chatBackUp(user,msg_id,msg);
        }


        @Override
        public  void run(){
            String msg;
            try{
                while(true){
                    msg = input.readLine();
                    if(msg.equals("end")){
                        clients.remove(this);
                        users.remove(user);
                        break;
                    }else{
                        sendToAllUser(user,msg);
                        saveInDb(user,msg);
                    }
                }
            }
            catch(Exception E){
                System.out.println(E.getMessage());
            }

        }
    }


}
