package com.example.darshan74.assignment4;

import android.os.Handler;
import android.view.View;


public class Circle {

    private Handler rediusIncreaseRadiusHandler = new Handler();
    private Handler circleMoveHandler = new Handler();

    public static final float radiusDefault = 50;
    private float x;
    private float y;
    private float velocityOfX;
    private float velocityOfY;
    private final long delay = 10;
    private float radius;
    private boolean increment;
    private boolean movement;
    private static final float restitution = 0.5f;
    public Circle(float x, float y) {
        radius = radiusDefault;
        this.x = x;
        this.y = y;
        this.y = y;
        increment = false;
        movement = false;
    }
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public void setXVelocity(float xVelocity) {
        this.velocityOfX = xVelocity;
    }
    public void stopIncreamentInRadius() {
        increment = false;
    }

    public void setYVelocity(float yVelocity) {
        this.velocityOfY = yVelocity;
    }
    public void increamentInRedius(View view) {
        increment = true;
        rediusIncreaseRadiusHandler.post(new RediusChange(view));
    }

    public boolean isCircleExits(float x, float y) {
        return  ((x  - getX()) * (x  - getX()) + (y  - getY()) * (y  - getY()))  < (radius * radius);
    }

    public void move(View view) {
        movement = true;
        circleMoveHandler.post(new CircleMover(view));
    }

    public void stop() {
        movement = false;
        velocityOfX = 0;
        velocityOfY = 0;
    }


    class CircleMover implements Runnable {
        private View view;
        public CircleMover(View view) {
            this.view = view;
        }
        public void run() {
            if (movement) {
                int height = view.getHeight();
                int width = view.getWidth();
                x += velocityOfX;
                y += velocityOfY;

                if ((y + radius) >= height) {
                    y = height - radius;
                    velocityOfY = -1 * velocityOfY * restitution;
                } else if ((y - radius) <= 0) {
                    y = radius;
                    velocityOfY = -1 * velocityOfY * restitution;
                }

                if ((x + radius) >= width) {
                    x = width - radius;
                    velocityOfX = -1 * velocityOfX * restitution;
                } else if ((x - radius) <= 0) {
                    x = radius;
                    velocityOfX = -1 * velocityOfX * restitution;
                }

                view.postInvalidate();
                circleMoveHandler.postDelayed(new CircleMover(view), delay);
            }
        }
    }
    class RediusChange implements Runnable {
        private View view;
        public RediusChange(View view) {
            this.view = view;
        }
        public void run() {
            if (increment) {
                radius = radius + 1;
                view.postInvalidate();
                rediusIncreaseRadiusHandler.postDelayed(new RediusChange(view), delay);
            }
        }
    }

}
