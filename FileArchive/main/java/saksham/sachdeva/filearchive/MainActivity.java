package saksham.sachdeva.filearchive;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.obsez.android.lib.filechooser.ChooserDialog;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText inputText, destination, password;
    ImageButton extrat, combine;
    String path = "", Destination = "";
    private static int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE);

        inputText = findViewById(R.id.myInputText);
        destination = findViewById(R.id.destination);
        password = findViewById(R.id.pass);
        inputText.setFocusable(false);

        extrat = findViewById(R.id.extract);
        combine = findViewById(R.id.combine);

        extrat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ZipFile zipFile = new ZipFile(inputText.getText().toString());
                    if (zipFile.isEncrypted()) {
                        zipFile.setPassword(password.getText().toString());
                    }
                    zipFile.extractAll(destination.getText().toString());
                }
                catch (ZipException e)
                {
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "File Is extracted", Toast.LENGTH_LONG).show();

            }
        });
        combine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String s= "", m= "";
                    if (!inputText.getText().toString().replaceAll("\\s","").isEmpty())
                    {
                        s = inputText.getText().toString();
                        m = s.substring(s.lastIndexOf("/"),s.lastIndexOf("."));
                        ZipFile zipFile = new ZipFile((destination.getText().toString())+m+".zip");
                        ZipParameters zipParameters = new ZipParameters();
                        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                        if (password.getText().toString().replaceAll("\\s","").isEmpty()) { }
                        else {
                            zipParameters.setEncryptFiles(true);
                            zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                            zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                            zipParameters.setPassword(password.getText().toString());
                        }
                        zipFile.addFile(new File(inputText.getText().toString()), zipParameters);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Please Enter all Required Field", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (ZipException e)
                {
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "File Is Compressed", Toast.LENGTH_LONG).show();
            }
        });

        inputText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooserDialog().with(MainActivity.this)
                        .withStartFile(path)
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path1, File pathFile) {
                                inputText.setText(path1);
                            }
                        })
                        .build()
                        .show();
            }
        });
        destination.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooserDialog().with(MainActivity.this)
                        .withFilter(true, false)
                        .withStartFile(Destination)
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path1, File pathFile) {
                                destination.setText(path1);
                                //Toast.makeText(MainActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build()
                        .show();
            }
        });

    }
}
