package propya.mr.jeevan.Helpers;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class SearchAnything {

    private QuerySnapshot querySnapshot;
    public Query getQuery(String collection,Query query,String key, Object value, String doWhat)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (query==null)
        {
            querySnapshot=null;
            CollectionReference  searchCollection = db.collection(collection);
            switch (doWhat)
            {
                case "gteq": {//greater than equal to
                    query=searchCollection.whereGreaterThanOrEqualTo(key,value);
                    break;
                }
                case "eq"://equal
                {
                   query= searchCollection.whereEqualTo(key,value);
                    break;
                }
                case "arct": //array contains
                {
                    query=searchCollection.whereArrayContains(key,value);
                    break;
                }
                case "arctany"://array contains any
                {
//                searchCollection.whereArrayContainsAny(key,);
                    break;
                }


            }
            return query;

        }
        else
        {
            switch (doWhat)
            {
                case "gteq": {//greater than equal to
                    query.whereGreaterThanOrEqualTo(key,value);
                    break;
                }
                case "eq"://equal
                {
                    query.whereEqualTo(key,value);
                    break;
                }
                case "arct": //array contains
                {
                    query.whereArrayContains(key,value);
                    break;
                }
                case "arctany"://array contains any
                {
//                query.whereArrayContainsAny(key,);
                }
                case "end":
                {
                    executeQuery(query);
                }

            }
            return query;
        }


    }

    public QuerySnapshot executeQuery(Query query)
    {

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                    }
                    querySnapshot= task.getResult();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    querySnapshot=null;
                }
            }
        });
        return querySnapshot;
    }
}
