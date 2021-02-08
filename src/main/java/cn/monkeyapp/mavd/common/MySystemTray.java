package cn.monkeyapp.mavd.common;

import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.manage.StageHelper;
import cn.monkeyapp.mavd.controller.AboutController;
import cn.monkeyapp.mavd.controller.MainController;
import cn.monkeyapp.mavd.controller.NewController;
import cn.monkeyapp.mavd.controller.PreferenceController;
import cn.monkeyapp.mavd.util.FileUtils;
import cn.monkeyapp.mavd.util.OpenBrowserUtils;
import cn.monkeyapp.mavd.util.OsInfoUtils;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 系统托盘
 *
 * @author Corbett Zhang
 */
public class MySystemTray {

    private static final Logger LOGGER = LogManager.getLogger(MySystemTray.class);
    private static final SystemTray TRAY = SystemTray.getSystemTray();
    private static TrayIcon trayIcon;

    private MySystemTray() {
    }

    private static class SingletonContainer {
        private static final MySystemTray INSTANCE = new MySystemTray();
    }

    public static MySystemTray getInstance() {
        return MySystemTray.SingletonContainer.INSTANCE;
    }

    public void enableTray(Stage stage, MainController mainController) {
        doEnableTray(stage, mainController);
    }

    public static void destroyTray() {
        TRAY.remove(trayIcon);
    }

    /**
     * 初始化系统托盘
     *
     * @param stage          为stage添加系统托盘
     * @param mainController 主窗体
     */
    private void doEnableTray(Stage stage, MainController mainController) {
        PopupMenu popupMenu = new PopupMenu();

        MenuItem openItem = new MenuItem("打开主界面", new MenuShortcut(KeyEvent.VK_R, false));
        openItem.addActionListener(e -> Platform.runLater(() -> {
            final Stage loadStage = mainController.loadStage(stage, MainController.class.getName());
            loadStage.show();
            loadStage.toFront();
        }));

        popupMenu.add(openItem);
        popupMenu.addSeparator();

        if (OsInfoUtils.isWindows()) {


            MenuItem newItem = new MenuItem("新建");
            newItem.addActionListener(event -> {
                StageHelper.showStage(false, new NewController(), Properties.NEW_FXML_URL);
            });

            MenuItem settingItem = new MenuItem("偏好设置");
            settingItem.addActionListener(e -> {
                StageHelper.showStage(false, new PreferenceController(), Properties.PREFERENCE_FXML_URL);
            });

            // 帮助菜单
            MenuItem aboutMenuItem = new MenuItem("关于");
            aboutMenuItem.addActionListener(event -> {
                StageHelper.showStage(false, new AboutController(), Properties.ABOUT_FXML_URL);
            });

            MenuItem updateMenuItem = new MenuItem("检查更新");
            updateMenuItem.addActionListener(event -> {
                OpenBrowserUtils.openUrl("https://github.com/corbettzhang/MAVD/releases/latest");
            });

            MenuItem supportMenuItem = new MenuItem("意见反馈");
            supportMenuItem.addActionListener(event -> {
                OpenBrowserUtils.openUrl("https://monkeyapp.cn/contacts");
            });

            popupMenu.add(newItem);
            popupMenu.add(settingItem);
            popupMenu.addSeparator();

            popupMenu.add(aboutMenuItem);
            popupMenu.add(updateMenuItem);
            popupMenu.add(supportMenuItem);
            popupMenu.addSeparator();
        }


        MenuItem quitItem = new MenuItem("退出");
        quitItem.addActionListener(e -> {
            TRAY.remove(trayIcon);
            Platform.runLater(mainController::exit);
        });

        popupMenu.add(quitItem);

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/logo.png")));
            trayIcon = new TrayIcon(image, FileUtils.getAppProperties(FileUtils.APP_NAME), popupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (OsInfoUtils.isWindows()) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            Platform.runLater(() -> {
                                stage.setIconified(false);
                                stage.show();
                                stage.toFront();
                            });
                        }
                    } else if (OsInfoUtils.isMacOS0()) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            Platform.runLater(() -> {
                                stage.setIconified(false);
                                stage.show();
                                stage.toFront();
                            });
                        }
                    }
                    super.mouseClicked(e);
                }
            });
            TRAY.add(trayIcon);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    public static void sendNotification(String title, String message, TrayIcon.MessageType messageType) {
        if (!SystemTray.isSupported()) {
            LOGGER.log(Level.WARNING, "System tray not supported!");
            return;
        }
        trayIcon.displayMessage(title, message, messageType);
    }

}
