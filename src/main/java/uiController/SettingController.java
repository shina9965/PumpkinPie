package uiController;

import app.BoolEx;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import listener.ActionListener;
import uiModel.SettingModel;
import uiView.SettingView;

public class SettingController implements ActionListener {

    private SettingModel model;
    private SettingView view;

    private boolean isOpened = false;

    public SettingController() {
        model = new SettingModel();
        view = new SettingView(this);

        // 初期化はコンストラクタで一度だけ
        view.initialize(model);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Button source = (Button) event.getSource();
        String buttonId = source.getId();

        BoolEx.ifTrueElse(
            buttonId.equals(model.getAdoptButton().id()),
            () -> onApplyButtonClicked()
        );

        BoolEx.ifTrueElse(
            buttonId.equals(model.getResetButton().id()),
            () -> onResetButtonClicked()
        );

        BoolEx.ifTrueElse(
            buttonId.equals(model.getFinishButton().id()),
            () -> onExitButtonClicked()
        );

        BoolEx.ifTrueElse(
            buttonId.equals(model.getReturnButton().id()),
            () -> onBackButtonClicked()
        );

        BoolEx.ifTrueElse(
            buttonId.equals(model.getCreditButton().id()),
            () -> onCreditButtonClicked()
        );
    }

    public void onApplyButtonClicked() {
        int rate = view.getRateValue();
        model.setAdoptionRate(rate);
        model.applyAdoptionRate();

        view.updateView(model.getAdoptionRate());

        view.showMessage("適用完了", "採用率を " + model.getAdoptionRate() + "% に適用しました。");
    }

    public void onResetButtonClicked() {
        model.resetToDefault();
        view.updateView(model.getAdoptionRate());
        view.showMessage("リセット完了", "採用率を初期値に戻しました。");
    }

    public void onExitButtonClicked() {
        System.exit(0);
    }

    public void onBackButtonClicked() {
        closeSetting();
    }

    public void onCreditButtonClicked() {
        view.showCredit();
    }

    public void openSetting() {
        BoolEx.ifTrueElse(
            !isOpened,
            () -> {
                view.open();
                isOpened = true;
            }
        );
    }

    public void closeSetting() {
        BoolEx.ifTrueElse(
            isOpened,
            () -> {
                view.close();
                isOpened = false;
            }
        );
    }

    public boolean getIsOpened() {
        return isOpened;
    }
}