package Rec1;

public class PrimQ1<T> implements SimpleQueue, Moves {

	private int moves;
	private int size;
	private T[] data;
	
	public PrimQ1(int i) {
		size = 0;
		data = (T[]) new Object[i];
	}

	public int getMoves() {
		return moves;
	}

	public void setMoves(int val) {
		moves = val;
	}

	public boolean offer(Object element) {
		T info = (T) element;
		if(size >= data.length)
			data = resize(data);
		
		for(int i = size - 1; i >= 0 ; i--){
			data[i + 1] = data[i];
			moves++;
		}
		size++;
		data[0] = info;
		moves++;
		return true;
	}

	public Object poll() {
		T info;
		if(size > 0){
			info = (T) data[size - 1];
			data[size - 1] = null;
			size--;
			moves++;
		}
		else
			info = null;
		return info;
	}

	public Object peek() {
		if(this.isEmpty())
			return null;
		else
			return data[size - 1];
	}

	public boolean isEmpty() {
		if(size == 0)
			return true;
		else
			return false;
	}

	public void clear() {
		for(int i = 0; i < data.length; i++)
			data[i] = null;
		size = 0;
	}
	
	private T[] resize( T[] oldAr){
		T[] newAr = (T[]) new Object[oldAr.length * 2];
		for(int i = 0; i < oldAr.length; i++)
			newAr[i] = oldAr[i];
		return newAr;
	}

}
