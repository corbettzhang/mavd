package cn.monkeyapp.mavd.common;

import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.controller.MainController;
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

        MenuItem quitItem = new MenuItem("退出");
        quitItem.addActionListener(e -> {
            TRAY.remove(trayIcon);
            Platform.runLater(mainController::exit);
        });

        popupMenu.add(openItem);
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
