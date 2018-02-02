package com.demoproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.demoproject.pojo.Cart;
import com.demoproject.pojo.Category;
import com.demoproject.pojo.Product;
import com.demoproject.pojo.Tax;
import com.demoproject.pojo.User;
import com.demoproject.pojo.Variant;
import com.demoproject.utils.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DB_Handler
extends SQLiteOpenHelper {
    private static final String CAT_ID = "category_id";
    private static final String COLOR = "color";
    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE listview(id INTEGER PRIMARY KEY,name TEXT NOT NULL)";
    private static final String CREATE_ORDER_HISTORY_TABLE = "CREATE TABLE order_history(id INTEGER PRIMARY KEY,product_id INTEGER NOT NULL,variant_id INTEGER NOT NULL,quantity INTEGER NOT NULL,email TEXT NOT NULL)";
    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE products(id INTEGER PRIMARY KEY,category_id INTEGER NOT NULL,name TEXT NOT NULL,added_on TEXT NOT NULL,tax_name TEXT NOT NULL,tax_value REAL NOT NULL,view_count INTEGER NOT NULL,order_count INTEGER NOT NULL,share_count INTEGER NOT NULL)";
    private static final String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE shopping_cart(id INTEGER PRIMARY KEY,product_id INTEGER NOT NULL,variant_id INTEGER NOT NULL,quantity INTEGER NOT NULL,email TEXT NOT NULL)";
    private static final String CREATE_SUBCATEGORIES_MAPPING_TABLE = "CREATE TABLE subcategories_mapping(id INTEGER PRIMARY KEY,category_id INTEGER NOT NULL,subcategory_id INTEGER NOT NULL)";
    private static final String CREATE_USER_TABLE = "CREATE TABLE user_details(email TEXT PRIMARY KEY,name TEXT NOT NULL,mobile_no TEXT NOT NULL,password TEXT NOT NULL)";
    private static final String CREATE_VARIANTS_TABLE = "CREATE TABLE variants(id INTEGER PRIMARY KEY,size TEXT,color TEXT NOT NULL,price TEXT NOT NULL,product_id INTEGER NOT NULL)";
    private static final String CREATE_WISHLIST_TABLE = "CREATE TABLE wishlist(id INTEGER PRIMARY KEY,product_id INTEGER NOT NULL,email TEXT NOT NULL)";
    private static final String CategoriesTable = "listview";
    private static final String DATABASE_NAME = "E-Commerce";
    private static final int DATABASE_VERSION = 1;
    private static final String DATE = "added_on";
    private static final String EMAIL = "email";
    private static final String ID = "id";
    private static final String MOBILE = "mobile_no";
    private static final String NAME = "name";
    public static final String ORDER_COUNT = "order_count";
    private static final String OrderHistoryTable = "order_history";
    private static final String PASSWORD = "password";
    private static final String PDT_ID = "product_id";
    private static final String PRICE = "price";
    private static final String ProductsTable = "products";
    private static final String QUANTITY = "quantity";
    public static final String SHARE_COUNT = "share_count";
    private static final String SIZE = "size";
    private static final String SUB_ID = "subcategory_id";
    private static final String ShoppingCartTable = "shopping_cart";
    private static final String SubCategoriesMappingTable = "subcategories_mapping";
    private static final String TAX_NAME = "tax_name";
    private static final String TAX_VALUE = "tax_value";
    private static final String UserTable = "user_details";
    private static final String VAR_ID = "variant_id";
    public static final String VIEW_COUNT = "view_count";
    private static final String VariantsTable = "variants";
    private static final String WishListTable = "wishlist";

    public DB_Handler(Context context) {
        super(context, "E-Commerce", null, 1);
    }

    private Variant getVariantDetailsById(int n) {
        String[] arrstring;
        Variant variant = new Variant();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT  * FROM variants WHERE id=?", arrstring = new String[]{String.valueOf((int)n)});
        if (cursor.moveToFirst()) {
            variant.setId(Integer.valueOf((int)cursor.getInt(cursor.getColumnIndex("id"))));
            variant.setColor(cursor.getString(cursor.getColumnIndex("color")));
            variant.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            variant.setSize((Object)cursor.getDouble(cursor.getColumnIndex("size")));
        }
        cursor.close();
        sQLiteDatabase.close();
        return variant;
    }

    private boolean isShortlistedItem(int n, String string2) {
        String[] arrstring;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT  * FROM wishlist WHERE email=? AND product_id=?", arrstring = new String[]{string2, String.valueOf((int)n)});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    public boolean deleteCartItem(int n) {
        String[] arrstring;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        if (sQLiteDatabase.delete("shopping_cart", "id=?", arrstring = new String[]{String.valueOf((int)n)}) > 0) {
            return true;
        }
        return false;
    }

    public void deleteCartItems() {
        this.getWritableDatabase().delete("shopping_cart", null, null);
    }

    public List<String> getAllColors() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT color FROM variants", null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add((Object)cursor.getString(cursor.getColumnIndex("color")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public List<String> getAllSizes() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT size FROM variants", null);
        if (cursor.moveToFirst()) {
            do {
                String string2;
                if ((string2 = cursor.getString(cursor.getColumnIndex("size"))) == null || string2.equalsIgnoreCase("null")) continue;
                arrayList.add((Object)string2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public int getCartItemCount(String string2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT  * FROM shopping_cart WHERE email=?", new String[]{string2});
        int n = cursor.getCount();
        cursor.close();
        sQLiteDatabase.close();
        return n;
    }

    public List<Cart> getCartItems(String string2) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT  * FROM shopping_cart WHERE email=?", new String[]{string2});
        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                int n = cursor.getInt(cursor.getColumnIndex("id"));
                int n2 = cursor.getInt(cursor.getColumnIndex("product_id"));
                int n3 = cursor.getInt(cursor.getColumnIndex("variant_id"));
                int n4 = cursor.getInt(cursor.getColumnIndex("quantity"));
                Product product = this.getProductDetailsById(n2, string2);
                Variant variant = this.getVariantDetailsById(n3);
                cart.setId(Integer.valueOf((int)n));
                cart.setItemQuantity(Integer.valueOf((int)n4));
                cart.setProduct(product);
                cart.setVariant(variant);
                arrayList.add((Object)cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public List<Category> getCategoryList() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM listview", null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                int n = cursor.getInt(cursor.getColumnIndex("id"));
                String string2 = cursor.getString(cursor.getColumnIndex("name"));
                String[] arrstring = new String[]{String.valueOf((int)n)};
                Cursor cursor2 = sQLiteDatabase.rawQuery("SELECT  * FROM subcategories_mapping WHERE subcategory_id=?", arrstring);
                if (!cursor2.moveToFirst()) {
                    category.setId(Integer.valueOf((int)n));
                    category.setName(string2);
                    arrayList.add((Object)category);
                }
                cursor2.close();
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public List<String> getColorBySelectedSize(int n, String string2) {
        String[] arrstring;
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT color FROM variants WHERE product_id=? AND size=?", arrstring = new String[]{String.valueOf((int)n), string2});
        if (cursor.moveToFirst()) {
            do {
                arrayList.add((Object)cursor.getString(cursor.getColumnIndex("color")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public List<Cart> getOrders(String string2) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT  * FROM order_history WHERE email=?", new String[]{string2});
        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                int n = cursor.getInt(cursor.getColumnIndex("id"));
                int n2 = cursor.getInt(cursor.getColumnIndex("product_id"));
                int n3 = cursor.getInt(cursor.getColumnIndex("variant_id"));
                int n4 = cursor.getInt(cursor.getColumnIndex("quantity"));
                Product product = this.getProductDetailsById(n2, string2);
                Variant variant = this.getVariantDetailsById(n3);
                cart.setId(Integer.valueOf((int)n));
                cart.setItemQuantity(Integer.valueOf((int)n4));
                cart.setProduct(product);
                cart.setVariant(variant);
                arrayList.add((Object)cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public List<String> getProductColorsById(int n) {
        String[] arrstring;
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT color FROM variants WHERE product_id=?", arrstring = new String[]{String.valueOf((int)n)});
        if (cursor.moveToFirst()) {
            do {
                arrayList.add((Object)cursor.getString(cursor.getColumnIndex("color")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public Product getProductDetailsById(int n, String string2) {
        String[] arrstring;
        Product product = new Product();
        Tax tax = new Tax();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT  * FROM products WHERE id=?", arrstring = new String[]{String.valueOf((int)n)});
        if (cursor.moveToFirst()) {
            product.setId(Integer.valueOf((int)cursor.getInt(cursor.getColumnIndex("id"))));
            product.setName(cursor.getString(cursor.getColumnIndex("name")));
            tax.setName(cursor.getString(cursor.getColumnIndex("tax_name")));
            tax.setValue(Double.valueOf((double)cursor.getDouble(cursor.getColumnIndex("tax_value"))));
            product.setShortlisted(Boolean.valueOf((boolean)this.isShortlistedItem(product.getId(), string2)));
            product.setTax(tax);
        }
        cursor.close();
        sQLiteDatabase.close();
        return product;
    }

    public String getProductPriceRangeById(int n) {
        String[] arrstring;
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT price FROM variants WHERE product_id=?", arrstring = new String[]{String.valueOf((int)n)});
        if (cursor.moveToFirst()) {
            do {
                arrayList.add((Object)Double.parseDouble((String)cursor.getString(cursor.getColumnIndex("price"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        double d = (Double)Collections.min((Collection)arrayList);
        double d2 = (Double)Collections.max((Collection)arrayList);
        if (d == d2) {
            return "Rs." + Util.formatDouble((double)d2);
        }
        return "Rs." + Util.formatDouble((double)d) + " - Rs." + Util.formatDouble((double)d2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public Variant getProductVariant(int n, String string2, String string3) {
        Cursor cursor;
        Variant variant = new Variant();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        if (string2 == null) {
            String[] arrstring = new String[]{String.valueOf((int)n), string3};
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM variants WHERE product_id=? AND color=?", arrstring);
        } else {
            String[] arrstring = new String[]{String.valueOf((int)n), string2, string3};
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM variants WHERE product_id=? AND size=? AND color=?", arrstring);
        }
        if (cursor.moveToFirst()) {
            int n2 = cursor.getInt(cursor.getColumnIndex("id"));
            String string4 = Util.formatDouble((double)Double.parseDouble((String)cursor.getString(cursor.getColumnIndex("price"))));
            variant.setId(Integer.valueOf((int)n2));
            variant.setPrice(string4);
        }
        cursor.close();
        sQLiteDatabase.close();
        return variant;
    }

    public List<Product> getProductsList(int var1_1, List<String> var2_2, List<String> var3_3, int var4_4, String var5_5) {
        throw new IllegalStateException("Decompilation failed");
    }

    public List<Product> getShortListedItems(String string2) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT  * FROM wishlist WHERE email=?", new String[]{string2});
        if (cursor.moveToFirst()) {
            do {
                int n = cursor.getInt(cursor.getColumnIndex("product_id"));
                Product product = this.getProductDetailsById(n, string2);
                product.setPrice_range(this.getProductPriceRangeById(n));
                arrayList.add((Object)product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public List<String> getSizeByProductId(int n) {
        String[] arrstring;
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT size FROM variants WHERE product_id=?", arrstring = new String[]{String.valueOf((int)n)});
        if (cursor.moveToFirst()) {
            do {
                String string2;
                if ((string2 = cursor.getString(cursor.getColumnIndex("size"))) == null || string2.equalsIgnoreCase("null")) continue;
                arrayList.add((Object)string2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public List<Category> getSubcategoryList(int n) {
        String[] arrstring;
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT  subcategory_id FROM subcategories_mapping WHERE category_id=?", arrstring = new String[]{String.valueOf((int)n)});
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.valueOf((int)cursor.getInt(cursor.getColumnIndex("subcategory_id"))));
                String[] arrstring2 = new String[]{String.valueOf((Object)category.getId())};
                Cursor cursor2 = sQLiteDatabase.rawQuery("SELECT  name FROM listview WHERE id= ?", arrstring2);
                if (cursor2.moveToFirst()) {
                    do {
                        category.setName(cursor2.getString(cursor2.getColumnIndex("name")));
                    } while (cursor2.moveToNext());
                    arrayList.add((Object)category);
                }
                cursor2.close();
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    public User getUser(String string2) {
        User user = new User();
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM user_details WHERE email=?", new String[]{string2});
        if (cursor.moveToFirst()) {
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setMobile(cursor.getString(cursor.getColumnIndex("mobile_no")));
        }
        cursor.close();
        sQLiteDatabase.close();
        return user;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void insertCategories(int n, String string2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Integer.valueOf((int)n));
        contentValues.put("name", string2);
        String[] arrstring = new String[]{String.valueOf((int)n)};
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM listview WHERE id=?", arrstring);
        boolean bl = cursor.moveToFirst();
        boolean bl2 = false;
        if (bl) {
            bl2 = true;
        }
        cursor.close();
        if (bl2) {
            String[] arrstring2 = new String[]{String.valueOf((int)n)};
            sQLiteDatabase.update("listview", contentValues, "id = ?", arrstring2);
        } else {
            sQLiteDatabase.insert("listview", null, contentValues);
        }
        sQLiteDatabase.close();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void insertChildCategoryMapping(int n, int n2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category_id", Integer.valueOf((int)n));
        contentValues.put("subcategory_id", Integer.valueOf((int)n2));
        String[] arrstring = new String[]{String.valueOf((int)n), String.valueOf((int)n2)};
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM subcategories_mapping WHERE category_id=? AND subcategory_id=?", arrstring);
        boolean bl = cursor.moveToFirst();
        int n3 = 0;
        boolean bl2 = false;
        if (bl) {
            bl2 = true;
            n3 = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        if (bl2) {
            String[] arrstring2 = new String[]{String.valueOf((int)n3)};
            sQLiteDatabase.update("subcategories_mapping", contentValues, "id = ?", arrstring2);
        } else {
            sQLiteDatabase.insert("subcategories_mapping", null, contentValues);
        }
        sQLiteDatabase.close();
    }

    public long insertIntoCart(int n, int n2, int n3, String string2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_id", Integer.valueOf((int)n));
        contentValues.put("variant_id", Integer.valueOf((int)n2));
        contentValues.put("quantity", Integer.valueOf((int)n3));
        contentValues.put("email", string2);
        String[] arrstring = new String[]{string2, String.valueOf((int)n), String.valueOf((int)n2)};
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM shopping_cart WHERE email=? AND product_id=? AND variant_id=?", arrstring);
        if (cursor.moveToFirst()) {
            cursor.close();
            return -1;
        }
        cursor.close();
        return sQLiteDatabase.insert("shopping_cart", null, contentValues);
    }

    public void insertOrderHistory(List<Cart> list, String string2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        for (int i = 0; i < list.size(); ++i) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("product_id", ((Cart)list.get(i)).getProduct().getId());
            contentValues.put("variant_id", ((Cart)list.get(i)).getVariant().getId());
            contentValues.put("quantity", ((Cart)list.get(i)).getItemQuantity());
            contentValues.put("email", string2);
            sQLiteDatabase.insert("order_history", null, contentValues);
        }
        sQLiteDatabase.close();
    }

    public void insertProducts(int n, int n2, String string2, String string3, String string4, Double d) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Integer.valueOf((int)n));
        contentValues.put("category_id", Integer.valueOf((int)n2));
        contentValues.put("name", string2);
        contentValues.put("added_on", string3);
        contentValues.put("tax_name", string4);
        contentValues.put("tax_value", d);
        String[] arrstring = new String[]{String.valueOf((int)n)};
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM products WHERE id=?", arrstring);
        boolean bl = cursor.moveToFirst();
        boolean bl2 = false;
        if (bl) {
            bl2 = true;
        }
        cursor.close();
        if (bl2) {
            String[] arrstring2 = new String[]{String.valueOf((int)n)};
            sQLiteDatabase.update("products", contentValues, "id = ?", arrstring2);
            sQLiteDatabase.close();
            return;
        }
        contentValues.put("view_count", Integer.valueOf((int)0));
        contentValues.put("order_count", Integer.valueOf((int)0));
        contentValues.put("share_count", Integer.valueOf((int)0));
        sQLiteDatabase.insert("products", null, contentValues);
        sQLiteDatabase.close();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void insertVariants(int n, String string2, String string3, String string4, int n2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Integer.valueOf((int)n));
        contentValues.put("size", string2);
        contentValues.put("color", string3);
        contentValues.put("price", string4);
        contentValues.put("product_id", Integer.valueOf((int)n2));
        String[] arrstring = new String[]{String.valueOf((int)n)};
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM variants WHERE id=?", arrstring);
        boolean bl = cursor.moveToFirst();
        boolean bl2 = false;
        if (bl) {
            bl2 = true;
        }
        cursor.close();
        if (bl2) {
            String[] arrstring2 = new String[]{String.valueOf((int)n)};
            sQLiteDatabase.update("variants", contentValues, "id = ?", arrstring2);
        } else {
            sQLiteDatabase.insert("variants", null, contentValues);
        }
        sQLiteDatabase.close();
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE user_details(email TEXT PRIMARY KEY,name TEXT NOT NULL,mobile_no TEXT NOT NULL,password TEXT NOT NULL)");
        sQLiteDatabase.execSQL("CREATE TABLE listview(id INTEGER PRIMARY KEY,name TEXT NOT NULL)");
        sQLiteDatabase.execSQL("CREATE TABLE products(id INTEGER PRIMARY KEY,category_id INTEGER NOT NULL,name TEXT NOT NULL,added_on TEXT NOT NULL,tax_name TEXT NOT NULL,tax_value REAL NOT NULL,view_count INTEGER NOT NULL,order_count INTEGER NOT NULL,share_count INTEGER NOT NULL)");
        sQLiteDatabase.execSQL("CREATE TABLE variants(id INTEGER PRIMARY KEY,size TEXT,color TEXT NOT NULL,price TEXT NOT NULL,product_id INTEGER NOT NULL)");
        sQLiteDatabase.execSQL("CREATE TABLE subcategories_mapping(id INTEGER PRIMARY KEY,category_id INTEGER NOT NULL,subcategory_id INTEGER NOT NULL)");
        sQLiteDatabase.execSQL("CREATE TABLE order_history(id INTEGER PRIMARY KEY,product_id INTEGER NOT NULL,variant_id INTEGER NOT NULL,quantity INTEGER NOT NULL,email TEXT NOT NULL)");
        sQLiteDatabase.execSQL("CREATE TABLE shopping_cart(id INTEGER PRIMARY KEY,product_id INTEGER NOT NULL,variant_id INTEGER NOT NULL,quantity INTEGER NOT NULL,email TEXT NOT NULL)");
        sQLiteDatabase.execSQL("CREATE TABLE wishlist(id INTEGER PRIMARY KEY,product_id INTEGER NOT NULL,email TEXT NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS user_details");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS listview");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS products");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS variants");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS subcategories_mapping");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS order_history");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS shopping_cart");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS wishlist");
        this.onCreate(sQLiteDatabase);
    }

    public long registerUser(String string2, String string3, String string4, String string5) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", string2);
        contentValues.put("email", string3);
        contentValues.put("mobile_no", string4);
        contentValues.put("password", string5);
        return sQLiteDatabase.insert("user_details", null, contentValues);
    }

    public boolean removeShortlistedItem(int n, String string2) {
        String[] arrstring;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        if (sQLiteDatabase.delete("wishlist", "product_id=? AND email=?", arrstring = new String[]{String.valueOf((int)n), string2}) > 0) {
            return true;
        }
        return false;
    }

    public long shortlistItem(int n, String string2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_id", Integer.valueOf((int)n));
        contentValues.put("email", string2);
        return sQLiteDatabase.insert("wishlist", null, contentValues);
    }

    public void updateCounts(String string2, int n, int n2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(string2, Integer.valueOf((int)n));
        String[] arrstring = new String[]{String.valueOf((int)n2)};
        sQLiteDatabase.update("products", contentValues, "id = ?", arrstring);
        sQLiteDatabase.close();
    }

    public void updateItemQuantity(int n, int n2) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", Integer.valueOf((int)n));
        String[] arrstring = new String[]{String.valueOf((int)n2)};
        sQLiteDatabase.update("shopping_cart", contentValues, "id=?", arrstring);
        sQLiteDatabase.close();
    }
}

