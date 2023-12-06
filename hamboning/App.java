package hamboning;

import engine.Application;
public class App extends Application {
        private HamboningScreen hs;
        private PauseScreen ps;
        public App(String title){
            super(title);
            this.hs = new HamboningScreen(this, this.currentStageSize);
            this.ps = new PauseScreen(this, this.currentStageSize, this.hs);
            addScreen(this.hs);
            addScreen(this.ps);
            setActiveScreen(this.ps);
        }
}
