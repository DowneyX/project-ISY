package isy.team4.projectisy.model.game;

public interface IGameObserver {
    void onStarted();
    void onUpdate();
    void onFinished();
    void onIllegal();
}
