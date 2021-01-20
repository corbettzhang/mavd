package cn.monkeyapp.mavd.common.stage;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * 公共显示alert组件
 *
 * @author Corbett Zhang
 */
public class MonkeyAppAlert {

    public static void showAlert(String content, Window window) {
        Platform.runLater(() -> {
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label(content));
            JFXAlert<Void> alert = new JFXAlert<>(window);
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.show();
        });
    }
}
