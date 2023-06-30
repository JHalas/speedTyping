package pl.edu.pjwstk.jhalas.gui.pro3;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;


public class TypingController {
    private final ExampleText exampleText;
    private boolean tabPressed = false;

    private final DictionaryReader dictionaryReader = new DictionaryReader();

    private final VBox vBox;
    private long startTimee;
    private long startTime;
    private long testTime = 30000;
    private long pauseTime;
    private long totalPauseTime;
    private Long pauseStartTime;
    private boolean isPaused = false;
    private Label resultLabel;
    private int charactersTyped;
    private int totalcharacterTyped;
    private int mistakesMade;
    ;
    int typedCount = 1;
    int wordCount = 0;
    private final List<Integer> wpmList = new ArrayList<>();
    private final List<Long> timeList = new ArrayList<>();

    public TypingController(ExampleText exampleText, VBox vBox, Label resultLabel) {
        this.exampleText = exampleText;
        this.vBox = vBox;
        this.resultLabel = resultLabel;


        // Inicjalizacja AnimationTimer do aktualizacji WPM co sekundę
        AnimationTimer wpmUpdater = new AnimationTimer() {
            private long previousTime = System.currentTimeMillis();

            @Override
            public void handle(long now) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - previousTime >= 1000) {
                    updateWPM(currentTime);
                    previousTime = currentTime;
                }
            }
        };
        wpmUpdater.start();
    }


    void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            getFinishResult();
        } else if (event.getCode() == KeyCode.TAB) {
            tabPressed = true;
        } else if (event.getCode() == KeyCode.ENTER && tabPressed) {
            if (!isPaused) {
                exampleText.setExampleText(dictionaryReader.getRandomTextFromDictionaryForGivenLanguage("english.txt", 30));
                exampleText.TextViewElement(vBox);
                resultLabel.setText("");
                setStartProperties();
            }
        } else if (event.getCode() == KeyCode.P && event.isControlDown() && event.isShiftDown()) {
            pause();
        } else {
            if (!isPaused) {
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime >= 1000) {
                    updateWPM(currentTime);
                    startTimee = currentTime;
                }
                int lineCount = (wordCount / 6);
                System.out.println(lineCount);
                if (lineCount > 4) {
                    lineCount = 0;
                }
                HBox vwordline = (HBox) vBox.getChildren().get(lineCount);
                int wordPositionInLine = wordCount;
                if (wordPositionInLine >= 6) {
                    wordPositionInLine = wordCount % 6;
                }
                HBox word = (HBox) vwordline.getChildren().get(wordPositionInLine);
                if (event.getCode() == KeyCode.SPACE) {
                    if (typedCount < word.getChildren().size()) {
                        for (int i = typedCount; i <= word.getChildren().size() - 1; i++) {
                            Text letter = (Text) word.getChildren().get(i);
                            letter.setFont(Font.font("Arial", 20));
                            letter.setFill(Color.BLACK);
                            mistakesMade++;
                            charactersTyped++;
                        }
                    }
                    wordCount++;
                    typedCount = 1;
                } else {
                    char pressedChar = event.getText().charAt(0);
                    if (typedCount > word.getChildren().size() - 1) {
                        Text addletter = new Text(String.valueOf(pressedChar));
                        addletter.setFill(Color.ORANGE);
                        addletter.setFont(Font.font("Arial", 20));
                        word.getChildren().add(addletter);
                        mistakesMade++;
                    } else {
                        Text letter = (Text) word.getChildren().get(typedCount);
                        letter.setFont(Font.font("Arial", 20));
                        char currentChar = letter.getText().charAt(0);

                        Timeline jumpAnimation = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(letter.translateYProperty(), 0)),
                                new KeyFrame(Duration.seconds(0.3), new KeyValue(letter.translateYProperty(), -10)),
                                new KeyFrame(Duration.seconds(0.4), new KeyValue(letter.translateYProperty(), 0))
                        );
                        letter.fillProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue.equals(Color.GREEN) || newValue.equals(Color.RED)) {
                                jumpAnimation.playFromStart();
                            }
                        });
                        if (currentChar == pressedChar) {
                            letter.setFill(Color.GREEN);
                        } else if (currentChar != pressedChar) {
                            mistakesMade++;
                            letter.setFill(Color.RED);
                        } else {
                            letter.setFill(Color.BLACK);
                        }

                    }
                    typedCount++;
                }
                tabPressed = false;
                charactersTyped++;

                if (exampleText.toString().length() >= charactersTyped && System.currentTimeMillis() - startTime >= testTime) {
                    if (totalcharacterTyped == 0) {
                        totalcharacterTyped = charactersTyped;
                    }
                    getFinishResult();
                    showWPMChart();
                } else getResult();
                if (charactersTyped >= exampleText.toString().length()) {
                    getNextText();
                }

            }
        }
    }

    private String formatTime(long time) {
        long seconds = time / 1000;
        return String.format("%d seconds", seconds);
    }

    private void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            System.out.println("Test paused");
            resultLabel.setText("TEST PAUSED");
            pauseStartTime = System.currentTimeMillis();
        } else {
            System.out.println("Test resumed");
            resultLabel.setText("");
            pauseTime = System.currentTimeMillis() - pauseStartTime;
            totalPauseTime += pauseTime;
        }

    }

    public void setTime(int timee) {
        testTime = timee;
    }

    public void resetStartTime() {
        startTime = 0;
    }

    public void setStartProperties() {
        resetStartTime();
        typedCount = 1;
        wordCount = 0;
        charactersTyped = 0;
        mistakesMade = 0;
        totalPauseTime = 0;

    }

    public void getResult() {
        double accuracy = ((double) (charactersTyped - mistakesMade) / charactersTyped) * 100;
        double wpm = (charactersTyped / 5.0) / ((System.currentTimeMillis() - startTime - totalPauseTime) / 60000.0);
        String timeInSeconds = formatTime(System.currentTimeMillis() - startTime - totalPauseTime);
        String resultText = String.format(
                "Time: %s ms\nAccuracy: %.2f%%\nWPM: %.2f",
                timeInSeconds, accuracy, wpm);
        resultLabel.setText(resultText);
    }

    public void getFinishResult() {
        double accuracy = ((double) (totalcharacterTyped - mistakesMade) / totalcharacterTyped) * 100;
        double wpm = (charactersTyped / 5.0) / (testTime / 60000.0);
        String timeInSeconds = formatTime(System.currentTimeMillis() - startTime - totalPauseTime);
        String resultText = String.format(
                "Time: %s ms\nAccuracy: %.2f%%\nWPM: %.2f",
                timeInSeconds, accuracy, wpm);
        resultLabel.setText(resultText);
        resultLabel.setStyle("-fx-font-size: 25px;");
        resultLabel.setAlignment(Pos.CENTER);
        vBox.getChildren().clear();
        HBox box = new HBox(resultLabel);


        List<String> words = new ArrayList<>();
        List<String> currentWords = extractWords(exampleText.toString());
        words.addAll(currentWords);

        StatisticWriter.writeStatistics("staty.txt", words, wpmList);

        Label endtesttt = new Label("END TEST");
        endtesttt.setStyle("-fx-font-size: 40 px");
        endtesttt.setAlignment(Pos.CENTER);
        HBox endbox = new HBox(endtesttt);
        endbox.setAlignment(Pos.CENTER);
        box.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(endbox, box);
    }

    public void getNextText() {
        exampleText.setExampleText(dictionaryReader.getRandomTextFromDictionaryForGivenLanguage("polish.txt", 30));
        exampleText.TextViewElement(vBox);
        totalcharacterTyped += charactersTyped;
        typedCount = 1;
        wordCount = 0;
        charactersTyped = 0;
    }

    private void updateWPM(long currentTime) {
        long elapsedTime = currentTime - startTime;
        double wpm = ((charactersTyped / 5.0) / ((double) elapsedTime / 60000.0));
        wpmList.add((int) wpm);
        timeList.add(elapsedTime);
    }

    private void showWPMChart() {
        Stage stage = new Stage();
        stage.setTitle("WPM Chart");

        NumberAxis xAxis = new NumberAxis(0, calculateTotalTime(), 1);  // Ustawienie dolnej granicy, górnej granicy i kroku osi X
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        yAxis.setLabel("WPM");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("WPM Chart");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < timeList.size(); i++) {
            series.getData().add(new XYChart.Data<>(timeList.get(i) / 1000, wpmList.get(i)));
        }

        lineChart.getData().add(series);

