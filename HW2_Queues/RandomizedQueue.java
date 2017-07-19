import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;
    private int n;

    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    private void resize(int capacity) {
        if (capacity < n) {
            throw new IllegalArgumentException("Capacity is too small: " + capacity);
        }
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int r = StdRandom.uniform(n);
        Item item = a[r];
        a[r] = a[n - 1];
        a[--n] = null;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int r = StdRandom.uniform(n);
        return a[r];
    }

    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }

    private class RandomArrayIterator implements Iterator<Item> {

        private boolean[] isSelected;
        private int m;

        public RandomArrayIterator() {
            isSelected = new boolean[n];
            m = n;
        }

        public boolean hasNext() {
            return m > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            while (true) {
                int i = StdRandom.uniform(n);
                if (!isSelected[i]) {
                    isSelected[i] = true;
                    m--;
                    return a[i];
                }
            }
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        for (int i = 0; i < 10; i++) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.enqueue(item);
            else if (!q.isEmpty()) StdOut.println(q.dequeue());
        }
        StdOut.println("(" + q.size() + " left on q)");
        StdOut.println("Samples: " + q.sample());
        for (String item : q) StdOut.print(item + " ");
    }
}
