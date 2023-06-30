package pl.edu.pjwstk.jhalas.gui.pro3;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class MexicanWaveAnimation {


    public static void startMexicanWaveAnimation(Label label , String txt) {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }

        String text = txt;
        int durationPerLetter = 333; // Czas trwania animacji dla każdej litery (w milisekundach)
        int delayPerLetter = 200; // Opóźnienie między animacjami kolejnych liter (w milisekundach)

        HBox letterBox = new HBox();

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            Label letterLabel = createLetterLabel(letter);
            animateLetter(letterLabel, i * delayPerLetter, durationPerLetter);
            letterBox.getChildren().add(letterLabel);
        }

        label.setGraphic(null);
        label.setGraphic(letterBox);
    }

    private static Label createLetterLabel(char letter) {
        Label letterLabel = new Label(Character.toString(letter));
        // Dodaj stylizację do letterLabel (np. rozmiar czcionki, kolor itp.)

        return letterLabel;
    }

    private static void animateLetter(Label letterLabel, int delay, int duration) {
        letterLabel.setTranslateY(-10); // Przesunięcie początkowe na osi Y
        letterLabel.setOpacity(0); // Początkowa przezroczystość

        Timeline timeline = new Timeline();
        KeyFrame startFrame = new KeyFrame(Duration.ZERO,
                new KeyValue(letterLabel.translateYProperty(), -10),
                new KeyValue(letterLabel.opacityProperty(), 0));
        KeyFrame endFrame = new KeyFrame(Duration.millis(duration),
                new KeyValue(letterLabel.translateYProperty(), 0),
                new KeyValue(letterLabel.opacityProperty(), 1));
        KeyFrame resetFrame = new KeyFrame(Duration.ZERO,
                new KeyValue(letterLabel.translateYProperty(), -10),
                new KeyValue(letterLabel.opacityProperty(), 1));

        timeline.getKeyFrames().addAll(startFrame, endFrame, resetFrame);
        timeline.setDelay(Duration.millis(delay));
        timeline.setOnFinished(event -> timeline.playFromStart()); // Zapętlenie animacji
        timeline.play();
    }
}
