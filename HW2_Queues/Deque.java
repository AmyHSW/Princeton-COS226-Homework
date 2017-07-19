import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private int n;
    private Node first, last;

    private class Node {
        private Node pre;
        private Item item;
        private Node next;
    }

    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node newfirst = new Node();
        newfirst.item = item;
        newfirst.next = first;
        if (isEmpty()) last = newfirst;
        else first.pre = newfirst;
        first = newfirst;
        n++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node newlast = new Node();
        newlast.item = item;
        newlast.pre = last;
        if (isEmpty()) first = newlast;
        else last.next = newlast;
        last = newlast;
        n++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        n--;
        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.pre = null;
        }
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        n--;
        if (isEmpty()) {
            last = null;
            first = null;
        } else {
            last = last.pre;
            last.next = null;
        }
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> q = new Deque<String>();
        for (int i = 0; i < 10; i++) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.addFirst(item);
            else if (!q.isEmpty()) StdOut.println(q.removeLast());
        }
        StdOut.println("(" + q.size() + " left on q)");
        for (String item : q) StdOut.print(item + " ");
    }
}
