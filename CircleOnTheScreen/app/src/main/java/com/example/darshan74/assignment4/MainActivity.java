package com.example.darshan74.assignment4;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawingView(this, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public class DrawingView extends View implements SensorEventListener {

        private static final int circleLimit = 15;
        private Paint paint;
        private SparseArray<Circle> activePointers;
        private Circle pickedCircle;
        private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
                Color.BLACK, Color.CYAN, Color.RED, Color.DKGRAY,
                Color.LTGRAY, Color.YELLOW};
        private static final int VELOCITY_SCALE = 2;
        private VelocityTracker vTracker;
        private static final float widthStorck = 2f;
        private GestureDetectorCompat gestureDetect;

        public DrawingView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float y = sensorEvent.values[1];
      /*  vTracker.addMovement(sensorEvent);
        pickedCircle.setX(sensorEvent.getX());
        pickedCircle.setY(sensorEvent.getY());*/
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        private void init(Context context) {
            activePointers = new SparseArray<>(circleLimit);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(widthStorck);
            gestureDetect = new GestureDetectorCompat(context, new GestureListener(this));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for (int i = 0; i < activePointers.size(); i++) {
                Circle circle = activePointers.valueAt(i);
                if (circle != null)
                    paint.setColor(colors[i % 8]);
                canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), paint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            this.gestureDetect.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    vTracker = VelocityTracker.obtain();
                    vTracker.addMovement(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    vTracker.addMovement(event);
                    pickedCircle.setX(event.getX());
                    pickedCircle.setY(event.getY());
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    pickedCircle.stopIncreamentInRadius();
                    vTracker.computeCurrentVelocity(VELOCITY_SCALE);
                    pickedCircle.setXVelocity(vTracker.getXVelocity());
                    pickedCircle.setYVelocity(vTracker.getYVelocity());
                    pickedCircle.move(this);
                    vTracker.recycle();
                    vTracker = null;
                    break;
                default:
                    return false;
            }
            invalidate();
            return true;
        }
        private int circleLocation(float x, float y) {
            for (int i=0; i < activePointers.size(); i++) {
                Circle circle = activePointers.get(i);
                if (circle.isCircleExits(x, y)) {
                    return i;
                }
            }
            return -1;
        }

        class GestureListener extends GestureDetector.SimpleOnGestureListener {
            private View view;
            public GestureListener(View view) {
                this.view = view;
            }
            @Override
            public boolean onDown(MotionEvent event) {
                int position = circleLocation(event.getX(), event.getY());
                if (position != -1) {
                    pickedCircle = activePointers.get(position);
                    pickedCircle.stop();
                } else {
                    if (activePointers.size() != circleLimit) {
                        pickedCircle = new Circle(event.getX(), event.getY());
                        activePointers.put(activePointers.size(), pickedCircle);
                    }
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent event) {
                pickedCircle.increamentInRedius(view);
            }
        }
    }
}