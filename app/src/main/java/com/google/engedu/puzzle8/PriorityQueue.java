package com.google.engedu.puzzle8;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Patrick on 11/12/15.
 */
public class PriorityQueue<T> {
    private ArrayList<T> arr;
    private Comparator<T> comparator;

    PriorityQueue(Comparator<T> comparator){
        arr = new ArrayList<T>();
        this.comparator = comparator;
    }

    public boolean isEmpty(){
        return arr.isEmpty();
    }

    public int size(){
        return arr.size();
    }

    public void add(T x){
        arr.add(x);
        buildMinHeap();
    }

    public T peek(){
        return arr.get(0);
    }

    public T remove(){
        // If the queue is empty, return null.
        // Otherwise, assign the minimum value
        // to min and assign the last value
        // in the heap to the first value.
        // Remove the last value from the heap.
        // Run heapify.
        if (arr.size() < 1){
            return null;
        }
        T min = arr.get(0);

        arr.set(0,arr.get(arr.size()-1));
        arr.remove(arr.size()-1);
        minHeapify(0);
        return min;
    }

    public void minHeapify(int i){
        if(arr.size() == 0) return;
        int l = left(i);
        int r = right(i);
        int smallest;
        if(l < arr.size() && comparator.compare(arr.get(l),arr.get(i)) < 0){
            smallest = l;
        } else smallest = i;
        if(r < arr.size() && comparator.compare(arr.get(r),arr.get(i)) < 0){
            smallest = r;
        }
        if(comparator.compare(arr.get(smallest),arr.get(i)) != 0){
            T temp = arr.get(i);
            arr.set(i,arr.get(smallest));
            arr.set(smallest,temp);
            minHeapify(smallest);
        }
    }

    public void buildMinHeap(){
        for(int i = arr.size()/2; i >= 0; i--){
            minHeapify(i);
        }
    }

    public int left(int index){
        return 2*index;
    }

    public int right(int index){
        return 2*index+1;
    }


}
