package hamboning;

import engine.support.FXApplication;
import engine.support.FXFrontEnd;

public class Main {
    public static void main(String[] args) {
        FXFrontEnd app = new App("Hamboning");
        FXApplication application = new FXApplication();
        application.begin(app);
    }
}
