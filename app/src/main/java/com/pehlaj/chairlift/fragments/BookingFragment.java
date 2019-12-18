package com.pehlaj.chairlift.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pehlaj.chairlift.activities.BookingDetailsActivity;
import com.pehlaj.chairlift.activities.BusDetailsActivity;
import com.pehlaj.chairlift.adapters.BookingAdapter;
import com.pehlaj.chairlift.components.RecyclerVerticalSpacingItemDecoration;
import com.pehlaj.chairlift.components.SpaceItemDecoration;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.constants.KeyConstants;
import com.pehlaj.chairlift.entities.BaseEntity;
import com.pehlaj.chairlift.entities.BookingResponse;
import com.pehlaj.chairlift.entities.Booking;
import com.pehlaj.chairlift.entities.Bus;
import com.pehlaj.chairlift.interfaces.ItemCallback;
import com.pehlaj.chairlift.interfaces.Services;
import com.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.pehlaj.chairlift.network.ApiClient;
import com.pehlaj.chairlift.network.WebClientCallBack;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;
import com.pehlaj.chairlift.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Pehlaj Rai
 * @since 17-June-17
 */

public class BookingFragment extends Fragment implements ItemCallback {

    private static final HashMap<String, String> BOOKING_MAP = new HashMap<>();
    private String bookingType;
    private ImageView imgClose;
    private EditText eTxtSearch;
    private TextView txtErrorMsg;
    private RecyclerView recyclerView;

    private Spinner spnBookingTypes;

    private View layoutSearch;

    private BookingResponse bookingResponse;

    private SpaceItemDecoration spaceItemDecoration;
    private RecyclerVerticalSpacingItemDecoration verticalSpacingItemDecoration;

    static {

        BOOKING_MAP.put("Future", "Idle");
        BOOKING_MAP.put("Current", "InProgress");
        BOOKING_MAP.put("Completed", "Complete");
    }

    public static BookingFragment newInstance(String type) {

        BookingFragment fragment = new BookingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("BookingType", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View containerView = inflater.inflate(R.layout.fragment_booking, null, false);

        initViews(containerView);

        if (getArguments() != null) {
            bookingType = getArguments().getString("BookingType");
        }

        return containerView;
    }

    private void initViews(View view) {

        layoutSearch = view.findViewById(R.id.layoutSearch);

        spnBookingTypes = view.findViewById(R.id.spnBookingTypes);
        imgClose = view.findViewById(R.id.imgClose);
        eTxtSearch = view.findViewById(R.id.eTxtSearch);
        txtErrorMsg = view.findViewById(R.id.txtErrorMsg);
        recyclerView = view.findViewById(R.id.recyclerView);

        eTxtSearch.setText("");
    }

    private void setupRecyclerView() {
        spaceItemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_4dp));
        verticalSpacingItemDecoration = new RecyclerVerticalSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_4dp));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        setListeners();

        setupRecyclerView();

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData (bookingType);
    }

    public void toggleSearchOptions(boolean show) {
        layoutSearch.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void filterData(String txt) {

        final List<Booking> filteredData = getFilteredData(txt);

        populateData(filteredData);
    }

    @NonNull
    private List<Booking> getFilteredData(String txt) {
        List<Booking> filteredData = new ArrayList<>();

//        if (bookingResponse != null && bookingResponse.getData() != null) {
//            for (Booking commission : bookingResponse.getData()) {
//                if (commission.contains(txt)) {
//                    filteredData.add(commission);
//                }
//            }
//        }
        return filteredData;
    }

    private void setListeners() {

        imgClose.setOnClickListener(closeSearchClickListener);

        eTxtSearch.addTextChangedListener(searchTextWatcher);
        eTxtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    filterData(textView.getText().toString());

                    return true;
                }

                return false;
            }
        });

        spnBookingTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String bookingType = (String) spnBookingTypes.getItemAtPosition (position);

                fetchData(bookingType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if(TextUtils.isEmpty(editable.toString())) {
               eTxtSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                filterData("");
                return;
            }

            eTxtSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            filterData(editable.toString());
        }
    };

    private View.OnClickListener closeSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Utils.hideKeyboard(getContext(), view);
            eTxtSearch.setText("");
            toggleSearchOptions(false);
            if(bookingResponse == null || bookingResponse.getData() == null) {
                populateData(new ArrayList<Booking>());
                return;
            }
            populateData(bookingResponse.getData());
        }
    };

    private void showList(boolean show) {
        txtErrorMsg.setText(getString(R.string.err_no_data_found));
        txtErrorMsg.setVisibility(show ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void populateData(List<Booking> productCommissionList) {

        if (productCommissionList == null || productCommissionList.isEmpty()) {
            showList(false);
            return;
        }

        recyclerView.destroyDrawingCache();
        removeDecorations();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(verticalSpacingItemDecoration);
        recyclerView.addItemDecoration(spaceItemDecoration);
        BookingAdapter adapter = new BookingAdapter(getContext(), productCommissionList, this);

        showList(true);
        recyclerView.setAdapter(adapter);
    }

    private void removeDecorations() {
        recyclerView.removeItemDecoration(spaceItemDecoration);
        recyclerView.removeItemDecoration(verticalSpacingItemDecoration);
    }

    private void fetchData(String bookingType) {

        this.bookingType = bookingType;
        int rider = PreferenceUtility.getInteger(getContext(), KeyConstants.KEY_RIDER, 0);
        String type = BOOKING_MAP.containsKey(bookingType) ? BOOKING_MAP.get(bookingType) : "";
        ApiClient.getClient().create(Services.class).getBookings(type, rider).enqueue(new WebClientCallBack<BookingResponse>(getContext(), new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (!(response instanceof BookingResponse)) {
                    showList(false);
                    return;
                }

                bookingResponse = (BookingResponse) response;
                populateData(((BookingResponse) response).getData());
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(getContext(), errorMessage);
            }
        }, true));
    }

    @Override
    public void onItemClick(BaseEntity entity) {

        if (!(entity instanceof Booking)) {
            return;
        }

        showBookingDetailsScreen((Booking) entity);

    }

    private void showBookingDetailsScreen(Booking booking) {

        Intent intent = new Intent(getContext(), BookingDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_BOOKING, booking);
        startActivity(intent);
    }
}
