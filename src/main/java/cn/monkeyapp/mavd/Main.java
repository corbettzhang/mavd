package cn.monkeyapp.mavd;

import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.init.LibraryHelper;
import cn.monkeyapp.mavd.controller.LoginController;
import de.codecentric.centerdevice.MenuToolkit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;

/**
 * Main Class  应用程序主启动类
 *
 * @author Corbett Zhang
 */
public class Main extends Application {
    static final String appName = "Standard";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        loadGlobalMenuBar();

        // 如果该属性为真，JavaFX运行时将在最后一个窗口关闭时隐式关闭;JavaFX启动程序将调用应用程序方法并终止JavaFX应用程序线程。
        // 如果此属性为false，则应用程序将继续正常运行，甚至在关闭最后一个窗口之后，直到应用程序调用exit。缺省值为true。
        // 这个方法可以从任何线程调用
        Platform.setImplicitExit(false);
        // 加载并启动程序
        Platform.runLater(() -> new LoginController().loadStage(primaryStage, Properties.LOGIN_FXML_URL).show());

        LibraryHelper.initialize();

    }

    private void loadGlobalMenuBar() {
        MenuToolkit tk = MenuToolkit.toolkit();

        MenuBar bar = new MenuBar();

        // Application Menu
        // TBD: services menu
        Menu appMenu = new Menu(appName); // Name for appMenu can't be set at
        // Runtime

        MenuItem prefsItem = new MenuItem("Preferences...");
        appMenu.getItems().addAll(prefsItem, new SeparatorMenuItem(),
                tk.createHideMenuItem(appName), tk.createHideOthersMenuItem(), tk.createUnhideAllMenuItem(),
                new SeparatorMenuItem(), tk.createQuitMenuItem(appName));

        // File Menu (items TBD)
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New...");
        fileMenu.getItems().addAll(newItem, new SeparatorMenuItem(), tk.createCloseWindowMenuItem(),
                new SeparatorMenuItem(), new MenuItem("TBD"));

        // Edit (items TBD)
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(new MenuItem("TBD"));

        // Format (items TBD)
        Menu formatMenu = new Menu("Format");
        formatMenu.getItems().addAll(new MenuItem("TBD"));

        // View Menu (items TBD)
        Menu viewMenu = new Menu("View");
        viewMenu.getItems().addAll(new MenuItem("TBD"));

        // Window Menu
        // TBD standard window menu items
        Menu windowMenu = new Menu("Window");
        windowMenu.getItems().addAll(tk.createMinimizeMenuItem(), tk.createZoomMenuItem(), tk.createCycleWindowsItem(),
                new SeparatorMenuItem(), tk.createBringAllToFrontItem());

        // Help Menu (items TBD)
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(new MenuItem("TBD"));

        bar.getMenus().addAll(appMenu, fileMenu, editMenu, formatMenu, viewMenu, windowMenu, helpMenu);

        tk.autoAddWindowMenuItems(windowMenu);
        tk.setGlobalMenuBar(bar);
    }

}
