package rhyme.a.is.nine.foodmanager.database;

import android.content.Context;

import java.util.List;

import rhyme.a.is.nine.foodmanager.product.Product;

/**
 * Created by martinmaritsch on 06/05/15.
 */
public class ShoppingListDatabase extends ProductDatabase {

    public static void writeToFile(Context context) {
        writeFile("shoppingList.dbase", context, products);
    }

    public static void readFromFile(Context context) {
        Object products_object = readFile("shoppingList.dbase", context);

        if(products_object != null)
            products = (List<Product>) products_object;
    }
}