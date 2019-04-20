
package com.seamk.mobile.elasticsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class SourceRealization {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("code")
    @Expose
    String code;
    @SerializedName("credits")
    @Expose
    Double credits;
    @SerializedName("minSeats")
    @Expose
    Integer minSeats;
    @SerializedName("maxSeats")
    @Expose
    Integer maxSeats;
    @SerializedName("startDate")
    @Expose
    String startDate;
    @SerializedName("endDate")
    @Expose
    String endDate;
    @SerializedName("enrollmentStart")
    @Expose
    String enrollmentStart;
    @SerializedName("enrollmentEnd")
    @Expose
    String enrollmentEnd;
    @SerializedName("localizedApproveRejectDescription")
    @Expose
    LocalizedApproveRejectDescription localizedApproveRejectDescription;
    @SerializedName("localizedCompletionAlternatives")
    @Expose
    LocalizedCompletionAlternatives localizedCompletionAlternatives;
    @SerializedName("localizedContent")
    @Expose
    LocalizedContent localizedContent;
    @SerializedName("localizedLearningMaterial")
    @Expose
    LocalizedLearningMaterial localizedLearningMaterial;
    @SerializedName("localizedCuEvaluationCriteria1")
    @Expose
    LocalizedCuEvaluationCriteria1 localizedCuEvaluationCriteria1;
    @SerializedName("localizedCuEvaluationCriteria2")
    @Expose
    LocalizedCuEvaluationCriteria2 localizedCuEvaluationCriteria2;
    @SerializedName("localizedCuEvaluationCriteria3")
    @Expose
    LocalizedCuEvaluationCriteria3 localizedCuEvaluationCriteria3;
    @SerializedName("localizedCuEvaluationCriteria4")
    @Expose
    LocalizedCuEvaluationCriteria4 localizedCuEvaluationCriteria4;
    @SerializedName("localizedCurEvaluationCriteria")
    @Expose
    LocalizedCurEvaluationCriteria localizedCurEvaluationCriteria;
    @SerializedName("localizedCurGrade0Description")
    @Expose
    LocalizedCurGrade0Description localizedCurGrade0Description;
    @SerializedName("localizedCurGrade1Description")
    @Expose
    LocalizedCurGrade1Description localizedCurGrade1Description;
    @SerializedName("localizedCurGrade3Description")
    @Expose
    LocalizedCurGrade3Description localizedCurGrade3Description;
    @SerializedName("localizedCurGrade5Description")
    @Expose
    LocalizedCurGrade5Description localizedCurGrade5Description;
    @SerializedName("localizedEmployerConnections")
    @Expose
    LocalizedEmployerConnections localizedEmployerConnections;
    @SerializedName("localizedEvaluationScale")
    @Expose
    LocalizedEvaluationScale localizedEvaluationScale;
    @SerializedName("localizedExamSchedule")
    @Expose
    LocalizedExamSchedule localizedExamSchedule;
    @SerializedName("localizedFurtherInformationOfCourse")
    @Expose
    LocalizedFurtherInformationOfCourse localizedFurtherInformationOfCourse;
    @SerializedName("localizedFurtherInformationOfRealization")
    @Expose
    LocalizedFurtherInformationOfRealization localizedFurtherInformationOfRealization;
    @SerializedName("localizedName")
    @Expose
    LocalizedName localizedName;
    @SerializedName("localizedObjective")
    @Expose
    LocalizedObjective localizedObjective;
    @SerializedName("localizedQualifications")
    @Expose
    LocalizedQualifications localizedQualifications;
    @SerializedName("localizedStudentWorkload")
    @Expose
    LocalizedStudentWorkload localizedStudentWorkload;
    @SerializedName("localizedTeachingMethods")
    @Expose
    LocalizedTeachingMethods localizedTeachingMethods;
    @SerializedName("localizedInternationalConnections")
    @Expose
    LocalizedInternationalConnections localizedInternationalConnections;
    @SerializedName("localizedContentScheduling")
    @Expose
    LocalizedContentScheduling localizedContentScheduling;
    @SerializedName("teachingLanguage")
    @Expose
    String teachingLanguage;
    @SerializedName("virtualProportion")
    @Expose
    Integer virtualProportion;
    @SerializedName("rdProportion")
    @Expose
    Integer rdProportion;
    @SerializedName("currentStatus")
    @Expose
    String currentStatus;
    @SerializedName("studyType")
    @Expose
    List<String> studyType = null;
    @SerializedName("degreeProgrammes")
    @Expose
    List<DegreeProgramme> degreeProgrammes = null;
    @SerializedName("educationalFields")
    @Expose
    List<EducationalField> educationalFields = null;
    @SerializedName("office")
    @Expose
    Office office;
    @SerializedName("studentGroups")
    @Expose
    List<StudentGroup> studentGroups = null;
    @SerializedName("schedulingGroups")
    @Expose
    List<SchedulingGroup> schedulingGroups = null;
    @SerializedName("tags")
    @Expose
    List<Tag> tags = null;
    @SerializedName("teacher")
    @Expose
    List<Teacher> teacher = null;
    @SerializedName("administrator")
    @Expose
    List<Administrator> administrator = null;
    @SerializedName("unit")
    @Expose
    Unit unit;
    @SerializedName("courseUnit")
    @Expose
    CourseUnit courseUnit;
    @SerializedName("composite")
    @Expose
    Boolean composite;
    @SerializedName("compositeLearningUnits")
    @Expose
    List<CompositeLearningUnit> compositeLearningUnits = null;
    @SerializedName("externalId")
    @Expose
    String externalId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Integer getMinSeats() {
        return minSeats;
    }

    public void setMinSeats(Integer minSeats) {
        this.minSeats = minSeats;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEnrollmentStart() {
        return enrollmentStart;
    }

    public void setEnrollmentStart(String enrollmentStart) {
        this.enrollmentStart = enrollmentStart;
    }

    public String getEnrollmentEnd() {
        return enrollmentEnd;
    }

    public void setEnrollmentEnd(String enrollmentEnd) {
        this.enrollmentEnd = enrollmentEnd;
    }

    public LocalizedApproveRejectDescription getLocalizedApproveRejectDescription() {
        return localizedApproveRejectDescription;
    }

    public void setLocalizedApproveRejectDescription(LocalizedApproveRejectDescription localizedApproveRejectDescription) {
        this.localizedApproveRejectDescription = localizedApproveRejectDescription;
    }

    public LocalizedCompletionAlternatives getLocalizedCompletionAlternatives() {
        return localizedCompletionAlternatives;
    }

    public void setLocalizedCompletionAlternatives(LocalizedCompletionAlternatives localizedCompletionAlternatives) {
        this.localizedCompletionAlternatives = localizedCompletionAlternatives;
    }

    public LocalizedContent getLocalizedContent() {
        return localizedContent;
    }

    public void setLocalizedContent(LocalizedContent localizedContent) {
        this.localizedContent = localizedContent;
    }

    public LocalizedLearningMaterial getLocalizedLearningMaterial() {
        return localizedLearningMaterial;
    }

    public void setLocalizedLearningMaterial(LocalizedLearningMaterial localizedLearningMaterial) {
        this.localizedLearningMaterial = localizedLearningMaterial;
    }

    public LocalizedCuEvaluationCriteria1 getLocalizedCuEvaluationCriteria1() {
        return localizedCuEvaluationCriteria1;
    }

    public void setLocalizedCuEvaluationCriteria1(LocalizedCuEvaluationCriteria1 localizedCuEvaluationCriteria1) {
        this.localizedCuEvaluationCriteria1 = localizedCuEvaluationCriteria1;
    }

    public LocalizedCuEvaluationCriteria2 getLocalizedCuEvaluationCriteria2() {
        return localizedCuEvaluationCriteria2;
    }

    public void setLocalizedCuEvaluationCriteria2(LocalizedCuEvaluationCriteria2 localizedCuEvaluationCriteria2) {
        this.localizedCuEvaluationCriteria2 = localizedCuEvaluationCriteria2;
    }

    public LocalizedCuEvaluationCriteria3 getLocalizedCuEvaluationCriteria3() {
        return localizedCuEvaluationCriteria3;
    }

    public void setLocalizedCuEvaluationCriteria3(LocalizedCuEvaluationCriteria3 localizedCuEvaluationCriteria3) {
        this.localizedCuEvaluationCriteria3 = localizedCuEvaluationCriteria3;
    }

    public LocalizedCuEvaluationCriteria4 getLocalizedCuEvaluationCriteria4() {
        return localizedCuEvaluationCriteria4;
    }

    public void setLocalizedCuEvaluationCriteria4(LocalizedCuEvaluationCriteria4 localizedCuEvaluationCriteria4) {
        this.localizedCuEvaluationCriteria4 = localizedCuEvaluationCriteria4;
    }

    public LocalizedCurEvaluationCriteria getLocalizedCurEvaluationCriteria() {
        return localizedCurEvaluationCriteria;
    }

    public void setLocalizedCurEvaluationCriteria(LocalizedCurEvaluationCriteria localizedCurEvaluationCriteria) {
        this.localizedCurEvaluationCriteria = localizedCurEvaluationCriteria;
    }

    public LocalizedCurGrade0Description getLocalizedCurGrade0Description() {
        return localizedCurGrade0Description;
    }

    public void setLocalizedCurGrade0Description(LocalizedCurGrade0Description localizedCurGrade0Description) {
        this.localizedCurGrade0Description = localizedCurGrade0Description;
    }

    public LocalizedCurGrade1Description getLocalizedCurGrade1Description() {
        return localizedCurGrade1Description;
    }

    public void setLocalizedCurGrade1Description(LocalizedCurGrade1Description localizedCurGrade1Description) {
        this.localizedCurGrade1Description = localizedCurGrade1Description;
    }

    public LocalizedCurGrade3Description getLocalizedCurGrade3Description() {
        return localizedCurGrade3Description;
    }

    public void setLocalizedCurGrade3Description(LocalizedCurGrade3Description localizedCurGrade3Description) {
        this.localizedCurGrade3Description = localizedCurGrade3Description;
    }

    public LocalizedCurGrade5Description getLocalizedCurGrade5Description() {
        return localizedCurGrade5Description;
    }

    public void setLocalizedCurGrade5Description(LocalizedCurGrade5Description localizedCurGrade5Description) {
        this.localizedCurGrade5Description = localizedCurGrade5Description;
    }

    public LocalizedEmployerConnections getLocalizedEmployerConnections() {
        return localizedEmployerConnections;
    }

    public void setLocalizedEmployerConnections(LocalizedEmployerConnections localizedEmployerConnections) {
        this.localizedEmployerConnections = localizedEmployerConnections;
    }

    public LocalizedEvaluationScale getLocalizedEvaluationScale() {
        return localizedEvaluationScale;
    }

    public void setLocalizedEvaluationScale(LocalizedEvaluationScale localizedEvaluationScale) {
        this.localizedEvaluationScale = localizedEvaluationScale;
    }

    public LocalizedExamSchedule getLocalizedExamSchedule() {
        return localizedExamSchedule;
    }

    public void setLocalizedExamSchedule(LocalizedExamSchedule localizedExamSchedule) {
        this.localizedExamSchedule = localizedExamSchedule;
    }

    public LocalizedFurtherInformationOfCourse getLocalizedFurtherInformationOfCourse() {
        return localizedFurtherInformationOfCourse;
    }

    public void setLocalizedFurtherInformationOfCourse(LocalizedFurtherInformationOfCourse localizedFurtherInformationOfCourse) {
        this.localizedFurtherInformationOfCourse = localizedFurtherInformationOfCourse;
    }

    public LocalizedFurtherInformationOfRealization getLocalizedFurtherInformationOfRealization() {
        return localizedFurtherInformationOfRealization;
    }

    public void setLocalizedFurtherInformationOfRealization(LocalizedFurtherInformationOfRealization localizedFurtherInformationOfRealization) {
        this.localizedFurtherInformationOfRealization = localizedFurtherInformationOfRealization;
    }

    public LocalizedName getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(LocalizedName localizedName) {
        this.localizedName = localizedName;
    }

    public LocalizedObjective getLocalizedObjective() {
        return localizedObjective;
    }

    public void setLocalizedObjective(LocalizedObjective localizedObjective) {
        this.localizedObjective = localizedObjective;
    }

    public LocalizedQualifications getLocalizedQualifications() {
        return localizedQualifications;
    }

    public void setLocalizedQualifications(LocalizedQualifications localizedQualifications) {
        this.localizedQualifications = localizedQualifications;
    }

    public LocalizedStudentWorkload getLocalizedStudentWorkload() {
        return localizedStudentWorkload;
    }

    public void setLocalizedStudentWorkload(LocalizedStudentWorkload localizedStudentWorkload) {
        this.localizedStudentWorkload = localizedStudentWorkload;
    }

    public LocalizedTeachingMethods getLocalizedTeachingMethods() {
        return localizedTeachingMethods;
    }

    public void setLocalizedTeachingMethods(LocalizedTeachingMethods localizedTeachingMethods) {
        this.localizedTeachingMethods = localizedTeachingMethods;
    }

    public LocalizedInternationalConnections getLocalizedInternationalConnections() {
        return localizedInternationalConnections;
    }

    public void setLocalizedInternationalConnections(LocalizedInternationalConnections localizedInternationalConnections) {
        this.localizedInternationalConnections = localizedInternationalConnections;
    }

    public LocalizedContentScheduling getLocalizedContentScheduling() {
        return localizedContentScheduling;
    }

    public void setLocalizedContentScheduling(LocalizedContentScheduling localizedContentScheduling) {
        this.localizedContentScheduling = localizedContentScheduling;
    }

    public String getTeachingLanguage() {
        return teachingLanguage;
    }

    public void setTeachingLanguage(String teachingLanguage) {
        this.teachingLanguage = teachingLanguage;
    }

    public Integer getVirtualProportion() {
        return virtualProportion;
    }

    public void setVirtualProportion(Integer virtualProportion) {
        this.virtualProportion = virtualProportion;
    }

    public Integer getRdProportion() {
        return rdProportion;
    }

    public void setRdProportion(Integer rdProportion) {
        this.rdProportion = rdProportion;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<String> getStudyType() {
        return studyType;
    }

    public void setStudyType(List<String> studyType) {
        this.studyType = studyType;
    }

    public List<DegreeProgramme> getDegreeProgrammes() {
        return degreeProgrammes;
    }

    public void setDegreeProgrammes(List<DegreeProgramme> degreeProgrammes) {
        this.degreeProgrammes = degreeProgrammes;
    }

    public List<EducationalField> getEducationalFields() {
        return educationalFields;
    }

    public void setEducationalFields(List<EducationalField> educationalFields) {
        this.educationalFields = educationalFields;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public List<StudentGroup> getStudentGroups() {
        return studentGroups;
    }

    public void setStudentGroups(List<StudentGroup> studentGroups) {
        this.studentGroups = studentGroups;
    }

    public List<SchedulingGroup> getSchedulingGroups() {
        return schedulingGroups;
    }

    public void setSchedulingGroups(List<SchedulingGroup> schedulingGroups) {
        this.schedulingGroups = schedulingGroups;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Teacher> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<Teacher> teacher) {
        this.teacher = teacher;
    }

    public List<Administrator> getAdministrator() {
        return administrator;
    }

    public void setAdministrator(List<Administrator> administrator) {
        this.administrator = administrator;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public CourseUnit getCourseUnit() {
        return courseUnit;
    }

    public void setCourseUnit(CourseUnit courseUnit) {
        this.courseUnit = courseUnit;
    }

    public Boolean getComposite() {
        return composite;
    }

    public void setComposite(Boolean composite) {
        this.composite = composite;
    }

    public List<CompositeLearningUnit> getCompositeLearningUnits() {
        return compositeLearningUnits;
    }

    public void setCompositeLearningUnits(List<CompositeLearningUnit> compositeLearningUnits) {
        this.compositeLearningUnits = compositeLearningUnits;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}
