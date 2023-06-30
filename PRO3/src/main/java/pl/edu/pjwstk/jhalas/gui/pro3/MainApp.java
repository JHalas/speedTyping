package pl.edu.pjwstk.jhalas.gui.pro3;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class MainApp extends Application {

    private ExampleText exampleText;
    private Label promptLabel;
    private Label weclome;
    private Label shortcutLabel ;
    private Label instruction;

    private Label resultLabel;
    VBox vBox;
    VBox vBoxForTestLine;


    @Override
    public void start(Stage stage) {
        Label shortCut = new Label("ENABLED SHORTCUT");
        shortCut.setStyle("-fx-font-size: 25px;");
        shortCut.setTextFill(Color.YELLOW);
        shortCut.setAlignment(Pos.CENTER);
        shortcutLabel = new Label(" tab + enter = restart test      crtl + shift + p = pause      esc = end test ") ;
        shortcutLabel.setStyle("-fx-font-size: 25px;");
        shortcutLabel.setTextFill(Color.YELLOW);
        shortcutLabel.setAlignment(Pos.CENTER);
        instruction = new Label("TO start select the language in the menu. test time is set to 30 seconds if you want you can change it in the menu. GOOD LUCK");
        instruction.setStyle("-fx-font-size: 15px;");
        instruction.setTextFill(Color.YELLOW);
        resultLabel = new Label();
        resultLabel.setAlignment(Pos.CENTER);
        vBoxForTestLine = new VBox();
        exampleText = new ExampleText(vBoxForTestLine);
        MenuView menuBar = new MenuView();

        vBox = new VBox(menuBar.getMenuBar());
        TypingController typingController = new TypingController(exampleText, vBoxForTestLine, resultLabel);
        menuBar.setLanguage(exampleText, vBoxForTestLine, typingController);
        System.out.println(exampleText.toString());
        menuBar.setMenuBarProperties();
        menuBar.setTime(typingController);
        menuBar.addMenu();

        weclome = new Label();
        weclome.setStyle("-fx-font-size: 50px");
        weclome.setAlignment(Pos.TOP_CENTER);
        promptLabel = new Label("Type the following text:");
        promptLabel.setStyle("-fx-font-size: 40px;");
        MexicanWaveAnimation.startMexicanWaveAnimation(weclome, "Welcome :D");

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setStyle("-fx-background-color: rgba(83,64,163,0.73)");
        vBox.getChildren().addAll(weclome, instruction, promptLabel, vBoxForTestLine, resultLabel ,shortCut, shortcutLabel);

        Scene scene = new Scene(vBox, 900,600 );
        scene.setOnKeyPressed(typingController::handleKeyPress);
        stage.setTitle("typing");
        stage.setScene(scene);
        scene.getRoot().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
