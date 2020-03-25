package com.example.da105_g4_v0.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.order.MemberOrderActivity;

public class VisaCard extends AppCompatActivity {
    private TextView payment, cardNumber, cardName, expiryDate, cvc, amount;
    private Button btn_pay;
    private String total;
    private String memberName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_card);
        SharedPreferences pref = this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memNo = pref.getString("member_no", "");
        String memName = pref.getString("member_name","");
        memberName = memName;

        total = getIntent().getExtras().getString("total");
        findViews();
        CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Intent intent = new Intent(VisaCard.this, MemberOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews() {

        payment = findViewById(R.id.payment_amount);
        payment.setText(total);
        amount = findViewById(R.id.payment_amount_holder);
        cardNumber = findViewById(R.id.card_number);
        amount.setText("總金額: ");
        cardName = findViewById(R.id.card_name);
        expiryDate = findViewById(R.id.expiry_date);
        cvc = findViewById(R.id.cvc);
        btn_pay = findViewById(R.id.btn_pay);
        btn_pay.setText("確認");
        btn_pay.setFocusable(true);


    }

    public void onInput(View view) {

        cardNumber.setText("4342 5581 4656 6662");
        cardName.setText(memberName);
        expiryDate.setText("04/20");
        cvc.setText("666");

    }

}
