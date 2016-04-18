package cn.gzw.sliddingmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SliddingMenuView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (SliddingMenuView) LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        setContentView(view);
        view.setReservedWidth(200);
        view.setAlpha(true);
        view.setScal(true);
    }
    public void click(View v){
        view.openMenu();
    }
}
