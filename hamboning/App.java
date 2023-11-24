package hamboning;

import engine.Application;
public class App extends Application {
        private HamboningScreen hs;
        public App(String title){
            super(title);
            this.hs = new HamboningScreen(this, this.currentStageSize);
            addScreen(this.hs);
            setActiveScreen(this.hs);
        }
}
