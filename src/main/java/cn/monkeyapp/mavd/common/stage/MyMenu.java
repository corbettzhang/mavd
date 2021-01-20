package cn.monkeyapp.mavd.common.stage;

import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.controller.AboutController;
import cn.monkeyapp.mavd.controller.AbstractController;
import cn.monkeyapp.mavd.util.OpenBrowserUtils;
import cn.monkeyapp.mavd.util.OsInfoUtils;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 系统菜单
 *
 * @author Corbett Zhang
 */
public class MyMenu {

    public static void showMenu(Stage stage, AbstractController controller) {
        if (!OsInfoUtils.isWindows()) {
            MenuBar menuBar = new MenuBar();
            menuBar.setUseSystemMenuBar(true);
            Menu helpMenu = new Menu("帮助");
            MenuItem aboutMenuItem = new MenuItem("关于");
            aboutMenuItem.setOnAction(event -> {
                final Stage aboutStage = new AboutController().loadStage(new Stage(), Properties.ABOUT_FXML_URL);
                aboutStage.setResizable(false);
                aboutStage.show();
                aboutStage.toFront();
            });
            helpMenu.getItems().add(aboutMenuItem);

            MenuItem supportMenuItem = new MenuItem("意见反馈");
            supportMenuItem.setOnAction(event -> {
                OpenBrowserUtils.openUrl("https://monkeyapp.cn/contacts");
            });
            helpMenu.getItems().add(supportMenuItem);

            menuBar.getMenus().addAll(helpMenu);

            ((Pane) stage.getScene().getRoot()).getChildren().add(menuBar);
        }
    }
}
