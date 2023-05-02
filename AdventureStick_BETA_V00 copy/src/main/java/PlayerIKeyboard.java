import academiadecodigo.simplegraphics.keyboard.Keyboard;
import academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import academiadecodigo.simplegraphics.keyboard.KeyboardHandler;

public class PlayerIKeyboard implements KeyboardHandler {
    MapEditor mapEditor;

    Client client;
    boolean isRpressed;

    public void init(MapEditor mapEditor) {
        this.mapEditor=mapEditor;
        Keyboard keyboard = new Keyboard(this);

        KeyboardEvent grow = new KeyboardEvent();
        grow.setKey(KeyboardEvent.KEY_SPACE);
        grow.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(grow);

        KeyboardEvent jump = new KeyboardEvent();
        jump.setKey(KeyboardEvent.KEY_UP);
        jump.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(jump);

        KeyboardEvent finishGrow = new KeyboardEvent();
        finishGrow.setKey(KeyboardEvent.KEY_SPACE);
        finishGrow.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);

        keyboard.addEventListener(finishGrow);

        KeyboardEvent restart = new KeyboardEvent();
        restart.setKey(KeyboardEvent.KEY_R);
        restart.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(restart);

        KeyboardEvent useAbility = new KeyboardEvent();
        useAbility.setKey(KeyboardEvent.KEY_E);
        useAbility.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(useAbility);


    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        if(keyboardEvent.getKey()==KeyboardEvent.KEY_R && mapEditor.getGameover() && !isRpressed){
            isRpressed=true;
            //mapEditor.getClient().deleteHighScores();

            try{
                Thread.sleep(30);
            }catch (InterruptedException e){}


            mapEditor.setGameover(false);
            mapEditor.display.hideGameover();
            mapEditor.restart();


        }
        if(keyboardEvent.getKey()==KeyboardEvent.KEY_UP && MapEditor.getCanJump){
            MapEditor.getCanJump=false;
            mapEditor.jump();
        }

        if (keyboardEvent.getKey() == KeyboardEvent.KEY_SPACE && mapEditor.getCanPress()&& !mapEditor.getGameover()) {
            mapEditor.setCanPress(false); //so it doesnt bug when continuing pressing sapce
            mapEditor.setCanGrow(true); //set the inical growth stage so it can grow
            try {
                mapEditor.growLine();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(keyboardEvent.getKey()==KeyboardEvent.KEY_E && MapEditor.specialAbilities.size()>0 && !mapEditor.getGameover()){
            mapEditor.useSpecialBall();
        }

    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.getKey() == KeyboardEvent.KEY_SPACE && mapEditor.getCanGrow() ) {
            //mapEditor.setCanPress(false);
            mapEditor.setCanGrow(false); //stops line growth
    }
}}
