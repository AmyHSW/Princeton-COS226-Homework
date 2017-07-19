import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private int n;
    private Node first, last;

    private class Node {
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
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        if (isEmpty()) last = first;
        n++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        n++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) last = null;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Node newlast = first;
        if (newlast.next == null) {
            newlast = null;
            first = null;
        } else {
            while (newlast.next.next != null) {
                newlast = newlast.next;
            }
            newlast.next = null;
        }
        Item item = last.item;
        last = newlast;
        n--;
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
