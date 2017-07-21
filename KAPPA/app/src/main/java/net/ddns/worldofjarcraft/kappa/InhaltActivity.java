package net.ddns.worldofjarcraft.kappa;

import android.app.Activity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static net.ddns.worldofjarcraft.kappa.R.id.space2;

public class InhaltActivity extends Activity{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    int nr=0;
    String name = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhalt);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            nr=b.getInt("schrank");
            name = b.getString("name");
        }
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                ladeFaecher();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                ladeLebensmittel();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        Button menu = (Button) findViewById(R.id.menuButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout);
                layout.openDrawer(GravityCompat.START);
            }
        });
        ladeFaecher();
        ladeLebensmittel();
       /* mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));*/
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });
        button.bringToFront();
        FloatingActionButton button2 = findViewById(R.id.floatingActionButton2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open(aktFach);
            }
        });
        button2.bringToFront();
    }

    private void addFood() {
        int index =aktFach.getItemId();
        final Pair<Integer,String> werte = faecher.get(index);
        //https://worldofjarcraft.ddns.net/kappa/neues_Lebensmittel.php?mail=admin@worldofjarcraft.ddns.net&pw=1234&schrank=Kueche&fach=7&name=RANZ&zahl=999&mhd=123466789987654321
        AlertDialog.Builder builder = new AlertDialog.Builder(InhaltActivity.this);
        builder.setTitle(R.string.neues_LM);
        TextView v1 = new TextView(this);
        v1.setText(R.string.info_Name_LM);
        final EditText name_lm = new EditText(this);
        name_lm.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        TextView v2 = new TextView(this);
        v2.setText(R.string.info_Anzahl_LM);
        final EditText zahl_lm = new EditText(this);
        name_lm.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        final CheckBox mhd = new CheckBox(this);
        mhd.setText(R.string.info_MHD);
        mhd.setSelected(false);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(v1);
        layout.addView(name_lm);
        layout.addView(v2);
        layout.addView(zahl_lm);
        layout.addView(mhd);
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mhd.isChecked()){
                    final DatePickerDialog dialog = new DatePickerDialog(InhaltActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            Calendar cal = Calendar.getInstance();
                            cal.set(i,i1,i2);
                            long time = cal.getTimeInMillis();
                            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/neues_Lebensmittel.php?mail="+data.mail+"&pw="+data.pw+"&schrank="+name+"&fach="+werte.second+"&name="+name_lm.getText().toString().replaceAll(" ","%20")+"&zahl="+zahl_lm.getText().toString()+"&mhd="+time);
                            conn.delegate= new AsyncResponse() {
                                @Override
                                public void processFinish(String output, String url) {
                                    open(aktFach);
                                }
                            };
                            conn.execute("params");
                        }
                    }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
                else{
                    HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/neues_Lebensmittel.php?mail="+data.mail+"&pw="+data.pw+"&schrank="+name+"&fach="+werte.second+"&name="+name_lm.getText().toString().replaceAll(" ","%20")+"&zahl="+zahl_lm.getText().toString()+"&mhd=0");
                    conn.delegate= new AsyncResponse() {
                        @Override
                        public void processFinish(String output, String url) {
                            open(aktFach);
                        }
                    };
                    conn.execute("params");
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    List<Pair<Integer,String>> faecher;
    public void ladeFaecher(){
        String url = "https://worldofjarcraft.ddns.net/kappa/get_Fach.php?mail="+data.mail+"&pw="+data.pw+"&schrank="+name;
        HTTP_Connection conn = new HTTP_Connection(url,1);
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                String[] neuefaecher = output.split("\\|");
                faecher = new ArrayList<>();
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
                Menu menu = navigationView.getMenu();
                menu.removeGroup(0);
                int akt = 0;
                for(String fach:neuefaecher){
                    String[] werte = fach.split(";");
                    if(werte.length==2){
                    Pair<Integer,String> fa = new Pair<>(new Integer(werte[0]),werte[1]);
                    faecher.add(fa);
                    menu.add(0, akt, Menu.NONE, werte[1]).setIcon(R.mipmap.icon).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            System.out.println("Menü geklickt...");
                            aktFach=menuItem;
                            open(menuItem);
                            return false;
                        }
                    });
                    akt++;}
                }
                if(menu.size()>0){
                    aktFach = menu.getItem(0);
                    open(menu.getItem(0));
                }
                menu.add(0, akt, Menu.NONE, R.string.neues_Fach).setIcon(R.mipmap.ic_launcher).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        System.out.println("Fach hinzufügen...");
                        addFach();
                        return false;
                    }
                });
            }
        };
        conn.execute("params");
    }
