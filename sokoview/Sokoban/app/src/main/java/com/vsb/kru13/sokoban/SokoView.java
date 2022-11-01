package com.vsb.kru13.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by kru13 on 12.10.16.
 */
public class SokoView extends View{

    Bitmap[] bmp;

    int lx = 10;
    int ly = 10;

    int box_width;
    int box_height;

    private int boxes[];

    private int level[] = {
            1,1,1,1,1,1,1,1,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,0,2,3,3,2,1,0,1,0,
            1,0,1,3,2,3,2,0,1,0,
            1,0,2,3,3,2,4,0,1,0,
            1,0,1,3,2,3,2,0,1,0,
            1,0,2,3,3,2,1,0,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,1,1,1,1,1,1,1,1,0,
            0,0,0,0,0,0,0,0,0,5
    };

    public SokoView(Context context) {
        super(context);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        bmp = new Bitmap[6];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            float myX = event.getX();
            Toast.makeText(getContext(), "Action Down " + myX, Toast.LENGTH_SHORT).show();

            level[0] = 5;
            invalidate();
        }
        if(action == MotionEvent.ACTION_UP){
            float myX = event.getX();
            Toast.makeText(getContext(), "Action Up " + myX, Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        box_width = w / ly;
        box_height = h / lx;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {
                canvas.drawBitmap(bmp[level[i*10 + j]], null,
                        new Rect(j* box_width, i* box_height,(j+1)* box_width, (i+1)* box_height), null);
            }
        }

    }
}
