package cn.monkeyapp.mavd.common.stage;

import cn.monkeyapp.mavd.common.manage.LogManager;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Logger;

/**
 * <a href="https://github.com/kwhat/jnativehook#software-and-hardware-requirements">JNativeHook</a>是一个提供Java全局键盘和鼠标侦听器的库。这将使您能够侦听全局快捷方式或鼠标移动，而使用纯Java则无法实现。为了完成此任务，JNativeHook通过Java的本机接口利用了依赖于平台的本机代码来创建低级系统范围的钩子，并将这些事件传递给您的应用程序。
 *
 * <b>jna调用外部接口，实现全局键盘，鼠标事件监听</b>
 *
 * @author Corbett Zhang
 */
public class GlobalKeyListener implements NativeKeyListener {

    private static final Logger LOGGER = LogManager.getLogger(GlobalKeyListener.class);
    private Stage stage;

    public GlobalKeyListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        Platform.runLater(() -> {
            if (e.getKeyCode() == NativeKeyEvent.VC_Y) {
                if (stage.isIconified() || !stage.isShowing()) {
                    stage.setIconified(false);
                    stage.show();
                    stage.toFront();
                } else if (stage.isShowing() && !stage.isIconified()) {
                    stage.setIconified(true);
                }
            }
        });
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }


}