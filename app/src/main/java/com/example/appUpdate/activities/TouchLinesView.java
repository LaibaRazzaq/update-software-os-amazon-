package com.example.appUpdate.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class TouchLinesView extends View {
        private Paint paint;
        private ArrayList<TountPoint> touchPoints;
        private int screenWidth, screenHeight;

        TextView textView;

        public TouchLinesView(Context context) {
                super(context);
                init();
        }

        public TouchLinesView(Context context, AttributeSet attrs) {
                super(context, attrs);
                init();
        }

        public TouchLinesView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }

        private void init() {
                paint = new Paint();
                paint.setStrokeWidth(5);
                paint.setStyle(Paint.Style.FILL);
                touchPoints = new ArrayList<>();
        }

        public  void setTextView(TextView textView){
                this.textView=textView;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                int pointerCount = event.getPointerCount();
                textView.setText("Touch Detected: "+pointerCount);

                for (int i = 0; i < pointerCount; i++) {
                        int pointerId = event.getPointerId(i);
                        float x = event.getX(i);
                        float y = event.getY(i);
                        float z = event.getAxisValue(MotionEvent.AXIS_PRESSURE, i);
                        TountPoint point = new TountPoint(pointerId, x, y, z, screenWidth, screenHeight);
                        int index = touchPoints.indexOf(point);
                        if (event.getActionMasked() == MotionEvent.ACTION_DOWN
                                || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                                if (index == -1) {
                                        touchPoints.add(point);
                                } else {
                                        touchPoints.set(index, point);
                                }
                        } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                                if (index != -1) {
                                        touchPoints.set(index, point);
                                }
                        }


                }
                invalidate();
                return true;
        }
        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                int screenWidth = getWidth();
                int screenHeight = getHeight();
                int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};

                for (int i=0; i<touchPoints.size(); i++) {
                        TountPoint point=touchPoints.get(i);
                        float x = point.getX();
                        float y = point.getY();
                        float z = point.getZ();

                        int pointerId=point.getId();
                        paint.setColor(colors[i % colors.length]);
                        canvas.drawCircle(x, y, 50, paint);


                        // Draw lines to edges of screen
                        paint.setStrokeWidth(5);
                        paint.setColor(colors[i % colors.length]);
                        canvas.drawLine(x, y, x, 0, paint); // top
                        canvas.drawLine(x, y, x, screenHeight, paint); // bottom
                        canvas.drawLine(x, y, 0, y, paint); // left
                        canvas.drawLine(x, y, screenWidth, y, paint); // right
                }
        }
        private int getColorForZValue(float z, int pointerId) {
                int color;

                if (pointerId % 2 == 0) {
                        if (z < 0.5f) {
                                color = Color.GREEN;
                        } else if (z >= 0.5f && z < 1.0f) {
                                color = Color.YELLOW;
                        } else {
                                color = Color.RED;
                        }
                } else {
                        if (z < 0.5f) {
                                color = Color.WHITE;
                        } else if (z >= 0.5f && z < 1.0f) {
                                color = Color.GRAY;
                        } else {
                                color = Color.BLACK;
                        }
                }

                return color;
        }


}