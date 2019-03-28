package com.example.paintview3;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
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

        String test2 = String.valueOf(GlobalClassTest.redoStackCount);
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);

        canvas.drawText(test, 200, 200, paint);
        canvas.drawText(test2,100,200,paint);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        radius = GlobalClassTest.radius;
        Log.e("test",event.toString());
        int rand = random.nextInt();
        balls.addLast(new ball(event.getX(),event.getY(),rand,radius));


        undoStack.push(new Painted(event.getX(),event.getY(),rand,radius));
        GlobalClassTest.undoStackCount = GlobalClassTest.undoStackCount + 1;
        test = String.valueOf(event.getPointerCount());
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
