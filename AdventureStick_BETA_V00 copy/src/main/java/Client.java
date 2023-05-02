import academiadecodigo.simplegraphics.graphics.Canvas;
import academiadecodigo.simplegraphics.graphics.Text;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

public class Client {
    private final static String SERVER_IP="192.168.1.16";
    private final static int portNumber=9990;
    private Socket socket;
    private BufferedReader inputBufferedReader;
    private PrintWriter outputBufferedWriter;

    String txtFromServer="";
    Text[] highScore;
    Text messageNotTop;

    //String highScores;

    int score;

    public Client(int score){
        this.score=score;
    }

    public void initConnection()  {
        try {
            socket = new Socket(SERVER_IP, portNumber);
            System.out.println("in server");
            setupSocketStreams();
        }catch (UnknownHostException e){
            System.out.println("Server Down, try again later");
            return;
        }catch(IOException ex){

            System.out.println("SERVER DOWN :(");
            return;
        }
        while (txtFromServer.split(",").length<10) {
            try{
                txtFromServer=inputBufferedReader.readLine();
            }catch (IOException e){
                System.out.println("couldnt read");
            }
        }
        System.out.println(txtFromServer);
        showHighScores();
        outputBufferedWriter.println(""+score+"");
        outputBufferedWriter.flush();
        String helper="";
        try{helper=inputBufferedReader.readLine();
        }catch(IOException e){
            System.out.println("error");
        }
        if(helper.toLowerCase().contains("write")){

            System.out.println("inside writing name");
            String clientName=JOptionPane.showInputDialog(null,"YOU ARE IN TOP 10!\nEnter your name:");
            while(!isNameValid(clientName)) clientName=JOptionPane.showInputDialog(null,"YOU ARE IN TOP 10!\nEnter your nickname:\n(Nickname must not be in use && can't have more than 15 characters && can only have letters)");

            outputBufferedWriter.println(clientName);//new Scanner(System.in).nextLine());//later will be player input
            System.out.println("name sent");

        }else{
            messageNotTop=new Text(155,625,"<-- Not in Top 10 -->");
            messageNotTop.grow(120,55);
            messageNotTop.draw();
            System.out.println("Not in Top 10");

        }
        try{
            socket.close();
            System.out.println("socket close");
        }catch (IOException e){
            System.out.println("upsi, couldnt close connection to  sevrer");
        }
    }
    private boolean isNameValid(String name){
        boolean doesNameOnlyContainLetters=true;
        try{doesNameOnlyContainLetters=name.chars().allMatch(Character::isLetter);}catch (NullPointerException ignored){name="DefaultBedro";}
        if(!doesNameOnlyContainLetters || name.length()>16) return false;

        for (String nameHighScore:txtFromServer.split(",") ){
            if(name.trim().equalsIgnoreCase(nameHighScore.split("-")[0])&&!name.equalsIgnoreCase("DefaultBedro")) return false;
        }
        return true;

    }
    public void setupSocketStreams() throws IOException {

        inputBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputBufferedWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

    }
    public void showHighScores(){
        String[] namesAndScores=txtFromServer.split(",");
        highScore=new Text[10];
        for (int i = 0; i < namesAndScores.length ; i++) {
            if(i==0){
                highScore[i]=new Text(130,40,"| 1st | >  "+namesAndScores[i].split("-")[0]+
                        " =*> "+namesAndScores[i].split("-")[1]+" pts");
                highScore[i].grow(120,17);

            }else{
            highScore[i]=new Text(40,45+(i*50),(i+1)+"º  >  "+namesAndScores[i].split("-")[0]+
                    " =*> "+namesAndScores[i].split("-")[1]+" pts");}
            try {
                highScore[i].draw();
            }catch (ConcurrentModificationException e){
                //i--;
            }
        }

    }
    public void deleteHighScores(){
        //System.out.println("in Delete high scores");
        //if (messageNotTop!=null) messageNotTop.delete();
        //if(highScore!=null){
        try{
            messageNotTop.delete();
        }catch (ConcurrentModificationException | NullPointerException e){
            //System.out.println("DEU MERDA EM DELETE HIGHSCORES");
            //i--;
        }
        for (int i = 0; i < 10; i++) {

            try{
                highScore[i].delete();
            }catch (ConcurrentModificationException | NullPointerException e){
                //System.out.println("DEU MERDA EM DELETE HIGHSCORES");
                //i--;
            }
        }
        //highScore=new Text[10];
    //}
    }

}
