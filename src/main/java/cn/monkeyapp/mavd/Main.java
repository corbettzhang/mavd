package cn.monkeyapp.mavd;

import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.init.LibraryHelper;
import cn.monkeyapp.mavd.controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Main Class  应用程序主启动类
 *
 * @author Corbett Zhang
 */
public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // 如果该属性为真，JavaFX运行时将在最后一个窗口关闭时隐式关闭;JavaFX启动程序将调用应用程序方法并终止JavaFX应用程序线程。
        // 如果此属性为false，则应用程序将继续正常运行，甚至在关闭最后一个窗口之后，直到应用程序调用exit。缺省值为true。
        // 这个方法可以从任何线程调用
        Platform.setImplicitExit(false);

        // 加载并启动程序
        Platform.runLater(() -> new LoginController().loadStage(primaryStage, Properties.LOGIN_FXML_URL).show());

        LibraryHelper.initialize();

    }

}
