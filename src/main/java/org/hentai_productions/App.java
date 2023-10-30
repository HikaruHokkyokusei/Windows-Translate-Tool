package org.hentai_productions;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static PopUp translatePopUp;
    private static GlobalKeyboardHook globalKeyboardHook;
    private static boolean isCtrlPressed = false, isAltPressed = false, isShiftPressed = false, isTPressed = false;
    private static Clipboard clipboard;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Operation System : " + System.getProperty("os.name"));
        scene = new Scene(loadFXML("primary"), 640, 480);
        translatePopUp = new PopUp();

        // Adding System Tray Icon for the Application and Menu Items in the tray pop-up
        FXTrayIcon fxTrayIcon = new FXTrayIcon(stage, getClass().getResource("/Translate Icon.png"));
        fxTrayIcon.show();
        fxTrayIcon.addExitItem(true);
        MenuItem testMenuItem = new MenuItem("Test Menu Item");
        testMenuItem.setOnAction(e -> System.out.println("Test Menu Item clicked..."));
        fxTrayIcon.addMenuItem(testMenuItem);

        clipboard = Clipboard.getSystemClipboard();

        // Setting Up Global KeyListener.
        try {
            globalKeyboardHook = new GlobalKeyboardHook(true);
            globalKeyboardHook.addKeyListener(new GlobalKeyAdapter() {

                @Override
                public void keyPressed(GlobalKeyEvent event) {
                    int keyCode = event.getVirtualKeyCode();
                    if (keyCode == GlobalKeyEvent.VK_MENU) {
                        isAltPressed = true;
                    } else if(keyCode == GlobalKeyEvent.VK_CONTROL) {
                        isCtrlPressed = true;
                    } else if (keyCode == GlobalKeyEvent.VK_SHIFT) {
                        isShiftPressed = true;
                    } else if (keyCode == GlobalKeyEvent.VK_T) {
                        isTPressed = true;
                    }

                    if(isShiftPressed && isCtrlPressed && isAltPressed && isTPressed) {
                        Platform.runLater(() -> performTranslation());
                    }
                }

                @Override
                public void keyReleased(GlobalKeyEvent event) {
                    int keyCode = event.getVirtualKeyCode();
                    if (keyCode == GlobalKeyEvent.VK_MENU) {
                        isAltPressed = false;
                    } else if(keyCode == GlobalKeyEvent.VK_CONTROL) {
                        isCtrlPressed = false;
                    } else if (keyCode == GlobalKeyEvent.VK_SHIFT) {
                        isShiftPressed = false;
                    } else if (keyCode == GlobalKeyEvent.VK_T) {
                        isTPressed = false;
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Setting GlobalKeyboardHook Error...");
            e.printStackTrace();
        }

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Closing the Application");
        if(globalKeyboardHook != null) {
            globalKeyboardHook.shutdownHook();
        }
        super.stop();
    }


    public void performTranslation() {
        try {
            translatePopUp.display(clipboard.getString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}