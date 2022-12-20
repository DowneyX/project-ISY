package isy.team4.projectisy.observer;

public interface IObservable<E> {
    public void addObserver(E o);

    public void removeObserver(E o);
}
