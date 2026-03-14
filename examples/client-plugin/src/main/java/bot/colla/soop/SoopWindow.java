package bot.colla.soop;

import com.avrix.ui.NanoColor;
import com.avrix.ui.notify.Notify;
import com.avrix.ui.widgets.*;

import java.util.Locale;

public class SoopWindow extends WindowWidget {
    private final LabelWidget statusLabel;
    private final ButtonWidget loginBtn;
    private final ButtonWidget logoutBtn;
    private final ButtonWidget startBtn;
    private final ButtonWidget stopBtn;
    private final InputTextWidget testInput;

    public SoopWindow(int x, int y, boolean debug) {
        super("SOOP API", x, y, 125, debug ? 300 : 225);
        setResizable(true);
        setBorderRadius(8);
        setDraggable(true);

        VerticalBoxWidget vb = new VerticalBoxWidget(10, getHeaderHeight() + 10, 0, 0, true);
        addChild(vb);

        statusLabel = new LabelWidget("상태: ", fontName, 0, 0, 100, 14, NanoColor.WHITE);
        loginBtn = new ButtonWidget("로그인", 0, 0, 100, 32, 0, NanoColor.BABY_BLUE, this::login);
        logoutBtn = new ButtonWidget("로그아웃", 0, 0, 100, 32, 0, NanoColor.BABY_BLUE, this::logout);
        startBtn = new ButtonWidget("시작", 0, 0, 100, 32, 0, NanoColor.BABY_BLUE, this::start);
        stopBtn = new ButtonWidget("중지", 0, 0, 100, 32, 0, NanoColor.BABY_BLUE, this::stop);

        loginBtn.setDrawBorder(false);
        logoutBtn.setDrawBorder(false);
        startBtn.setDrawBorder(false);
        stopBtn.setDrawBorder(false);

        vb.addChild(statusLabel);
        vb.addChild(loginBtn);
        vb.addChild(logoutBtn);
        vb.addChild(startBtn);
        vb.addChild(stopBtn);

        if (debug) {
            testInput = new InputTextWidget(0, 0, 100, 30);
            testInput.setPlaceholder("별풍선 개수");
            testInput.setDrawBorder(false);
            vb.addChild(testInput);

            ButtonWidget testBtn = new ButtonWidget("테스트", 0, 0, 100, 32, 0, NanoColor.BABY_BLUE, this::test);
            testBtn.setDrawBorder(false);
            vb.addChild(testBtn);
        } else {
            testInput = null;
        }
    }

    @Override
    public void update() {
        super.update();
        boolean connected = false;
        boolean started = false;
        boolean hasAuth = false;
        statusLabel.setText(connected
                ? "상태: 연결됨"
                : started
                ? "상태: 연결 끊김"
                : "상태: 중지됨");
        if (!hasAuth) {
            loginBtn.setEnable(true);
            logoutBtn.setEnable(false);
            startBtn.setEnable(false);
            stopBtn.setEnable(false);
        } else {
            loginBtn.setEnable(false);
            logoutBtn.setEnable(true);
            if (!started) {
                startBtn.setEnable(true);
                stopBtn.setEnable(false);
            } else {
                startBtn.setEnable(false);
                stopBtn.setEnable(true);
            }
        }
    }

    private void login() {
        Notify.success("로그인 하였습니다.", 5);
    }

    private static String[] getOpenUrlArguments(String url) {
        String string = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (string.contains("win")) {
            return new String[]{"rundll32", "url.dll,FileProtocolHandler", url};
        } else if (string.contains("mac")) {
            return new String[]{"open", url};
        } else {
            return new String[]{"xdg-open", url};
        }
    }

    private void logout() {
        Notify.success("로그아웃 하였습니다.", 5);
    }

    private void start() {
        Notify.success("별풍선 연동을 시작합니다.", 5);
    }

    private void stop() {
        Notify.success("별풍선 연동을 중지합니다.", 5);
    }

    private void test() {
        int count = Integer.parseInt(testInput.getValue());
        Soop.QUEUE.offer(new ChatPacket(ChatPacket.Type.BALLOON, "테스트", count));
    }
}
