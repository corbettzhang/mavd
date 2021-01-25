package cn.monkeyapp.mavd.common;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.controller.AboutController;
import cn.monkeyapp.mavd.controller.MainController;
import cn.monkeyapp.mavd.controller.NewController;
import cn.monkeyapp.mavd.controller.PreferenceController;
import cn.monkeyapp.mavd.entity.Config;
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
     * @param stage 为stage添加系统托盘
     */
    private void doEnableTray(Stage stage, MainController mainController) {
        PopupMenu popupMenu = new PopupMenu();

        MenuItem openItem = new MenuItem("打开主界面", new MenuShortcut(KeyEvent.VK_R, false));
        openItem.addActionListener(e -> Platform.runLater(() -> {
            final Stage loadStage = mainController.loadStage(stage, MainController.class.getName());
            loadStage.show();
            loadStage.toFront();
        }));

        MenuItem openSiteItem = new MenuItem("打开网站");
        openSiteItem.addActionListener(e -> OpenBrowserUtils.openUrl(((Config) LocalCache.getInstance().get(Properties.CONFIG_KEY)).getWebSite()));

        MenuItem quitItem = new MenuItem("退出");
        quitItem.addActionListener(e -> {
            TRAY.remove(trayIcon);
            Platform.runLater(mainController::exit);
        });

        popupMenu.add(openItem);

        if (OsInfoUtils.isWindows()) {

            MenuItem newMenuItem = new MenuItem("新建");
            newMenuItem.addActionListener(event -> {
                Platform.runLater(() -> {
                    final Stage newStage = new NewController().loadStage(new Stage(), Properties.NEW_FXML_URL);
                    newStage.setResizable(false);
                    newStage.show();
                    newStage.toFront();
                });
            });

            MenuItem preferenceMenuItem = new MenuItem("偏好设置");
            preferenceMenuItem.addActionListener(e -> {
                Platform.runLater(() -> {
                    final Stage preferenceStage = new PreferenceController().loadStage(new Stage(), Properties.PREFERENCE_FXML_URL);
                    preferenceStage.setResizable(false);
                    preferenceStage.show();
                    preferenceStage.toFront();
                });
            });

            MenuItem aboutMenuItem = new MenuItem("关于MAVD");
            aboutMenuItem.addActionListener(event -> {
                Platform.runLater(() -> {
                    final Stage aboutStage = new AboutController().loadStage(new Stage(), Properties.ABOUT_FXML_URL);
                    aboutStage.setResizable(false);
                    aboutStage.show();
                    aboutStage.toFront();
                });
            });

            MenuItem supportMenuItem = new MenuItem("意见反馈");
            supportMenuItem.addActionListener(event -> {
                OpenBrowserUtils.openUrl("https://monkeyapp.cn/contacts");
            });

            popupMenu.addSeparator();
            popupMenu.add(newMenuItem);
            popupMenu.add(preferenceMenuItem);
            popupMenu.add(aboutMenuItem);
            popupMenu.add(supportMenuItem);
            popupMenu.addSeparator();

        }

        popupMenu.add(openSiteItem);
        popupMenu.addSeparator();
        popupMenu.add(quitItem);

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/logo.png")));
            trayIcon = new TrayIcon(image, "MAVD", popupMenu);
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
