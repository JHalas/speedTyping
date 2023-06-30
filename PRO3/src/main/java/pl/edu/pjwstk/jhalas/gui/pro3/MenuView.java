package pl.edu.pjwstk.jhalas.gui.pro3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class MenuView {

    private DictionaryReader dictionaryReader;
    private final MenuBar menuBar = new MenuBar();
    private final Menu languageMenu = new Menu("Wybór języka");
    private final Menu timeMenu = new Menu("Wybór czasu");


    void setMenuBarProperties() {
        menuBar.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        menuBar.setId("menu");
        languageMenu.setId("languageMenu");
        timeMenu.setId("timeMenu");
    }

    void  setTime(TypingController typingController) {
      int[]  tabTime = { 15000, 20000 , 45000 , 60000 , 90000 , 120000 , 300000 } ;
      for (int timee : tabTime) {
          MenuItem timeItem = new MenuItem(timee+" ms");
          timeItem.setId("timeItem");
          timeItem.setOnAction(event  -> {
              System.out.println("Switch time to :" + timee + " ms");
              typingController.setTime(timee);
          });
          timeMenu.getItems().add(timeItem);
      }

    }


    void setLanguage(ExampleText exampleText, VBox vBox , TypingController typingController) {
        dictionaryReader = new DictionaryReader();
        for (String languagename : dictionaryReader.getAvailableLanguageForMenu()) {
            MenuItem languageItem = new MenuItem(languagename.substring(0, languagename.length() - 3));
            languageItem.setId("languageItem");
            languageItem.setOnAction(event -> {
                System.out.println("Switch language " + languagename.substring(0, languagename.length() - 3));
                exampleText.setExampleText(dictionaryReader.getRandomTextFromDictionaryForGivenLanguage(languagename, 30));
                System.out.println(exampleText);
                exampleText.TextViewElement(vBox);
                typingController.setStartProperties();

            });
            languageMenu.getItems().add(languageItem);
        }

    }

    void addMenu() {
        menuBar.getMenus().addAll(languageMenu, timeMenu);
    }

    MenuBar getMenuBar() {
        return menuBar;
    }
}
