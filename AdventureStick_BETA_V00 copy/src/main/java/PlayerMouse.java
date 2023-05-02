import academiadecodigo.simplegraphics.mouse.Mouse;
import academiadecodigo.simplegraphics.mouse.MouseEvent;
import academiadecodigo.simplegraphics.mouse.MouseHandler;

public class PlayerMouse implements MouseHandler {
    private MapEditor mapEditor;
    private StageDisplay stageDisplay;
    private boolean gameStarted;
    private boolean inTutorial;
    public void init(MapEditor mapEditor,StageDisplay stageDisplay){

        this.mapEditor=mapEditor;
        this.stageDisplay=stageDisplay;
        Mouse mouse=new Mouse(this);
        //mouse.addEventListener(MouseEventType.MOUSE_CLICKED);



    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getX()+" x,y "+mouseEvent.getY());
        if(320<mouseEvent.getX() && mouseEvent.getX()<320+150
                && 250<mouseEvent.getY() && mouseEvent.getY()<250+150 && mapEditor.getGameover() && gameStarted){
            mapEditor.getClient().deleteHighScores();

            try{
                Thread.sleep(30);
            }catch (InterruptedException e){}


            mapEditor.setGameover(false);
            mapEditor.display.hideGameover();
            mapEditor.restart();

            //mapEditor=new MapEditor();
           // mapEditor.init();


        }
        if(stageDisplay.startScreenDisplay[2].getX()<mouseEvent.getX()
                && mouseEvent.getX()<stageDisplay.startScreenDisplay[2].getMaxX()
                && stageDisplay.startScreenDisplay[2].getY()<mouseEvent.getY()
                && mouseEvent.getY()<stageDisplay.startScreenDisplay[2].getMaxY() && !gameStarted &&!inTutorial){
            gameStarted=true;
            mapEditor.init();
        }
        if (stageDisplay.startScreenDisplay[3].getX()<mouseEvent.getX()
                && mouseEvent.getX()<stageDisplay.startScreenDisplay[3].getMaxX()
                && stageDisplay.startScreenDisplay[3].getY()<mouseEvent.getY()
                && mouseEvent.getY()<stageDisplay.startScreenDisplay[3].getMaxY()+25 && !gameStarted &&!inTutorial){
            inTutorial=true;
            stageDisplay.showTutorialText();
        }
        if (400<mouseEvent.getX()
                && mouseEvent.getX()<495
                && 0<mouseEvent.getY()
                && mouseEvent.getY()<75 && !gameStarted && inTutorial){
            gameStarted=true;
            stageDisplay.deleteTutorialText();
            mapEditor.init();

        }


    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
