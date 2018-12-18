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
public class ProfileFragment extends FragmentWithUser<AuthenticatedUser> {
    private static final String OWNER_TAG = "OWNER_TAG";
    private static final int CAMERA_CODE = 1234;
    private static final int W_STORAGE_PERM_CODE = 260;
    private static final int ANGLE_ROTATED_90 = 90;
    private static final int ANGLE_ROTATED_180 = 180;
    private static final int ANGLE_ROTATED_270 = 270;

    private int internal = 0;
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
     * @param user User data
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(AuthenticatedUser user, boolean profileOwner) {
        if(user == null)
            throw new IllegalArgumentException("user can't be null");
        Bundle bundle = new Bundle();
        bundle.putBoolean(OWNER_TAG, profileOwner);
        bundle.putSerializable(ARG_USER, user);

        // Transmit data
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profileOwner = (Boolean) getArguments().get(OWNER_TAG);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, user.getFirstNames() + "'s Profile");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            pictureRef = storageRef.child("images/" + user.getSciper() + ".jpg");

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
        if (user.getFirstNames() != null && user.getFirstNames().length() > 1) {
            builder.append(user.getFirstNames().substring(0, 1).toUpperCase());
            builder.append(user.getFirstNames().substring(1));
        }
        if (user.getLastNames() != null && user.getLastNames().length() > 1) {
            builder.append(" ").append(user.getLastNames().substring(0, 1).toUpperCase());
            builder.append(user.getLastNames().substring(1));
        }

        if (user.getRoles().contains(UserRole.ADMIN.toString())) {
            builder.append(" - ADMIN");
        }

        String username = builder.toString();

        user_view.setText(username);

        pic = view.findViewById(R.id.profile_image);


        View add_picture_view = view.findViewById(R.id.profile_add_photo);
        if (profileOwner) {
            add_picture_view.setOnClickListener(v -> {
                if (askPermissions()) {
                    goToCamera();
                }
            });
        } else {
            add_picture_view.setVisibility(View.GONE);
        }

        if (askPermissions()) {
            setBitmapFromStorage();
        }
        TextView gasparView = view.findViewById(R.id.profile_gaspar_text);
        gasparView.setText(user.getGaspar());

        TextView emailView = view.findViewById(R.id.profile_email_edit);
        emailView.setText(user.getEmail());

        TextView sciperView = view.findViewById(R.id.profile_sciper_edit);
        sciperView.setText(user.getSciper());

        TextView unit = view.findViewById(R.id.profile_unit_edit);
        unit.setText(new StringBuilder().append(user.getSection()).append("-").append(user.getSemester()));

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
            pictureRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                IdlingResourceFactory.decrementCountingIdlingResource();
                // Local temp file has been created
                setRescaledImage(pathToTemp);
            }).addOnFailureListener(exception -> {
                IdlingResourceFactory.decrementCountingIdlingResource();
                // Handle any errors
                Toast.makeText(getActivity(), "Unsuccessful load", Toast.LENGTH_SHORT).show();
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
    private File createFileForPicture(){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File (directory + "/user" + user.getSciper() + ".jpg");

        pathToImage = image.getAbsolutePath();
        return image;
    }


    /**
     * generates an intent for the camera with the right uri and file
     * so that the camera saves the file in the SD and on the firebaseStorage
     *
     * adapted from : https://developer.android.com/training/camera/photobasics
     */
    private void goToCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File picture;
            picture = createFileForPicture();

            if (picture != null) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "ch.epfl.sweng.zuluzulu.fileprovider", picture);
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
            uploadTask.addOnFailureListener(exception -> {
                IdlingResourceFactory.decrementCountingIdlingResource();
                Toast.makeText(getActivity(), "Unsuccessful upload", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                IdlingResourceFactory.decrementCountingIdlingResource();
                Toast.makeText(getActivity(), "Successful upload", Toast.LENGTH_SHORT).show();
            });
        }
    }

    /**
     * helper method that takes a path to a file and scale it to make it fit inside the imagebutton
     *
     * @param path the path to the file to rescale
     *
     * adapted from : https://developer.android.com/training/camera/photobasics
     */
    private Bitmap setRescaledImage(String path) {
        int targetHeight = pic.getHeight();
        if(targetHeight == 0){
            targetHeight = 50;

        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int currentHeight = options.outHeight;

        int scaling = currentHeight / targetHeight;

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaling;
        options.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        bitmap = rotateImageDependingOnPhoneModel(bitmap, pathToImage);
        if (options.outHeight != 0) {
            pic.setImageBitmap(bitmap);
        }

        return bitmap;

    }




    /**
     * helper method that checks the angle the picture as been rotated and put it back straight
     * @param bitmap the possibly rotated picture
     * @param path the path to the picture
     * @return the bitmap with the good orientation
     *
     * adapted from : https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a
     */
    private Bitmap rotateImageDependingOnPhoneModel(Bitmap bitmap , String path){
        ExifInterface exifInterface;
        try{
         exifInterface = new ExifInterface(path);
        }catch (Exception e){
            return bitmap;
        }
        int angleToRotate = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap correctBitmap;
        switch(angleToRotate) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                correctBitmap = BitmapUtils.rotateBitmap(bitmap, ANGLE_ROTATED_90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                correctBitmap = BitmapUtils.rotateBitmap(bitmap, ANGLE_ROTATED_180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                correctBitmap = BitmapUtils.rotateBitmap(bitmap, ANGLE_ROTATED_270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                correctBitmap = bitmap;

        }

        return correctBitmap;
    }



}
