package com.vsb.kru13.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class ShowView extends View {
    int FLOOR = 0;
    int WALL = 1;
    int BOX = 2;
    int GOAL = 3;
    int HERO = 4;
    int BOXOK = 5;
    int HEROOK = 4;

    Bitmap[] bmp;
    String data;

    int lx;
    int ly;

    public ShowView(Context context) {
        super(context);
        init();
    }

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        bmp = new Bitmap[6];
        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);
    }

    public void load(String data){
        this.data = data;
    }

    private int char_to_int(char charAt) {
        if(charAt == ' ') return FLOOR;
        if(charAt == '#') return WALL;
        if(charAt == '.') return GOAL;
        if(charAt == '$') return BOX;
        if(charAt == '*') return BOXOK;
        if(charAt == '@') return HERO;
        if(charAt == '+') return HEROOK;
        return FLOOR;
    }

    protected void onDraw(Canvas canvas) {
//        int box_width = getWidth() / ly;
//        int box_height = getHeight() / lx;
        int box_width = 20;
        int box_height = 20;

        String text = data;
        String[] lines_old = text.split("\n", 0);
        String[] lines = Arrays.copyOfRange(lines_old, 1, lines_old.length);
        lx = lines.length;
        ly = lines[0].length();
        for(String line : lines){
            ly = Math.max(ly, line.length());
        }

        for(int x = 0; x < lines.length; x++){
            String line = lines[x];
            for(int y = 0; y < line.length(); y++){
                char c = line.charAt(y);
                int value = char_to_int(c);

                canvas.drawBitmap(bmp[value], null,
                        new Rect(y * box_width,
                                x * box_height,
                                (y + 1) * box_width,
                                (x + 1) * box_height), null);
            }
        }
    }
}
