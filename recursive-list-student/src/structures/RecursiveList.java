package structures;

import java.util.Iterator;

public class RecursiveList<T> implements ListInterface<T> {

	protected DLNode<T> head;
	protected DLNode<T> tail;
	protected int size;
	
	@Override
	public Iterator<T> iterator() {
		return new RecursiveListIterator<>(head);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public ListInterface<T> insertFirst(T elem) throws NullPointerException {
		if (elem == null) throw new NullPointerException("Null elements not permitted");
		head = new DLNode<T>(elem, head, null);
		if (head.getNext() == null) //Check if there is only one element present
			tail = head; //If there is only one element, both 'tail' and 'head' point to the same node
		else
			head.getNext().setPrev(head); //Link the next node and the new head node
		size++; //Increment the size
		return this;
		
	}
	
	@Override
	public ListInterface<T> insertLast(T elem) throws NullPointerException {
		if (elem == null) throw new NullPointerException("Null elements not permitted");
		tail = new DLNode<T>(elem, null, tail);
		if (tail.getPrev() == null) //Check if there is only one element present
			head = tail; //If there is only one element, both 'tail' and 'head' point to the same node
		else
			tail.getPrev().setNext(tail); //Link the previous node and the new tail node
		size++; //Increment the size
		return this;
	}
	
	@Override
	public ListInterface<T> insertAt(int index, T elem) throws IndexOutOfBoundsException, NullPointerException {
		if (index < 0 || index > size) throw new IndexOutOfBoundsException();  //Ensure index is within range of deque's size
		if (elem == null) throw new NullPointerException("Null elements not permitted"); //Ensure no null elements are being inserted (violates assumption about lists)
		
		if (index == 0) //If index is 0:
			return insertFirst(elem);  //insert node at the head
		else if (index == size) //If the index is size (not size - 1)
			return insertLast(elem); //insert a new tail node
		else 
			linkNextAndPriorNodeToCurrentNode(new DLNode<T>(elem, getNode(index), getNode(index - 1))); //Create a new node and link the node before it and after it to the newly created node
		return this;
	}
	
	@Override
	public T removeFirst() throws IllegalStateException {
		if (isEmpty()) throw new IllegalStateException("Cannot remove from an empty dequeue"); //Ensure queue isn't empty
		T data = head.getData();
		if(head.getNext() != null) //Check that this isn't the only element
			head.getNext().setPrev(null); //Set the next node's 'previous' pointer to null
		else
			tail = null; //If this is the only element, then since 'tail' and 'head' refer to the same node, both are now null
		head = head.getNext(); //If there is a next node, head is now that. If there isn't, head is now null
		size--; //Decrement the size
		return data;
	}
	
	@Override
	public T removeLast()  throws IllegalStateException{
		if (isEmpty()) throw new IllegalStateException("Cannot remove from an empty dequeue");
		T data = tail.getData();
		if (tail.getPrev() != null) //Check that this isn't the only element
			tail.getPrev().setNext(null); //Set the previous node's 'next' pointer to null
		else
			head = null; //If this is the only element, then since 'tail' and 'head' refer to the same node, both are now null
		tail = tail.getPrev(); //If there is a previous node, tail is now that. If there isn't, tail is null
		size--; //Decrement the size
		return data;
	}
	
	@Override
	public T removeAt(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i >= size) throw new IndexOutOfBoundsException(); //Ensure that we are removing something IN the actual deque
		T data = get(i); //Get the i'th element in the deqeue
		remove(data);  //Remove the node that contained data
		return data; //Return the data we originally extracted
	}
	
	@Override
	public T getFirst() throws IllegalStateException{
		if(isEmpty()) throw new IllegalStateException("Cannot get a value from an empty dequeue"); //Ensure that we can actually obtain a value
		return head.getData(); //Return data contained in the head node
	}
	
	@Override
	public T getLast() throws IllegalStateException{
		if(isEmpty()) throw new IllegalStateException("Cannot get a value from an empty dequeue"); //Ensure that we can actually obtain a value
		return tail.getData(); //Return data contained in the head node
	}
	
