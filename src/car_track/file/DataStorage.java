package car_track.file;

import java.util.ArrayList;

public class DataStorage {
    private ArrayList<Integer> data;
    private ArrayList<DataStorage> subStorages;
    //TODO 5 bits allocated to each value to determine bit length of value (max 32 bits per value)
    public DataStorage() {
        data = new ArrayList<>();
        subStorages = new ArrayList<>();
    }

    //TODO optimize this method
    public DataStorage fromString(String string) {
        clear();
        clearSubStorage();
        int[] values = new int[string.length()/4];

        for (int i = 0; i < values.length; i++) {
            int m = i*4;
            values[i] = fromChars(string.substring(m, m+4).toCharArray());
        }
        fromString(values, this, 0);
        return this;
    }

    public DataStorage fromStringReadable(String string){
        String[] split = string.split("/");
        int[] values = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            values[i] = Integer.parseInt(split[i]);
        }
        fromString(values, this, 0);
        return this;
    }

    private int fromString(int[] values, DataStorage storage, int index){
        int dataLength = values[index], subStorages = values[index+1];
        int endIndex = index+dataLength+2;
        for (int i = index+2; i < endIndex; i++) {
            storage.add(values[i]);
        }

        for (int i = 0; i < subStorages; i++) {
            DataStorage subStorage = new DataStorage();
            endIndex = fromString(values, subStorage, endIndex);
            storage.addSubStorage(subStorage);
        }
        return endIndex;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(toChars(data.size()));
        out.append(toChars(subStorages.size()));
        for (Integer value : data) {
            out.append(toChars(value));
        }
        for (DataStorage subStorage : subStorages) {
            out.append(subStorage.toString());
        }
        return out.toString();
    }

    public String toStringReadeable(){
        StringBuilder out = new StringBuilder();
        for(Integer integer : data){
            out.append(integer).append("/");
        }
        for(DataStorage storage : subStorages){
            out.append(storage.toStringReadeable());
        }
        return out.toString();
    }

    private char[] toChars(int value) {
        int n = value >> 24;
        value -= n<<24;
        int n1 = value >> 16;
        value -= n1<<16;
        int n2 = value >> 8;
        return new char[]{
                (char) n, (char) n1, (char) n2, (char) (value - (n2<<8))
        };
    }

    private int fromChars(char[] chars) {
        return (chars[0] << 24) + (chars[1] << 16) + (chars[2] << 8) + chars[3];
    }

    public int get(int i){
        for (int j = data.size(); j <= i; j++) {
            data.add(0);
        }
        return data.get(i);
    }

    public float getFloat(int i){
        return Float.intBitsToFloat(get(i));
    }

    public DataStorage add(int data){
        this.data.add(data);
        return this;
    }

    public DataStorage addFloat(float data){
        return add(Float.floatToIntBits(data));
    }

    public DataStorage set(int index, int data){
        for (int i = this.data.size(); i <= index; i++) {
            this.data.add(0);
        }
        this.data.set(index, data);
        return this;
    }

    public DataStorage setFloat(int index, float data){
        return set(index, Float.floatToIntBits(data));
    }

    public DataStorage remove(int index){
        for (int i = data.size(); i <= index; i++) {
            data.add(0);
        }
        data.remove(index);
        return this;
    }

    public DataStorage clear(){
        this.data.clear();
        return this;
    }

    public int size(){
        return data.size();
    }

    public DataStorage getSubStorage(int storage){
        for (int i = subStorages.size(); i <= storage; i++) {
            subStorages.add(new DataStorage());
        }
        return subStorages.get(storage);
    }

    public DataStorage addSubStorage(DataStorage storage){
        subStorages.add(storage);
        return this;
    }

    public DataStorage setSubStorage(int index, DataStorage storage){
        for (int i = subStorages.size(); i <= index; i++) {
            subStorages.add(new DataStorage());
        }
        subStorages.set(index, storage);
        return this;
    }

    public DataStorage removeSubStorage(DataStorage storage){
        subStorages.remove(storage);
        return this;
    }

    public DataStorage clearSubStorage(){
        subStorages.clear();
        return this;
    }

    public int subStorageSize(){
        return subStorages.size();
    }
}