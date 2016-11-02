package structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RecursiveListIterator<T> implements Iterator<T> {
	private DLNode<T> head;

	public RecursiveListIterator(DLNode<T> head) {
		this.head = head;
	}

	@Override
	public boolean hasNext() {
		return (head != null);
	}

	@Override
	public T next() {
		if (hasNext()) {
			T data = head.getData();
			head = head.getNext();
			return data;
		} else 
			throw new NoSuchElementException();
	}

}