package anand.archana.com.commentcodingtest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CommetAdapter mAdapter;
    private List<CommentModelClass> commentlist = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noCommntView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

Toast.makeText(MainActivity.this,"Click on + Button to add comment",Toast.LENGTH_LONG).show();
        coordinatorLayout = findViewById(R.id.cor);
        recyclerView = findViewById(R.id.recycler_view);
        noCommntView = findViewById(R.id.empty_comnt_view);

        db = new DatabaseHelper(this);

        commentlist.addAll(db.getAllComments());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentDialog(false, null, -1);
            }
        });

        mAdapter = new CommetAdapter(this, commentlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptycomnt();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    /**
     * Inserting new comment in db
     * and refreshing the list
     */
    private void createComment(String comment) {
        // inserting commet in db and getting
        // newly inserted  comnt id
        long id = db.insertComment(comment);

        // get the newly inserted commet from db
        CommentModelClass n = db.getComment(id);

        if (n != null) {
            // adding new comment to array list at 0 position
            commentlist.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptycomnt();
        }
    }

    /**
     * Updating comnt in db and updating
     * item in the list by its position
     */
    private void updateComment(String comnt, int position) {
        CommentModelClass n = commentlist.get(position);
        // updating comnt text
        n.setComment(comnt);

        // updating comnt in db
        db.updateCommet(n);

        // refreshing the list
        commentlist.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptycomnt();
    }

    /**
     * Deleting comnt from SQLite and removing the
     * item from the list by its position
     */
    private void deleteComment(int position) {
        // deleting the comnt from db
        db.deleteCommnt(commentlist.get(position));

        // removing the cmt5 from the list
        commentlist.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptycomnt();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showCommentDialog(true, commentlist.get(position), position);
                } else {
                    deleteComment(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a  comnt
     * when shouldUpdate=true, it automatically displays old commnt and changes the
     * button text to UPDATE
     */
    private void showCommentDialog(final boolean shouldUpdate, final CommentModelClass cmt, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.comment_alertbox, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputcomment = view.findViewById(R.id.comment);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_comment_title) : getString(R.string.lbl_edit_comment_title));

        if (shouldUpdate && cmt != null) {
            inputcomment.setText(cmt.getComment());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputcomment.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter commnt!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating commt
                if (shouldUpdate && cmt != null) {
                    // update commnt by it's id
                    updateComment(inputcomment.getText().toString(), position);
                } else {
                    // create new ncmnt
                    createComment(inputcomment.getText().toString());
                }
            }
        });
    }

    /**
     * Toggling list and empty commnt view
     */
    private void toggleEmptycomnt() {
        // you can check comntlist.size() > 0

        if (db.getCommentsCount() > 0) {
            noCommntView.setVisibility(View.GONE);
        } else {
            noCommntView.setVisibility(View.VISIBLE);
        }
    }
}
