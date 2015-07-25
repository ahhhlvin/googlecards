//package madelyntav.c4q.nyc.googlecards;
//
//
//import android.accounts.AccountManager;
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.api.client.extensions.android.http.AndroidHttp;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.DateTime;
//import com.google.api.client.util.ExponentialBackOff;
//import com.google.api.services.calendar.CalendarScopes;
//import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.model.Events;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class CalendarFragment extends Fragment {
//    public com.google.api.services.calendar.Calendar mService;
//    public GoogleAccountCredential credential;
//    TextView mStatusText;
//    public List<String> eventStrings;
//    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
//    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//    CardFragment mCardFragment;
//    AllEventsFragment mAllEventsFragment;
//    AddEventToCal mAddEventToCal;
//    static final int REQUEST_ACCOUNT_PICKER = 1000;
//    static final int REQUEST_AUTHORIZATION = 1001;
//    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
//    public static final String PREF_ACCOUNT_NAME = "accountName";
//    public static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};
//    public PagesAdapter mPagesAdapter;
//    public static final String TAG = MainActivity.class.getSimpleName();
//    private WeatherInf mCurrentWeather;
//    CalendarFragment myContext;
//
//    public CalendarFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
//
//
//
//        if (savedInstanceState != null) {
////            //Restore the fragment's instance
////            mCardFragment = (CardFragment) getFragmentManager().getFragment(
////                    savedInstanceState, "mCardFragment");
////            mAllEventsFragment = (AllEventsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mTomorrowFragment");
////
////            mAddEventToCal = (AddEventToCal) getSupportFragmentManager().getFragment(savedInstanceState, "mAddEventToCal");
//        }
//        FragmentManager fragmentManager = getFragmentManager();
//
//        //mPagesAdapter = new PagesAdapter(getFragmentManager());
//
//        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
//        viewPager.setAdapter(mPagesAdapter);
//        mStatusText = (TextView) view.findViewById(R.id.mStatusText);
//
//
//        // Initialize credentials and service object.
//        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
//        credential = GoogleAccountCredential.usingOAuth2(
//                getActivity(), Arrays.asList(SCOPES))
//                .setBackOff(new ExponentialBackOff())
//                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
//
//        mService = new com.google.api.services.calendar.Calendar.Builder(
//                transport, jsonFactory, credential)
//                .setApplicationName("Google Calendar API Android Quickstart")
//                .build();
//
//        return view;
//    }
//
//    @Override
//    public void onActivityResult(
//            int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_GOOGLE_PLAY_SERVICES:
//                if (resultCode == Activity.RESULT_OK) {
//                    refreshResults();
//                } else {
//                    refreshResults();
//                }
//                break;
//            case REQUEST_ACCOUNT_PICKER:
//                if (resultCode == Activity.RESULT_OK && data != null &&
//                        data.getExtras() != null) {
//                    String accountName =
//                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                    if (accountName != null) {
//                        credential.setSelectedAccountName(accountName);
//                        SharedPreferences settings =
//                                getActivity().getPreferences(Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString(PREF_ACCOUNT_NAME, accountName);
//                        editor.commit();
//                        refreshResults();
//                    }
//                } else if (resultCode == Activity.RESULT_CANCELED) {
//                    mStatusText.setText("Account unspecified.");
//                }
//                break;
//            case REQUEST_AUTHORIZATION:
//                if (resultCode == Activity.RESULT_OK) {
//                    refreshResults();
//                } else {
//                    chooseAccount();
//                }
//                break;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void refreshResults() {
//        if (credential.getSelectedAccountName() == null) {
//            chooseAccount();
//        } else {
//            new ApiAsyncTask((MainActivity) getActivity()).execute();
//        }
//    }
//
//    /**
//     * Clear any existing Google Calendar API data from the TextView and update
//     * the header message; called from background threads and async tasks
//     * that need to update the UI (in the UI thread).
//     */
//    public void clearResultsText() {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mStatusText.setText("Retrieving data…");
//
//            }
//        });
//    }
//
//    /**
//     * Starts an activity in Google Play Services so the user can pick an
//     * account.
//     */
//    private void chooseAccount() {
//        startActivityForResult(
//                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
//    }
//
//    private boolean isNetworkAvailable() {
//
//        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//        boolean isAvailable = false;
//
//        if (networkInfo != null && networkInfo.isConnected()) {
//            isAvailable = true;
//        }
//        return isAvailable;
//    }
//
//
//    public void onMapReady(final GoogleMap googleMap) {
//        googleMap.setMyLocationEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.getUiSettings().setCompassEnabled(true);
//        googleMap.getUiSettings().setScrollGesturesEnabled(true);
//        googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
//
//
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                googleMap.clear();
//                googleMap.addMarker(new MarkerOptions()
//                        .position(latLng));
//                Toast.makeText(getActivity(), "Marker has been added", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//
//    }
//
//    public class ApiAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
//        public MainActivity mActivity;
//        private OnTaskCompleted listener;
//        private Context contxt;
//        private Activity activity;
//        private ArrayList<String> eventStrings;
//        private ArrayList<String> todayEventstrings;
//
//        ApiAsyncTask(MainActivity activity) {
//            this.mActivity = activity;
//        }
//
//        public ApiAsyncTask(OnTaskCompleted listener) {
//            this.listener = listener;
//        }
//
//        public ApiAsyncTask(Context context, OnTaskCompleted listener) {
//            // API = apiURL;
//            this.contxt = context;
//            this.listener = listener;
//        }
//
//
//        /**
//         * Background task to call Google Calendar API.
//         */
//        @Override
//        protected ArrayList<String> doInBackground(Void... params) {
//            ArrayList<String> results = new ArrayList<String>();
//            try {
//                results.addAll(getDataFromApi());
//
//            } catch (IOException e) {
//                // log an error
//            }
//            return results;
//        }
//
//        public void onTaskCompleted(Boolean success) {
//            mPagesAdapter.notifyDataSetChanged();
//        }
//
//
//        @Override
//        protected void onPostExecute(final ArrayList<String> eventStrings) {
//            if (eventStrings.size() > 0) {
//                //mActivity.clearResultsText();
//
//
//            } else {
//
//            }
//            mAllEventsFragment.updateEventData(eventStrings);
//
//            if (todayEventstrings.size() > 0) {
//                mCardFragment.updateEventData(todayEventstrings);
//            } else {
//                mStatusText.setText("No Events Found");
//            }
//            mStatusText.setText("");
//        }
//        // Fetch a list of events from the primary calendar.
//        private ArrayList<String> getDataFromApi() throws IOException {
//
//            DateTime now = new DateTime(System.currentTimeMillis());
//            DateTime tomorrow = new DateTime(System.currentTimeMillis() + 86400000);
//
//            eventStrings = new ArrayList<String>();
//            todayEventstrings = new ArrayList<>();
//
////            com.google.api.services.calendar.Calendar mService;
////            mService = new com.google.api.services.calendar.Calendar.Builder(
////                    transport, jsonFactory, credential)
////                    .setApplicationName("Google Calendar API Android Quickstart")
////                    .build();
//
//            Events events = mActivity.mService.events().list("primary")
//                    .setTimeMin(now)
//                    .setOrderBy("startTime")
//                    .setSingleEvents(true)
//                    .execute();
//            if (events != null) {
//                final List<Event> items = events.getItems();
//
//                for (Event event : items) {
//                    DateTime start = event.getStart().getDateTime();
//
//                    DateTime end = event.getEnd().getDateTime();
//
//                    String location = event.getLocation();
//                    if (location == null) {
//                        location = "";
//                    }
//                    if (start == null) {
//                        // All-day events don't have start times, so just use
//                        // the start date.
//                        start = event.getStart().getDate();
//                        if (end == null) {
//                            end = event.getEnd().getDate();
//                        }
//                    }
//                    eventStrings.add(
//                            String.format(event.getSummary() + " /" + location + " /" + start + " /" + end));
//                }
//            } else {
//
//                SharedPreferences allEvents=getActivity().getSharedPreferences("Events", Context.MODE_PRIVATE);
//                eventStrings= (ArrayList<String>) allEvents.getAll();
//
//            }
//            //Get all of the events of the primary calendar for the next 24 hours
//            Events events1 = mActivity.mService.events().list("primary")
//                    .setTimeMin(now)
//                    .setTimeMax(tomorrow)
//                    .setSingleEvents(true)
//                    .execute();
//
//
//            List<Event> items1 = events1.getItems();
//            for (Event event : items1) {
//                DateTime start1 = event.getStart().getDateTime();
//
//                DateTime end1 = event.getEnd().getDateTime();
//
//                String location = event.getLocation();
//                if (location == null) {
//                    location = "";
//                }
//                if (start1 == null) {
//                    // All-day events don't have start times, so just use
//                    // the start date.
//                    start1 = event.getStart().getDate();
//                    if (end1 == null) {
//                        end1 = event.getEnd().getDate();
//                    }
//                }
//                todayEventstrings.add(event.getSummary() + "/" + location + "/" + start1 + "/" + end1);
//            }
//
//
//            SharedPreferences allEvents=getActivity().getSharedPreferences("Events", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor=allEvents.edit();
//            for (String line: eventStrings){
//                editor.putString(line.toString(),line);
//            }
//            editor.apply();
//            return eventStrings;
//        }
//
//    }
//
//    public class PagesAdapter extends FragmentPagerAdapter {
//        public PagesAdapter(android.support.v4.app.FragmentManager fragmentManager) {
//            super(fragmentManager);
//            mCardFragment = new CardFragment();
//            mAllEventsFragment = new AllEventsFragment();
//            mAddEventToCal = new AddEventToCal();
//        }
//
//
//        @Override
//        public android.support.v4.app.Fragment getItem(int position) {
//            if (position == 0) {
//                return mCardFragment;
//            } else if (position == 1) {
//                return mAllEventsFragment;
//            } else if (position == 2) {
//                return mAddEventToCal;
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            if (position == 0) {
//                return "Events In The Next 24 Hours";
//            } else if (position == 1) {
//                return "All Upcoming Events";
//            } else if (position == 2) {
//                return "Create New Event";
//            }
//            return null;
//        }
//    }
//
//}
