package com.near.kevin.PKLMobileV2;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.near.kevin.PKLMobileV2.db.ProductDbHelper;
import com.near.kevin.PKLMobileV2.service.PklAccountManager;

import static android.widget.Toast.makeText;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private ContentValues accountIn;
    private ProductDbHelper productDbHelper;

    private EditText name;
    private EditText basePrice;
    private EditText sellPrice;
    
    private boolean alreadyAdded;
    private int productId;
    private String emailData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        this.accountIn = PklAccountManager.getLoggedIn(ProductDetailActivity.this);
        this.productDbHelper = new ProductDbHelper(ProductDetailActivity.this);

        this.name = (EditText) findViewById(R.id.activity_product_detail_name);
        this.basePrice = (EditText) findViewById(R.id.activity_product_detail_base_price);
        this.sellPrice = (EditText) findViewById(R.id.activity_product_detail_sell_price);

        this.productId = getIntent().getIntExtra(PRODUCT_ID, -1);
        this.alreadyAdded = this.productId < 0;
        this.emailData = this.accountIn.getAsString(PklAccountManager.LOGGED_IN_EMAIL);

        if (!this.alreadyAdded) {
            ContentValues product = this.productDbHelper.getProduct(this.productId);
            this.name.setText(product.getAsString(ProductDbHelper.COLUMN_NAME_NAME));
            this.basePrice.setText(product.getAsString(ProductDbHelper.COLUMN_NAME_BASE_PRICE));
            this.sellPrice.setText(product.getAsString(ProductDbHelper.COLUMN_NAME_SELL_PRICE));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductDetailActivity.this, CatalogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private String getNameField() {
        return this.name.getText().toString().trim();
    }

    private String getBasePriceField() {
        return this.basePrice.getText().toString().trim();
    }

    private String getSellPriceField() {
        return this.sellPrice.getText().toString().trim();
    }

    public void save(View view) {
        String productName = getNameField();
        int basePrice = 0;
        try {
            basePrice = Integer.parseInt(getBasePriceField());
        } catch (NumberFormatException e) {
        }
        int sellPrice = 0;
        try {
            sellPrice = Integer.parseInt(getSellPriceField());
        } catch (NumberFormatException e) {
        }
        if (productName.equals("")) {
            makeText(ProductDetailActivity.this, "Please fill in the product name", Toast.LENGTH_LONG).show();
        } else if (basePrice == 0) {
            makeText(ProductDetailActivity.this, "Please fill in the product base price", Toast.LENGTH_LONG).show();
        } else if (sellPrice == 0) {
            makeText(ProductDetailActivity.this, "Please fill in the product sell price", Toast.LENGTH_LONG).show();
        } else if (alreadyAdded) {
            this.productDbHelper.insertProduct(productName, basePrice, sellPrice, this.emailData);
            makeText(ProductDetailActivity.this, "Product successfully added", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            this.productDbHelper.updateProduct(this.productId, productName, basePrice, sellPrice);
            makeText(ProductDetailActivity.this, "Product successfully added", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    public void add(View view) {
        String productName = getNameField();
        int basePrice = 0;
        try {
            basePrice = Integer.parseInt(getBasePriceField());
        } catch (NumberFormatException e) {
        }
        int sellPrice = 0;
        try {
            sellPrice = Integer.parseInt(getSellPriceField());
        } catch (NumberFormatException e) {
        }
        if (productName.equals("")) {
            makeText(ProductDetailActivity.this, "Please fill in the product name", Toast.LENGTH_LONG).show();
        } else if (basePrice == 0) {
            makeText(ProductDetailActivity.this, "Please fill in the product base price", Toast.LENGTH_LONG).show();
        } else if (sellPrice == 0) {
            makeText(ProductDetailActivity.this, "Please fill in the product sell price", Toast.LENGTH_LONG).show();
        } else if (alreadyAdded) {
            this.productDbHelper.insertProduct(productName, basePrice, sellPrice, this.emailData);
            makeText(ProductDetailActivity.this, "Product successfully added", Toast.LENGTH_LONG).show();
            this.name.getText().clear();
            this.basePrice.getText().clear();
            this.sellPrice.getText().clear();
        } else {
            this.productDbHelper.updateProduct(this.productId, productName, basePrice, sellPrice);
            makeText(ProductDetailActivity.this, "Product successfully updated", Toast.LENGTH_LONG).show();
            this.productId = -1;
            this.alreadyAdded = true;
            this.name.getText().clear();
            this.basePrice.getText().clear();
            this.sellPrice.getText().clear();
        }
    }

    public void back(View view) {
        onBackPressed();
    }
}
