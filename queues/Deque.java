import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int sizeCounter;

    private class Node {
        Item item;
        Node next;
        Node before;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        sizeCounter = 0;

    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return sizeCounter;
    }

    // add the item to the front
    public void addFirst(Item item) {

        if (item == null)
            throw new IllegalArgumentException("Cannot call addFirst with null.");

        Node oldfirst = first;
        first = new Node();
        first.item = item;

        if (oldfirst == null) {
            first.next = null;
            first.before = null;
            last = first;
        } else {
            first.next = oldfirst;
            oldfirst.before = first;
        }

        sizeCounter++;

    }

    // add the item to the back
    public void addLast(Item item) {

        if (item == null)
            throw new IllegalArgumentException("Cannot call addLast with null.");

        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.before = oldlast;
        if (isEmpty())
            first = last;
        else
            oldlast.next = last;

        sizeCounter++;

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty, cannot call removeFirst");

        Item item = first.item;
        first = first.next;
        if (isEmpty())
            last = null;
        else
            first.before = null;
        sizeCounter--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty, cannot call removeLast");

        Item item = last.item;
        last = last.before;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        sizeCounter--;
        return item;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("DequeIterator has no next element.");

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported.");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

        Deque<String> d = new Deque<String>();
        System.out.println(d.size());
        d.addFirst("item");
        System.out.println(d.size());
        System.out.println(d.removeLast());
        System.out.println(d.size());
        d.addLast("item1");
        d.addFirst("item2");
        System.out.println(d.size());
        System.out.println(d.removeFirst());
        System.out.println(d.size());
        System.out.println(d.removeFirst());
        System.out.println(d.size());

        System.out.println(d.isEmpty());

        d.addLast("item1");
        d.addFirst("item2");
        d.addLast("item3");
        d.addFirst("item4");

        Iterator<String> dIter = d.iterator();

        System.out.println(dIter.hasNext());
        System.out.println(dIter.next());
        System.out.println(dIter.next());
        System.out.println(dIter.next());
        System.out.println(dIter.next());
        System.out.println(dIter.hasNext());

        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addLast(5);
        System.out.println(deque.removeFirst());// ==> 2
        deque.addFirst(7);
        deque.addLast(8);
        System.out.println(deque.removeLast());// ==> 8
        System.out.println(deque.removeFirst());// ==> 7
        System.out.println(deque.size());// ==> 2

        for (Integer integer : deque) {
            System.out.println(integer);// ==> [1, 5, 8]
        }

    }
}

/*
 * Throw an IllegalArgumentException if the client calls either addFirst() or
 * addLast() with a null argument.
 * Throw a java.util.NoSuchElementException if the client calls either
 * removeFirst() or removeLast when the deque is empty.
 * Throw a java.util.NoSuchElementException if the client calls the next()
 * method in the iterator when there are no more items to return.
 * Throw an UnsupportedOperationException if the client calls the remove()
 * method in the iterator.
 * 
 * Unit testing. Your main() method must call directly every public constructor
 * and method to help verify that they work as prescribed (e.g., by printing
 * results to standard output).
 * 
 * Performance requirements. Your deque implementation must support each deque
 * operation (including construction) in constant worst-case time. A deque
 * containing n items must use at most 48n + 192 bytes of memory. Additionally,
 * your iterator implementation must support each operation (including
 * construction) in constant worst-case time.
 */