package com.example.projecta;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.projecta.adapter.ChatAdapter;
import com.example.projecta.database.UserDB;
import com.example.projecta.manager.PermissionManager;
import com.example.projecta.manager.PhotoManager;
import com.example.projecta.model.ChatMsg;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ChatroomFragment extends Fragment {
    private static final int REQUEST_CAMERA_AND_WRITE_STORAGE = 5000;
    public static final String AUTHORITY = "com.example.projecta.fileprovider";

    FloatingActionButton fabSend, fabCamera, fabAlbum;
    EditText editMsg;
    RecyclerView chatRecyclerView;
    ChatAdapter chatAdapter;
    LinearLayoutManager linearLayoutManager;

    SharedPreferences sharedPreferences;
    private DatabaseReference reference;
//    private StorageReference storageReference;
    private InputMethodManager imm;
    private Context context;
    private Window window;

    String name, address, event;
    String uuid;
    ArrayList<String> keyList = new ArrayList<>();

    private Uri cameraUri;
    private File cameraFile;
    private String cameraFileName, cameraPath;

    private String filePath, avatarPath;

    public ChatroomFragment(Context context, String name, String address, String event, Window window) {
        this.context = context;
        this.name = name;
        this.address = address;
        this.event = event;
        this.window = window;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chatroom, container, false);
        getUUID();
        initDatabase();
        initView(v);

        fabSend.setOnClickListener(this::fabSend);
        fabCamera.setOnClickListener(this::fabCamera);
        fabAlbum.setOnClickListener(this::fabAlbum);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (chatAdapter != null)
                    chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        editMsg.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_DONE)
                return false;
            if (TextUtils.isEmpty(editMsg.getText()))
                return false;
            sendMsg();
            chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        });

        editMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    fabSend.setVisibility(View.VISIBLE);
                    fabCamera.setVisibility(View.GONE);
                    fabAlbum.setVisibility(View.GONE);
                } else {
                    fabSend.setVisibility(View.GONE);
                    fabCamera.setVisibility(View.VISIBLE);
                    fabAlbum.setVisibility(View.VISIBLE);
                }
            }
        });

        return v;
    }

    private void getUUID() {
        uuid = context.getSharedPreferences("user", MODE_PRIVATE).getString("UUID", "");
    }

    public void initDatabase() {
        sharedPreferences = context.getSharedPreferences("chatTool", MODE_PRIVATE);
        reference = new UserDB().getDatabase()
                .child("chats").child(address).child(event);
//        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void initView(View view) {
        fabSend = view.findViewById(R.id.fab_send);
        fabCamera = view.findViewById(R.id.fab_camera);
        fabAlbum = view.findViewById(R.id.fab_album);
        editMsg = view.findViewById(R.id.edit_msg);

        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatRecyclerView = view.findViewById(R.id.msg_rv);
        displayChatMsg();

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void fabSend(View view) {
        try {
            if (TextUtils.isEmpty(editMsg.getText()))
                return;
            sendMsg();
            chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {

                    sendImg(Uri.fromFile(cameraFile));
                } else {
                    Log.d(TAG, "FAIL RESULT CODE = " + result.getResultCode());
                }
            });

    public void fabCamera(View view) {
        if (checkPhotoPermission()) {
            cameraFileName = PhotoManager.getFileName();
            File dir = PhotoManager.getFileDir();
            Log.d(TAG, "fabCamera: dir = " + dir);
            cameraFile = new File(dir, cameraFileName);
            Log.d(TAG, "fabCamera: file = " + cameraFile);
            cameraPath = cameraFile.getPath();
            Log.d(TAG, "fabCamera: path = " + cameraPath);
            if (Build.VERSION.SDK_INT >= 24) {
                cameraUri = FileProvider.getUriForFile(context,
                        AUTHORITY, cameraFile);
                Log.d(TAG, "fabCamera: cameraURI_A = " + cameraUri);

            } else {
                cameraUri = Uri.fromFile(cameraFile);
                Log.d(TAG, "fabCamera: cameraURI_B = " + cameraUri);
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//使用拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            intent.putExtra("URI", cameraUri.toString());
            cameraLauncher.launch(intent);
        } else {
            // 要求權限
            PermissionManager.requestMultiPermission((ChatActivity) getActivity(),
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            },
                            REQUEST_CAMERA_AND_WRITE_STORAGE);

        }
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {
                    Log.d(TAG, "fabAlbum: I got data!! = " + result.getData());
                    sendImg(result.getData().getData());
                } else {
                    Log.d(TAG, "FAIL RESULT CODE = " + result.getResultCode());
                }
            }
    );

    public void fabAlbum(View view) {
        if (checkAlbumPermission()) {
            Log.d(TAG, "fabAlbum: I have permission");
            cameraFileName = PhotoManager.getFileName();
            File dir = PhotoManager.getFileDir();
            cameraFile = new File(dir, cameraFileName);
            cameraPath = cameraFile.getPath();


            Intent albumIntent = new Intent();
            albumIntent.setType("image/*");//設定只顯示圖片區，不要秀其它的資料夾
            albumIntent.setAction(Intent.ACTION_GET_CONTENT);//取得本機相簿的action
            launcher.launch(albumIntent);
            chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        } else {
            Log.d(TAG, "fabAlbum: I dont have permission");
            PermissionManager.requestReadExternalStoragePermission((ChatActivity) getActivity());
        }
    }



    private void sendMsg() {
        String msg = editMsg.getText().toString();
        long time = new Date().getTime();

        String key = reference.push().getKey();
        keyList.add(key);
        reference.child(key).setValue(new ChatMsg(name, msg, time, uuid));

        editMsg.setText("");
        Set<String> saveKeyList = new HashSet<>();
        for (String mKey : keyList)
            saveKeyList.add(mKey);
        sharedPreferences.edit().putStringSet("keyList", saveKeyList).apply();
    }

    private void sendImg(Uri imgUri) {
        StorageReference firestore = new UserDB().getFirestore();
        long time = new Date().getTime();
        String fileName = generateImgName(name, uuid, time);
        firestore = firestore.child("images/" + fileName);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg").build();

        UploadTask uploadTask = firestore.putFile(imgUri, metadata);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            String key = reference.push().getKey();
            keyList.add(key);
            reference.child(key).setValue(new ChatMsg(name, "", time, uuid, fileName));
            Set<String> saveKeyList = new HashSet<>();
            for (int i = 0; i < keyList.size(); i++) {
                saveKeyList.add(keyList.get(i));
            }
            sharedPreferences.edit().putStringSet("keyList", saveKeyList).apply();
        });

    }

    private String generateImgName(String username, String uuid, long date) {
        String sentTime = String.valueOf(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH).format(date));
        return username.concat(uuid.substring(0, 4).concat(sentTime.concat(".jpg")));
    }

    private void displayChatMsg() {
        Query chatQuery = reference; //.limitToLast(10);
        FirebaseRecyclerOptions<ChatMsg> chatOptions = new FirebaseRecyclerOptions.Builder<ChatMsg>()
                .setQuery(chatQuery, ChatMsg.class)
                .setLifecycleOwner(ChatroomFragment.this)
                .build();

        chatRecyclerView.setLayoutManager(
                new WrapContentLinearLayoutManager((ChatActivity) getActivity()
                        , LinearLayoutManager.VERTICAL, false));
        chatRecyclerView.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(chatOptions, uuid, context);
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        keyList = new ArrayList<>();
        Set<String> saveKeyList = new HashSet<>();
        saveKeyList = sharedPreferences.getStringSet("keyList", saveKeyList);
        keyList.addAll(saveKeyList);
    }

    private boolean checkPhotoPermission() {
        if (PermissionManager.isWriteExternalStorageGranted((ChatActivity) getActivity())
                && PermissionManager.isCameraGranted((ChatActivity) getActivity())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAlbumPermission() {
        if (PermissionManager.isWriteExternalStorageGranted((ChatActivity) getActivity())
                && PermissionManager.isCameraGranted((ChatActivity) getActivity())) {
            return true;
        } else {
            return false;
        }
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Activity activity, int line, boolean flag) {
            super(activity, line, flag);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

}