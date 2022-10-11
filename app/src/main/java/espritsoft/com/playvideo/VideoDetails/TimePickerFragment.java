package espritsoft.com.playvideo.VideoDetails;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c= Calendar.getInstance();
        int houre = 24;
        int minut = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getContext(), (TimePickerDialog.OnTimeSetListener) getContext(),houre,minut, android.text.format.DateFormat.is24HourFormat(getActivity()));
    }
}
