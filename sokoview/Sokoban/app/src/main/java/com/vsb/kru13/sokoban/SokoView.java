package com.vsb.kru13.sokoban;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

    int move_count = 0;

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
        game_set_up();
        invalidate();
    }

    public void load_last(){
        SharedPreferences pref = getContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        current_index = pref.getInt("D_current_index", 0);
        move_count = pref.getInt("D_move_count", 0);
        box_count = pref.getInt("D_box_count", 0);
        boxok_count =  pref.getInt("D_boxok_count", 0);
        lx =  pref.getInt("D_lx", 0);
        ly =  pref.getInt("D_ly", 0);
        hero_x = pref.getInt("D_hero_x", 0);
        hero_y = pref.getInt("D_hero_y", 0);

        backup = new int[lx * ly];
        level = backup.clone();
        boxes = backup.clone();

        string_to_arr("D_level", level);
        string_to_arr("D_boxes", boxes);
        string_to_arr("D_backup", backup);

        box_width = getWidth() / ly;
        box_height = getHeight() / lx;
        invalidate();

        Toast.makeText(getContext(), "LOADED", Toast.LENGTH_SHORT).show();
    }

    public void save(){
        SharedPreferences pref = getContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        int i = pref.getInt("D_current_index", 0);

        pref.edit().putInt("D_current_index", current_index).apply();
        pref.edit().putInt("D_move_count", move_count).apply();
        pref.edit().putInt("D_box_count", box_count).apply();
        pref.edit().putInt("D_boxok_count", boxok_count).apply();
        pref.edit().putInt("D_lx", lx).apply();
        pref.edit().putInt("D_ly", ly).apply();
        pref.edit().putInt("D_hero_x", hero_x).apply();
        pref.edit().putInt("D_hero_y", hero_y).apply();
        pref.edit().putString("D_level", arr_to_string(level)).apply();
        pref.edit().putString("D_boxes", arr_to_string(boxes)).apply();
        pref.edit().putString("D_backup", arr_to_string(backup)).apply();

        Toast.makeText(getContext(), "SAVED", Toast.LENGTH_SHORT).show();
    }

    String arr_to_string(int [] arr){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            stringBuilder.append(arr[i]);
        }
        return stringBuilder.toString();
    }

    private void string_to_arr(String name, int [] arr) {
        SharedPreferences pref = getContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String data = pref.getString(name, "");
        for(int i = 0; i < arr.length; i++){
            arr[i] = Character.getNumericValue(data.charAt(i));
        }
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

        game_set_up();

        box_width = getWidth() / ly;
        box_height = getHeight() / lx;
        invalidate();
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
        move_count = 0;

        boxes = new int[lx * ly];
        level = backup.clone();

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

    private boolean check_count() {
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
                game_won();
                load(current_index);
                return true;
            }
            else{
                Toast.makeText(getContext(), left + " boxes left", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }

    private void game_won(){
        MyDatabase db = new MyDatabase(getContext());
        String level_id = String.valueOf(current_index + 1);
        int minMoves = Integer.parseInt(db.getMoves(level_id));

        if(minMoves == 0 || minMoves > move_count){
            int c = db.setMoves(level_id, String.valueOf(move_count));
        }

        if(current_index < levelData.size() - 1){
            current_index++;
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
        move_count++;
        int new_hero_x = hero_x + x;
        int new_hero_y = hero_y + y;

        if (level[pos(new_hero_x, new_hero_y)] == WALL) return;

        if (boxes[pos(new_hero_x, new_hero_y)] == BOX) {

            if (!move_box(new_hero_x, new_hero_y, x, y)) return;

            if(check_count()) return;
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
