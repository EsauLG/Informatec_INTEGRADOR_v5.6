package com.fcp.tec.informatecvdos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.fcp.tec.informatecvdos.activities.FiveFragment_Logros;
import com.fcp.tec.informatecvdos.activities.FourFragment_Testimoniales;
import com.fcp.tec.informatecvdos.activities.OneFragment_Inicio;
import com.fcp.tec.informatecvdos.activities.ThreeFragment_Extraescolares;
import com.fcp.tec.informatecvdos.activities.TwoFragment_Instalaciones;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final String TAG = MainActivity.class.getSimpleName();

    public boolean esTablet(Context context){
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: Iniciando MainActivity");
        //Forza la orientacion en modo retrato unicamente

        if (esTablet(this))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //Esconde los menus de navegacion (Atras, Home y Multitareas)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        setSupportActionBar(toolbar);

        if (esTablet(this))
        {

            //codigo para poner el layout inicio como predeterminado
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new OneFragment_Inicio()).commit();

            //acerca de
            ImageButton btntec = (ImageButton) findViewById(R.id.btntec);

            //Codigo para el drawer lateral
            //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

        else {
            getSupportActionBar().setTitle("Informatec");
            tabLayout.setupWithViewPager(viewPager);

            setupViewPager(viewPager);
            setIconForTabs();
        }
    }
//codigo de la tableta
           //Crear Opciones del menu del drawer layout


        //Seleccionar el item de opciones del menu
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();

            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")

        //Selección del item del navigation View
        @Override
        public boolean onNavigationItemSelected(MenuItem item)
        {
            //manejar la vista de navegación cuando hace clic
            int id = item.getItemId();

            FragmentManager fragmentManager=getSupportFragmentManager();

            if (id == R.id.nav_inicio)
            {
                fragmentManager.beginTransaction().replace(R.id.contenedor,new OneFragment_Inicio()).commit();
            }

            else if (id == R.id.nav_instalaciones)
            {
                fragmentManager.beginTransaction().replace(R.id.contenedor,new TwoFragment_Instalaciones()).commit();
            }

            else if (id == R.id.nav_extraescolares)
            {
                fragmentManager.beginTransaction().replace(R.id.contenedor,new ThreeFragment_Extraescolares()).commit();
            }

            else if (id == R.id.nav_testimonios)
            {
                fragmentManager.beginTransaction().replace(R.id.contenedor,new FourFragment_Testimoniales()).commit();
            }

            else if (id == R.id.nav_logros)
            {
                fragmentManager.beginTransaction().replace(R.id.contenedor,new FiveFragment_Logros()).commit();
            }

            return true;
        }


    //Fin codigo de tableta

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment)
        {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return null;
        }

    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new OneFragment_Inicio());
        adapter.addFrag(new TwoFragment_Instalaciones());
        adapter.addFrag(new ThreeFragment_Extraescolares());
        adapter.addFrag(new FourFragment_Testimoniales());
        adapter.addFrag(new FiveFragment_Logros());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        getSupportActionBar().setTitle("Inicio");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Instalaciones");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Extraescolares");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Testimonios");
                        break;
                    case 4:
                        getSupportActionBar().setTitle("Logros");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }


    private void setIconForTabs()
    {
        //la ruta es res/mipmap
        int[] tabIcons = {
                R.mipmap.ic_inicio,
                R.mipmap.ic_instalaciones,
                R.mipmap.ic_extraescolares,
                R.mipmap.ic_testimonios,
                R.mipmap.ic_logros
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }


}
