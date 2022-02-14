package lesson8;

import java.util.Hashtable;
import java.util.Objects;

public class HashTableChainImpl<K, V> implements HashTable<K, V> {

    private final Item<K, V>[] data;
    private int size;

    static class Item<K, V> implements Entry<K, V> {
        private final K key;
        private V value;
        Item<K,V> next;

        public Item(K key, V value, Item<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Item{" + "key=" + key + ", value=" + value + ", next=" + next + '}';
        }
    }

    public HashTableChainImpl(int initialCapacity) {
        data = new Item[initialCapacity * 2];
    }

    public HashTableChainImpl() {
        this(16);
    }

    @Override
    public boolean put(K key, V value) {
        if (size() == data.length) {
            return false;
        }

        int index = hashFunc(key);
        int n = 0;

        if (data[index] == null) {
            data[index] = new Item<>(key, value, null);
        }
        else {
            Item<K, V> tmp = data[index];
            if (isKeysEquals(tmp, key)) {
                tmp.setValue(value);
                return true;
            }
            while (tmp.next != null) {
                tmp = tmp.next;
                if (isKeysEquals(tmp, key)) {
                    tmp.setValue(value);
                    return true;
                }
            }
            tmp.next =  new Item<>(key, value, null);
        }
        size++;
        return true;
    }

    private boolean isKeysEquals(Item<K, V> item, K key) {
         return (item.getKey() == null) ? (key == null) : item.getKey().equals(key);
    }

    private int hashFunc(K key) {
        return Math.abs(key.hashCode() % data.length);
    }

    @Override
    public V get(K key) {
        int index = hashFunc(key);
        Item<K, V> tmp = data[index];
        if (tmp == null) return null;
        if (isKeysEquals(tmp, key)) {
            return tmp.getValue();
        }
        else {
            while (tmp.next != null) {
                tmp = tmp.next;
                if (isKeysEquals(tmp, key)) {
                    return tmp.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int index = hashFunc(key);
        Item<K, V> tmp = data[index];
        if (isKeysEquals(tmp, key)) {
            if (tmp.next==null) {
                data[index] = null;
            }
            else {
                data[index] = tmp.next;
            }
            size--;
            return tmp.getValue();
        }
        else {
            while (tmp.next != null) {
                Item<K, V> prev = tmp;
                tmp = tmp.next;
                if (isKeysEquals(tmp, key)) {
                    if (tmp.next!=null) {
                        prev.next = tmp.next;
                    }
                    else {
                        prev.next = null;
                    }
                    size--;
                    return tmp.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void display() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%s = [%s]%n", i, data[i]));
        }
        return sb.toString();
    }
}