// Dodanie statystyk
        double accuracy = calculateAccuracy();
        long totalTime = calculateTotalTime();
        double averageWPM = calculateAverageWPM();

        VBox statsBox = new VBox(
                new Label("Accuracy: " + String.format("%.2f", accuracy) + "%"),
                new Label("Total Time: " + calculateTotalTime() + " seconds"),
                new Label("Average WPM: " + String.format("%.2f", averageWPM))
        );
        statsBox.setSpacing(10);

        VBox root = new VBox(lineChart, statsBox);
        Scene scene = new Scene(root, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private double calculateAccuracy() {
        if (charactersTyped == 0) {
            return 0.0;
        }
        return ((double) (charactersTyped - mistakesMade) / charactersTyped) * 100;
    }

    private long calculateTotalTime() {
        long totalTime = timeList.get(timeList.size() - 1) / 1000; // Czas w sekundach
        return totalTime;
    }

    private double calculateAverageWPM() {
        long totalTime = calculateTotalTime();

        if (totalTime == 0) {
            return 0.0;
        }

        return ((charactersTyped/5.0) / ((double) totalTime / 60));
    }

    private List<String> extractWords(String text) {
        List<String> words = new ArrayList<>();
        String[] splitText = text.split("\\s+");
        for (String word : splitText) {
            // Usuń znaki interpunkcyjne z końca każdego słowa
            String cleanedWord = word.replaceAll("[^\\p{L}\\p{N}]", "");
            if (!cleanedWord.isEmpty()) {
                words.add(cleanedWord);
            }
        }
        return words;
    }
}



