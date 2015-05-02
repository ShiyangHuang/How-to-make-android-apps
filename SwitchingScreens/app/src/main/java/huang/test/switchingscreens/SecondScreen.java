package huang.test.switchingscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shiyanghuang on 15/4/30.
 */
public class SecondScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_layout);

        Intent activityThatCalled = getIntent();

        String previousActivity = activityThatCalled.getExtras().getString("callingActivity");

        TextView callingActivityMessage = (TextView) findViewById(R.id.calling_activity_info_text_view);

        callingActivityMessage.append(" " + previousActivity);
    }

    public void onSendUserName(View view) {
        EditText userNameET = (EditText) findViewById(R.id.user_name_edit_text);

        String usersName = String.valueOf(userNameET.getText());

        Intent goingBack = new Intent();

        goingBack.putExtra("UsersName", usersName);

        setResult(RESULT_OK, goingBack);

        Toast.makeText(this, usersName, Toast.LENGTH_LONG).show();

        finish();
    }
}
