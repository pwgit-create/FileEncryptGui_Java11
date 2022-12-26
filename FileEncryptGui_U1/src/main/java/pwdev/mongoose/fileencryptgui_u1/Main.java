package pwdev.mongoose.fileencryptgui_u1;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        MyLaunch myLaunch = new MyLaunch();
        myLaunch.start(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
