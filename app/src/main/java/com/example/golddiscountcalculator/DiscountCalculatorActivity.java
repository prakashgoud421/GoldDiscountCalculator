package com.example.golddiscountcalculator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.DialogInterface;
import android.widget.Button;

public class DiscountCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "discount.txt";
    Button buttonCalculate,buttonShowPrice, buttonPrintPaper, buttonSaveFile;
    EditText editTextGoldPrice, editTextWeight, editTextDiscount ;
    TextView textViewDiscount,textViewWelcomeText;
    LinearLayout llDiscountlayout;
    int discountPrice;
    int grams,discount,price = 0;
    Boolean isPrivilegedUser;
    String userName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_activity);
        inItViews();
    }

    private void inItViews() {

        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonShowPrice = findViewById(R.id.buttonGetPrice);
        buttonPrintPaper = findViewById(R.id.buttonPrintFile);
        buttonSaveFile = findViewById(R.id.buttonSaveFile);

        editTextGoldPrice = findViewById(R.id.goldPriceEditText);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextDiscount = findViewById(R.id.editTextDiscount);

        textViewDiscount = findViewById(R.id.textViewDiscount);
        textViewWelcomeText = findViewById(R.id.welcomeText);

        llDiscountlayout = findViewById(R.id.discountLayout);

        buttonCalculate.setOnClickListener(this);
        buttonShowPrice.setOnClickListener(this);
        buttonPrintPaper.setOnClickListener(this);
        buttonSaveFile.setOnClickListener(this);

        intent = getIntent();
        userName = intent.getStringExtra("Name");
        isPrivilegedUser = intent.getBooleanExtra("isPrivilegedUser",false);

        if(!userName.isEmpty()){
            textViewWelcomeText.setText("Welocme ! "+userName+ " Your'e "+(isPrivilegedUser.equals(true) ? "Previlged User" : "regular User"));
        }

        if(isPrivilegedUser){
        llDiscountlayout.setVisibility(View.VISIBLE);
        }else{
            llDiscountlayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonCalculate:
                 calculatePrice();
                break;
            case R.id.buttonGetPrice:
               String text = " The Discount Price is "+calculatePrice();
               showTextonPopUp(text);
                break;
            case R.id.buttonPrintFile:
                String textToPrint = String.valueOf(calculatePrice());
                printPaper(textToPrint);
                break;
            case R.id.buttonSaveFile:
                String textToSave = String.valueOf(calculatePrice());
                saveToFile(textToSave);
                break;
        }
    }

    private int calculatePrice() {

        if(!TextUtils.isEmpty(editTextGoldPrice.getText())&& !TextUtils.isEmpty(editTextGoldPrice.getText()))
        {
            grams = Integer.valueOf(editTextWeight.getText().toString());
            price = Integer.valueOf(editTextGoldPrice.getText().toString());
        } else {
            showToast("Enter values to calculate");
        }
        if(isPrivilegedUser && !TextUtils.isEmpty(editTextDiscount.getText())){
            discount = Integer.valueOf(editTextDiscount.getText().toString());
        }else{
            showToast("Default discount is 2%");
            discount = 2;
        }
        //calculating final Discount price
        int s = 100 - discount;
        discountPrice = s*(grams*price)/100;

        textViewDiscount.setText("Total Price is : "+discountPrice);

        return discountPrice;
    }

    public void saveToFile(String text) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
            showToast("Saved to \" + getFilesDir() + \"/\" "+ FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void printPaper(String text){
        showToast("Feature not implemented . Will be available Soon ! ");
    }
    private void showTextonPopUp(String text) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(text);
        dialog.setTitle("Discount Price");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        showToast("Thank You !");
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private void showToast(String toast){
        Toast.makeText(this, toast,
                Toast.LENGTH_SHORT).show();
    }
}