package com.vsb.kru13.sokoban;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kru13 on 12.10.16.
 */


public class SokoView extends View {
    static int current_index = 0;
    static ArrayList<String> names = new ArrayList<>();
    static ArrayList<String> levelData = new ArrayList<>();

    int FLOOR = 0;
    int WALL = 1;
    int BOX = 2;
    int GOAL = 3;
    int HERO = 4;
    int BOXOK = 5;
    int HEROOK = 6;

    Bitmap[] bmp;

    int box_count = 0;
    int boxok_count = 0;

    int lx, ly;

    int hero_x, hero_y;

    float finger_x, finger_y;

    int box_width;
    int box_height;

    private int[] level;

    private int[] boxes;

    private int[] backup;

    public SokoView(Context context) {
        super(context);
        init();
    }

    public SokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void reset() {
        level = backup.clone();
        game_set_up();
        invalidate();
    }

    void init() {
        bmp = new Bitmap[6];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);
    }

    public void load(int index) {
        current_index = index;
        String text = levelData.get(index);
        String[] lines_old = text.split("\n", 0);
        String[] lines = Arrays.copyOfRange(lines_old, 1, lines_old.length);
        lx = lines.length;
        ly = lines[0].length();
        for(String line : lines){
            ly = Math.max(ly, line.length());
        }

        backup = new int[lx * ly];
        for(int x = 0; x < lines.length; x++){
            String line = lines[x];
            for(int y = 0; y < line.length(); y++){
                backup[pos(x, y)] = char_to_int(line.charAt(y));
            }
        }

        boxes = new int[lx * ly];
        level = backup.clone();
        game_set_up();

        box_width = getWidth() / ly;
        box_height = getHeight() / lx;
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

    void game_set_up() {
        box_count = 0;
        boxok_count = 0;

        for (int x = 0; x < lx; x++) {
            for (int y = 0; y < ly; y++) {
                int c = backup[pos(x, y)];
                if (c == HERO) {
                    hero_x = x;
                    hero_y = y;
                    level[pos(x, y)] = FLOOR;

                } else if (c == HEROOK){
                    hero_x = x;
                    hero_y = y;
                    level[pos(x, y)] = GOAL;
                }
                else if (c == BOX) {
                    boxes[pos(x, y)] = BOX;
                    level[pos(x, y)] = FLOOR;
                    box_count++;
                }
                else if(c == BOXOK){
                    boxes[pos(x, y)] = BOX;
                    level[pos(x, y)] = GOAL;
                    box_count++;
                    boxok_count++;
                }
            }
        }
    }

    int pos(int x, int y) {
        return x * ly + y;
    }

    private void check_count() {
        int new_boxok_count = 0;
        for (int x = 0; x < lx; x++) {
            for (int y = 0; y < ly; y++) {
                if (boxes[pos(x, y)] == BOX & level[pos(x, y)] == GOAL) {
                    new_boxok_count++;
                }
            }
        }

        if (new_boxok_count != boxok_count) {
            boxok_count = new_boxok_count;
            int left = box_count - boxok_count;
            if(left == 0){
                Toast.makeText(getContext(), "LEVEL COMPLETED", Toast.LENGTH_LONG).show();
                if(current_index < levelData.size() - 1){
                    current_index++;
                    load(current_index);
                }
            }
            else{
                Toast.makeText(getContext(), left + " boxes left", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean move_box(int new_x, int new_y, int x, int y) {
        int next_pos_x = new_x + x;
        int next_pos_y = new_y + y;

        if (level[pos(next_pos_x, next_pos_y)] == WALL) return false;

        if (boxes[pos(next_pos_x, next_pos_y)] == BOX) return false;

        boxes[pos(next_pos_x, next_pos_y)] = BOX;
        boxes[pos(new_x, new_y)] = FLOOR;
        return true;
    }

    private void move(int x, int y) {
        int new_hero_x = hero_x + x;
        int new_hero_y = hero_y + y;

        if (level[pos(new_hero_x, new_hero_y)] == WALL) return;

        if (boxes[pos(new_hero_x, new_hero_y)] == BOX) {

            if (!move_box(new_hero_x, new_hero_y, x, y)) return;

            check_count();
        }

        hero_x = new_hero_x;
        hero_y = new_hero_y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            finger_x = event.getX();
            finger_y = event.getY();
        }
        if (action == MotionEvent.ACTION_UP) {
            finger_x -= event.getX();
            finger_y -= event.getY();

            if (Math.abs(finger_x) > Math.abs(finger_y)) {
                //RIGHT
                if (finger_x > 0) {
                    move(0, -1);
                }//LEFT
                else {
                    move(0, 1);
                }
            } else {
                //UP
                if (finger_y > 0) {
                    move(-1, 0);
                }//DOWN
                else {
                    move(1, 0);
                }
            }

            invalidate();
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        box_width = w / ly;
        box_height = h / lx;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        for (int x = 0; x < lx; x++) {
            for (int y = 0; y < ly; y++) {
                int num = level[pos(x, y)];
                if (boxes[pos(x, y)] == BOX) {
                    num = (num == GOAL) ? BOXOK : BOX;
                }

                canvas.drawBitmap(bmp[num], null,
                        new Rect(y * box_width,
                                x * box_height,
                                (y + 1) * box_width,
                                (x + 1) * box_height), null);
            }
        }

        canvas.drawBitmap(bmp[HERO], null,
                new Rect(hero_y * box_width,
                        hero_x * box_height,
                        (hero_y + 1) * box_width,
                        (hero_x + 1) * box_height), null);
    }
}