MenuItem aktFach;
    List<Integer> ids;
    private void open(MenuItem menuItem) {
        ids=new ArrayList<>();
        int index =menuItem.getItemId();
        Pair<Integer,String> werte = faecher.get(index);
        TextView info_fach = findViewById(R.id.Info_Fach);
        info_fach.setText(R.string.fach_akt_inhalt);
        info_fach.append(werte.second);
        HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/getLebensmittel.php?mail="+data.mail+"&pw="+data.pw+"&schrank="+name+"&fach="+werte.second);
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                System.out.println(output);
                String[] lm = output.split("\\|");
                TableLayout tabelle = findViewById(R.id.lebensmittel);
                tabelle.removeAllViews();
                TableRow kopfzeile = new TableRow(InhaltActivity.this);
                TextView v1= new TextView(InhaltActivity.this);
                v1.setText(R.string.name);
                v1.setTextSize(24);
                TextView v2= new TextView(InhaltActivity.this);
                v2.setText(R.string.anzahl);
                v2.setTextSize(24);
                TextView v3= new TextView(InhaltActivity.this);
                v3.setText(R.string.haltbar);
                v3.setTextSize(24);
                Space space = new Space(InhaltActivity.this);
                space.setMinimumWidth(30);
                Space space2 = new Space(InhaltActivity.this);
                space2.setMinimumWidth(30);
                Space space3 = new Space(InhaltActivity.this);
                space3.setMinimumWidth(30);
                kopfzeile.addView(v1);
                kopfzeile.addView(space);
                kopfzeile.addView(v2);
                kopfzeile.addView(space2);
                kopfzeile.addView(v3);
                kopfzeile.addView(space3);
                tabelle.addView(kopfzeile);
                for(String lebensmittel:lm){
                    String[] attribute = lebensmittel.split(";");
                    if(attribute.length>3){
                        try {

                    ids.add(new Integer(attribute[0]));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    TableRow zeile = new TableRow(InhaltActivity.this);
                    TextView i1= new TextView(InhaltActivity.this);
                    i1.setText(attribute[1]);
                    i1.setTextSize(24);
                    TextView i2= new TextView(InhaltActivity.this);
                    i2.setText(attribute[2]);
                        i2.setTextSize(24);
                    TextView i3= new TextView(InhaltActivity.this);
                    i3.setText(R.string.keine_Angabe);
                        i3.setTextSize(24);
                    try {
                        Long mhd = new Long(attribute[3]);
                        if(mhd>0){
                            long rest = mhd - Calendar.getInstance().getTimeInMillis();
                            long aktzeit = Calendar.getInstance().getTimeInMillis();
                            if(rest>0){
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(rest);
                                String text="";
                                long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day= Calendar.DAY_OF_MONTH;
                                System.out.println(year+","+month+","+day);
                                if(cal.get(Calendar.YEAR)-1970>0)
                                    text+=(cal.get(Calendar.YEAR)-1970)+" " + getResources().getString(R.string.jahre)+", ";
                                if(cal.get(Calendar.MONTH)>0)
                                    text+=cal.get(Calendar.MONTH)+" " + getResources().getString(R.string.monate)+", ";

                                    text+=cal.get(Calendar.DAY_OF_MONTH)+" " + getResources().getString(R.string.tage)+".";
                                i3.setText(text);
                            }
                            else{
                                i3.setText(R.string.abgelaufen);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                         space = new Space(InhaltActivity.this);
                        space.setMinimumWidth(30);
                         space2 = new Space(InhaltActivity.this);
                        space2.setMinimumWidth(30);
                         space3 = new Space(InhaltActivity.this);
                        space3.setMinimumWidth(30);
                    zeile.addView(i1);
                    zeile.addView(space);
                    zeile.addView(i2);

                        zeile.addView(space2);
                    zeile.addView(i3);

                        zeile.addView(space3);
                        zeile.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                if(view instanceof TableRow) {
                                    final TableRow v = (TableRow) view;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InhaltActivity.this);
                                    builder.setTitle(R.string.essen_loeschen_titel);
                                    builder.setMessage(R.string.essen_loeschen);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            deleteEssen(v);
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                                return false;
                            }
                        });
                    tabelle.addView(zeile);
                    }
                }
            }
        };
        conn.execute("params");
    }
    public void deleteEssen(TableRow row){
        TableLayout layout = findViewById(R.id.lebensmittel);
        int index = layout.indexOfChild(row);
        if(index>=0){
            int order_id = ids.get(index-1);
            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/delete_Lebensmittel.php?mail="+data.mail+"&pw="+data.pw+"&id="+order_id);
            conn.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String url) {
                    open(aktFach);
                    if(!output.equals("Erfolg"))
                        Toast.makeText(InhaltActivity.this,R.string.network_error,Toast.LENGTH_LONG).show();
                }
            };
            conn.execute("params");
        }
    }
    public void addFach(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_Fach);
        builder.setMessage(R.string.add_Fach_info);
// Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "https://worldofjarcraft.ddns.net/kappa/neues_Fach.php?mail="+data.mail+"&pw="+data.pw+"&name="+input.getText().toString().replaceAll(" ","%20")+"&schrank="+name.replaceAll(" ","%20");
                HTTP_Connection login = new HTTP_Connection(url,2);
                AsyncResponse response = new AsyncResponse() {
                    @Override
                    public void processFinish(String output, String url) {
                        ladeFaecher();
                    }
                };
                login.delegate = response;
                login.execute();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        ladeFaecher();
    }
    public void ladeLebensmittel(){

    }

}
