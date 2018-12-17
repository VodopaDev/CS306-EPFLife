package ch.epfl.sweng.zuluzulu.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
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

import java.io.File;
import java.io.IOException;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.idlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.UserRole;
import ch.epfl.sweng.zuluzulu.Utility.BitmapUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends SuperFragment {
    private static final String USER_DATA_TAG = "USER_DATA_TAG";
    private static final String OWNER_TAG = "OWNER_TAG";
    private static final int CAMERA_CODE = 1234;
    private static final int W_STORAGE_PERM_CODE = 260;

    private int internal = 0;

    private AuthenticatedUser userData;

    private boolean profileOwner;

    private ImageButton pic;

    private String pathToImage;
    private String pathToTemp;
    private StorageReference pictureRef;

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
    public static ProfileFragment newInstance(AuthenticatedUser userData, boolean profileOwner) {
        if(userData == null)
            throw new IllegalArgumentException("user can't be null");
        Bundle bundle = new Bundle();
        bundle.putBoolean(OWNER_TAG, profileOwner);
        bundle.putSerializable(USER_DATA_TAG, userData);

        // Transmit data
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userData = (AuthenticatedUser) getArguments().getSerializable(USER_DATA_TAG);
            profileOwner = (Boolean) getArguments().get(OWNER_TAG);

            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, userData.getFirstNames() + "'s Profile");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            pictureRef = storageRef.child("images/" + userData.getSciper() + ".jpg");

        } else {
            throw new AssertionError("No argument");
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView user_view = view.findViewById(R.id.profile_name_text);

        StringBuilder builder = new StringBuilder();
        if (userData.getFirstNames() != null && userData.getFirstNames().length() > 1) {
            builder.append(userData.getFirstNames().substring(0, 1).toUpperCase());
            builder.append(userData.getFirstNames().substring(1));
        }
        if (userData.getLastNames() != null && userData.getLastNames().length() > 1) {
            builder.append(" ").append(userData.getLastNames().substring(0, 1).toUpperCase());
            builder.append(userData.getLastNames().substring(1));
        }

        if (userData.getRoles().contains(UserRole.ADMIN.toString())) {
            builder.append(" - ADMIN");
        }

        String username = builder.toString();

        user_view.setText(username);

        pic = view.findViewById(R.id.profile_image);


        View add_picture_view = view.findViewById(R.id.profile_add_photo);
        if (profileOwner) {
            add_picture_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (askPermissions()) {
                        goToCamera();
                    }
                }
            });
        } else {
            add_picture_view.setVisibility(View.GONE);
        }

        if (askPermissions()) {
            setBitmapFromStorage();
        }
        TextView gasparView = view.findViewById(R.id.profile_gaspar_text);
        gasparView.setText(userData.getGaspar());

        TextView emailView = view.findViewById(R.id.profile_email_edit);
        emailView.setText(userData.getEmail());

        TextView sciperView = view.findViewById(R.id.profile_sciper_edit);
        sciperView.setText(userData.getSciper());

        TextView unit = view.findViewById(R.id.profile_unit_edit);
        unit.setText(new StringBuilder().append(userData.getSection()).append("-").append(userData.getSemester()));

        return view;
    }

    /**
     * ask permissions for the different resources we use
     *
     * @return if the permissions are granted
     */
    private boolean askPermissions() {
        return (hasPermission()) || (internal++ < 3 && askPermissions());
    }

    /**
     * Check if user has permission
     * @return boolean
     */
    private boolean hasPermission(){
        boolean storage_write = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean storage_read = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean camera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        if (!storage_write) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, W_STORAGE_PERM_CODE);
        } else if (!storage_read) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, W_STORAGE_PERM_CODE+1);
        } else if (!camera) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, W_STORAGE_PERM_CODE+2);
        }

        return storage_read && storage_write && camera;
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
            IdlingResourceFactory.incrementCountingIdlingResource();
            pictureRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    IdlingResourceFactory.decrementCountingIdlingResource();
                    // Local temp file has been created
                    setRescaledImage(pathToTemp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    IdlingResourceFactory.decrementCountingIdlingResource();
                    // Handle any errors
                    Toast.makeText(getActivity(), "Unsuccessful load", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * create the file in which we will put the image
     * and save the path to the file into pathToImage
     *
     * @return the file
     * @throws IOException if creation fails
     */
    private File createFileForPicture() throws IOException {

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File (directory + "/user" + userData.getSciper() + ".jpg");

        pathToImage = image.getAbsolutePath();
        return image;
    }


    /**
     * generates an intent for the camera with the right uri and file
     * so that the camera saves the file in the SD and on the firebaseStorage
     */
    private void goToCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File picture;
            try {
                picture = createFileForPicture();
            } catch (IOException ex) {
                Log.e("creating picture file", "unable to create a file for intent");
                return;
            }
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
     * receives the result of the camera activity, set the picture, and upload it to the storage
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {

            Bitmap correctImage = setRescaledImage(pathToImage);

            BitmapUtils.writeBitmapInSDCard(correctImage,pathToImage);

            Uri uriFile = Uri.fromFile(new File(pathToImage));
            UploadTask uploadTask = pictureRef.putFile(uriFile);
            IdlingResourceFactory.incrementCountingIdlingResource();
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    IdlingResourceFactory.decrementCountingIdlingResource();
                    Toast.makeText(getActivity(), "Unsuccessful upload", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    IdlingResourceFactory.decrementCountingIdlingResource();
                    Toast.makeText(getActivity(), "Successful upload", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * helper method that takes a path to a file and scale it to make it fit inside the imagebutton
     *
     * @param path the path to the file to rescale
     */
    private Bitmap setRescaledImage(String path) {
        int targetHeight = pic.getHeight();
        if(targetHeight == 0){
            targetHeight = 50;

        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);

        int currentHeight = bmOptions.outHeight;

        int scaling = currentHeight / targetHeight;

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaling;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);

        bitmap = rotateImageDependingOnPhoneModel(bitmap, pathToImage);
        if (bmOptions.outHeight != 0) {
            pic.setImageBitmap(bitmap);
        }

        return bitmap;

    }




    /**
     * helper method that checks the angle the picture as been rotated and put it back straight
     * @param bitmap the possibly rotated picture
     * @param path the path to the picture
     * @return the bitmap with the good orientation
     */
    private Bitmap rotateImageDependingOnPhoneModel(Bitmap bitmap , String path){
        ExifInterface exifInterface;
        try{
         exifInterface = new ExifInterface(path);
        }catch (Exception e){
            return bitmap;
        }
        int angleToRotate = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap;
        switch(angleToRotate) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = BitmapUtils.rotateBitmap(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = BitmapUtils.rotateBitmap(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = BitmapUtils.rotateBitmap(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;

        }

        return rotatedBitmap;
    }



}
