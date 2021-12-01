package edu.sjsu.android.stockmanagerproto3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appabr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.homeOp){
            NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
            assert navHost != null;
            NavController controller = navHost.getNavController();
            controller.navigate(R.id.action_global_homeFragment);
        }
        else if(item.getItemId() == R.id.watchlistOp){
            NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
            assert navHost != null;
            NavController controller = navHost.getNavController();
            controller.navigate(R.id.action_global_watchFragment);
        }
        else if(item.getItemId() == R.id.uninstall){
            this.uninstall();
        }
        return super.onOptionsItemSelected(item);
    }

    public void uninstall(){
        Intent delete = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + getPackageName()));
        startActivity(delete);
    }
}