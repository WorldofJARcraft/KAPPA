package net.ddns.worldofjarcraft.kappa;

import android.app.Activity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.ddns.worldofjarcraft.kappa.Model.Fach;
import net.ddns.worldofjarcraft.kappa.Model.Lebensmittel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static net.ddns.worldofjarcraft.kappa.R.id.lebensmittel;
import static net.ddns.worldofjarcraft.kappa.R.id.space2;

public class InhaltActivity extends Activity {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    int nr = 0;
    String name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhalt);
        int[][] state = new int[][]{
                new int[]{android.R.attr.state_checked}, // checked
                new int[]{-android.R.attr.state_checked}
        };

        int[] color = new int[]{
                getResources().getColor(R.color.MycolorAccent),
                (Color.BLACK)
        };

        ColorStateList csl = new ColorStateList(state, color);
        int[][] state2 = new int[][]{
                new int[]{android.R.attr.state_checked}, // checked
                new int[]{-android.R.attr.state_checked}
        };

        int[] color2 = new int[]{
                getResources().getColor(R.color.MycolorAccent),
                (Color.GRAY)
        };

        ColorStateList csl2 = new ColorStateList(state2, color2);
        ProgressBar bar = findViewById(R.id.progressBarInhalt);
        bar.bringToFront();
        bar.setVisibility(View.VISIBLE);
        ImageButton imageButton = findViewById(R.id.back_Inhalt);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back(view);
            }
        });
        Bundle b = getIntent().getExtras();
        if (b != null) {
            nr = b.getInt("schrank");
            name = b.getString("name");
        }
        DrawerLayout layout = findViewById(R.id.drawer_layout);
        layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                ladeFaecher();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        FloatingActionButton menu = findViewById(R.id.menuButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout layout = findViewById(R.id.drawer_layout);
                layout.openDrawer(GravityCompat.START);
            }
        });
        menu.bringToFront();
        ladeFaecher();
       /* mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));*/
        FloatingActionButton button = findViewById(R.id.floatingActionButton);
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
        bar.setVisibility(View.GONE);
        NavigationView nav = findViewById(R.id.navigation);
        nav.setItemTextColor(csl);
        nav.setItemIconTintList(csl2);

    }

    private void deleteFach(MenuItem aktFach) {
        ProgressBar bar = findViewById(R.id.progressBarInhalt);
        bar.setVisibility(View.VISIBLE);
        int index = aktFach.getItemId();
        Fach fach = faecher.get(index);
        HTTP_Connection conn = new HTTP_Connection(Constants.Server_Adress + "/schrank/" + nr + "/" + fach.getlNummer() + "/delete");
        conn.setMethod("DELETE");
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                if (!output.equals("Erfolg")) {
                    Toast.makeText(InhaltActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                }
                Bundle b = new Bundle(2);
                b.putString("name", name);
                b.putInt("schrank", nr);
                Intent intent = new Intent(InhaltActivity.this, InhaltActivity.class);
                intent.putExtras(b);
                ProgressBar bar = findViewById(R.id.progressBarInhalt);
                bar.setVisibility(View.GONE);
                startActivity(intent);
            }
        };
        conn.execute("params");
    }

    private void addFood() {
        alterFood("newLM",R.string.neues_LM);
    }

    private void updateFood(View v) {
        if (!(v instanceof TableRow)) return;
        TableLayout layout = findViewById(R.id.lebensmittel);
        int index = layout.indexOfChild(v);
        if (index < 0) return;
        Lebensmittel lm = lebensmittels.get(index-1);
        alterFood(lm.getNummer() + "/update",R.string.LM_aendern);
    }

    private void alterFood(final String path, int titleID) {
        if (aktFach != null) {
            int index = aktFach.getItemId();
            final Fach fach = faecher.get(index);
            //https://worldofjarcraft.ddns.net/kappa/neues_Lebensmittel.php?mail=admin@worldofjarcraft.ddns.net&pw=1234&schrank=Kueche&fach=7&name=RANZ&zahl=999&mhd=123466789987654321
            AlertDialog.Builder builder = new AlertDialog.Builder(InhaltActivity.this);
            builder.setTitle(titleID);
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
                    final HashMap<String, String> params = new HashMap<>();
                    params.put("name", name_lm.getText().toString());
                    params.put("Anzahl", zahl_lm.getText().toString());
                    params.put("haltbarkeit", "0");
                    params.put("eingelagert", Calendar.getInstance().getTimeInMillis() + "");
                    if (mhd.isChecked()) {
                        final DatePickerDialog dialog = new DatePickerDialog(InhaltActivity.this, new DatePickerDialog.OnDateSetListener() {
                            private boolean called = false;

                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                //verhindert mehrfachen Aufruf
                                if (!called) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(i, i1, i2);
                                    long time = cal.getTimeInMillis();
                                    params.put("haltbarkeit", time + "");
                                    HTTP_Connection conn;
                                    try {
                                        conn = new HTTP_Connection(Constants.Server_Adress + "/schrank/" + nr + "/" + fach.getlNummer() + "/" + path, 3, params, "POST");
                                    } catch (UnsupportedEncodingException e) {
                                        return;
                                    }
                                    //"neues_Lebensmittel.php?mail=" + data.mail + "&pw=" + data.pw + "&schrank=" + name + "&fach=" + fach.getName() + "&name=" + name_lm.getText().toString().replaceAll(" ", "%20") + "&zahl=" + zahl_lm.getText().toString().replaceAll(" ", "%20") + "&mhd=" + time+"&eingelagert="+Calendar.getInstance().getTimeInMillis());
                                    conn.delegate = new AsyncResponse() {
                                        @Override
                                        public void processFinish(String output, String url) {
                                            ProgressBar bar = findViewById(R.id.progressBarInhalt);
                                            bar.setVisibility(View.GONE);
                                            open(aktFach);
                                        }
                                    };
                                    conn.execute("params");
                                    called = true;
                                }
                            }
                        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        dialog.show();
                    } else {
                        HTTP_Connection conn;
                        try {
                            conn = new HTTP_Connection(Constants.Server_Adress + "/schrank/" + nr + "/" + fach.getlNummer() + "/" + path, 3, params, "POST");
                        } catch (UnsupportedEncodingException e) {
                            return;
                        }
                        conn.delegate = new AsyncResponse() {
                            @Override
                            public void processFinish(String output, String url) {
                                ProgressBar bar = findViewById(R.id.progressBarInhalt);
                                bar.setVisibility(View.GONE);
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
                    ProgressBar bar = findViewById(R.id.progressBarInhalt);
                    bar.setVisibility(View.GONE);
                }
            });
            builder.show();
            ProgressBar bar = findViewById(R.id.progressBarInhalt);
            bar.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.erst_fach, Toast.LENGTH_LONG).show();
        }
    }

    List<net.ddns.worldofjarcraft.kappa.Model.Fach> faecher;

    public void ladeFaecher() {
        ProgressBar bar = findViewById(R.id.progressBarInhalt);
        bar.setVisibility(View.VISIBLE);
        String url = Constants.Server_Adress + "/schrank/" + nr + "/getAll";
        HTTP_Connection conn = new HTTP_Connection(url, 1);
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                Fach[] neuefaecher = new Gson().fromJson(output, Fach[].class);
                faecher = new ArrayList<>();
                NavigationView navigationView = findViewById(R.id.navigation);
                TextView view = navigationView.findViewById(R.id.drawerHeaderTitle);
                ImageButton close = navigationView.findViewById(R.id.closeMenuButton);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DrawerLayout layout = findViewById(R.id.drawer_layout);
                        layout.closeDrawer(GravityCompat.START);
                    }
                });
                view.setText(name);
                Menu menu = navigationView.getMenu();
                menu.removeGroup(0);
                int akt = 0;
                for (Fach fach : neuefaecher) {
                    faecher.add(fach);
                    menu.add(0, akt, Menu.NONE, fach.getName()).setIcon(R.mipmap.schnee).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            System.out.println("Menü geklickt...");
                            aktFach.setChecked(false);
                            aktFach = menuItem;
                            open(menuItem);
                            return false;
                        }
                    });
                    akt++;
                }
                if (menu.size() > 0) {
                    aktFach = menu.getItem(0);
                    open(menu.getItem(0));
                }
                menu.add(0, akt, Menu.NONE, R.string.neues_Fach).setIcon(R.drawable.plus).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        System.out.println("Fach hinzufügen...");
                        addFach();
                        return false;
                    }
                });
                menu.add(0, akt, Menu.NONE, R.string.update_Fach).setIcon(R.drawable.reload).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        System.out.println("Fach hinzufügen...");
                        updateFach(aktFach);
                        return false;
                    }
                });
                menu.add(0, akt + 1, Menu.NONE, getResources().getString(R.string.fach_loeschen)).setIcon(R.drawable.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(InhaltActivity.this);
                        builder.setTitle(R.string.fach_loeschen);
                        builder.setMessage(R.string.fach_loeschen_info);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteFach(aktFach);
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
                        return false;
                    }
                });
                menu.add(0, akt + 2, Menu.NONE, R.string.suche_LM).setIcon(R.drawable.lupe).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Bundle b = new Bundle(2);
                        b.putInt("schrank", nr);
                        b.putString("name", name);
                        Intent i = new Intent(InhaltActivity.this, SucheActivity.class);
                        i.putExtras(b);
                        startActivity(i);
                        return false;
                    }
                });
                ProgressBar bar = findViewById(R.id.progressBarInhalt);
                bar.setVisibility(View.GONE);
            }
        };
        conn.execute("params");
    }

    MenuItem aktFach;
    List<Lebensmittel> lebensmittels;

    private void open(MenuItem menuItem) {
        ProgressBar bar = findViewById(R.id.progressBarInhalt);
        bar.setVisibility(View.VISIBLE);
        menuItem.setChecked(true);
        lebensmittels = new ArrayList<>();
        int index = menuItem.getItemId();
        Fach fach = faecher.get(index);
        TextView info_fach = findViewById(R.id.Info_Fach);
        info_fach.setText(R.string.fach_akt_inhalt);
        info_fach.append(fach.getName());
        HTTP_Connection conn = new HTTP_Connection(Constants.Server_Adress + "/schrank/" + nr + "/" + fach.getlNummer() + "/getAll");
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                System.out.println(output);
                Lebensmittel[] lm = new Gson().fromJson(output, Lebensmittel[].class);
                TableLayout tabelle = findViewById(R.id.lebensmittel);
                tabelle.removeAllViews();
                TableRow kopfzeile = new TableRow(InhaltActivity.this);
                final TextView v1 = new TextView(InhaltActivity.this);
                v1.setText(R.string.name);
                v1.setTextSize(24);
                v1.setTypeface(v1.getTypeface(), Typeface.BOLD);
                TextView v2 = new TextView(InhaltActivity.this);
                v2.setText(R.string.anzahl);
                v2.setTextSize(24);
                v2.setTypeface(v2.getTypeface(), Typeface.BOLD);
                TextView v3 = new TextView(InhaltActivity.this);
                v3.setText(R.string.haltbar);
                v3.setTextSize(24);
                v3.setTypeface(v3.getTypeface(), Typeface.BOLD);
                TextView v4 = new TextView(InhaltActivity.this);
                v4.setText(R.string.eingelagert_am);
                v4.setTextSize(24);
                v4.setTypeface(v4.getTypeface(), Typeface.BOLD);
                Space space = new Space(InhaltActivity.this);
                space.setMinimumWidth(30);
                Space space2 = new Space(InhaltActivity.this);
                space2.setMinimumWidth(30);
                Space space3 = new Space(InhaltActivity.this);
                space3.setMinimumWidth(30);
                Space space4 = new Space(InhaltActivity.this);
                space4.setMinimumWidth(30);
                kopfzeile.addView(v1);
                kopfzeile.addView(space);
                kopfzeile.addView(v2);
                kopfzeile.addView(space2);
                kopfzeile.addView(v3);
                kopfzeile.addView(space3);
                kopfzeile.addView(v4);
                kopfzeile.addView(space4);
                tabelle.addView(kopfzeile);
                for (Lebensmittel lebensmittel : lm) {
                    try {

                        lebensmittels.add(lebensmittel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    TableRow zeile = new TableRow(InhaltActivity.this);
                    TextView i1 = new TextView(InhaltActivity.this);
                    i1.setText(lebensmittel.getName());
                    i1.setTextSize(24);
                    i1.setBackgroundResource(R.drawable.textviewstyle);
                    TextView i2 = new TextView(InhaltActivity.this);
                    i2.setText(lebensmittel.getAnzahl());
                    i2.setTextSize(24);
                    i2.setBackgroundResource(R.drawable.textviewstyle);
                    TextView i3 = new TextView(InhaltActivity.this);
                    i3.setText(R.string.keine_Angabe);
                    i3.setTextSize(24);
                    i3.setBackgroundResource(R.drawable.textviewstyle);
                    TextView i4 = new TextView(InhaltActivity.this);
                    i4.setText(R.string.keine_Angabe);
                    i4.setTextSize(24);
                    i4.setBackgroundResource(R.drawable.textviewstyle);
                    try {
                        long mhd = lebensmittel.getHaltbarkeitsdatum();
                        if (mhd > 0) {
                            long rest = mhd - Calendar.getInstance().getTimeInMillis();
                            if (rest > 0) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(rest);
                                String text = "";
                                long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = Calendar.DAY_OF_MONTH;
                                System.out.println(year + "," + month + "," + day);
                                if (cal.get(Calendar.YEAR) - 1970 > 0)
                                    text += (cal.get(Calendar.YEAR) - 1970) + " " + getResources().getString(R.string.jahre) + ", ";
                                if (cal.get(Calendar.MONTH) > 0)
                                    text += cal.get(Calendar.MONTH) + " " + getResources().getString(R.string.monate) + ", ";

                                text += cal.get(Calendar.DAY_OF_MONTH) + " " + getResources().getString(R.string.tage) + ".";
                                i3.setText(text);
                                if (cal.get(Calendar.YEAR) - 1970 == 0 && !text.contains(getResources().getString(R.string.monate)) && cal.get(Calendar.DAY_OF_MONTH) < 8) {
                                    i3.setTextColor(getResources().getColor(R.color.ORANGE));
                                } else
                                    i3.setTextColor(getResources().getColor(R.color.DRAKGREEN));
                            } else {
                                i3.setText(R.string.abgelaufen);

                                i3.setTextColor(Color.RED);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        long eingel = lebensmittel.getEingelagert();
                        if (eingel > 0) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(eingel);
                            long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = Calendar.DAY_OF_MONTH;
                            System.out.println(year + "," + month + "," + day);
                            java.text.DateFormat formatter = java.text.DateFormat.getDateInstance(
                                    java.text.DateFormat.LONG); // one of SHORT, MEDIUM, LONG, FULL, or DEFAULT
                            formatter.setTimeZone(cal.getTimeZone());
                            String formatted = formatter.format(cal.getTime());
                            i4.setText(formatted);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    space = new Space(InhaltActivity.this);
                    space.setMinimumWidth(30);
                    space2 = new Space(InhaltActivity.this);
                    space2.setMinimumWidth(30);
                    space3 = new Space(InhaltActivity.this);
                    space3.setMinimumWidth(30);
                    space4 = new Space(InhaltActivity.this);
                    space4.setMinimumWidth(30);
                    zeile.addView(i1);
                    zeile.addView(space);
                    zeile.addView(i2);

                    zeile.addView(space2);
                    zeile.addView(i3);

                    zeile.addView(space3);
                    zeile.addView(i4);
                    zeile.addView(space4);
                    zeile.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            final View v = view;

                            PopupMenu popup = new PopupMenu(InhaltActivity.this, v);
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.delete:
                                            deleteEssen(v);
                                            break;
                                        case R.id.update:
                                            updateFood(v);
                                    }
                                    return false;
                                }
                            });
                            MenuInflater inflater = popup.getMenuInflater();
                            inflater.inflate(R.menu.update_delete_menu, popup.getMenu());
                            popup.show();
                            return false;
                        }
                    });
                    tabelle.addView(zeile);
                }
                ProgressBar bar = findViewById(R.id.progressBarInhalt);
                bar.setVisibility(View.GONE);
            }
        };
        conn.execute("params");

    }

    public void deleteEssen(View view) {


        if (view instanceof TableRow) {
            final TableRow v = (TableRow) view;
            AlertDialog.Builder builder = new AlertDialog.Builder(InhaltActivity.this);
            builder.setTitle(R.string.essen_loeschen_titel);
            builder.setMessage(R.string.essen_loeschen);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ProgressBar bar = findViewById(R.id.progressBarInhalt);
                    bar.setVisibility(View.VISIBLE);
                    TableLayout layout = findViewById(R.id.lebensmittel);
                    int index = layout.indexOfChild(v);
                    int fachIndex = aktFach.getItemId();
                    if (index >= 0 && fachIndex >= 0) {
                        Fach fach = faecher.get(fachIndex);
                        int order_id = lebensmittels.get(index - 1).getNummer();
                        HTTP_Connection conn = new HTTP_Connection(Constants.Server_Adress + "/schrank/" + nr + "/" + fach.getlNummer() + "/" + order_id + "/delete");
                        conn.setMethod("DELETE");
                        conn.delegate = new AsyncResponse() {
                            @Override
                            public void processFinish(String output, String url) {
                                open(aktFach);
                                if (output.equals("Unauthorized!"))
                                    Toast.makeText(InhaltActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                                ProgressBar bar = findViewById(R.id.progressBarInhalt);
                                bar.setVisibility(View.GONE);
                            }
                        };
                        conn.execute("params");
                    }
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

    }

    private void updateFach(MenuItem aktFach) {
        int index = aktFach.getItemId();
        if (index > faecher.size() - 1) {
            return;
        }
        Fach fach = faecher.get(index);
        alterFach(fach.getlNummer() + "/update", R.string.update_Fach, "newName");
    }

    public void addFach() {
        alterFach("newFach", R.string.add_Fach, "name");
    }

    public void alterFach(final String path, int TitleID, final String paramName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TitleID);
        builder.setMessage(R.string.add_Fach_info);
// Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = Constants.Server_Adress + "/schrank/" + nr + "/" + path;
                HashMap<String, String> params = new HashMap<>();
                params.put(paramName, input.getText().toString());
                HTTP_Connection login;
                try {
                    login = new HTTP_Connection(url, 2, params, "POST");
                } catch (UnsupportedEncodingException e) {
                    return;
                }
                login.delegate = new AsyncResponse() {
                    @Override
                    public void processFinish(String output, String url1) {
                        ladeFaecher();
                        ProgressBar bar = findViewById(R.id.progressBarInhalt);
                        bar.setVisibility(View.GONE);
                    }
                };
                login.execute();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ProgressBar bar = findViewById(R.id.progressBarInhalt);
                bar.setVisibility(View.GONE);
            }
        });
        builder.show();
        ProgressBar bar = findViewById(R.id.progressBarInhalt);
        bar.setVisibility(View.VISIBLE);
        ladeFaecher();
    }


    public void back(View view) {
        this.finish();
    }

}
