import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Array implementation of a circular queue.
 *  
 * @author Thomas Lonowski
 * @param <T>	-	a generic object type
 * @date 4 October 2023
 */
public class CircularQueue<T> {
	private T[] array;
	private final int DEFAULT_CAPACITY = 10;
	private int head;	// Index of head element
	private int tail;	// Index of tail element
	
	/**
	 * No-param constructor to create a new empty circular queue
	 */
	@SuppressWarnings("unchecked")
	public CircularQueue(Class<T> type) {
		array = (T[]) Array.newInstance(type, DEFAULT_CAPACITY);
		head = tail = 0;	// Conditions for new empty queue
	}

	/**
	 * Parameterized constructor to create a circular queue from a provided generic array.
	 * 
	 * @param queue	-	a generic array to make a queue with
	 * @param head	-	the index of the first element in the array
	 * @param tail	-	the index of the next available space in the array
	 */
	public CircularQueue(T[] queue, int head, int tail) {
		array = queue;
		this.head = head;
		this.tail = tail;
	}
	
	/**
	 * Returns true if there are no elements in the queue, false otherwise.
	 * 
	 * @return true is elements are in the queue, false if otherwise
	 */
	public boolean isEmpty() {
		return head == tail;	// The only time when (head == queue) is when the array is empty
	}
	
	/**
	 * Inserts an element at the back of the queue.
	 * 
	 * @param n	-	a new element to add to the queue 
	 */
	public void enqueue(T element) {
		try {
			if(head == (tail+1) % array.length) throw new Exception("Queue overflow");	// Case: queue is full
			else {
				array[tail] = element;	// Insert new element
				if(tail == array.length) tail = 1;	// Wrap around to front of array
				else tail++;
			}
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}

	/**
	 * Removes and returns an element from the front of the queue. If the queue is empty, returns null.
	 * 
	 * @return remove	-	the element removed from the queue, or null if queue is empty.
	 */
	public T dequeue() {
		T remove = null;
		try {
			if(isEmpty()) throw new Exception("Queue underflow");
			else {
				remove = array[head];	// Get element at front of queue
				if(head == array.length) head = 1;	// Wrap around to front of array
				else head++;
			}
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		return remove;
	}
	
	/**
	 * Returns a copy of the underlying array.
	 */
	public T[] getQueueArray() {
		return Arrays.copyOf(array, array.length);
	}
	
	/**
	 * Returns the index of the first element in the queue.
	 * 
	 * @return this.head	-	the index of the first element in the queue.
	 */
	public int getHead() {
		return head;
	}
	
	/**
	 * Returns the index of the next available space to insert an element in the queue.
	 * 
	 * @return this.tail	-	the index of the next available space in the queue
	 */
	public int getTail() {
		return tail;
	}
}
