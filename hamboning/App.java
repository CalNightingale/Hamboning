package hamboning;

import engine.Application;
import javafx.scene.input.KeyEvent;

public class App extends Application {
    private HamboningScreen hs;
    private StartScreen ps;
    public App(String title){
        super(title);
        this.hs = new HamboningScreen(this, this.currentStageSize);
        this.ps = new StartScreen(this, this.currentStageSize, this.hs);
        this.hs.setMenuScreen(this.ps);
        addScreen(this.hs);
        addScreen(this.ps);
        setActiveScreen(this.ps);
    }
    @Override
    public void onKeyPressed(KeyEvent e) {
        this.activeScreen.onKeyPressed(e);
    }
}
