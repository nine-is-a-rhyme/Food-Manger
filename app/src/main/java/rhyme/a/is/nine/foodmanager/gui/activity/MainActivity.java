package rhyme.a.is.nine.foodmanager.gui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import rhyme.a.is.nine.foodmanager.R;
import rhyme.a.is.nine.foodmanager.database.PriceDatabase;
import rhyme.a.is.nine.foodmanager.database.ProductDatabase;
import rhyme.a.is.nine.foodmanager.gui.TabsPagerAdapter;
import rhyme.a.is.nine.foodmanager.gui.fragment.FridgeFragment;
import rhyme.a.is.nine.foodmanager.gui.fragment.ShoppingListFragment;
import rhyme.a.is.nine.foodmanager.product.PriceEntity;
import rhyme.a.is.nine.foodmanager.product.Product;
import rhyme.a.is.nine.foodmanager.database.RecipeDatabase;
import rhyme.a.is.nine.foodmanager.gui.fragment.ShoppingListFragment;
import rhyme.a.is.nine.foodmanager.gui.fragment.RecipeFragment;

public class MainActivity extends ActionBarActivity implements
        ActionBar.TabListener {


    public static ProductDatabase fridgeDatabase = null;
    public static ProductDatabase shoppingListDatabase = null;
    public static RecipeDatabase recipeDatabase = null;
    public static ProductDatabase historyDatabase = null;
    public static PriceDatabase priceDatabase = null;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Kühlschrank", "Einkaufsliste", "Rezepte", "Preise"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        int tabId = 0;
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this).setTag(tabId));
            tabId++;
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        fridgeDatabase = new ProductDatabase("fridge.db");
        shoppingListDatabase = new ProductDatabase("shopping_list.db");
        historyDatabase = new ProductDatabase("history.db");
        priceDatabase = new PriceDatabase("prices.db");
        recipeDatabase = new RecipeDatabase("ichkoche.json");

        fridgeDatabase.readFromFile(getBaseContext());
        shoppingListDatabase.readFromFile(getBaseContext());
        historyDatabase.readFromFile(getBaseContext());
        priceDatabase.readFromFile(getBaseContext());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -8);
        priceDatabase.addPriceEntity(new PriceEntity("ad", 1.23f, new Date()));
        priceDatabase.addPriceEntity(new PriceEntity("asfd", 12.23f, cal.getTime()));
        recipeDatabase.readFromFile(getBaseContext());    
}

    @Override
    public void onDestroy() {
        fridgeDatabase.writeToFile(getBaseContext());
        shoppingListDatabase.writeToFile(getBaseContext());
        historyDatabase.writeToFile(getBaseContext());
        priceDatabase.readFromFile(getBaseContext());

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fridgeDatabase.writeToFile(getBaseContext());
        shoppingListDatabase.writeToFile(getBaseContext());
        historyDatabase.writeToFile(getBaseContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_tab, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    public void onMinusButtonFridgeClicked(View v) {
        Product product = (Product) FridgeFragment.getAdapter().getItem((int) v.getTag());
        shoppingListDatabase.addProduct(new Product(product.getName(), product.getCategory(), product.getCategory(), product.getSize(), 1));
        FridgeFragment.getAdapter().decreaseProductCount((int) v.getTag());
        FridgeFragment.getAdapter().notifyDataSetChanged();
        ShoppingListFragment.getAdapter().notifyDataSetChanged();
    }

    public void onPlusButtonFridgeClicked(View v) {
        FridgeFragment.getAdapter().increaseProductCount((int) v.getTag());
        FridgeFragment.getAdapter().notifyDataSetChanged();
    }

    public void onMinusButtonShoppingListClicked(View v) {
        ShoppingListFragment.getAdapter().decreaseProductCount((int) v.getTag());
        ShoppingListFragment.getAdapter().notifyDataSetChanged();
    }

    public void onPlusButtonShoppingListClicked(View v) {
        ShoppingListFragment.getAdapter().increaseProductCount((int) v.getTag());
        ShoppingListFragment.getAdapter().notifyDataSetChanged();
    }

}