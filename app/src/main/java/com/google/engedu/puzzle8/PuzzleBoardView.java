package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

//import java.util.PriorityQueue;

public class PuzzleBoardView extends View {
    public static final String TAG = "PuzzleBoardView Log";
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap, View parent) {
        int width = 1000;
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d(TAG, "onDraw called.");
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null ) {
            Random r = new Random();
            for(int i = 0; i < NUM_SHUFFLE_STEPS; i++) {
                ArrayList<PuzzleBoard> neighbors
                        = puzzleBoard.neighbours();
                puzzleBoard = neighbors.get(
                        r.nextInt(neighbors.size()));
                invalidate();
            }
        }
        puzzleBoard.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(puzzleBoard == null){
                        return false;
                    }
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }


    public void solve() {
        puzzleBoard.reset();
        Comparator<PuzzleBoard> comparator = new PuzzleBoardComparator();
        PriorityQueue<PuzzleBoard> priorityQueue = new PriorityQueue<PuzzleBoard>(comparator);
        priorityQueue.add(puzzleBoard);
        int count = 0;

        PuzzleBoard leastPriorityBoard = priorityQueue.peek();
        while(!priorityQueue.isEmpty()) {
            leastPriorityBoard = priorityQueue.remove();
            // If the removed PuzzleBoard is not the solution,
            // insert onto the PriorityQueue all neighbouring
            // states (reusing the neighbours method).
            if (!leastPriorityBoard.resolved()) {
                ArrayList<PuzzleBoard> neighbors =
                        leastPriorityBoard.neighbours();
                for (PuzzleBoard i : neighbors) {
                    priorityQueue.add(i);
                }
                count++;
                if (count > 100000){
                    Toast toast = Toast.makeText
                            (activity, "I give up!",
                                    Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
            } else break;
        }
        ArrayList<PuzzleBoard> solutionSteps= new ArrayList<PuzzleBoard>();
        solutionSteps.add(leastPriorityBoard);
        PuzzleBoard backtrack = leastPriorityBoard.getPreviousBoard();
        while(backtrack != null){
            solutionSteps.add(backtrack);
            backtrack = backtrack.getPreviousBoard();
        }
        Collections.reverse(solutionSteps);
        animation = solutionSteps;
        puzzleBoard = leastPriorityBoard;
        invalidate();
    }

    public class PuzzleBoardComparator
            implements Comparator<PuzzleBoard>
    {
        @Override
        public int compare(PuzzleBoard x, PuzzleBoard y)
        {

            return x.priority() - y.priority();
        }
    }
}
