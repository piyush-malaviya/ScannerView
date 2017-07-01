package com.pcm.scannerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class ScannerView extends View {

    private static int BITMAP_WIDTH = 40; // Default
    private static int BITMAP_HEIGHT = 40; // Default
    private static int BITMAP_CIRCLE_RADIUS = BITMAP_WIDTH / 2;
    private static int RADAR_RADIUS_METER = 100;
    private static int NO_OF_RING = 5;
    private ArrayList<RadarObject> arrRadarObjects;
    private int backCircleColor = Color.parseColor("#000000");
    private int backCenterColor = Color.parseColor("#00ff00");
    private Handler mHandler = new Handler();
    private float counter = 0;
    // draw radius back
    private Paint mPaintBack;
    private Paint mPaintRing;
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            counter = (counter + 0.01f) % 6.28f;
            invalidate();
        }
    };

    public ScannerView(Context context) {
        this(context, null);
    }

    public ScannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        arrRadarObjects = new ArrayList<>();
        setDummyData();

        mPaintBack = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRing = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    public void setDummyData() {
        arrRadarObjects.add(new RadarObject("A", 50, 0));
        arrRadarObjects.add(new RadarObject("B", 20, 90));
        arrRadarObjects.add(new RadarObject("C", 40, 180));
        arrRadarObjects.add(new RadarObject("D", 60, 270));
        arrRadarObjects.add(new RadarObject("E", 80, 360));
        arrRadarObjects.add(new RadarObject("F", 100, 90));
    }

    /**
     * Set No of objects on radar view
     *
     * @param radarObjects
     */
    public void setRadarObjects(ArrayList<RadarObject> radarObjects) {
        arrRadarObjects = radarObjects;
    }

    /**
     * Set View Height
     *
     * @param height : height in int
     */
    public void setViewHeight(int height) {
        BITMAP_HEIGHT = height;
    }

    /**
     * Set View Width
     *
     * @param width : width in int
     */
    public void setViewWidth(int width) {
        BITMAP_WIDTH = width;
    }

    /**
     * Set No of rings
     *
     * @param noOfRings : no of rings counts
     */
    public void setNoOfClircles(int noOfRings) {
        NO_OF_RING = noOfRings;
    }


    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(widthMeasureSpec);
        int squareSize = (width < height) ? width : height;
        super.onMeasure(MeasureSpec.makeMeasureSpec(squareSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(squareSize, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = (getWidth() / 2);
        float centerY = (getHeight() / 2);

        float cx = centerX - (BITMAP_WIDTH / 2);

        //mPaintBack.setColor(Color.BLACK);
        //canvas.drawCircle(centerX, centerY, centerX, mPaintBack);

        // draw ring
        mPaintRing.setColor(Color.GREEN);
        mPaintRing.setStrokeWidth(2);
        mPaintRing.setStyle(Paint.Style.STROKE);
        for (int ring = 0; ring < NO_OF_RING; ring++) {
            float ringRadius = (ring + 1) * (cx / NO_OF_RING);
            canvas.drawCircle(centerX, centerY, ringRadius, mPaintRing);
        }

        mHandler.post(mRunnable);

        // Draw Green Line
        float stopX = (float) (centerX + cx * Math.sin(counter));
        float stopY = (float) (centerY - cx * Math.cos(counter));
        canvas.drawLine(centerX, centerY, stopX, stopY, mPaintRing);

        // draw object
        for (int object = 0; object < arrRadarObjects.size(); object++) {

            Bitmap bitmap = createAndGetBitmap(Color.GREEN);

            double pos = 0;
            if (arrRadarObjects.get(object).getDistance() > 0) {
                pos = (cx * arrRadarObjects.get(object).getDistance()) / RADAR_RADIUS_METER;
            }
            float angle = (float) (arrRadarObjects.get(object).getAngle() * Math.PI / 180f); // Need to convert to radians first
            float startX = (float) (centerX + pos * Math.sin(angle));
            float startY = (float) (centerX - pos * Math.cos(angle));
            canvas.drawBitmap(bitmap, startX - (bitmap.getWidth() / 2), startY - (bitmap.getHeight() / 2), mPaintBack);
            bitmap.recycle();
        }
    }

    private Bitmap createAndGetBitmap(int color) {
        Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(BITMAP_WIDTH / 2, BITMAP_HEIGHT / 2, BITMAP_CIRCLE_RADIUS, paint);
        Bitmap bIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        bIcon = Bitmap.createScaledBitmap(bIcon, BITMAP_WIDTH / 2, BITMAP_HEIGHT / 2, false);
        canvas.drawBitmap(bIcon, BITMAP_WIDTH / 2 - (bIcon.getWidth() / 2), BITMAP_HEIGHT / 2 - (bIcon.getWidth() / 2), paint);
        return bitmap;
    }

}
