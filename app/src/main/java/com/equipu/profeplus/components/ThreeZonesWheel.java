package com.equipu.profeplus.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by Herbert Caller on 27/01/2018.
 */

public class ThreeZonesWheel extends View {

    ClickOnThirdWheelListener clickOnThirdWheelListener;

    private float sourceSize = 512f;
    protected int canvasPixSize;
    protected float canvasSize;
    protected int[] mainColors = new int[]{
            0xff4B9700,
            0xffF67F3B,
            0xff38AAE6
    };
    protected int[] initColors = mainColors.clone();
    protected int[] grayColors = new int[]{
            0xff565755,
            0xff868786,
            0xffB1B2B1
    };
    protected String[] textButton = new String[]{"F", "G", "H"};

    protected float touchX, touchY;
    protected float lastX, lastY;

    Paint mPaint = new Paint();
    Path mPath = new Path();

    public ThreeZonesWheel(Context context) {
        super(context);
        shuffleColors();
    }

    public ThreeZonesWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        shuffleColors();
    }

    public ThreeZonesWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        shuffleColors();
    }

    public void setClickOnThirdWheelListener(ClickOnThirdWheelListener clickOnThirdWheelListener) {
        this.clickOnThirdWheelListener = clickOnThirdWheelListener;
    }

    private void shuffleColors() {
        Random rnd = new Random();
        for (int i = 2; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = mainColors[index];
            mainColors[index] = mainColors[i];
            mainColors[i] = a;
        }
        initColors = mainColors.clone();
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
        canvasSize = 1f * canvasPixSize;
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
        drawBottom(canvas);
        drawFirstAriaLabel(canvas);
        drawSecondAriaLabel(canvas);
        drawThirdAriaLabel(canvas);
        // Destination image (dst)
        canvas.restoreToCount(sc);
    }

    private void drawTopLeftButton(Canvas canvas) {
        mPath = new Path();
        mPath = drawSvgPath("M 237.90234,0.640625", mPath, 1f);
        mPath = drawSvgPath("C 141.16065,7.4969551 56.599866,68.432988 19.486328,158.03516", mPath, 1f);
        mPath = drawSvgPath("c -37.1136,89.6026 -20.40619077,192.48455 43.154297,265.73828", mPath, 1f);
        mPath = drawSvgPath("L 159.43945,339.7832", mPath, 1f);
        mPath = drawSvgPath("C 139.22053,316.54775 128.05805,286.80077 128,256", mPath, 1f);
        mPath = drawSvgPath("c 3e-5,-67.18105 51.9382,-122.9304 118.95117,-127.67969 -1.13884,-16.0691 -2.38602,-33.666965 -3.39331,-47.879882", mPath, 1f);
        mPath = drawSvgPath("l 22.86889,-16.959961 -25.1311,-14.95996", mPath, 1f);
        mPath = drawSvgPath("C 240.15681,32.451405 238.90963,14.853542 237.90234,0.640625", mPath, 1f);
        mPath.close();
        mPaint.setColor(initColors[0]);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawTopRightButton(Canvas canvas) {
        mPath = new Path();
        mPath = drawSvgPath("M 274.02539,0.63476562 265.00391,128.44922", mPath, 1f);
        mPath = drawSvgPath("C 331.98514,133.17254 383.93068,188.85247 384,256", mPath, 1f);
        mPath = drawSvgPath("c -0.0322,30.87254 -11.2215,60.69322 -31.50586,83.9668 " +
                "13.15455,11.44672 23.3119,20.28535 36.23511,31.53076", mPath, 1f);
        mPath = drawSvgPath("l 0.0784,27.51025 24.07837,-6.48974", mPath, 1f);
        mPath = drawSvgPath("c 13.15456,11.44672 23.3119,20.28535 36.23511,31.53076 " +
                "63.71656,-73.22262 80.54195,-176.1823 43.44922,-265.87891", mPath, 1f);
        mPath = drawSvgPath("C 455.47786,68.473387 370.84796,7.4690712 274.02539,0.63476562", mPath, 1f);
        mPath.close();
        mPaint.setColor(initColors[1]);
        canvas.drawPath(mPath, mPaint);
    }

    private void drawBottom(Canvas canvas) {
        mPath = new Path();
        mPath = drawSvgPath("M 342.17969,350.45508", mPath, 1f);
        mPath = drawSvgPath("C 318.63738,371.98587 287.90318,383.94897 256,384 " +
                "224.2756,383.9459 193.70165,372.11261 170.20703,350.79492", mPath, 1f);
        mPath = drawSvgPath("l -32.24634,35.62939 -25.74878,-2.12353 4.25122,25.87646", mPath, 1f);
        mPath = drawSvgPath("c -10.82228,11.95769 -22.67416,25.05298 -32.246333,35.6294 " +
                "97.809923,88.52264 246.874493,88.22332 344.328123,-0.69141", mPath, 1f);
        mPath.close();
        mPaint.setColor(initColors[2]);
        canvas.drawPath(mPath, mPaint);

    }


    private void drawFirstAriaLabel(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        // aria-label="VERDADERO"
        float scale = 3.8f;
        mPath = new Path();
        mPath = drawSvgPath("M 36.854306,25.543075 32.756486,29.020359 34.151858,30.66474 37.913446,27.472772 39.054708,28.817698 35.29312,32.009666 36.982723,34.000786 41.269673,30.363012 42.451058,31.75522 36.58277,36.734868 30.011596,28.99104 35.690753,24.171882", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 41.818116,22.413867 43.094591,24.825383 45.219892,23.700409", mPath, scale);
        mPath = drawSvgPath("Q 45.853219,23.365173 46.092521,23.051403 46.517388,22.499081 46.117681,21.74396 45.685743,20.927942 44.995483,20.934701 44.60719,20.937501 43.998221,21.259882", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 43.562768,19.494623", mPath, scale);
        mPath = drawSvgPath("Q 44.51634,19.013262 45.134604,18.958857 45.758957,18.901227 46.331352,19.128369 46.804168,19.314667 47.193872,19.63851 47.583576,19.962353 47.864014,20.492156 48.202473,21.131573 48.208509,21.923562 48.211309,22.709462 47.603763,23.358486 48.357071,23.279376 48.857818,23.606807 49.361431,23.924926 49.861061,24.868828", mPath, scale);
        mPath = drawSvgPath("L 50.18018,25.471707", mPath, scale);
        mPath = drawSvgPath("Q 50.505745,26.086765 50.670506,26.280207 50.927317,26.588638 51.282296,26.611229", mPath, scale);
        mPath = drawSvgPath("L 51.401562,26.836547 49.337159,27.929287", mPath, scale);
        mPath = drawSvgPath("Q 49.093955,27.67602 48.960715,27.51267 48.687786,27.17379 48.472174,26.781184", mPath, scale);
        mPath = drawSvgPath("L 48.018386,25.953344", mPath, scale);
        mPath = drawSvgPath("Q 47.551704,25.101145 47.095719,24.976101 46.645824,24.847833 45.817626,25.286218", mPath, scale);
        mPath = drawSvgPath("L 43.881106,26.311266 45.744243,29.831105 43.911248,30.801355 39.159925,21.825157", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 56.92889,23.586193", mPath, scale);
        mPath = drawSvgPath("Q 58.144485,23.251214 58.608126,22.158602 59.07841,21.064159 58.622616,19.410153 58.168655,17.76279 57.204139,17.063733 56.244435,16.356202 55.02884,16.691182 53.813246,17.026161 53.336319,18.122434 52.859392,19.218707 53.315179,20.872713 53.77097,22.526719 54.742129,23.223946 55.713287,23.921173 56.928882,23.586193", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 60.668537,18.846363", mPath, scale);
        mPath = drawSvgPath("Q 61.395241,21.483472 60.300176,23.293258 59.505707,24.741476 57.406647,25.31991 55.307588,25.898344 53.883431,25.06151 52.009257,24.069952 51.282553,21.432842 50.541206,18.742592 51.657558,16.984117 52.452027,15.535899 54.551086,14.957465 56.650146,14.379031 58.074303,15.215865 59.92719,16.156113 60.668537,18.846363", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 54.551086,14.957465", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 18.894454,54.605451", mPath, scale);
        mPath = drawSvgPath("C 17.46002,54.553061 15.886845,55.157428 15.183405,56.476047 14.651069,57.477125 14.597651,58.629507 14.379932,59.721892 14.22906,60.601611 14.078187,61.481332 13.927315,62.36105 17.264062,62.93328 20.600809,63.50551 23.937557,64.077741 24.196953,62.523425 24.484612,60.973321 24.725185,59.4162 24.875828,58.262374 24.460594,57.048711 23.586685,56.269847 22.331506,55.118536 20.56782,54.630417 18.894454,54.605451", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 18.860864,56.747957", mPath, scale);
        mPath = drawSvgPath("C 20.097053,56.813477 21.431893,57.068518 22.350869,57.963156 22.915075,58.505523 23.014521,59.34639 22.833915,60.075799 22.738255,60.633599 22.642601,61.191401 22.546944,61.749201 20.369299,61.375752 18.191654,61.002306 16.01401,60.628857 16.172257,59.795901 16.26503,58.948332 16.483246,58.128598 16.656661,57.511111 17.134913,56.959433 17.788387,56.847459 18.138396,56.760899 18.501425,56.738517 18.860864,56.747959", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 18.678963,45.168301", mPath, scale);
        mPath = drawSvgPath("C 18.363392,45.902623 18.047821,46.636947 17.732251,47.371269 20.365509,49.817975 22.998768,52.264682 25.632027,54.711388 25.923999,54.031842 26.21597,53.352297 26.507942,52.672752 25.961034,52.182688 25.414124,51.692621 24.867216,51.202558 25.359692,50.056891 25.852168,48.911226 26.344645,47.765559 27.070182,47.839799 27.79572,47.914043 28.521257,47.988286 28.824253,47.283592 29.127249,46.578895 29.430244,45.874201 25.846484,45.638902 22.262723,45.4036 18.678963,45.168301", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 20.330542,47.205386", mPath, scale);
        mPath = drawSvgPath("C 21.721327,47.343189 23.112112,47.480995 24.502897,47.618798 24.163727,48.407896 23.824558,49.196997 23.485388,49.986096 22.433773,49.059192 21.382157,48.13229 20.330542,47.205386", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 24.701282,38.09293 30.008205,42.0643 31.180625,40.497599", mPath, scale);
        mPath = drawSvgPath("Q 32.080582,39.294991 31.249555,37.932995 30.792629,37.186585 29.893431,36.513681 28.652207,35.584826 27.697115,35.481112 26.740636,35.367754 25.873705,36.52623", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 24.676132,35.182539", mPath, scale);
        mPath = drawSvgPath("Q 25.252735,34.435028 25.793901,34.056869 26.715123,33.412344 27.861685,33.444196 28.786381,33.473526 29.60809,33.84748 30.429799,34.221431 31.069719,34.700307 32.691586,35.914011 33.329829,37.407125 34.186362,39.42504 32.807529,41.267568", mPath, scale);
        mPath = drawSvgPath("L 30.186095,44.770579 22.054698,38.685549", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 28.733646,87.739862", mPath, scale);
        mPath = drawSvgPath("C 25.192949,88.248013 21.652253,88.756166 18.111556,89.264317 18.445213,89.919057 18.778871,90.573798 19.112528,91.228539 21.739241,90.722454 24.365954,90.21637 26.992667,89.710285 25.036542,91.532053 23.080417,93.353817 21.124292,95.175585 21.467423,95.848928 21.810555,96.522274 22.153686,97.195618 24.65052,94.639184 27.147353,92.082751 29.644186,89.526318 29.340672,88.930833 29.037161,88.335346 28.733646,87.739862", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        scale = 1f;
        mPath = new Path();
        mPath = drawSvgPath("M 93.050781,287.08789 86.414062,288.98047 92.240234,309.41602 82.748047,312.12109", mPath, scale);
        mPath = drawSvgPath("C 81.04427,306.14388 79.340492,300.16666 77.636719,294.18945 75.499997,294.79883 73.363281,295.4082 71.226562,296.01758 72.93034,301.99479 74.634117,307.97201 76.337891,313.94922", mPath, scale);
        mPath = drawSvgPath("L 68.498047,316.18359", mPath, scale);
        mPath = drawSvgPath("C 66.641928,309.67253 64.78581,303.16146 62.929688,296.65039 60.751304,297.27148 58.572915,297.89258 56.394531,298.51367", mPath, scale);
        mPath = drawSvgPath("L 64.111328,325.58594", mPath, scale);
        mPath = drawSvgPath("C 76.416015,322.07813 88.720704,318.57031 101.02539,315.0625 98.367188,305.73763 95.708987,296.41276 93.050781,287.08789", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 89.478516,248.61328", mPath, scale);
        mPath = drawSvgPath("C 88.784049,248.66048 88.08202,250.07702 87.244141,250.16211 84.118656,250.99944 80.810446,250.5628 77.658203,251.27539 75.122552,251.66534 72.930906,253.49588 72.171875,255.95703 71.414484,255.23504 69.649915,252.02309 67.371094,251.69336 63.29544,250.03458 58.305264,251.05274 55.300781,254.29492 52.66557,256.85102 52.249372,260.71451 52.458984,264.19531", mPath, scale);
        mPath = drawSvgPath("L 53.632812,282.98633", mPath, scale);
        mPath = drawSvgPath("C 66.403027,282.18808 79.173144,281.39004 91.943359,280.5918 91.78036,277.98404 91.618089,275.37729 91.455078,272.76953 86.447491,273.08255 81.439227,273.39401 76.431641,273.70703 76.372721,272.7639 76.31482,271.82204 76.255859,270.87891 76.158949,268.0721 75.685248,265.25526 76.035156,262.45508 76.188666,260.60432 77.870307,259.41751 79.595703,259.18359 83.221483,258.54547 87.034776,258.90911 90.496094,257.43359 90.316857,254.5663 90.13821,251.69932 89.958984,248.83203 89.797785,248.66387 89.638777,248.60239 89.478516,248.61328", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 64.714844,258.87109", mPath, scale);
        mPath = drawSvgPath("C 65.040625,258.87709 65.361499,258.90549 65.669922,258.95309 67.816399,259.15358 69.116785,261.21411 69.320312,263.20114 69.725259,266.82102 69.822858,270.47034 70.089844,274.10349 66.659038,274.31794 63.229634,274.53357 59.798828,274.74802", mPath, scale);
        mPath = drawSvgPath("L 59.503906,270.04294", mPath, scale);
        mPath = drawSvgPath("C 59.431416,267.44895 58.92041,264.82804 59.398438,262.25192 59.893608,259.82252 62.434378,258.82637 64.714844,258.87106", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

    }

    private void drawSecondAriaLabel(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        float scale = 3.8f;
        // aria-label="FALSO"
        mPath = new Path();
        mPath = drawSvgPath("M 100.97804,25.134454 106.59361,29.641009 105.47667,31.032809 101.50547,27.845877 100.04354,29.667576 103.52035,32.457759 102.41635,33.833438 98.939539,31.043255 96.274418,34.364229 94.630052,33.044606", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 106.0309,38.396467 107.61412,40.429689 109.98834,36.974126", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 111.09653,34.617907 112.56969,36.509782 106.76614,45.587343 105.35225,43.771578 106.59733,41.972456 104.29869,39.020479 102.21967,39.748626 100.85658,37.998098", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 111.82464,35.552972", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 116.56792,42.733158 117.42495,44.674586 109.80421,48.038707 111.83549,52.640143 110.1651,53.377523 107.2768,46.834658", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 113.93336,55.736868", mPath, scale);
        mPath = drawSvgPath("Q 113.26871,55.964522 112.98452,56.307641 112.4678,56.934688 112.70741,58.165599 112.85091,58.902793 113.10275,59.331097 113.58369,60.142996 114.4291,59.978431 114.92281,59.882321 115.10909,59.396817 115.2886,58.912625 115.31113,57.946564", mPath, scale);
        mPath = drawSvgPath("L 115.34243,56.84542", mPath, scale);
        mPath = drawSvgPath("Q 115.39133,55.221414 115.68802,54.545941 116.18171,53.403932 117.64934,53.118247 118.98846,52.857577 120.06402,53.65902 121.13959,54.460464 121.5069,56.34741 121.81365,57.923246 121.19091,59.195667 120.57626,60.473535 118.99743,60.837023", mPath, scale);
        mPath = drawSvgPath("L 118.60774,58.835101", mPath, scale);
        mPath = drawSvgPath("Q 119.49672,58.605899 119.73327,57.801743 119.88873,57.266078 119.74787,56.54241 119.5912,55.737584 119.17309,55.320586 118.75499,54.903589 118.17335,55.016809 117.63905,55.120814 117.46744,55.645586 117.35084,55.984161 117.30765,57.024437", mPath, scale);
        mPath = drawSvgPath("L 117.22945,58.822609", mPath, scale);
        mPath = drawSvgPath("Q 117.17875,60.004732 116.83755,60.667815 116.30795,61.697481 114.97559,61.956836 113.60941,62.222772 112.49908,61.350882 111.39682,60.484439 111.02556,58.577204 110.64641,56.629389 111.31517,55.340989 111.99069,54.051273 113.54624,53.748474", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 121.5231,56.358303", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("m 113.54879,68.67561", mPath, scale);
        mPath = drawSvgPath("q -0.0398,1.260277 0.86668,2.026503 0.90624,0.773112 2.62105,0.827231 1.70791,0.0539 2.6611,-0.660526 0.95985,-0.707324 0.99963,-1.967601 0.0398,-1.260278 -0.86625,-2.040277 -0.90602,-0.779999 -2.62082,-0.834118 -1.71481,-0.05412 -2.66821,0.667195 -0.9534,0.721315 -0.99318,1.981593", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("m 3.42078,4.974857", mPath, scale);
        mPath = drawSvgPath("q -2.73404,-0.08629 -4.13868,-1.667895 -1.14823,-1.187473 -1.07954,-3.36369 0.0687,-2.176217 1.2895,-3.288922 1.50179,-1.496775 4.23583,-1.410488 2.78914,0.08803 4.13847,1.674783 1.14823,1.187473 1.07954,3.363689 -0.0687,2.176217 -1.2895,3.288922 -1.44648,1.491627 -4.23562,1.403601", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("m 5.52512,-4.692523", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private void drawThirdAriaLabel(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        float scale = 3.8f;
        // aria-label="NO SE"
        mPath = new Path();
        mPath = drawSvgPath("M 46.583891,112.85726", mPath, scale);
        mPath = drawSvgPath("H 48.809423", mPath, scale);
        mPath = drawSvgPath("L 52.853965,119.94726", mPath, scale);
        mPath = drawSvgPath("V 112.85726", mPath, scale);
        mPath = drawSvgPath("H 54.831449", mPath, scale);
        mPath = drawSvgPath("V 123.0134", mPath, scale);
        mPath = drawSvgPath("H 52.709271", mPath, scale);
        mPath = drawSvgPath("L 48.561376,115.79937", mPath, scale);
        mPath = drawSvgPath("V 123.0134", mPath, scale);
        mPath = drawSvgPath("H 46.583891", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 50.786907,112.83656", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 61.308229,121.49756", mPath, scale);
        mPath = drawSvgPath("Q 62.569133,121.49756 63.306384,120.56738 64.050524,119.6372 64.050524,117.92155 64.050524,116.21278 63.306384,115.2826 62.569133,114.34554 61.308229,114.34554 60.047324,114.34554 59.296293,115.27571 58.545262,116.20589 58.545262,117.92155 58.545262,119.6372 59.296293,120.56738 60.047324,121.49756 61.308229,121.49756", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 66.172703,117.92155", mPath, scale);
        mPath = drawSvgPath("Q 66.172703,120.65695 64.636191,122.11078 63.485529,123.2959 61.308229,123.2959 59.130928,123.2959 57.980266,122.11078 56.436864,120.65695 56.436864,117.92155 56.436864,115.13102 57.980266,113.73231 59.130928,112.5472 61.308229,112.5472 63.485529,112.5472 64.636191,113.73231 66.172703,115.13102 66.172703,117.92155", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 61.308229,112.5472", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 73.235148,119.87836", mPath, scale);
        mPath = drawSvgPath("Q 73.331608,120.57427 73.614109,120.91878 74.130873,121.54579 75.384888,121.54579 76.135919,121.54579 76.604452,121.38042 77.493286,121.06347 77.493286,120.2022 77.493286,119.69922 77.052314,119.42361 76.611342,119.15489 75.667386,118.94818", mPath, scale);
        mPath = drawSvgPath("L 74.592516,118.70703", mPath, scale);
        mPath = drawSvgPath("Q 73.007772,118.34874 72.401435,117.92844 71.374797,117.22564 71.374797,115.73047 71.374797,114.36621 72.366984,113.46359 73.359172,112.56098 75.281535,112.56098 76.886949,112.56098 78.016941,113.41536 79.153822,114.26285 79.208944,115.88205", mPath, scale);
        mPath = drawSvgPath("H 77.169447", mPath, scale);
        mPath = drawSvgPath("Q 77.114327,114.96565 76.370185,114.5798 75.874091,114.32487 75.136841,114.32487 74.316908,114.32487 73.827705,114.6556 73.338501,114.98632 73.338501,115.57888 73.338501,116.12321 73.820814,116.39192 74.130873,116.57107 75.143731,116.81223", mPath, scale);
        mPath = drawSvgPath("L 76.89384,117.23253", mPath, scale);
        mPath = drawSvgPath("Q 78.044501,117.50813 78.630168,117.96978 79.539673,118.68636 79.539673,120.04372 79.539673,121.43554 78.471693,122.35883 77.410604,123.27522 75.46757,123.27522 73.483195,123.27522 72.346314,122.37261 71.209432,121.4631 71.209432,119.87836", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 75.295315,112.5472", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 88.634727,114.6556", mPath, scale);
        mPath = drawSvgPath("H 83.260378", mPath, scale);
        mPath = drawSvgPath("V 116.81223", mPath, scale);
        mPath = drawSvgPath("H 88.193755", mPath, scale);
        mPath = drawSvgPath("V 118.57611", mPath, scale);
        mPath = drawSvgPath("H 83.260378", mPath, scale);
        mPath = drawSvgPath("V 121.1875", mPath, scale);
        mPath = drawSvgPath("H 88.882774", mPath, scale);
        mPath = drawSvgPath("V 123.0134", mPath, scale);
        mPath = drawSvgPath("H 81.186431", mPath, scale);
        mPath = drawSvgPath("V 112.85726", mPath, scale);
        mPath = drawSvgPath("H 88.634727", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 87.31181,109.98405 85.217192,112.04421", mPath, scale);
        mPath = drawSvgPath("H 83.797813", mPath, scale);
        mPath = drawSvgPath("L 85.12073,109.98405", mPath, scale);
        mPath.close();
        mPath = drawSvgPath("M 84.962255,112.85726", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        mPath = new Path();
        mPath = drawSvgPath("M 93.6625,194.07707", mPath, scale);
        mPath = drawSvgPath("C 116.57448,209.06292 114.74915,236.36117 106.09791,252.2854", mPath, scale);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            touchX = event.getX() - .5f * canvasSize;
            touchY = .5f * canvasSize - event.getY();
            Log.d("Coordinates", String.format("x: %f, y: %f", touchX, touchY));
            float outterRadius = .5f * canvasSize;
            float innerRadius = .25f * canvasSize;
            boolean inside = (touchX * touchX + touchY * touchY < outterRadius * outterRadius)
                    && (touchX * touchX + touchY * touchY > innerRadius * innerRadius);
            if (inside) {
                onTouchInsideSlice();
            }
        }
        return true;
        //return super.onTouchEvent(event);
    }

    protected void onTouchInsideSlice() {
        if (isEnabled()) {
            if (touchX < 0 && touchY > Math.tan(30f*Math.PI/180f)*touchX){
                highlightFraction(0);
            } else if (touchX > 0 && touchY > -Math.tan(30f*Math.PI/180f)*touchX){
                highlightFraction(1);
            } else {
                highlightFraction(2);
            }
        }

    }

    private void highlightFraction(int i) {
        initColors = mainColors.clone();
        initColors[i] = lightenColour(mainColors[i]);
        invalidate();
        if (clickOnThirdWheelListener != null) {
            clickOnThirdWheelListener.onClickThird(textButton[i]);
        }
    }

    public void setGrayColors() {
        initColors = grayColors.clone();
        invalidate();
    }

    public void colorize(){
        initColors = mainColors.clone();
        invalidate();
    }

    public void rearrangeColors() {
        shuffleColors();
        invalidate();
    }

    protected int lightenColour(int rgb) {
        // hue, saturation, value
        float[] hsv = new float[3];
        Color.colorToHSV(rgb, hsv);
        hsv[2] *= .5f;
        return Color.HSVToColor(hsv);
    }

    public Path drawSvgPath(String pathString, Path path, float scale) {
        if (pathString.charAt(0) == 'M') {
            pathString = pathString.replace("M", "").trim();
            String[] c = pathString.split(" ");
            String[] d = c[0].split(",");
            lastX = getCoord(d[0], scale);
            lastY = getCoord(d[1], scale);
            path.moveTo(lastX, lastY);
            if (c.length>1){
                for (int i = 1; i < c.length; i++) {
                    String[] d1 = c[i].split(",");
                    lastX = getCoord(d1[0], scale);
                    lastY = getCoord(d1[1], scale);
                    path.lineTo(lastX, lastY);
                }
            }

        } else if (pathString.charAt(0) == 'C') {
            pathString = pathString.replace("C", "").trim();
            String[] c = pathString.split(" ");
            for (int i = 0; i < c.length; i = i + 3) {
                String[] d1 = c[i].split(",");
                String[] d2 = c[i + 1].split(",");
                String[] d3 = c[i + 2].split(",");
                lastX = getCoord(d1[0], scale);
                lastY = getCoord(d1[1], scale);
                path.cubicTo(lastX, lastY,
                        getCoord(d2[0], scale), getCoord(d2[1], scale),
                        getCoord(d3[0], scale), getCoord(d3[1], scale));
            }
        } else if (pathString.charAt(0) == 'L') {
            pathString = pathString.replace("L", "").trim();
            String[] c = pathString.split(" ");
            for (int i = 0; i < c.length; i++) {
                String[] d1 = c[i].split(",");
                lastX = getCoord(d1[0], scale);
                lastY = getCoord(d1[1], scale);
                path.lineTo(lastX, lastY);
            }
        } else if (pathString.charAt(0) == 'm') {
            pathString = pathString.replace("m", "").trim();
            String[] c = pathString.split(" ");
            String[] d = c[0].split(",");
            path.rMoveTo(getCoord(d[0], scale),
                    getCoord(d[1], scale));
            if (c.length>1){
                for (int i = 1; i < c.length; i++) {
                    String[] d1 = c[i].split(",");
                    path.rLineTo(getCoord(d1[0], scale), getCoord(d1[1], scale));
                }
            }
        } else if (pathString.charAt(0) == 'c') {
            pathString = pathString.replace("c", "").trim();
            String[] c = pathString.split(" ");
            for (int i = 0; i < c.length; i = i + 3) {
                String[] d1 = c[i].split(",");
                String[] d2 = c[i + 1].split(",");
                String[] d3 = c[i + 2].split(",");
                path.rCubicTo(getCoord(d1[0], scale), getCoord(d1[1], scale),
                        getCoord(d2[0], scale), getCoord(d2[1], scale),
                        getCoord(d3[0], scale), getCoord(d3[1], scale));
            }
        } else if (pathString.charAt(0) == 'l') {
            pathString = pathString.replace("l", "").trim();
            String[] c = pathString.split(" ");
            for (int i = 0; i < c.length; i++) {
                String[] d1 = c[i].split(",");
                path.rLineTo(getCoord(d1[0], scale), getCoord(d1[1], scale));
            }
        } else if (pathString.charAt(0) == 'q') {
            pathString = pathString.replace("q", "").trim();
            String[] c = pathString.split(" ");
            for (int i = 0; i < c.length; i = i + 2) {
                String[] d1 = c[i].split(",");
                String[] d2 = c[i + 1].split(",");
                path.rQuadTo(getCoord(d1[0], scale), getCoord(d1[1], scale),
                        getCoord(d2[0], scale), getCoord(d2[1], scale));
            }
        } else if (pathString.charAt(0) == 'Q') {
            pathString = pathString.replace("Q", "").trim();
            String[] c = pathString.split(" ");
            for (int i = 0; i < c.length; i = i + 2) {
                String[] d1 = c[i].split(",");
                String[] d2 = c[i + 1].split(",");
                lastX = getCoord(d1[0], scale);
                lastY = getCoord(d1[1], scale);
                path.quadTo(lastX, lastY,
                        getCoord(d2[0], scale), getCoord(d2[1], scale));
            }
        } else if (pathString.charAt(0) == 'H') {
            pathString = pathString.replace("H", "").trim();
            lastX = getCoord(pathString, scale);
            path.lineTo(lastX, lastY);
        } else if (pathString.charAt(0) == 'V') {
            pathString = pathString.replace("V", "").trim();
            lastY = getCoord(pathString, scale);
            path.lineTo(lastX, lastY);
        }
        return path;
    }


    public float getCoord(String coord, float scale) {
        return scale * Float.parseFloat(coord) * canvasSize / sourceSize;
    }

    public interface ClickOnThirdWheelListener {
        void onClickThird(String answer);
    }

}