	@Override
	public T get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > size) throw new IndexOutOfBoundsException(); //Ensure we can actually obtain a node within the current dequeue
		return get(i, head, 0); //Call the helper 'get' method which runs recursively
	}
	
	@Override
	public boolean remove(T elem) throws NullPointerException {
		if (elem == null) throw new NullPointerException("Null elements don't exist in the dequeue!"); //Ensure elem isn't null (violates assumption of lists)
		int index = indexOf(elem); //get the index of the node that contains elem

		if (index == -1) //-1 indicates that nothing was found
			return false; //Thus return false
		else if (index == 0) //If it exists at the very start of the list
			removeFirst(); //Then simply remove the head
		else if (index == size() - 1) //If it refers to the last element
			removeLast(); //Then remove the tail
		else 
			linkNextAndPriorNode(getNode(index)); //Otherwise link the nodes that are AFTER and BEFORE the node that holds the element so they hold no references to the node
		return true; //Successfully removed node
	}
	
	@Override
	public int indexOf(T elem) throws NullPointerException {
		if (elem == null) throw new NullPointerException("Null elements don't exist in the dequeue!"); //Ensure we are looking for an actual element
		return indexOf(elem, head, 0);  //Call the helper 'indexOf' method which runs recursively
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0; //The dequeue is empty if the size is 0 since that means no nodes exist
	}
	
	/* Helper Methods */
	
	/**
	 * Gives the index of {@code DLNode} that contains {@code elem}
	 * @param elem - element to search for
	 * @param currNode - the current node of the recursive call
	 * @param index -  index of {@code currNode}
	 * @return index of {@code DLNode} for which {@code getData()} returns {@code elem}
	 */
	public int indexOf(T elem, DLNode<T> currNode, int index){
		return (currNode == null ? -1 : 
			currNode.getData().equals(elem) ? index 
					: indexOf(elem, currNode.getNext(), ++index));
		
	}
	
	/**
	 * Returns data contained within the i'th {@code DLNode} 
	 * @param i - index of node that contains the element
	 * @param currNode - the current node of the recursive call
	 * @param index - index of {@code currNode}
	 * @return data contained within i'th {@code DLNode} in deque
	 */
	public T get(int i, DLNode<T> currNode, int index){
		return (index == i ? currNode.getData() : get(i, currNode.getNext(), ++index));
	}
	
	 /**
	  * Returns the ith node in the dequeue
	  * @param i - index of node to find
	  * @return i'th {@code DLNode} in deque
	  * @throws IndexOutOfBoundsException {@code i} is less than 0 or {@code i} is greater than or equal to {@code size}
	  */
	public DLNode<T> getNode(int i) throws IndexOutOfBoundsException{
		if (i < 0 || i >= size) throw new IndexOutOfBoundsException();
		return getNode(i, head, 0);
	}
	
	/**
	 * Returns the ith node in the dequeue
	 * @param i - index of node to find
	 * @param currNode - the current node of the recursive call
	 * @param index - index of {@code currNode}
	 * @return i'th {@code DLNode} in deque 
	 */
	public DLNode<T> getNode(int i, DLNode<T> currNode, int index){
		return (i == index ? currNode : getNode(i, currNode.getNext(), ++index));
	}
	
	/**
	 * Sets the {@code currNode}'s previous node's 'next' link to {@code currNode}'s 'next' node.
	 * Sets the {@code currNode}'s next node's 'previous' link to {@code currNode}'s 'previous' node
	 * This method should only be used when removing a node node from the middle of the dequeue.
	 * @param currNode the node being removed from the middle of the list
	 * 
	 */
	public void linkNextAndPriorNode(DLNode<T> currNode){
		DLNode<T> nextNode = currNode.getNext();
		DLNode<T> prevNode = currNode.getPrev();
		prevNode.setNext(nextNode);
		nextNode.setPrev(prevNode);
		size--;
	}
	
	/**
	 * Sets the {@code currNode}'s previous node's 'next' link to {@code currNode}.
	 * Sets the {@code currNode}'s next node's 'previous' link to {@code currNode}
	 * This method should only be used when adding a brand new node to the middle of the dequeue.
	 * @param currNode the node which it's 'next' and 'previous' node will have links to
	 * 
	 */
	public void linkNextAndPriorNodeToCurrentNode(DLNode<T> currNode){
		currNode.getPrev().setNext(currNode);
		currNode.getNext().setPrev(currNode);
		size++;
	}
	
}
