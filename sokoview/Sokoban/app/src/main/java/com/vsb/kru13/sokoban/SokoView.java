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

/**
 * Created by kru13 on 12.10.16.
 */


public class SokoView extends View {
    int FLOOR = 0;
    int WALL = 1;
    int BOX = 2;
    int GOAL = 3;
    int HERO = 4;
    int BOXOK = 5;

    Bitmap[] bmp;

    int box_count = 0;
    int boxok_count = 0;

    int lx = 10;
    int ly = 10;

    int hero_x;
    int hero_y;

    float finger_x;
    float finger_y;

    int box_width;
    int box_height;

    private final int[] boxes = new int[lx * ly];

    private final int[] level = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0,
            1, 0, 2, 3, 3, 2, 1, 0, 1, 0,
            1, 0, 1, 3, 2, 3, 2, 0, 1, 0,
            1, 0, 2, 3, 3, 2, 4, 0, 1, 0,
            1, 0, 1, 3, 2, 3, 2, 0, 1, 0,
            1, 0, 2, 3, 3, 2, 1, 0, 1, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

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

    void init() {
        bmp = new Bitmap[6];


        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

        game_set_up();
    }

    void game_set_up() {
        for (int x = 0; x < lx; x++) {
            for (int y = 0; y < ly; y++) {
                boxes[pos(x, y)] = FLOOR;

                if (level[pos(x, y)] == HERO) {
                    hero_x = x;
                    hero_y = y;
                    level[pos(x, y)] = FLOOR;
                } else if (level[pos(x, y)] == BOX) {
                    level[pos(x, y)] = FLOOR;
                    boxes[pos(x, y)] = BOX;
                    box_count++;
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
            Toast.makeText(getContext(), left + " boxes left", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean move_box(int new_x, int new_y, int x, int y) {
        int next_pos_x = new_x + x;
        int next_pos_y = new_y + y;

        if (level[pos(next_pos_x, next_pos_y)] == WALL) return false;

        if (boxes[pos(next_pos_x, next_pos_y)] == BOX) {

            if (!move_box(next_pos_x, next_pos_y, x, y)) return false;
        }

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
