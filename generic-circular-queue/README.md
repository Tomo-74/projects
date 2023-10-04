# Generic Circular Queue
* Author: Thomas Lonowski
* Date: 4 October 2023
* Project type: personal  

# Description
An implementation of the circular queue abstract data type using a quasi-generic array.
The array cannot be truly generic, as this is not possible in Java. Rather, 
the array is of type Object at compile time and is later casted to the specified data
when a CircularQueue object is instantiated.  
  
CircularQueue contains basic enqueue, dequeue, and isEmpty methods like any other queue
implementation. However since it is circular, much less memory is wasted.

With a normal array implementation, when an element is removed from the front of the queue 
that space cannot be reused - it is essentially wasted. With a circular array implementation,
elements added to the end of the queue can loop back around to the front, writing over unused
space. This results in fewer memory locations being used.

# Compilation and Run
From inside the directory where the project is stored:  

1. Compile: `javac CircularQueueTest.java`
2. Run: `java CircularQueueTest`  

# Test Results
Results of a simple unit test suite below:

`**************************************************`  
`Test: createEmptyQueue_QueueIsEmpty`  
`Result: pass`  
  
`Test: createNewQueueWithElements_QueueHasElements`  
`Result: pass`  

`Test: newQueue_Enqueue1_ExpectedValue`  
`Result: pass`  

`Test: newQueue_Enqueue2_ExpectedValues`  
`Result: pass`  

`Test: oneElementQueue_Dequeue1_QueueIsEmpty`  
`Result: pass`  

`Test: oneElementQueue_Dequeue1_ExpectedValue`  
`Result: pass`  

`Test: twoElementQueue_Dequeue2_QueueIsEmpty`  
`Result: pass`  

`Test: twoElementQueue_Dequeue2_ExpectedValues`  
`Result: pass`  

`**************************************************`  

# Sources
Stack Overflow post consulted for creating a "generic" array:  
`https://stackoverflow.com/questions/383888/what-is-the-easiest-alternative-to-a-generic-array-in-java`