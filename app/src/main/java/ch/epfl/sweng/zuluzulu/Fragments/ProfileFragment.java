package ch.epfl.sweng.zuluzulu.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends SuperFragment {
    private static final String PROFILE_TAG = "PROFILE_TAG";
    private static final int CAMERA_CODE = 1234;
    private static final int W_STORAGE_PERM_CODE = 260;

    private User user;
    private ImageButton pic;

    private String pathToImage;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference pictureRef;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user User
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROFILE_TAG, user);

        // Transmit data
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = (User) getArguments().getSerializable(PROFILE_TAG);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, user.getFirstNames() + "'s Profile");
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            pictureRef = storageRef.child("images/" + user.getSciper() + ".jpg");

        } else {
            throw new AssertionError("No argument");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView user_view = view.findViewById(R.id.profile_name_text);

        StringBuilder builder = new StringBuilder();
        if (user.getFirstNames() != null && user.getFirstNames().length() > 1) {
            builder.append(user.getFirstNames().substring(0, 1).toUpperCase());
            builder.append(user.getFirstNames().substring(1));
        }
        if (user.getLastNames() != null && user.getLastNames().length() > 1) {
            builder.append(" ").append(user.getLastNames().substring(0, 1).toUpperCase());
            builder.append(user.getLastNames().substring(1));
        }
        if (user.hasRole(UserRole.ADMIN)) {
            builder.append(" - ADMIN");
        }

        String username = builder.toString();

        user_view.setText(username);

        askPermissions();

        pic = view.findViewById(R.id.profile_image);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (askPermissions()) {
                    goToCamera();
                }
            }
        });
        getBitmapFromStorage();

        TextView gaspar = view.findViewById(R.id.profile_gaspar_text);
        gaspar.setText(user.getGaspar());


        TextView email = view.findViewById(R.id.profile_email_edit);
        email.setText(user.getEmail());


        TextView sciper = view.findViewById(R.id.profile_sciper_edit);
        sciper.setText(user.getSciper());

        TextView unit = view.findViewById(R.id.profile_unit_edit);
        unit.setText(new StringBuilder().append(user.getSection()).append("-").append(user.getSemester()));

        return view;
    }

    private boolean askPermissions(){
        boolean storage_write = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean storage_read = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!storage_write) {
            ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, W_STORAGE_PERM_CODE);
        } else if (!storage_read) {
            ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, W_STORAGE_PERM_CODE);
        }

        return storage_read && storage_write;
    }

    private Bitmap getBitmapFromStorage(){
        final long ONE_MEGABYTE = 1024 * 1024;
        pictureRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                pic.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Unsuccessful load", Toast.LENGTH_SHORT).show();
            }
        });

        return null;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File directory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                user.getSciper(),  /* prefix */
                ".jpg",         /* suffix */
                directory      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pathToImage = image.getAbsolutePath();
        return image;
    }


    /**
     * generates an intent for the camera with the right uri and file
     */
    private void goToCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File picture = null;
            try {
                picture = createImageFile();
            } catch (IOException ex) {
                Log.e("creating picture file", "unable to create a file for intent");
                return;
            }
            // Continue only if the File was successfully created
            if (picture != null) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "ch.epfl.sweng.zuluzulu.fileprovider",
                        picture);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA_CODE);
            }
        }

    }

    /**
     * receives the result of the camera activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {

            System.out.println("here");

            // to change
            int targetH = pic.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathToImage, bmOptions);
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaling = photoH/targetH;

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaling;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(pathToImage, bmOptions);
            pic.setImageBitmap(bitmap);


            Uri file = Uri.fromFile(new File(pathToImage));
            UploadTask uploadTask = pictureRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getContext(), "Unsuccessful upload", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Successful upload", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}
