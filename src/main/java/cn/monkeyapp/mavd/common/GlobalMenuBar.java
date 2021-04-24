package cn.monkeyapp.mavd.common;

import cn.monkeyapp.mavd.common.manage.StageHelper;
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

/**
 * 系统菜单配置
 *
 * @author zhangcong
 */
public class GlobalMenuBar {

    /**
     * mac系统的全局菜单配置
     */
    public static void loadMacMenuBar() {
        MenuToolkit tk = MenuToolkit.toolkit();
        MenuBar bar = new MenuBar();
        Menu appMenu = new Menu();
        appMenu.getItems().addAll(
                tk.createHideMenuItem(null),
                tk.createHideOthersMenuItem(),
                tk.createUnhideAllMenuItem(),
                new SeparatorMenuItem(),
                tk.createQuitMenuItem(null));
        // 窗口菜单
        Menu windowMenu = new Menu("窗口");
        windowMenu.getItems().addAll(
                tk.createMinimizeMenuItem(),
                tk.createZoomMenuItem(),
                tk.createCycleWindowsItem(),
                new SeparatorMenuItem(),
                tk.createBringAllToFrontItem(),
                new SeparatorMenuItem(),
                tk.createCloseWindowMenuItem()
        );
        bar.getMenus().addAll(appMenu, getFileMenu(), windowMenu, getHelpMenu());
        tk.autoAddWindowMenuItems(windowMenu);
        tk.setGlobalMenuBar(bar);
    }

    public static Menu getFileMenu() {
        Menu fileMenu = new Menu("文件");
        MenuItem newItem = new MenuItem("新建");
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.META_DOWN, KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(event -> {
            StageHelper.showStage(false, new NewController(), Properties.NEW_FXML_URL);
        });

        MenuItem settingItem = new MenuItem("偏好设置");
        settingItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.META_DOWN, KeyCombination.CONTROL_DOWN));
        settingItem.setOnAction(e -> {
            StageHelper.showStage(false, new PreferenceController(), Properties.PREFERENCE_FXML_URL);
        });
        fileMenu.getItems().addAll(newItem, settingItem);
        return fileMenu;
    }

    public static Menu getHelpMenu() {
        // 帮助菜单
        Menu helpMenu = new Menu("帮助");
        MenuItem aboutMenuItem = new MenuItem("关于");
        aboutMenuItem.setOnAction(event -> {
            StageHelper.showStage(false, new AboutController(), Properties.ABOUT_FXML_URL);
        });
        helpMenu.getItems().add(aboutMenuItem);
        MenuItem updateMenuItem = new MenuItem("检查更新");
        updateMenuItem.setOnAction(event -> {
            OpenBrowserUtils.openUrl("https://github.com/corbettzhang/mavd/releases/latest");
        });
        helpMenu.getItems().add(updateMenuItem);
        MenuItem supportMenuItem = new MenuItem("意见反馈");
        supportMenuItem.setOnAction(event -> {
            OpenBrowserUtils.openUrl("https://github.com/corbettzhang/mavd/issues");
        });
        helpMenu.getItems().add(supportMenuItem);
        return helpMenu;
    }

}
