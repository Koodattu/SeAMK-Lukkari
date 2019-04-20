package com.seamk.mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.seamk.mobile.elasticsearch.Administrator;
import com.seamk.mobile.elasticsearch.CompositeLearningUnit;
import com.seamk.mobile.elasticsearch.DegreeProgramme;
import com.seamk.mobile.elasticsearch.EducationalField;
import com.seamk.mobile.elasticsearch.SchedulingGroup;
import com.seamk.mobile.elasticsearch.SourceRealization;
import com.seamk.mobile.elasticsearch.StudentGroup;
import com.seamk.mobile.elasticsearch.Tag;
import com.seamk.mobile.elasticsearch.Teacher;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

public class ActivityRealizationDetails extends AppCompatActivity {

    @BindView(R.id.table_layout) TableLayout tableLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    SourceRealization realization;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_realization_details);
        ButterKnife.bind(this);

        setTitle(getString(R.string.realization_details));

        Bundle extras = getIntent().getExtras();
        realization = Parcels.unwrap(extras.getParcelable("realization"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (realization.getLocalizedName() != null) {
            addText(getString(R.string.name), realization.getLocalizedName().getValueFi());
        }
        if (realization.getCode() != null) {
            addText(getString(R.string.code), realization.getCode());
        }
        if (realization.getCredits() != null) {
            addText(getString(R.string.credits), Double.toString(realization.getCredits()));
        }
        if (realization.getStartDate() != null) {
            addText(getString(R.string.start_date), realization.getStartDate().substring(0, realization.getStartDate().indexOf("T")));
        }
        if (realization.getEndDate() != null) {
            addText(getString(R.string.end_date), realization.getEndDate().substring(0, realization.getEndDate().indexOf("T")));
        }
        if (realization.getEnrollmentStart() != null) {
            addText(getString(R.string.enrollment_start), realization.getEnrollmentStart().substring(0, realization.getEnrollmentStart().indexOf("T")));
        }
        if (realization.getEnrollmentEnd() != null) {
            addText(getString(R.string.enrollment_end), realization.getEnrollmentEnd().substring(0, realization.getEnrollmentEnd().indexOf("T")));
        }
        if (realization.getLocalizedApproveRejectDescription() != null) {
            addText(getString(R.string.approve_reject_desc), realization.getLocalizedApproveRejectDescription().getValueFi());
        }
        if (realization.getLocalizedCompletionAlternatives() != null) {
            addText(getString(R.string.completion_alternative), realization.getLocalizedCompletionAlternatives().getValueFi());
        }
        if (realization.getLocalizedContent() != null) {
            addText(getString(R.string.content), realization.getLocalizedContent().getValueFi());
        }
        if (realization.getLocalizedLearningMaterial() != null) {
            addText(getString(R.string.material), realization.getLocalizedLearningMaterial().getValueFi());
        }
        if (realization.getLocalizedCurEvaluationCriteria() != null) {
            addText(getString(R.string.evaluation_criteria), realization.getLocalizedCurEvaluationCriteria().getValueFi());
        }
        if (realization.getLocalizedCuEvaluationCriteria1() != null) {
            addText(getString(R.string.evaluation_criteria) + " 1", realization.getLocalizedCuEvaluationCriteria1().getValueFi());
        }
        if (realization.getLocalizedCuEvaluationCriteria2() != null) {
            addText(getString(R.string.evaluation_criteria) + " 2", realization.getLocalizedCuEvaluationCriteria2().getValueFi());
        }
        if (realization.getLocalizedCuEvaluationCriteria3() != null) {
            addText(getString(R.string.evaluation_criteria) + " 3", realization.getLocalizedCuEvaluationCriteria3().getValueFi());
        }
        if (realization.getLocalizedCuEvaluationCriteria4() != null) {
            addText(getString(R.string.evaluation_criteria) + " 4", realization.getLocalizedCuEvaluationCriteria4().getValueFi());
        }
        if (realization.getLocalizedCurGrade0Description() != null) {
            addText(getString(R.string.grade_description) + " 0", realization.getLocalizedCurGrade0Description().getValueFi());
        }
        if (realization.getLocalizedCurGrade1Description() != null) {
            addText(getString(R.string.grade_description) + " 1", realization.getLocalizedCurGrade1Description().getValueFi());
        }
        if (realization.getLocalizedCurGrade3Description() != null) {
            addText(getString(R.string.grade_description) + " 3", realization.getLocalizedCurGrade3Description().getValueFi());
        }
        if (realization.getLocalizedCurGrade5Description() != null) {
            addText(getString(R.string.grade_description) + " 5", realization.getLocalizedCurGrade5Description().getValueFi());
        }
        if (realization.getLocalizedEmployerConnections() != null) {
            addText(getString(R.string.employer_connection), realization.getLocalizedEmployerConnections().getValueFi());
        }
        if (realization.getLocalizedEmployerConnections() != null) {
            addText(getString(R.string.evaluation_scale), realization.getLocalizedEmployerConnections().getValueFi());
        }
        if (realization.getLocalizedEvaluationScale() != null) {
            addText(getString(R.string.exam_schedule), realization.getLocalizedEvaluationScale().getValueFi());
        }
        if (realization.getLocalizedFurtherInformationOfCourse() != null) {
            addText(getString(R.string.info_of_course), realization.getLocalizedFurtherInformationOfCourse().getValueFi());
        }
        if (realization.getLocalizedFurtherInformationOfCourse() != null) {
            addText(getString(R.string.info_of_realization), realization.getLocalizedFurtherInformationOfCourse().getValueFi());
        }
        if (realization.getLocalizedObjective() != null) {
            addText(getString(R.string.objective), realization.getLocalizedObjective().getValueFi());
        }
        if (realization.getLocalizedQualifications() != null) {
            addText(getString(R.string.qualifications), realization.getLocalizedQualifications().getValueFi());
        }
        if (realization.getLocalizedStudentWorkload() != null) {
            addText(getString(R.string.student_workload), realization.getLocalizedStudentWorkload().getValueFi());
        }
        if (realization.getLocalizedTeachingMethods() != null) {
            addText(getString(R.string.teaching_methods), realization.getLocalizedTeachingMethods().getValueFi());
        }
        if (realization.getLocalizedInternationalConnections() != null) {
            addText(getString(R.string.international_connection), realization.getLocalizedInternationalConnections().getValueFi());
        }
        if (realization.getLocalizedContentScheduling() != null) {
            addText(getString(R.string.content_scheduling), realization.getLocalizedContentScheduling().getValueFi());
        }
        if (realization.getTeachingLanguage() != null) {
            addText(getString(R.string.teaching_languange), realization.getTeachingLanguage());
        }
        if (realization.getVirtualProportion() != null) {
            addText(getString(R.string.virtual_proportion), Integer.toString(realization.getVirtualProportion()));
        }
        if (realization.getRdProportion() != null) {
            addText(getString(R.string.rd_proportion), Integer.toString(realization.getRdProportion()));
        }
        if (realization.getStudyType() != null) {
            List<String> list = new ArrayList<>(realization.getStudyType());
            String string = listToLongString(list);
            addText(getString(R.string.study_type), string);
        }
        if (realization.getDegreeProgrammes() != null) {
            List<String> list = new ArrayList<>();
            for (DegreeProgramme degreeProgramme : realization.getDegreeProgrammes()) {
                list.add(degreeProgramme.getCode());
            }
            String string = listToLongString(list);
            addText(getString(R.string.degree_programmes), string);
        }
        if (realization.getEducationalFields() != null) {
            List<String> list = new ArrayList<>();
            for (EducationalField educationalField : realization.getEducationalFields()) {
                list.add(educationalField.getLocalizedName().getValueFi());
            }
            String string = listToLongString(list);
            addText(getString(R.string.educational_fields), string);
        }
        if (realization.getOffice() != null) {
            addText(getString(R.string.office), realization.getOffice().getLocalizedName().getValueFi());
        }
        if (realization.getStudentGroups() != null) {
            List<String> list = new ArrayList<>();
            for (StudentGroup studentGroup : realization.getStudentGroups()) {
                list.add(studentGroup.getCode());
            }
            String string = listToLongString(list);
            addText(getString(R.string.student_groups), string);
        }
        if (realization.getSchedulingGroups() != null) {
            List<String> list = new ArrayList<>();
            for (SchedulingGroup schedulingGroup : realization.getSchedulingGroups()) {
                list.add(schedulingGroup.getName());
            }
            String string = listToLongString(list);
            addText(getString(R.string.scheduling_groups), string);
        }
        if (realization.getTags() != null) {
            List<String> list = new ArrayList<>();
            for (Tag tag : realization.getTags()) {
                list.add(tag.getLocalizedName().getValueFi());
            }
            String string = listToLongString(list);
            addText(getString(R.string.tags), string);
        }
        if (realization.getTeacher() != null) {
            List<String> list = new ArrayList<>();
            for (Teacher teacher : realization.getTeacher()) {
                list.add(teacher.getName());
            }
            String string = listToLongString(list);
            addText(getString(R.string.teachers), string);
        }
        if (realization.getAdministrator() != null) {
            List<String> list = new ArrayList<>();
            for (Administrator administrator : realization.getAdministrator()) {
                list.add(administrator.getName());
            }
            String string = listToLongString(list);
            addText(getString(R.string.administrators), string);
        }
        if (realization.getUnit() != null) {
            addText(getString(R.string.unit), realization.getUnit().getLocalizedName().getValueFi());
        }
        if (realization.getCourseUnit() != null) {
            addText(getString(R.string.course_unit), realization.getCourseUnit().getLocalizedName().getValueFi());
        }
        if (realization.getCompositeLearningUnits() != null) {
            List<String> list = new ArrayList<>();
            for (CompositeLearningUnit compositeLearningUnit : realization.getCompositeLearningUnits()) {
                list.add(compositeLearningUnit.getCode());
            }
            String string = listToLongString(list);
            addText(getString(R.string.composite_learning_unit), string);
        }
        if (realization.getExternalId() != null) {
            addText(getString(R.string.external_id), realization.getExternalId());
        }

    }

    void addText(String titleText, String dataText){
        TextView title = new TextView(this);
        title.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.4f));
        title.setTextAppearance(this, android.support.v7.appcompat.R.style.Base_TextAppearance_AppCompat_Display1);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        title.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
        title.setText(titleText);

        TextView data = new TextView(this);
        data.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6f));
        data.setTextAppearance(this, android.support.v7.appcompat.R.style.Base_TextAppearance_AppCompat_Display1);
        data.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        data.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        data.setText(dataText);

        TableRow row = new TableRow(this);
        row.setWeightSum(1f);
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        row.addView(title);
        row.addView(data);

        tableLayout.addView(row);

        LayoutInflater inflater = LayoutInflater.from(this);
        View inflatedLayout = inflater.inflate(R.layout.view_line_slim, null, false);
        tableLayout.addView(inflatedLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String listToLongString(List<String> stringList) {
        String tmpString = "";
        if (stringList.size() > 0) {
            for (String s : stringList) {
                tmpString = tmpString + ", " + s;
            }
            tmpString = tmpString.substring(2);
        }
        return tmpString;
    }
}