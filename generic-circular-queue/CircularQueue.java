import java.lang.reflect.Array;
import java.util.LinkedList;

/**
 * Single linked list implementation of a circular queue.
 * 
 * SLL is the best way to implement a circular queue, since it can add to rear
 * and remove from front in constant time, which are the main functions of a queue.
 *  
 * @author Thomas Lonowski
 * @param <T>	-	a generic object type
 * @date 4 October 2023
 */
public class CircularQueue<T> {
	private LinkedList<T> list;
	private int head;	// Index of head element
	private int tail;	// Index of tail element
	
	/**
	 * No-param constructor to create a new empty queue
	 */
	public CircularQueue() {
		list = new LinkedList<T>();
		head = tail = 0;	// Conditions for new empty queue
	}

	/**
	 * Parameterized constructor creates a circular queue from a provided listList 
	 * 
	 * @param queue	-	a generic listList to make a queue with
	 * @param head	-	the index of the first element in the listList
	 * @param tail	-	the index of the next available space in the listList
	 */
	public CircularQueue(LinkedList<T> queue, int head, int tail) {
		list = queue;
		this.head = head;
		this.tail = tail;
	}
	
	/**
	 * Returns true if there are no elements in the queue, false otherwise.
	 * 
	 * @return true is elements are in the queue, false if otherwise
	 */
	public boolean isEmpty() {
		return head == tail;	// The only time when (head == queue) is when the list is empty
	}
	
	/**
	 * Inserts an element at the back of the queue.
	 * 
	 * @param n	-	a new element to add to the queue 
	 */
	public void enqueue(T element) {
		try {
			if(head == (tail+1) % list.size()) throw new Exception("Queue overflow");	// Case: queue is full
			else {
				list.set(tail, element);	// Insert new element
				if(tail == list.size()) tail = 1;	// Wrap around to front of list
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
				remove = list.get(head);	// Get element at front of queue
				if(head == list.size()) head = 1;	// Wrap around to front of list
				else head++;
			}
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		return remove;
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
