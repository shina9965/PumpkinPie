package uiModel;

import app.BoolEx;

/**
 * 設定画面で扱うデータを管理するクラス。
 * 採用率を保持する。
 */
public class SettingModel {

    /** デフォルト採用率。 */
    private final int defaultAdoptionRate = 10;

    /** 現在の採用率。 */
    private int adoptionRate;

    /**
     * コンストラクタ。
     * 初期値としてデフォルト採用率を設定する。
     */
    public SettingModel() {
        this.adoptionRate = defaultAdoptionRate;
    }

    /**
     * 現在の採用率を返す。
     *
     * @return 現在の採用率
     */
    public int getAdoptionRate() {
        return adoptionRate;
    }

    /**
     * 採用率を設定する。
     * 0〜100以外の場合は例外を投げる。
     *
     * @param value 設定したい採用率
     */
    public void setAdoptionRate(int value) {
        BoolEx.ifTrueElse(
                value < 0 || value > 100,
                () -> {
                    throw new IllegalArgumentException("採用率は0〜100の範囲で入力してください。");
                }
        );

        this.adoptionRate = value;
    }

    /**
     * 採用率をデフォルト値に戻す。
     */
    public void resetToDefault() {
        this.adoptionRate = defaultAdoptionRate;
    }

    /**
     * デフォルト採用率を返す。
     *
     * @return デフォルト採用率
     */
    public int getDefaultAdoptionRate() {
        return defaultAdoptionRate;
    }
}