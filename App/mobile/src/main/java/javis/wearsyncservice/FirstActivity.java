package javis.wearsyncservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first2);

        Button go = (Button)findViewById(R.id.GoBttn);
        final EditText zipcode = (EditText)findViewById(R.id.Zipcode);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("BAZOOKA Zipcode = " + zipcode.getText());
                if (zipcode==null || zipcode.getText()==null || (zipcode!=null && zipcode.getText().equals("")==true)) //Display a toast if the user enters nothing
                //TODO: FIX THIS
                {
                    Toast t = Toast.makeText(getApplicationContext(),"Please enter a zipcode" +
                            " or use current location.", Toast.LENGTH_LONG);
                    t.show();
                }
                else
                {
                    fly_away(zipcode.getText().toString());
                }
            }
        });
    }

    public void fly_away(String location)
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Toast", "False");
        i.putExtra("Zipcode", location);
        startActivity(i);
    }

    /*public void go_to_list(View w)
            *//*
            Called onClick of of go!
             *//*
    {
        EditText zipcode = (EditText)w.findViewById(R.id.Zipcode);
        if (zipcode!=null) {
            System.out.println("BAZOOKA Zipcode = " + zipcode.getText());
        }
        if (zipcode==null)
        {
            System.out.println("BAZOOKA The zipcode is null");
        }
        if (zipcode==null || (zipcode!=null && zipcode.getText().equals("")==true)) //Display a toast if the user enters nothing
        {
            Toast t = Toast.makeText(getApplicationContext(),"Please enter a zipcode" +
                    " or use current location.", Toast.LENGTH_LONG);
            t.show();
        }
        else
        {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("Toast", "False");
            i.putExtra("Zipcode", zipcode.getText().toString());
            startActivity(i);
        }
    }*/

    public void go_to_list_current(View w)
            /*
            Called onClick of of use current location
             */
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Toast", "False");
        i.putExtra("Zipcode", "Current Location");
        startActivity(i);
    }
}
