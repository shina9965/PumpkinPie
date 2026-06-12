package uiModel;

import app.BoolEx;

/**
 * 設定画面で扱うデータを管理するクラス。
 * 採用率を保持する。
 */
public class SettingModel {

    /** デフォルト採用率。 */
    private static final int DEFAULT_ADOPTION_RATE = 10;

    /** システム全体で使う、適用済みの採用率。 */
    private static int appliedAdoptionRate = DEFAULT_ADOPTION_RATE;

    /** 現在、設定画面上で編集中の採用率。 */
    private int adoptionRate;

    /**
     * コンストラクタ。
     * 前回適用された採用率を初期値として使う。
     */
    public SettingModel() {
        this.adoptionRate = appliedAdoptionRate;
    }

    /**
     * 現在編集中の採用率を返す。
     *
     * @return 現在編集中の採用率
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
     * 現在編集中の採用率を、システム全体へ適用する。
     */
    public void applyAdoptionRate() {
        appliedAdoptionRate = adoptionRate;
    }

    /**
     * 適用済みの採用率を返す。
     *
     * @return 適用済みの採用率
     */
    public int getAppliedAdoptionRate() {
        return appliedAdoptionRate;
    }

    /**
     * 他のクラスから適用済み採用率を取得するためのメソッド。
     *
     * @return 適用済みの採用率
     */
    public static int getSystemAdoptionRate() {
        return appliedAdoptionRate;
    }

    /**
     * 採用率をデフォルト値に戻す。
     * この時点では画面上の編集中の値だけを10%に戻す。
     * 実際にシステム全体へ反映するには、適用ボタンを押す必要がある。
     */
    public void resetToDefault() {
        this.adoptionRate = DEFAULT_ADOPTION_RATE;
    }

    /**
     * 適用済みの採用率もデフォルト値に戻す。
     * 必要になった場合に使う。
     */
    public void resetAppliedRateToDefault() {
        appliedAdoptionRate = DEFAULT_ADOPTION_RATE;
        this.adoptionRate = DEFAULT_ADOPTION_RATE;
    }

    /**
     * デフォルト採用率を返す。
     *
     * @return デフォルト採用率
     */
    public int getDefaultAdoptionRate() {
        return DEFAULT_ADOPTION_RATE;
    }
}