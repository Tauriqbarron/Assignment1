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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class PaintView3 extends View implements View.OnTouchListener{    private GestureDetector lpDetector;


    private boolean isLP = false;
    private GestureDetector.OnGestureListener lpListen = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e ("test","Longpress deteted");
            float ptX = e.getX();
            float pty = e.getY();
            search:
            for(ball pt: balls){
                float ptUpperx = (pt.x + 100);
                float ptLowerX = (pt.x - 100);
                float ptUppery = (pt.y + 100);
                float ptLowerY = (pt.y - 100);

                if(  (ptX < (ptUpperx) && ptX > (ptLowerX))&& (pty < (ptUppery) && pty > (ptLowerY)) ){
                             Log.e ("test","Match detected");
                             Log.e ("test","Cycling color");
                             pt.color = random.nextInt();
                             break search;
                     }

            }
            invalidate();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

    };

    SparseArray<PointF> activePointers = new SparseArray<PointF>();
    LinkedList<ball> balls = new LinkedList<ball>();
    LinkedList<ball> lpBalls = new LinkedList<ball>();

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
        setup(context);
    }
    public PaintView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        setup(context);
    }
    public PaintView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        setup(context);
    }
    public void setup(Context context){
        lpDetector = new GestureDetector(context,lpListen);
    }

    @Override
    protected  void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Point size = new Point();
        screen.getSize(size);

        int maxX = size.x;
        int maxy =size.y ; ;
        int min = 10+ radius;



            for(ball pt: balls){
                paint.setColor(pt.color);
                canvas.drawCircle(pt.x,pt.y,(pt.radius + 30),paint);
            }


    }


    @Override
    public boolean onTouch(View v, MotionEvent event)  {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        radius = GlobalClassTest.radius;
        lpDetector.onTouchEvent(event);
        int rand = random.nextInt();
        PointF f = new PointF();




        search:
        switch (maskedAction){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:{

                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                activePointers.put(pointerId, f);

                for (ball pt :balls){
                    float pointUpperX = pt.x + 100;
                    float pointLowerX = pt.x - 100;
                    float pointUpperY = pt.y + 100;
                    float pointLowerY = pt.y - 100;
                    float pX = f.x;
                    float pY = f.y;

                    if ((pX < (pointUpperX) && pX > (pointLowerX)) && (pY < (pointUpperY) && pY > (pointLowerY))) {
                        Log.e ("test","Inbounds");
                        break search;
                    }
                }
                balls.addLast(new ball(f.x, f.y, rand, radius));
                undoStack.push(new Painted(f.x, f.y, rand, radius));
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                    for(int size = event.getPointerCount(),i = 0;
                        i< size;i++){
                        PointF point = activePointers.get(event.getPointerId(i));
                        if (point != null){
                            rand = random.nextInt();
                            if((event.getX() >= (point.x +100) || event.getX() <= (point.x - 100))
                                    || (event.getY() >= (point.y +100) || event.getY() <= (point.y - 100))){
                                isLP = false;
                                point.x = event.getX();
                                point.y = event.getY();
                                balls.addLast(new ball(point.x,point.y,rand,radius));
                                undoStack.push(new Painted(point.x,point.y,rand,radius ));
                            }
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
