package pl.edu.pjwstk.jhalas.gui.pro3;


import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ExampleText {

    private String exampleText = "This is simple test text :D " ;
    private final VBox vBox;

    public ExampleText(VBox vBox) {
        this.vBox = vBox;
    }


    public void TextViewElement(VBox vBox) {
        vBox.getChildren().clear();
        HBox hboxline = new HBox();
        hboxline.setStyle("-fx-font-size: 20px; -fx-background-color: PlUM; -fx-animated: true;");
        hboxline.setAlignment(Pos.CENTER);

        int wordInLineCount = 0;

        for (String word : exampleText.split(" ")) {
            if (wordInLineCount == 6) {
                vBox.getChildren().add(hboxline);
                hboxline = new HBox();
                hboxline.setStyle("-fx-font-size: 20px; -fx-background-color: PlUM; -fx-animated: true;");
                hboxline.setAlignment(Pos.CENTER);
                wordInLineCount = 0;
            }
            wordInLineCount++;
            HBox hBox = new HBox();
            hBox.setStyle("-fx-font-size: 20px;  -fx-background-color: PlUM; -fx-animated: true ; ");
            hBox.setAlignment(Pos.CENTER);
            Text space = new Text(" ");
            hBox.getChildren().add(space);
            for (char c : word.toCharArray()) {
                Text letter = new Text(String.valueOf(c));
                letter.setFont(Font.font("Arial", 20));
                letter.setFill(Color.GRAY);
                hBox.getChildren().add(letter);
            }
            hboxline.getChildren().add(hBox);
        }
        vBox.getChildren().add(hboxline);
    }

    public void setExampleText(String txt) {
        exampleText = txt;
    }

    @Override
    public String toString() {
        return exampleText;
    }


}
