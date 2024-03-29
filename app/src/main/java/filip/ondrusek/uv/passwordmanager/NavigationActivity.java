package filip.ondrusek.uv.passwordmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import filip.ondrusek.uv.passwordmanager.databinding.ActivityNavigationBinding;

public class NavigationActivity extends AppCompatActivity {

    private ActivityNavigationBinding binding;
    private String masterPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        masterPassword = (String) getIntent().getSerializableExtra("masterPassword");
        Bundle bundleVault = new Bundle();
        bundleVault.putString("masterPassword", masterPassword);
        VaultFragment vaultFragment = new VaultFragment();
        vaultFragment.setArguments(bundleVault);
        replaceFragment(vaultFragment);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.vault:
                    Bundle bundleVaultSwitch = new Bundle();
                    bundleVaultSwitch.putString("masterPassword", masterPassword);
                    VaultFragment vaultFragmentSwitch = new VaultFragment();
                    vaultFragmentSwitch.setArguments(bundleVaultSwitch);
                    replaceFragment(vaultFragmentSwitch);
                    break;
                case R.id.add:
                    Bundle bundleAdd = new Bundle();
                    bundleAdd.putString("masterPassword", masterPassword);
                    AddItemFragment addItemFragment = new AddItemFragment();
                    addItemFragment.setArguments(bundleAdd);
                    replaceFragment(addItemFragment);
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Intent intent = new Intent(NavigationActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }

    public void showToast(String text)
    {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.toast_layout, findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0,600);
        toast.setDuration(Toast.LENGTH_LONG);
        toastText.setText(text);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onBackPressed() { }
}