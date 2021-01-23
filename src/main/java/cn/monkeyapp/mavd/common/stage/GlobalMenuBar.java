package cn.monkeyapp.mavd.common.stage;

import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.controller.AboutController;
import cn.monkeyapp.mavd.controller.NewController;
import cn.monkeyapp.mavd.controller.PreferenceController;
import cn.monkeyapp.mavd.util.OpenBrowserUtils;
import de.codecentric.centerdevice.MenuToolkit;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * mac系统的全局菜单配置
 *
 * @author zhangcong
 */
public class GlobalMenuBar {

    public static void loadGlobalMenuBar() {

        MenuToolkit tk = MenuToolkit.toolkit();

        MenuBar bar = new MenuBar();

        Menu appMenu = new Menu();
        appMenu.getItems().addAll(
                tk.createHideMenuItem(null),
                tk.createHideOthersMenuItem(),
                tk.createUnhideAllMenuItem(),
                new SeparatorMenuItem(),
                tk.createQuitMenuItem(null));

        Menu fileMenu = new Menu("文件");
        MenuItem newItem = new MenuItem("新建");
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.META_DOWN, KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(event -> {
            final Stage stage = new NewController().loadStage(new Stage(), Properties.NEW_FXML_URL);
            stage.setResizable(false);
            stage.show();
            stage.toFront();
        });

        MenuItem settingItem = new MenuItem("偏好设置");
        settingItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.META_DOWN, KeyCombination.CONTROL_DOWN));
        settingItem.setOnAction(e -> {
            final Stage preferenceStage = new PreferenceController().loadStage(new Stage(), Properties.PREFERENCE_FXML_URL);
            preferenceStage.setResizable(false);
            preferenceStage.show();
            preferenceStage.toFront();
        });
        fileMenu.getItems().addAll(newItem, settingItem, new SeparatorMenuItem(), tk.createCloseWindowMenuItem());

        // 窗口菜单
        Menu windowMenu = new Menu("窗口");
        windowMenu.getItems().addAll(tk.createMinimizeMenuItem(), tk.createZoomMenuItem(), tk.createCycleWindowsItem(),
                new SeparatorMenuItem(), tk.createBringAllToFrontItem());

        // 帮助菜单
        Menu helpMenu = new Menu("帮助");
        MenuItem aboutMenuItem = new MenuItem("关于MAVD");
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


        bar.getMenus().addAll(appMenu, fileMenu, windowMenu, helpMenu);

        tk.autoAddWindowMenuItems(windowMenu);
        tk.setGlobalMenuBar(bar);
    }
}
