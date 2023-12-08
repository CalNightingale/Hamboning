package hamboning;

import engine.Application;
public class App extends Application {
        private HamboningScreen hs;
        private StartScreen ps;
        public App(String title){
            super(title);
            this.hs = new HamboningScreen(this, this.currentStageSize);
            this.ps = new StartScreen(this, this.currentStageSize, this.hs);
            addScreen(this.hs);
            addScreen(this.ps);
            setActiveScreen(this.ps);
        }
}
