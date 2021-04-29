package cyrille.mbia.gestiondesprsences.BottomSheet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import cyrille.mbia.gestiondesprsences.ClassDetailActivity;
import cyrille.mbia.gestiondesprsences.R;
import cyrille.mbia.gestiondesprsences.realm.Students_List;
import io.realm.Realm;

public class Student_Edit_Sheet extends BottomSheetDialogFragment {
    public String _name, _regNo, _mobNo;
    public long _id;
    public EditText name_student, regNo_student, mobNo_student;
    public CardView call;
    public TextView modify;
    private Realm realm;
    private long id;

    public Student_Edit_Sheet(long iD, String stuName, String regNo, String mobileNo) {
        _name = stuName;
        _regNo = regNo;
        _mobNo = mobileNo;
        _id = iD;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false);

        // initializing our edittext and buttons
        realm = Realm.getDefaultInstance();

        name_student = v.findViewById(R.id.stu_name_edit);
        regNo_student = v.findViewById(R.id.stu_regNo_edit);
        mobNo_student = v.findViewById(R.id.stu_mobNo_edit);
        modify = v.findViewById(R.id.idBtnUpdate);
        call = v.findViewById(R.id.call_edit);


        name_student.setText(_name);
        regNo_student.setText(_regNo);
        mobNo_student.setText(_mobNo);

        /*call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + _mobNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });*/


        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting data from edittext fields.
                String _name = name_student.getText().toString();
                String _regNo = regNo_student.getText().toString();
                String _mobNo = mobNo_student.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(_name)) {
                    name_student.setError("Veuillez Entrer le nom d'étudiant");
                } else if (TextUtils.isEmpty(_regNo)) {
                    regNo_student.setError("Veuillez Entrer l'identifiant d'étudiant");
                } else if (TextUtils.isEmpty(_mobNo)) {
                    mobNo_student.setError("Veuillez Entrer le téléphone d'étudiant");
                } else {
                    // on below line we are getting data from our modal where
                    // the id of the course equals to which we passed previously.
                    final Students_List students_list = realm.where(Students_List.class).equalTo("id", _id).findFirst();
                    System.out.println(_id);
                    updateStudent(students_list);
                    Toast.makeText(getActivity(), "Informations mises à jour.", Toast.LENGTH_SHORT).show();
                }

                // on below line we are displaying a toast message when course is updated.

            }
        });

        return v;
    }

    private void updateStudent(final Students_List students_list) {
        final String _name = name_student.getText().toString();
        final String _regNo = regNo_student.getText().toString();
        final String _mobNo = mobNo_student.getText().toString();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                students_list.setName_student(_name);
                students_list.setRegNo_student(_regNo);
                students_list.setMobileNo_student(_mobNo);
                realm.copyToRealmOrUpdate(students_list);
            }
        });

        Intent intent = new Intent(getContext(), ClassDetailActivity.class);
        startActivity(intent);

    }
}
