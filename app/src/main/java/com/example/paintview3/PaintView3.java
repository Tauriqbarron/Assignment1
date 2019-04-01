package com.example.paintview3;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class PaintView3 extends View implements View.OnTouchListener {


    SparseArray<PointF> activePointers = new SparseArray<PointF>();
    LinkedList<ball> balls = new LinkedList<ball>();
    Paint paint = new Paint();
    Random random = new Random();

    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    Display screen = wm.getDefaultDisplay();
    Stack undoStack = new Stack();
    Stack redoStack = new Stack();
    String test ="" ;

    int radius = GlobalClassTest.radius;
    int up = -7;
    int down = 7;

    public PaintView3(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public PaintView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public PaintView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
     protected  void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Point size = new Point();
        screen.getSize(size);

        int maxX = size.x;
        int maxy =size.y ; ;
        int min = 10+ radius;





        paint.setColor(random.nextInt());

        for(ball pt: balls){
            paint.setColor(pt.color);
            canvas.drawCircle(pt.x,pt.y,pt.radius,paint);
        }
       // for(ball pt: balls) {
         //   pt.y = pt.y + (pt.directionY);
           // if (pt.y > maxy) {
             //   pt.directionY = up;
            //}
           // else if (pt.y < min) {
            //    pt.directionY = down;
            //}
       // }
        //for(ball pt: balls){
          //  pt.x = pt.x +(pt.directionX);
           // if(pt.x > maxX ){
             //   pt.directionX = up;
            //}
            //else if(pt.x < min){
             // pt.directionX = down;
            //}


        //}
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("test",event.toString());
        radius = GlobalClassTest.radius;
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        switch (maskedAction){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:{
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                int rand = random.nextInt();
                balls.addLast(new ball(f.x,f.y,rand,radius));
                undoStack.push(new Painted(f.x,f.y,rand,radius ));
                activePointers.put(pointerId,f);

            }
            case MotionEvent.ACTION_MOVE:{
                for(int size = event.getPointerCount(),i = 0;
                    i< size;i++){
                    PointF point = activePointers.get(event.getPointerId(i));
                    if (point != null){
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                        int rand = random.nextInt();
                        balls.addLast(new ball(point.x,point.y,rand,radius));
                        undoStack.push(new Painted(point.x,point.y,rand,radius ));
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:{
                activePointers.remove(pointerId);
                break;
            }
        }

//set up get and set for radius on main activity


        invalidate();
        return true;
    }

    public void Undo(){
        int size = balls.size() -1;
        balls.removeLast();
        redoStack.push(undoStack.pop());
        invalidate();
    }
    public void Redo(){
            Painted paint =(Painted) redoStack.peek();
            undoStack.push(redoStack.pop());
            balls.addLast(new ball(paint.x,paint.y,paint.color,paint.radius));
            invalidate();
    }

}
