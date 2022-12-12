package isy.team4.projectisy.observer;

public interface ISubject {
    public void registerObserver(IObserver o);

    public void removeObserver(IObserver o);

    public void notifyObservers(String msg);
}
