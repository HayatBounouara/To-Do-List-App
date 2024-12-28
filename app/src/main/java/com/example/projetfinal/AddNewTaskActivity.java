package com.example.projetfinal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetfinal.Utils.DatabaseHandler;
import com.example.projetfinal.Model.ToDoModel;

public class AddNewTaskActivity extends AppCompatActivity {

    private EditText newTaskText;
    private Button newTaskSaveButton;

    private DatabaseHandler db;
    private int taskId = -1;  // ID de la tâche, -1 signifie nouvelle tâche

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task); // Assurez-vous que ce layout existe
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change la couleur de la barre de statut pour les versions Lollipop et supérieures
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.bar)); // Remplacez yourColor par la couleur de votre choix
        }
        newTaskText = findViewById(R.id.newtasktext);
        newTaskSaveButton = findViewById(R.id.newtaskbutton);

        db = new DatabaseHandler(this);
        db.openDatabase();

        // Récupérer les données passées dans l'Intent
        Intent intent = getIntent();
        if (intent != null) {
            taskId = intent.getIntExtra("id", -1);  // Récupérer l'ID de la tâche (si c'est une tâche existante)
            String taskText = intent.getStringExtra("task");  // Récupérer le texte de la tâche

            // Si taskId est valide, alors nous sommes en mode édition
            if (taskId != -1 && taskText != null) {
                newTaskText.setText(taskText);  // Afficher le texte de la tâche dans le champ
            }
        }

        // Gérer l'action du bouton Save
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = newTaskText.getText().toString();
                if (!taskText.isEmpty()) {
                    // Si c'est une tâche existante, mettre à jour, sinon insérer une nouvelle tâche
                    ToDoModel task = new ToDoModel();
                    task.setTask(taskText);
                    task.setStatus(0); // Statut initial (0 = non terminé)

                    if (taskId != -1) {
                        // Mise à jour de la tâche existante dans la base de données
                        task.setId(taskId); // Définir l'ID de la tâche à modifier
                        db.updateTask(task); // Méthode pour mettre à jour la tâche dans la base de données
                        Toast.makeText(AddNewTaskActivity.this, "Tâche mise à jour!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Insérer une nouvelle tâche dans la base de données
                        db.insertTask(task);
                        Toast.makeText(AddNewTaskActivity.this, "Tâche ajoutée!", Toast.LENGTH_SHORT).show();
                    }

                    // Terminer l'activité et revenir à la précédente
                    setResult(RESULT_OK);  // Retourner un résultat à l'activité appelante
                    finish();
                } else {
                    Toast.makeText(AddNewTaskActivity.this, "Veuillez entrer une tâche", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
