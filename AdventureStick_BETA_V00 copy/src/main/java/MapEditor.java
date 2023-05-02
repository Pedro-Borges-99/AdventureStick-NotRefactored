
import academiadecodigo.simplegraphics.graphics.Canvas;
import academiadecodigo.simplegraphics.graphics.Ellipse;
import academiadecodigo.simplegraphics.graphics.Line;
import academiadecodigo.simplegraphics.graphics.Rectangle;
import academiadecodigo.simplegraphics.graphics.Text;
import academiadecodigo.simplegraphics.pictures.Picture;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class MapEditor {

    Client client;//=new Client();
    private volatile int nextLevelAdjust;
    Thread showingStarAbility = new Thread();
    Picture plusOneBall;


    private boolean isAbilityShowing = false;
    Thread nextLevel = new Thread();
    private volatile boolean isInvincible;
    private volatile boolean isAlwaysWin;

    private volatile int score = 0;
    public static volatile ArrayList<SpecialBalls> specialAbilities = new ArrayList();

    //private Picture myBall;
    private boolean canRun = false;
    private boolean canPress = true;

    public boolean getCanPress() {
        return canPress;
    }

    public void setCanPress(boolean canPress) {
        this.canPress = canPress;
    }

    private boolean canGrow = true;

    public void setCanGrow(boolean canGrow) {
        this.canGrow = canGrow;
    }

    public boolean getCanGrow() {
        return canGrow;
    }

    private volatile boolean gameover = false;

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public boolean getGameover() {
        return gameover;
    }

    //public static volatile Rectangle background = new Rectangle(0, 10, 700, 700);
    public static Picture background = new Picture(0, 10, "background_ICE.png");
    public static final int X_POS = 50;
    public volatile static int LINE_SIZE = 200;//this will be defined by time space is pressed

    public static final int Y_POS = background.getMaxY() - 200;
    private volatile static Line line;
    private volatile Line lineToDelete = new Line(0, 0, 0, 0);

    private volatile Rectangle[] platforms = new Rectangle[2];
    PlayerIKeyboard keyboard = new PlayerIKeyboard();
    PlayerMouse mouse = new PlayerMouse();
    StageDisplay display = new StageDisplay();
    Text score1;

    static boolean getCanJump = true;
    //Thread moving;
    //volatile Ellipse myBall=new Ellipse(650, Y_POS-8,6,2 );
    //Thread moveBall;
    Thread throwingBalls;


    public MapEditor() {

        platforms[0] = makePlat("left");
        platforms[1] = makePlat("right");
        mouse.init(this, this.display);

    }

    public void highscoreDynamic() {
        client = new Client(score);
        client.initConnection();
    }

    public void startScreen() {
        display.showStartScreen();
    }

    public void init() {
        display.deleteStartScreen();
        keyboard.init(this);
        background.draw();
        score1 = new Text(background.getWidth() / 2, 80, "" + score + "");
        score1.grow(20, 20);
        score1.draw();
        line = new Line(0, 0, 0, 0);
        //mouse.init(this);
        display.initStick(true);
        // background.draw();
        platforms[0].fill();
        platforms[1].fill();

        startThrowingThings();
    }


    public static Rectangle makePlat(String side) {
        return side.equalsIgnoreCase("left") ? new Rectangle(50, Y_POS, 20 + ((int) (Math.random() * 30)), 200) : new Rectangle(100 + ((int) (Math.random() * 300)), Y_POS, 30 + ((int) (Math.random() * 60)), 200);

    }
    public Client getClient(){
        return client;

    }

    public void restart() {
        //client.deleteHighScores();
        //if (showingStarAbility.isAlive()) showingStarAbility.interrupt();//.join();
        //if (nextLevel.isAlive()) nextLevel.join();
        if (plusOneBall != null) plusOneBall.delete();
        isAlwaysWin = false;
        isAbilityShowing = false;
        isInvincible=false;

        specialAbilities = new ArrayList<>();
        display.showSpecialBalls();
        //Rectangle

        //background.setColor(Color.WHITE);
        background.draw();
        //background.draw();
        //movingBall();
        score = 0;
        score1.delete();
        score1 = new Text(background.getWidth() / 2, 80, "" + score + "");
        score1.grow(20, 20);
        score1.draw();
        lineToDelete.delete();
        line.delete();
        platforms[0].delete();
        platforms[1].delete();
        //background.setColor(Color.WHITE); background.fill();
        line = new Line(0, 0, 0, 0);
        platforms[0] = makePlat("left");
        platforms[1] = makePlat("right");
        platforms[0].fill();
        platforms[1].fill();
        setCanPress(true);
        keyboard.isRpressed=false;//keyboard.init(this);
        //moveBall.interrupt();
        //moveBall.join();
        //throwingBalls.join();
        //movingBall(new Ellipse(650, Y_POS-11,10,10 ));

        //startThrowingThings();
        //mouse.init(this);
    }

    public boolean stillAlive() throws IOException {
        if (gameover) return false;

        if (platforms[1].getX() <= 60 + line.getWidth() && (platforms[1].getX() + platforms[1].getWidth()) >= 60 + line.getWidth()) {
            System.out.println("Plataforms X and Xfinal: "+platforms[1].getX()+" and "+(platforms[1].getX() + platforms[1].getWidth())+"\n"
            +"Your line coordinate is: "+(line.getWidth()+60));
            return true;
        }
        System.out.println("Plataforms X and Xfinal: "+platforms[1].getX()+" and "+(platforms[1].getX() + platforms[1].getWidth())+"\n"
                +"Your line coordinate is: "+(line.getWidth()+60));

        //System.out.println(platforms[1].getX() + "<" + (70 + line.getWidth()) + " && " + (platforms[1].getX() + platforms[1].getWidth()) + ">" + (70 + line.getWidth()));


        gameover = true;
        display.initGameover();
        //highscoreDynamic(); this line shouldn't be commented in order to connect to server.
        return false;
    }

    public void jump() {
        display.jump();
    }

    public void startThrowingThings() {
        //throwingBalls.join();
        Runnable throwing = () -> {
            while (true) {
                try {
                    Thread.sleep(1650 + (long) (Math.random() * 3500));
                    //Ellipse balls=new Ellipse(background.getWidth(), Y_POS-(5+(int)(Math.random()*20)),4,4 );
                    //balls.draw();
                    if (Math.random() > 0.82) {
                        movingBallGood();
                    } else {
                        movingBall();
                    }


                } catch (InterruptedException e) {
                    startThrowingThings();
                    //throw new RuntimeException(e);
                }
            }
            //startThrowingThings();

        };
        throwingBalls = new Thread(throwing);
        throwingBalls.start();
    }

    public void movingBallGood() {
        if (gameover) return;

        SpecialBalls randomSpecialBall = SpecialBalls.values()[(int) (Math.random() * SpecialBalls.values().length)];
        int posRandom = ((int) (Math.random() * 50));
        Picture plusOneBall = new Picture(480, Y_POS - 12 - posRandom, randomSpecialBall.getImagePath());
        try {
            plusOneBall.draw();
        } catch (ConcurrentModificationException ignored) {
            //plusOneBall.delete();
            tryDeleteGoodBall(plusOneBall);
            movingBallGood();
            //System.out.println("in return moving ball GOOD ");
            return;
        }

        Runnable movingBall = () -> {
            //if (gameover) tryDeleteGoodBall(plusOneBall);
            while (plusOneBall.getX() > 0 && !gameover) {

                try {

                    Thread.sleep(4);
                    try {
                        plusOneBall.translate(-1, 0);

                    } catch (ConcurrentModificationException | NullPointerException e) {

                    }


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (plusOneBall.getX() < display.runStick.getX() + display.runStick.getWidth()
                        && plusOneBall.getX() + plusOneBall.getWidth() > display.runStick.getX()
                        && plusOneBall.getY() < display.runStick.getY() + display.runStick.getHeight()
                        && plusOneBall.getY() + plusOneBall.getHeight() > display.runStick.getY()
                        && !gameover) {
                    //setGameover(true);
                    //score++;
                    if (specialAbilities.size() < 3) {
                        specialAbilities.add(randomSpecialBall);
                        display.showSpecialBalls();
                        break;
                    }
                }

            }
            try {
                plusOneBall.delete();
            } catch (ConcurrentModificationException e) {
                tryDeleteGoodBall(plusOneBall);
            }

        };
        new Thread(movingBall).start();
    }

    private void tryDeleteGoodBall(Picture ball) {
        try {
            ball.delete();
        } catch (ConcurrentModificationException e) {
            tryDeleteGoodBall(ball);
        }
    }

    public void useSpecialBall() {
        boolean isDraw = false;

        if (isAbilityShowing) return;
        if (specialAbilities.get(0).equals(SpecialBalls.PLUS_ONE)) {
            //System.out.println("using plus 1 ball");
            isAbilityShowing = true;
            isAlwaysWin = true;
            plusOneBall = new Picture(30, 300, specialAbilities.get(0).getImagePath());
            plusOneBall.grow(15, 15);
            while (!isDraw) {
                try {
                    plusOneBall.draw();
                    isDraw = true;
                } catch (ConcurrentModificationException e) {
                    isDraw = false;
                }
            }
            specialAbilities.remove(0);
            display.showSpecialBalls();


        } else if (specialAbilities.get(0).equals(SpecialBalls.IMMORTALITY)) {
            isInvincible = true;
            isAbilityShowing = true;
            Picture invincibleStar = new Picture(30, 300, specialAbilities.get(0).getImagePath());
            specialAbilities.remove(0);
            display.showSpecialBalls();
            invincibleStar.grow(15, 15);
            while (!isDraw) {
                try {
                    invincibleStar.draw();
                    isDraw = true;
                } catch (ConcurrentModificationException ignored) {
                    isDraw = false;
                }
            }


            //invincibleStar.draw();
            Runnable invincible = () -> {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                }
                //System.out.println("using invincibility ball");
                isAbilityShowing = false;
                isInvincible = false;
                try {
                    invincibleStar.delete();
                } catch (ConcurrentModificationException ignored) {
                    invincibleStar.delete();
                }
            };
            showingStarAbility = new Thread(invincible);
            showingStarAbility.start();

        }

    }


    public void movingBall() {
        if (gameover) return;
        int numRandom = (int) (Math.random() * 7);
        Ellipse myBall = new Ellipse(475, Y_POS - 11 - numRandom, 7 + numRandom, 7 + numRandom);
        try {

            myBall.fill();
        } catch (ConcurrentModificationException e) {
            tryDeleteBall(myBall);
            movingBall();
            //System.out.println("in return moving ball");

        }

        Runnable movingBall = () -> {
            //if(gameover) tryDeleteBall(myBall);

            while (myBall.getX() > 0 && !gameover) {

                try {

                    Thread.sleep(6 - nextLevelAdjust);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    myBall.translate(-1, 0);
                } catch (ConcurrentModificationException | NullPointerException e) {

                }
                if (myBall.getX() < display.runStick.getX() + display.runStick.getWidth() - 2
                        && myBall.getX() + myBall.getWidth() > display.runStick.getX() + 2
                        && myBall.getY() < display.runStick.getY() + display.runStick.getHeight() - 1
                        && myBall.getY() + myBall.getHeight() > display.runStick.getY() + 1
                        && !gameover && !isInvincible) {


                    tryDeleteBall(myBall);
                    gameover = true;
                    display.initGameover();
                    //highscoreDynamic();

                    //break;


                    //highscoreDynamic();
                    //System.out.println("game is over");
                }

            }
            try {
                //System.out.println("in delete ball");
                myBall.delete();

            } catch (ConcurrentModificationException e) {
                tryDeleteBall(myBall);
            }
            //if(gameover) highscoreDynamic();
            //if(!gameover)movingBall();
            //myBall=new Ellipse(650, Y_POS-11,5,5 );

        };
        //moveBall=new Thread(movingBall);
        new Thread(movingBall).start();
        //moveBall.start();
        //moving.start();
    }

    private void tryDeleteBall(Ellipse ball) {

        try {
            ball.delete();
        } catch (ConcurrentModificationException e) {
            tryDeleteBall(ball);
        }
    }

    /**
     * THE FOLLOWING CODE IS A TRAIN OF ACTIONS
     * REPRESENTING THE SINGLE UNITY OF GAME ACTION
     **/

    public void growLine() throws InterruptedException {

        class GrowLine implements Runnable {
            @Override
            public void run() {
                int counter = 0;
                System.out.println(platforms[1].getX());
                while ((isAlwaysWin ? counter < platforms[1].getX() - 50 : canGrow) && counter < 420) {


                    try {
                        Thread.sleep(4);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    counter++;


                    try {
                        line.delete();
                        //counter[0]++;

                        line = new Line(60, 500, 60, 500 - counter );
                        line.draw();

                    } catch (ConcurrentModificationException e) {
                    }
                }
                if (isAlwaysWin) {
                    isAlwaysWin = false;
                    if (plusOneBall != null) tryDeleteGoodBall(plusOneBall);//plusOneBall.delete();
                    isAbilityShowing = false;
                }

                LINE_SIZE = 500 - counter;
                try {
                    moveLine();
                } catch (InterruptedException e) {
                    //System.exit(1);
                    throw new RuntimeException(e);
                }

            }
        }

        new Thread(new GrowLine()).start();
        //grow.start();
    }

    public void moveLine() throws InterruptedException {

        class MoveLine implements Runnable {
            @Override
            public void run() {
                int tamanho = line.getHeight();
                int y = line.getHeight();
                for (int i = 0; i < tamanho + 1; i++) {
                    if(gameover) {
                        line.delete();
                        return;}
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    y--;
                    try {
                        line.delete();
                        line = new Line(60, Y_POS, 60 + (Math.sqrt((tamanho * tamanho) - (y * y))), Y_POS - tamanho + i);

                        line.draw();
                    } catch (ConcurrentModificationException e) {
                        //i--;
                    }
                }

                try {
                    if (stillAlive()) {
                        try {
                            score++;
                            score1.delete();
                            score1 = new Text(background.getWidth() / 2, 80, "" + score + "");
                            score1.grow(20, 20);
                            score1.draw();
                            nextChallenge();
                        } catch (InterruptedException |ConcurrentModificationException e) {
                           // throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Server is down :(");
                    //throw new RuntimeException(e);
                }

            }
        }
        //movingBall();
        new Thread(new MoveLine()).start();

    }

    public void nextChallenge() throws InterruptedException {
        display.running();

        class nextLevel implements Runnable {
            @Override
            public void run() {
                nextLevelAdjust = 2;
                while (platforms[1].getX() > 30) {
                    if(gameover) {
                        display.setRunning(false);
                        return;
                    }

                    try {
                        Thread.sleep(6);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        line.translate(-1, 0);
                        platforms[0].translate(-1, 0);
                        platforms[1].translate(-1, 0);
                    } catch (ConcurrentModificationException e) {
                    }
                }
                nextLevelAdjust = 0;


                synchronized (this) {

                    display.setRunning(false);}


                lineToDelete.delete();
                lineToDelete = line;
                line = new Line(0, 0, 0, 0);
                platforms[0].delete();
                platforms[0] = platforms[1];

                int xPos = platforms[0].getX() + platforms[0].getWidth() + 10 + ((int) (Math.random() * 280));
                int platWidth = 30 + ((int) (Math.random() * 60));
                for (int i = 0; i <= 200; i++) {
                    if(gameover) return;

                    try {
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        platforms[0].fill();

                        platforms[1].delete();
                        if(gameover) break;
                        platforms[1] = new Rectangle(xPos, background.getMaxY() - i, platWidth, i);
                        platforms[1].fill();
                    } catch (ConcurrentModificationException e) {
                    }
                }
                setCanPress(true);

            }
        }
        nextLevel = new Thread(new nextLevel());
        nextLevel.start();
        //display.running();

    }
}
