package car_track.position;

import java.util.ArrayList;

public class AtomicList <T> {
    private ArrayList<T> added, removed, list;
    public AtomicList(){
        added = new ArrayList<>();
        removed = new ArrayList<>();
        list = new ArrayList<>();
    }

    public T get(int i){
        return list.get(i);
    }

    public void add(T object){
        added.add(object);
    }

    public void remove(T object){
        removed.add(object);
    }

    public void update(){
        list.addAll(added);
        list.removeAll(removed);
        added.clear();
        removed.clear();
    }

    public int size(){
        return list.size();
    }
}
