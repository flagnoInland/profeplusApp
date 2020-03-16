package com.equipu.profeplus.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by Herbert Caller on 27/01/2018.
 */

public class FiveZonesWheel extends View {

    ClickOnLetterWheelListener clickOnLetterWheelListener;

    private float sourceSize = 512f;
    protected int canvasPixSize;
    protected float canvasSize;
    protected int[] mainColors = new int[]{
            0xff39ABE7,
            0xff499800,
            0xffFAD203,
            0xffF67F3B,
            0xffE0303F
    };
    protected int[] initColors = mainColors.clone();
    protected int[] grayColors = new int[]{
            0xff898A89,
            0xff6E6F6E,
            0xffC7C8C7,
            0xff9B9C9B,
            0xff656665
    };
    protected String[] textButton = new String[]{"A", "B", "C", "D", "E"};

    protected float touchX, touchY;
    protected int indexButton = -1;

    Paint mPaint = new Paint();
    Path mPath = new Path();
    
    public FiveZonesWheel(Context context) {
        super(context);

    }

    public FiveZonesWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FiveZonesWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setClickOnLetterWheelListener(ClickOnLetterWheelListener clickOnLetterWheelListener) {
        this.clickOnLetterWheelListener = clickOnLetterWheelListener;
    }

    private void shuffleColors(){
        Random rnd = new Random();
        for (int i = 4; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = mainColors[index];
            mainColors[index] = mainColors[i];
            mainColors[i] = a;
        }
        initColors = mainColors.clone();
    }

