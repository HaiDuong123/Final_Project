package com.example.final_project.ui.tracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;
import com.example.final_project.data.model.Answer;
import com.example.final_project.data.model.Question;
import com.example.final_project.ui.ketqua.KetQuaTracNghiemActivity;

import java.util.Arrays;
import java.util.List;

public class ChonTracNghiemActivity extends AppCompatActivity {

    private TextView textCauHoi, textTienDo;
    private LinearLayout answer1, answer2, answer3, answer4;
    private LinearLayout progressBar, btnNext, fullTiendO;

    private ImageView cb1, cb2, cb3, cb4;

    private List<Question> questions;
    private int currentIndex = 0;
    private int totalScore = 0;
    private int selectedScore = -1;
    private int fullWidth = 0;

    // ================= LIFECYCLE =================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_chon_tracnghiem);

        initViews();
        loadLocalQuestions();

        fullTiendO.post(() -> fullWidth = fullTiendO.getWidth());
    }

    // ================= INIT UI =================
    private void initViews() {

        textCauHoi = findViewById(R.id.textCauHoi);
        textTienDo = findViewById(R.id.texttiendo);

        progressBar = findViewById(R.id.thanhtiendo);
        fullTiendO = findViewById(R.id.fulltiendo);

        answer1 = findViewById(R.id.btnanswer1);
        answer2 = findViewById(R.id.btnanswer2);
        answer3 = findViewById(R.id.btnanswer3);
        answer4 = findViewById(R.id.btnanswer4);

        btnNext = findViewById(R.id.btnNext);

        cb1 = answer1.findViewById(R.id.rfhcopeyua74);
        cb2 = answer2.findViewById(R.id.rn2sqq8hs6s);
        cb3 = answer3.findViewById(R.id.r2h3dli005i);
        cb4 = answer4.findViewById(R.id.r1y9q5zgzvp4);
    }

    // ================= LOCAL QUESTIONS =================
    private void loadLocalQuestions() {

        questions = Arrays.asList(

                new Question("Bạn có ít hứng thú hoặc không còn thích làm việc gì?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có cảm thấy buồn, chán nản hoặc tuyệt vọng?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có khó ngủ, ngủ không sâu hoặc ngủ quá nhiều?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có cảm thấy mệt mỏi hoặc thiếu năng lượng?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có ăn kém ngon hoặc ăn quá nhiều?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có cảm thấy bản thân tệ, thất bại, hoặc làm gia đình thất vọng?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có khó tập trung khi đọc sách hoặc xem TV?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có di chuyển hoặc nói chậm hơn bình thường, hoặc ngược lại quá bồn chồn?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có cảm thấy lo lắng hoặc căng thẳng quá mức?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        )),

                new Question("Bạn có cảm thấy cuộc sống mất ý nghĩa hoặc thiếu động lực?",
                        Arrays.asList(
                                new Answer("Không bao giờ",0),
                                new Answer("Vài ngày",1),
                                new Answer("Hơn nửa số ngày",2),
                                new Answer("Gần như mỗi ngày",3)
                        ))
        );

        currentIndex = 0;
        totalScore = 0;
        showQuestion();
    }

    // ================= SHOW QUESTION =================
    private void showQuestion() {

        if (currentIndex >= questions.size()) {
            goToResult();
            return;
        }

        selectedScore = -1;
        resetSelection();

        Question q = questions.get(currentIndex);

        textCauHoi.setText(q.getText());
        textTienDo.setText("Câu " + (currentIndex + 1) + "/" + questions.size());

        if (fullWidth > 0) {
            int progressWidth =
                    (int)(((float)(currentIndex + 1)/questions.size())*fullWidth);
            progressBar.getLayoutParams().width = progressWidth;
            progressBar.requestLayout();
        }

        ((TextView)answer1.findViewById(R.id.rkfgxuhfnj))
                .setText(q.getAnswers().get(0).getText());

        ((TextView)answer2.findViewById(R.id.ri1layallfa))
                .setText(q.getAnswers().get(1).getText());

        ((TextView)answer3.findViewById(R.id.rml03nipvo3c))
                .setText(q.getAnswers().get(2).getText());

        ((TextView)answer4.findViewById(R.id.rsxnewt2ggn9))
                .setText(q.getAnswers().get(3).getText());

        answer1.setOnClickListener(v -> selectAnswer(0,q));
        answer2.setOnClickListener(v -> selectAnswer(1,q));
        answer3.setOnClickListener(v -> selectAnswer(2,q));
        answer4.setOnClickListener(v -> selectAnswer(3,q));

        btnNext.setOnClickListener(v -> {

            if (selectedScore == -1) return;

            totalScore += selectedScore;
            currentIndex++;
            showQuestion();
        });
    }

    // ================= ANSWER =================
    private void selectAnswer(int index, Question q) {

        resetSelection();
        selectedScore = q.getAnswers().get(index).getScore();

        switch(index){
            case 0: cb1.setImageResource(R.drawable.hieuung); break;
            case 1: cb2.setImageResource(R.drawable.hieuung); break;
            case 2: cb3.setImageResource(R.drawable.hieuung); break;
            case 3: cb4.setImageResource(R.drawable.hieuung); break;
        }
    }

    private void resetSelection(){

        cb1.setImageResource(R.drawable.checkbox);
        cb2.setImageResource(R.drawable.checkbox);
        cb3.setImageResource(R.drawable.checkbox);
        cb4.setImageResource(R.drawable.checkbox);
    }

    // ================= RESULT =================
    private void goToResult(){

        Intent intent =
                new Intent(this, KetQuaTracNghiemActivity.class);

        intent.putExtra("score", totalScore);
        startActivity(intent);
        finish();
    }
}