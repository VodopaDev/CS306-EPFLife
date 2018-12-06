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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, Object> userData;
    private String firstName;
    private String lastName;
    private String sciper;
    private String section;
    private String semester;
    private String gaspar;
    private String email;
    private List<String> roles;

    private ImageButton pic;

    private String pathToImage;
    private String pathToTemp;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference pictureRef;
    private byte[] imageBytes;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userData User data
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(Map<String, Object> userData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROFILE_TAG, (HashMap) userData);

        // Transmit data
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        Log.d("newInstance", (String) userData.get("sciper"));
        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userData = (Map<String, Object>) getArguments().getSerializable(PROFILE_TAG);
            Log.d("create", (String) userData.get("sciper"));
            firstName = (String) userData.get("first_name");
            lastName = (String) userData.get("last_name");
            sciper = (String) userData.get("sciper");
            section = (String) userData.get("section");
            semester = (String) userData.get("semester");
            gaspar = (String) userData.get("gaspar");
            email = (String) userData.get("email");
            roles = (List<String>) userData.get("roles");
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, firstName + "'s Profile");
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            pictureRef = storageRef.child("images/" + sciper + ".jpg");

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
        if (firstName != null && firstName.length() > 1) {
            builder.append(firstName.substring(0, 1).toUpperCase());
            builder.append(firstName.substring(1));
        }
        if (lastName != null && lastName.length() > 1) {
            builder.append(" ").append(lastName.substring(0, 1).toUpperCase());
            builder.append(lastName.substring(1));
        }

        if (roles.contains(UserRole.ADMIN.toString())) {
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

        if (askPermissions()) {
            setBitmapFromStorage();
        }
        TextView gasparView = view.findViewById(R.id.profile_gaspar_text);
        gasparView.setText(gaspar);


        TextView emailView = view.findViewById(R.id.profile_email_edit);
        emailView.setText(email);


        TextView sciperView = view.findViewById(R.id.profile_sciper_edit);
        sciperView.setText(sciper);

        TextView unit = view.findViewById(R.id.profile_unit_edit);
        unit.setText(new StringBuilder().append(section).append("-").append(semester));

        return view;
    }

    /**
     * ask permissions for the different resources we use
     *
     * @return if the permissions are granted
     */
    private boolean askPermissions() {
        boolean storage_write = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean storage_read = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!storage_write) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, W_STORAGE_PERM_CODE);
        } else if (!storage_read) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, W_STORAGE_PERM_CODE);
        }

        return storage_read && storage_write;
    }

    /**
     * download the file from firebase and set it into the imagebutton
     */
    private void setBitmapFromStorage() {
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            Log.e("creating temp file", "unable to create a temporary file for intent");
        }
        if (localFile != null) {
            pathToTemp = localFile.getAbsolutePath();
            pictureRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    setRescaledImage(pathToTemp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    //Toast.makeText(getActivity(), "Unsuccessful load", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * create the file in which we will put the image
     *
     * @return the file
     * @throws IOException if creation fails
     */

    private File createImageFile() throws IOException {
        // Create an image file name
        File directory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                sciper,  /* prefix */
                ".jpg",         /* suffix */
                directory      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pathToImage = image.getAbsolutePath();
        return image;
    }


    /**
     * generates an intent for the camera with the right uri and file
     * so that the camera saves the file in the SD and on the firebaseStorage
     */
    private void goToCamera() {
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
     * helper method that takes a path to a file and scale it to make it fit inside the imagebutton
     *
     * @param path the path to the file to rescale
     */
    private void setRescaledImage(String path) {
        int targetH = pic.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);

        int photoH = bmOptions.outHeight;

        int scaling = photoH / targetH;

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaling;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        pic.setImageBitmap(bitmap);
    }

    /**
     * receives the result of the camera activity, set the picture, and upload it to the storage
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {

            System.out.println("here");

            //scale the image and put it in the imagebutton
            setRescaledImage(pathToImage);

            Uri file = Uri.fromFile(new File(pathToImage));
            UploadTask uploadTask = pictureRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getActivity(), "Unsuccessful upload", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Successful upload", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
