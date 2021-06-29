package com.example.golddiscountcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button buttonLogin,buttonCancel;
    EditText editTextUserName, editTextPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inItViews();
    }


    private void inItViews() {
        
        buttonLogin = findViewById(R.id.button_login);
        buttonCancel = findViewById(R.id.button_cancel);
        
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextUserPassword);

        buttonLogin.setOnClickListener((View.OnClickListener) this);
        buttonCancel.setOnClickListener((View.OnClickListener) this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                String userName = editTextUserName.getText().toString();
                String userPassWord = editTextPassword.getText().toString();
                validateCredentials(userName,userPassWord);
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private void validateCredentials(String userName, String userPassWord) {
        try {
            InputStream is = getAssets().open("users.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("user");
            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    //Validate User is present or not in our db
                    if(userName.equalsIgnoreCase( getValue("name", element2))&&
                    userPassWord.equalsIgnoreCase(getValue("password", element2))){
                        launchDiscountCalculatorActivity(getValue("privileged", element2),userName);
                        break;
                    }else{
                        Toast.makeText(this, "User Not Found",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }//end of for loop

        } catch (Exception e) {e.printStackTrace();}

    }
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    private void launchDiscountCalculatorActivity(String privileged, String userName) {
        Intent intent = new Intent(this, DiscountCalculatorActivity.class);
        Boolean privelaged = Boolean.valueOf(privileged);
        intent.putExtra("isPrivilegedUser", privelaged);
        intent.putExtra("Name",userName);
        startActivity(intent);
    }
}