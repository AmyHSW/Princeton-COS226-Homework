import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private int n;
    private Node dummy;

    private class Node {
        private Node prev;
        private Item item;
        private Node next;
    }

    public Deque() {
        dummy = new Node();;
        dummy.prev = dummy;
        dummy.next = dummy;
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
        Node node = new Node();
        node.item = item;
        node.next = dummy.next;
        dummy.next = node;
        node.prev = dummy;
        node.next.prev = node;
        n++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node node = new Node();
        node.item = item;
        node.next = dummy;
        dummy.prev.next = node;
        node.prev = dummy.prev;
        dummy.prev = node;
        n++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = dummy.next.item;
        dummy.next = dummy.next.next;
        dummy.next.prev = dummy;
        n--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = dummy.prev.item;
        dummy.prev = dummy.prev.prev;
        dummy.prev.next = dummy;
        n--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = dummy.next;

        public boolean hasNext() {
            return current != dummy;
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
