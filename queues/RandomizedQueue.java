import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int tail = 0;
    private Item[] q = (Item[]) new Object[1];

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return tail == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return tail;
    }

    private Item[] resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];

        for (int i = 0; i < tail; i++) {
            copy[i] = q[i];
        }
        return copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Cannot call enque with null.");

        if (size() == q.length - 1) {
            q = resize(2 * q.length);
        }

        q[tail++] = item;

    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Cannot call dequeue with empty que.");

        if (size() > 0 && size() == q.length / 4) {
            q = resize(q.length / 2);
        }

        int i = StdRandom.uniform(0, size());

        Item item = q[i];
        q[i] = q[tail - 1];
        tail--;

        return item;

    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("Cannot call sample with empty que.");

        int i = StdRandom.uniform(0, size());
        return q[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int current = 0;
        private final int length = size();
        private final Item[] qRdm;

        private RandomizedQueueIterator() {
            qRdm = resize(size());
            StdRandom.shuffle(qRdm);
        }

        public boolean hasNext() {
            return current < length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("DequeIterator has no next element.");

            Item item = qRdm[current];
            current++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported.");
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        System.out.println(q.isEmpty());
        q.enqueue("str1");
        q.enqueue("str2");
        q.enqueue("str3");
        q.enqueue("str4");

        System.out.println(q.isEmpty());
        System.out.println(q.size());
        System.out.println(q.sample());

        Iterator<String> qIter = q.iterator();

        System.out.println(qIter.hasNext());
        System.out.println(qIter.next());
        System.out.println(qIter.next());
        System.out.println(qIter.next());
        System.out.println(qIter.next());

        System.out.println(qIter.hasNext());

        System.out.println(q.dequeue());
        System.out.println(q.dequeue());
        System.out.println(q.dequeue());
        System.out.println(q.dequeue());

        System.out.println(q.size());
        System.out.println(q.isEmpty());

        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        System.out.println(queue.size());// ==> 0
        System.out.println(queue.size());// ==> 0
        queue.enqueue(65);
        System.out.println(queue.dequeue());// ==> 65
        System.out.println(queue.isEmpty());// ==> true
        System.out.println(queue.isEmpty());// ==> true
        System.out.println(queue.isEmpty());// ==> true
        System.out.println(queue.isEmpty());// ==> true
        queue.enqueue(818);

        RandomizedQueue<Integer> queue2 = new RandomizedQueue<>();
        queue2.enqueue(397);
        System.out.println(queue2.size());// ==> 1
        System.out.println(queue2.isEmpty());// ==> false
        System.out.println(queue2.dequeue());// ==> 397
        queue2.enqueue(136);
        queue2.enqueue(110);
        System.out.println(queue2.size());// ==> 2
        queue2.enqueue(65);
        queue2.enqueue(29);
        System.out.println(queue2.dequeue());// ==> 136
        queue2.enqueue(390);

    }

}

/*
 * Iterator. Each iterator must return the items in uniformly random order.
 * The order of two or more iterators to the same randomized queue must be
 * mutually
 * independent; each iterator must maintain its own random order.
 * 
 * Corner cases. Throw the specified exception for the following corner cases:
 * Throw an IllegalArgumentException if the client calls enqueue() with a null
 * argument.
 * Throw a java.util.NoSuchElementException if the client calls either sample()
 * or dequeue() when the randomized queue is empty.
 * Throw a java.util.NoSuchElementException if the client calls the next()
 * method in the iterator when there are no more items to return.
 * Throw an UnsupportedOperationException if the client calls the remove()
 * method in the iterator.
 * 
 * Unit testing. Your main() method must call directly every public constructor
 * and method to verify that they work as prescribed (e.g., by printing results
 * to standard output).
 * 
 * Performance requirements. Your randomized queue implementation must support
 * each randomized queue operation (besides creating an iterator) in constant
 * amortized time. That is, any intermixed sequence of m randomized queue
 * operations (starting from an empty queue) must take at most cm steps in the
 * worst case, for some constant c. A randomized queue containing n items must
 * use at most 48n + 192 bytes of memory. Additionally, your iterator
 * implementation must support operations next() and hasNext() in constant
 * worst-case time; and construction in linear time; you may (and will need to)
 * use a linear amount of extra memory per iterator.
 */