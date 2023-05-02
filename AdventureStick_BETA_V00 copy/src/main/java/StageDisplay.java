import academiadecodigo.simplegraphics.graphics.Color;
import academiadecodigo.simplegraphics.graphics.Rectangle;
import academiadecodigo.simplegraphics.pictures.Picture;

import java.util.ConcurrentModificationException;

public class StageDisplay {
    //volatile Picture gameoverBackground = new Picture(0, 10, "Images/GameoverBackground.png");
    volatile Rectangle gameoverBackground=new Rectangle(0,10,490,700);
    volatile Picture restartButton = new Picture(320, 250, "RestartButton.png");


    volatile Picture staticStick = new Picture(60, MapEditor.Y_POS - 18, "staticStick.png");
    volatile Picture runStick = new Picture(50, MapEditor.Y_POS - 15, "runStick.png");
    volatile Picture ultraRunStick = new Picture(60, MapEditor.Y_POS - 15, "ultraRunStick.png");


    private volatile boolean running = false;

    public boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean runState) {
        this.running = runState;
    }

    Thread jumping;
    Picture[] loadedAbilities = new Picture[3];
    Picture tutorialText=new Picture(0,10,"Tutorial_text.jpg");
    Picture playInTutorial=new Picture(400,10,"Start_in_Tutorial.png");

    Picture[] startScreenDisplay=new Picture[]{new Picture(0,10,"start_background.png"),
            new Picture(80,30,"Tittle.png"),
            new Picture(165,250,"start_button.png"),
            new Picture(165,400,"Tutorial_button.png")};


    public void showTutorialText(){
        try{
            tutorialText.draw();
            playInTutorial.draw();
        }catch (ConcurrentModificationException e){
            showTutorialText();

        }
    }
    public void deleteTutorialText(){
        try{
            tutorialText.delete();
            playInTutorial.delete();
        }catch (ConcurrentModificationException e){
            deleteTutorialText();

        }

    }



    public void showStartScreen(){
        for (int i = 0; i < startScreenDisplay.length; i++) {
            try{
                startScreenDisplay[i].draw();
            }catch (ConcurrentModificationException e){i--;}
        }

    }
    public void deleteStartScreen(){
        for (int i = 0; i < startScreenDisplay.length; i++) {
            try{
                startScreenDisplay[i].delete();
            }catch (ConcurrentModificationException e){i--;}
        }

    }

    public void showSpecialBalls() {
        for (int j = 0; j < loadedAbilities.length; j++) {
            if(loadedAbilities[j]!=null) {
                try{
                    loadedAbilities[j].delete();
                }catch (ConcurrentModificationException e){j--;}
            }

        }

        for (int i = 0; i < MapEditor.specialAbilities.size(); i++) {

            //loadedAbilities[i]=new Picture(30,100,MapEditor.specialAbilities.get(i).getImagePath());
            try{
                loadedAbilities[i]=new Picture(30,100,MapEditor.specialAbilities.get(i).getImagePath());
                loadedAbilities[i].translate(20*i, 0);
            loadedAbilities[i].draw();}catch (ConcurrentModificationException e){i--;}
        }

    }


    public void initGameover() {

        try{
            gameoverBackground.setColor(Color.LIGHT_GRAY);
            gameoverBackground.fill();
            restartButton.draw();}catch(ConcurrentModificationException e){initGameover();}
    }

    public void hideGameover() {

        try{
            gameoverBackground.delete();
            restartButton.delete();}catch(ConcurrentModificationException e){hideGameover();}
    }

    public void initStick(boolean show) {
        runStick.load("staticStick.png");
        if (show) {
            runStick.draw();
        } else {
            runStick.delete();
        }
    }

    public synchronized void running() {

        class RunAnimation implements Runnable {
            @Override
            public void run() {
                //initStick(false);
                int counter = 0;
                running = true;
                //System.out.println("entering run mode");
                while (running) {
                    try {
                        runStick.draw();
                        if (counter % 2 == 0) {
                            runStick.load("ultraRunStick.png");
                        } else {
                            runStick.load("runStick.png");
                        }
                    } catch (ConcurrentModificationException e) {
                        //counter--;
                        //continue;
                    }

                    try {
                        Thread.sleep(160);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    counter++;

                }
                try{runStick.load("staticStick.png");}catch (ConcurrentModificationException ignored){}
            }
        }
        Thread running = new Thread(new RunAnimation());
        running.start();

    }

    public synchronized void jump() {

        Runnable jump = () -> {
            int counter = 1;
            int initialY = runStick.getY() + 1;
            while (runStick.getY() != initialY) {
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (counter == 5) initialY--;

                try {
                    if (counter <= 45) {
                        runStick.translate(0, -1);
                    } else {
                        runStick.translate(0, 1);
                    }
                } catch (ConcurrentModificationException e) {
                    //counter--;
                    //continue;
                }

                counter++;

            }
            MapEditor.getCanJump = true;
        };
        jumping = new Thread(jump);
        jumping.start();

    }
}
