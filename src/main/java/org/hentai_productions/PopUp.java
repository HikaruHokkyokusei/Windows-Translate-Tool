package org.hentai_productions;

import com.darkprograms.speech.translator.GoogleTranslate;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.Scanner;

public class PopUp {

    private final Stage popUpWindowStage;
    private static String targetLanguage, sourceLanguage;
    private static String translatedInput;

    PopUp() {
        setLanguage();
        popUpWindowStage = new Stage() {
            @Override
            public void close() {
                System.out.println("Closing the PopUp Window");
                super.close();
            }
        };
        popUpWindowStage.initStyle(StageStyle.UTILITY);
        popUpWindowStage.initModality(Modality.NONE);
        popUpWindowStage.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (aBoolean && !t1) {
                popUpWindowStage.close();
            }
        });
    }

    public void display(String input) throws IOException {

        if(popUpWindowStage.isShowing()) {
            popUpWindowStage.close();
        }

        System.out.println("Performing Translation on clipboard Data : \"" + input + "\"");

        popUpWindowStage.setTitle("Translation");

        Label translatedLabel = new Label("Translated Text (2 Clicks to Copy) :-");
        TextFlow translatedText = new TextFlow();
        Label originalLabel = new Label("Original Text :-");
        TextFlow originalText = new TextFlow();
        Label emptyLabel = new Label("");

        String inputLang = GoogleTranslate.detectLanguage(input);
        System.out.println("Detected Language : " + inputLang);

        if (inputLang.equalsIgnoreCase(targetLanguage)) {
            translatedInput = GoogleTranslate.translate(targetLanguage, sourceLanguage, input);
        } else {
            translatedInput = GoogleTranslate.translate(targetLanguage, input);
        }
        translatedText.getChildren().addAll(new Text(translatedInput));
        translatedText.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(translatedInput);
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        }));
        originalText.getChildren().addAll(new Text(input));

        String parentVBoxLayout = "-fx-border-color: black;\n" +
                "-fx-border-radius: 10 10 10 10;\n" +
                "-fx-border-insets: 2;\n" +
                "-fx-border-width: 1;\n";

        String childVBoxLayout = "-fx-border-color: red;\n" +
                "-fx-border-radius: 1 1 1 1;\n" +
                "-fx-border-insets: 2;\n" +
                "-fx-border-width: 0.5;\n";

        VBox parentVBox = new VBox(10);
        Scene scene = new Scene(parentVBox, 250, 400);
        ScrollPane translatedScrollPane = new ScrollPane(), originalScrollPane = new ScrollPane();

        parentVBox.setStyle(parentVBoxLayout);
        parentVBox.setFillWidth(true);
        translatedScrollPane.setStyle(childVBoxLayout);
        translatedScrollPane.prefHeightProperty().bind(parentVBox.heightProperty());
        translatedScrollPane.setFitToWidth(true);
        originalScrollPane.setStyle(childVBoxLayout);
        originalScrollPane.prefHeightProperty().bind(parentVBox.heightProperty());
        originalScrollPane.setFitToWidth(true);

        parentVBox.getChildren().addAll(translatedLabel, translatedScrollPane, originalLabel, originalScrollPane, emptyLabel);
        translatedScrollPane.setContent(translatedText);
        originalScrollPane.setContent(originalText);
        parentVBox.setAlignment(Pos.CENTER);

        popUpWindowStage.setScene(scene);
        popUpWindowStage.setAlwaysOnTop(true);
        popUpWindowStage.setResizable(false);
        popUpWindowStage.show();
        popUpWindowStage.toFront();
        popUpWindowStage.requestFocus();
    }

    public void setLanguage() {
        try {
            targetLanguage = "en";
            sourceLanguage = "ja";
//            Scanner scanner = new Scanner(new FileInputStream("targetLanguage.txt"));
//            targetLanguage = scanner.nextLine().trim();
//            scanner = new Scanner(new FileInputStream("sourceLanguage.txt"));
//            sourceLanguage = scanner.nextLine().trim();
//            System.out.println("Languages set to : " + sourceLanguage + ", " + targetLanguage);
//            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}