    public void shuffleLetters(){
        Random rnd = new Random();
        for (int i = 4; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = textButton[index];
            textButton[index] = textButton[i];
            textButton[i] = a;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Set size to 360dp
        //int size = Math.round(360f * getResources().getDisplayMetrics().density);
        //setMeasuredDimension(size, size);

        int size = Math.round(240f * getResources().getDisplayMetrics().density);
        int desiredWidth = getMeasuredWidth() != 0 ? getMeasuredWidth() : size;
        int desiredHeight = desiredWidth;

        canvasPixSize = resolveSize(desiredWidth, widthMeasureSpec);
        //canvasLong = resolveSize(desiredHeight, heightMeasureSpec);
        canvasSize = 1f*canvasPixSize;
        setMeasuredDimension(canvasPixSize, canvasPixSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        //canvas.drawRect(0, 0, getWidth(), getWidth(), mPaint);
        int sc = canvas.saveLayer(0, 0, getWidth(), getWidth(), null,
                Canvas.ALL_SAVE_FLAG);
        // Source image (src)
        drawTopLeftButton(canvas);
        drawTopRightButton(canvas);
        drawBottomLeftButton(canvas);
        drawBottomRightButton(canvas);
        drawBottom(canvas);
        drawLetters(canvas);
        // Destination image (dst)
        canvas.restoreToCount(sc);
    }

    private void drawTopLeftButton(Canvas canvas) {
        mPath = new Path();
        mPath = drawSvgPath("M 238.20117,0.61914062", mPath);
        mPath = drawSvgPath("C 138.83938,7.5439589 52.514485,71.47162 16.912109,164.49414", mPath);
        mPath = drawSvgPath("L 136.5918,210.29883", mPath);
        mPath = drawSvgPath("c 17.76635,-46.48768 60.86754,-78.46336 110.50976,-81.98438 " +
                "-1.12016,-16.07107 -2.34688,-33.671085 -3.33765,-47.885741", mPath);
        mPath = drawSvgPath("L 264.43859,63.628824", mPath);
        mPath = drawSvgPath("L 241.53881,48.504881", mPath);
        mPath = drawSvgPath("C 240.42627,32.542967 239.31372,16.581054 238.20117,0.61914062", mPath);
        mPath.close();
        mPaint.setColor(initColors[0]);
        canvas.drawPath(mPath, mPaint);

    }

    private void drawTopRightButton(Canvas canvas) {
        mPath = new Path();
        mPath = drawSvgPath("M 270.7793,0.42773438", mPath);
        mPath = drawSvgPath("L 263.38086,128.36914", mPath);
        mPath = drawSvgPath("c 49.25795,2.85614 92.48862,33.77003 111.11719,79.45898 " +
                "16.1533,-6.56677 28.62614,-11.63733 44.49536,-18.08862", mPath);
        mPath = drawSvgPath("l 22.37352,10.72987", mPath);
        mPath = drawSvgPath("l 7.29005,-22.78895", mPath);
        mPath = drawSvgPath("c 16.1533,-6.56677 28.62614,-11.63733 44.49536,-18.08862", mPath);
        mPath = drawSvgPath("C 455.94801,68.074343 369.40525,6.131149 270.7793,0.42773438", mPath);
        mPath.close();
        mPaint.setColor(initColors[1]);
        canvas.drawPath(mPath, mPaint);
    }


    private void drawBottomRightButton(Canvas canvas) {
        mPath = new Path();
        mPath = drawSvgPath("M 501.79102,184.42578", mPath);
        mPath = drawSvgPath("l -122.9629,35.80664", mPath);
        mPath = drawSvgPath("C 382.23498,231.85035 383.97629,243.89288 384,256", mPath);
        mPath = drawSvgPath("c -0.0615,36.7249 -15.89436,71.65425 -43.47266,95.90625 " +
                "11.53633,13.08941 20.44415,23.19645 31.77759,36.05566", mPath);
        mPath = drawSvgPath("l -3.65297,24.58812", mPath);
        mPath = drawSvgPath("l 24.83803,-0.55101", mPath);
        mPath = drawSvgPath("c 10.66497,12.10075 22.34456,25.35272 31.77759,36.05567 " +
                "74.44942,-65.6162 104.26947,-168.34848 76.52344,-263.62891 ", mPath);
        mPath.close();
        mPaint.setColor(initColors[2]);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawBottom(Canvas canvas) {
        mPath = new Path();
        mPath = drawSvgPath("M 329.32227,360.73047", mPath);
        mPath = drawSvgPath("C 307.84556,375.8215 282.24853,383.94496 256,384", mPath);
        mPath = drawSvgPath("c -24.3338,-0.0505 -48.14903,-7.03638 -68.6543,-20.13867 " +
                "-9.36743,14.71668 -16.60053,26.08022 -25.80322,40.53809", mPath);
        mPath = drawSvgPath("l -23.68455,2.61907", mPath);
        mPath = drawSvgPath("l 6.4824,24.40632", mPath);
        mPath = drawSvgPath("c -8.65989,13.6051 -18.14366,28.50454 -25.80322,40.53808 " +
                "87.31247,55.57568 199.49953,53.10925 284.28516,-6.25", mPath);
        mPath.close();
        mPaint.setColor(initColors[3]);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawBottomLeftButton(Canvas canvas) {
        mPath = new Path();
        mPath = drawSvgPath("M 7.703125,193.66992", mPath);
        mPath = drawSvgPath("c -24.315236,96.86347 9.918286,199.02418 87.6875,261.67969", mPath);
        mPath = drawSvgPath("L 175.76367,355.58984", mPath);
        mPath = drawSvgPath("C 145.60199,331.32381 128.04249,294.71131 128,256", mPath);
        mPath = drawSvgPath("c 0.0534,-10.50069 1.39884,-20.9549 4.00586,-31.12695 " +
                "-16.92227,-4.24792 -29.98886,-7.52796 -46.613526,-11.70117", mPath);
        mPath = drawSvgPath("L 75.720287,189.99816", mPath);
        mPath = drawSvgPath("L 54.316651,205.37109", mPath);
        mPath = drawSvgPath("C 38.672555,201.44403 21.54013,197.14336 7.703125,193.66992", mPath);
        mPath.close();
        mPaint.setColor(initColors[4]);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawLetters(Canvas canvas){
        mPaint.setColor(Color.WHITE);
        mPaint.setFakeBoldText(true);
        mPaint.setTextSize(80f);
        String mText = textButton[0];
        Rect rect = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), rect);
        canvas.drawText(mText, getCoord("120"), getCoord("120"), mPaint);
        mText = textButton[1];
        canvas.drawText(mText, getCoord("392") - rect.width(), getCoord("120"), mPaint);
        mText = textButton[2];
        canvas.drawText(mText, getCoord("410"), getCoord("340"), mPaint);
        mText = textButton[3];
        canvas.drawText(mText, getCoord("256") - rect.centerX(), getCoord("480"), mPaint);
        mText = textButton[4];
        canvas.drawText(mText, getCoord("102") - rect.width(), getCoord("340"), mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            touchX = event.getX()-.5f*canvasSize;
            touchY = .5f*canvasSize - event.getY();
            Log.d("Coordinates", String.format("x: %f, y: %f", touchX ,touchY));
            float outterRadius = .5f*canvasSize;
            float innerRadius = .25f*canvasSize;
            boolean inside = (touchX*touchX + touchY*touchY < outterRadius*outterRadius)
                    && (touchX*touchX + touchY*touchY > innerRadius*innerRadius);
            if (inside) {
                onTouchInsideSlice();
            }
        }
        return true;
        //return super.onTouchEvent(event);
    }

    protected void onTouchInsideSlice() {
        if (isEnabled()) {
            if (touchX < 0 && touchY > -Math.tan(18f * Math.PI / 180f) * touchX) {
                highlightFraction(0);
            }
            if (touchX > 0 && touchY > Math.tan(18f * Math.PI / 180f) * touchX) {
                highlightFraction(1);
            }
            if (touchX > 0 && touchY < Math.tan(18f * Math.PI / 180f) * touchX
                    && touchY > -Math.tan(54f * Math.PI / 180f) * touchX) {
                highlightFraction(2);
            }
            if (touchY < 0 && touchX < -Math.tan(36f * Math.PI / 180f) * touchY
                    && touchX > Math.tan(36f * Math.PI / 180f) * touchY) {
                highlightFraction(3);
            }
            if (touchX < 0 && touchY < -Math.tan(18f * Math.PI / 180f) * touchX
                    && touchY > Math.tan(54f * Math.PI / 180f) * touchX) {
                highlightFraction(4);
            }
        }

    }

    private void highlightFraction(int i){
        initColors = mainColors.clone();
        initColors[i] = lightenColour(mainColors[i]);
        invalidate();
        if (clickOnLetterWheelListener != null){
            clickOnLetterWheelListener.onClickLetter(textButton[i]);
        }
    }

    public void setTextButton(String[] textButton) {
        this.textButton = textButton;
        invalidate();
    }

    public void colorize(){
        initColors = mainColors.clone();
        invalidate();
    }

    public void setGrayColors(){
        initColors = grayColors.clone();
        invalidate();
    }

    public void rearrangeColors(){
        shuffleColors();
        invalidate();
    }


    protected int lightenColour(int rgb){
        // hue, saturation, value
        float[] hsv = new float[3];
        Color.colorToHSV(rgb, hsv);
        hsv[2] *= .5f;
        return Color.HSVToColor(hsv);
    }

    public Path drawSvgPath(String pathString, Path path){
        if (pathString.charAt(0) == 'M'){
            pathString = pathString.replace("M","").trim();
            String[] c = pathString.split(",");
            path.moveTo(getCoord((c[0])),
                    getCoord((c[1])));
        } else if (pathString.charAt(0) == 'C'){
            pathString = pathString.replace("C","").trim();
            String[] c = pathString.split(" ");
            String[] d1 = c[0].split(",");
            String[] d2 = c[1].split(",");
            String[] d3 = c[2].split(",");
            path.cubicTo(getCoord(d1[0]),getCoord(d1[1]),
                    getCoord(d2[0]),getCoord(d2[1]),
                    getCoord(d3[0]),getCoord(d3[1]));
        } else if (pathString.charAt(0) == 'L'){
            pathString = pathString.replace("L","").trim();
            String[] c = pathString.split(",");
            path.lineTo(getCoord((c[0])),
                    getCoord((c[1])));
        } else if (pathString.charAt(0) == 'm'){
            pathString = pathString.replace("m","").trim();
            String[] c = pathString.split(",");
            path.rMoveTo(getCoord((c[0])),
                    getCoord((c[1])));
        } else if (pathString.charAt(0) == 'c'){
            pathString = pathString.replace("c","").trim();
            String[] c = pathString.split(" ");
            for (int i = 0; i<c.length; i=i+3){
                String[] d1 = c[i].split(",");
                String[] d2 = c[i+1].split(",");
                String[] d3 = c[i+2].split(",");
                path.rCubicTo(getCoord(d1[0]),getCoord(d1[1]),
                        getCoord(d2[0]),getCoord(d2[1]),
                        getCoord(d3[0]),getCoord(d3[1]));
            }
        } else if (pathString.charAt(0) == 'l'){
            pathString = pathString.replace("l","").trim();
            String[] c = pathString.split(",");
            path.rLineTo(getCoord((c[0])),
                    getCoord((c[1])));
        }
        return path;
    }

    public float getCoord(String coord){
        return Float.parseFloat(coord)*canvasSize/sourceSize;
    }

    public interface ClickOnLetterWheelListener {
        void onClickLetter(String answer);
    }


}
