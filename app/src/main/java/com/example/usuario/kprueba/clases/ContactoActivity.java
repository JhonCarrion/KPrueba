package com.example.usuario.kprueba.clases;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.usuario.kprueba.R;
import com.example.usuario.kprueba.modelo.Contacto;
import com.example.usuario.kprueba.utiles.GeneralCompat;
import com.example.usuario.kprueba.utiles.ImageLoadingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContactoActivity extends GeneralCompat {

    protected ImageLoadingUtils imageLoadingUtils;
    protected LruCache<String, Bitmap> memoryCache;
    protected boolean tipoFoto;
    protected final static int REQUEST_PERMISSION_GALERIA = 1001;
    protected final static int REQUEST_PERMISSION_CAMARA = 1002;
    protected final int REQUEST_CODE_FROM_GALLERY = 1;
    protected final int REQUEST_CODE_CLICK_IMAGE = 2;
    protected Uri mImageCaptureUri, photoUriDomicilio;
    protected String resultado, filename, nombre;
    protected Bitmap bitmapGlobal;
    protected File photoFile;
    protected AlertDialog alertDialog;

    protected ImageView img_Contacto;


    protected EditText txt_Nombre, txt_Apellido, txt_Numero;
    protected Button btn_GuardarContacto, btn_CancelarContacto;

    protected String IMG_NAME="";

    protected String[] optionEdit = new String[]{"ABRIR CAMARÁ", "SELECCIONAR GALERÍA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_Nombre = findViewById(R.id.txt_contacto_nombre);
        txt_Apellido = findViewById(R.id.txt_contacto_apellido);
        txt_Numero = findViewById(R.id.txt_contacto_numero);
        img_Contacto = findViewById(R.id.img_contacto);

        final Intent intent = new Intent();
        final String idCliente = getIntent().getStringExtra("idCliente");

        if (idCliente != null) {
            Contacto conta = helperContactooBD.leer(2, idCliente).get(0);
            txt_Nombre.setText(conta.getNombre());
            txt_Apellido.setText(conta.getApellido());
            txt_Numero.setText(conta.getNumero());
            String img = conta.getImagen();
            img = img.replace("@drawable/", "android.resource://" + MainActivity.PACKAGE_NAME + "/drawable/");
            Uri uriImagen = Uri.parse(img);
            img_Contacto.setImageURI(uriImagen);
            IMG_NAME = conta.getImagen();
        }

        btn_GuardarContacto = findViewById(R.id.btn_Contacto_Guardar);

        btn_GuardarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txt_Nombre.getText().toString().isEmpty() &&
                        !txt_Apellido.getText().toString().isEmpty() &&
                        !txt_Numero.getText().toString().isEmpty() &&
                        !IMG_NAME.isEmpty()) {
                    if (idCliente != null) {
                        intent.putExtra("result", helperContactooBD.editar(idCliente, cargarContacto()));
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        intent.putExtra("result", helperContactooBD.insertar(cargarContacto()));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    createSimpleDialog(ContactoActivity.this, "ERROR", "Verifique que todos los datos más la foto esten ingresados", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cerrarDialog();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cerrarDialog();
                        }
                    }).show();
                }
            }
        });


        btn_CancelarContacto = findViewById(R.id.btn_Contacto_Cancelar);
        btn_CancelarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        img_Contacto = findViewById(R.id.img_contacto);

        img_Contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ContactoActivity.this);
                dialog.setTitle("Opciones");
                dialog.setItems(optionEdit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                abrirCamara();
                                break;
                            case 1:
                                cargarImagen();
                                break;
                        }
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        imageLoadingUtils = new ImageLoadingUtils(this);
        int cachesize = 60 * 1024 * 1024;

        memoryCache = new LruCache<String, Bitmap>(cachesize) {
            @SuppressLint("NewApi")
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    private Contacto cargarContacto() {
        Contacto cont = new Contacto();
        cont.setNombre(txt_Nombre.getText().toString());
        cont.setApellido(txt_Apellido.getText().toString());
        cont.setNumero(txt_Numero.getText().toString());
        cont.setImagen(IMG_NAME);
        return cont;
    }

    public void abrirCamara() {
        if (ContextCompat.checkSelfPermission(ContactoActivity.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactoActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("ex", ex.getLocalizedMessage());
                }
                if (photoFile != null) {

                    if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
                        photoUriDomicilio = FileProvider.getUriForFile(this,
                                "com.example.usuario.kprueba.provider",
                                photoFile);
                    } else {
                        photoUriDomicilio = Uri.fromFile(photoFile);
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUriDomicilio);
                }
                startActivityForResult(takePictureIntent, REQUEST_CODE_CLICK_IMAGE);
            }

        }

    }

    private void cargarImagen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_GALERIA);
        } else {
            tipoFoto = true;
            Log.e("tipoFoto", "true");
            Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(takePictureIntent, REQUEST_CODE_FROM_GALLERY);
        }

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_PERMISSION_CAMARA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirCamara();
                }
            }
            break;
            case REQUEST_PERMISSION_GALERIA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cargarImagen();
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FROM_GALLERY:
                    mImageCaptureUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(mImageCaptureUri, filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        resultado = cursor.getString(columnIndex);
                        new ImageCompressionAsyncTask(true).execute(resultado);
                        cursor.close();
                    }
                    break;
                case REQUEST_CODE_CLICK_IMAGE:
                    File file1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/KPrueba/" + resultado);
                    if (file1.getPath() != null) {
                        if (!file1.getPath().isEmpty()) {
                            new ImageCompressionAsyncTask(true).execute(file1.getPath());
                        } else {
                            finish();
                        }
                    } else {
                        finish();
                    }
                    break;
            }
        }
    }

    /**
     * Algoritmo de compreción Angel Gálvez
     */
    @SuppressLint("StaticFieldLeak")
    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;

        ImageCompressionAsyncTask(boolean fromGallery) {
            this.fromGallery = fromGallery;
        }

        @Override
        protected String doInBackground(String... params) {
            if (params != null) {
                String filePath = compressImage(params[0]);
                Log.e("tamanio", String.valueOf(bitmapGlobal.getByteCount()));
                Log.e("tamanio", String.valueOf(bitmapGlobal.getRowBytes() * bitmapGlobal.getHeight()));
                return filePath;
            }
            return "";
        }

        String compressImage(String filePath) {
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = imageLoadingUtils.calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
                bitmapGlobal = bmp;
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out;
            if (tipoFoto) {
                filename = getFilename();
                Log.e("fileGaleria", filename);
            } else {
                filename = photoFile.getAbsolutePath();
                Log.e("fileCamara", filename);
            }
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filename;

        }

        String getFilename() {
            String intStorageDirectory = getFilesDir().toString();
            File file = new File(intStorageDirectory, "KPrueba/Imagenes");
            if (!file.exists()) {
                file.mkdirs();
            }
            String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
            return uriSting;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tipoFoto = false;
            resultado = result;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            if (resultado != null && !resultado.isEmpty()) {
                alertDialog = cargarImagenEnviar(resultado);
                alertDialog.show();
            }
        }
    }

    public Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);
    }


    public File createImageFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + "KPrueba");
        File image;
        String img = "id" + "kprueba";
        image = File.createTempFile(img,
                ".jpg",
                storageDir
        );

        resultado = image.getName();
        return image;
    }

    public AlertDialog cargarImagenEnviar(final String imgDireccion) {
        final AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dlg_preview_img, null);
        Button btnAceptar = (Button) v.findViewById(R.id.btn_preview_guardar);
        Button btnCancelar = (Button) v.findViewById(R.id.btn_preview_cancelar);
        ImageView imgFoto = (ImageView) v.findViewById(R.id.img_preview);

        bitmapGlobal = resizeImage(bitmapGlobal, bitmapGlobal.getWidth(), bitmapGlobal.getHeight());
        imgFoto.setImageBitmap(resizeImage(bitmapGlobal, bitmapGlobal.getWidth(), bitmapGlobal.getHeight()));

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("imgdir", imgDireccion);
                IMG_NAME = imgDireccion;

                //imagen
                String img = imgDireccion;
                //Crea ruta de la imagen.
                img = img.replace("@drawable/", "android.resource://" + MainActivity.PACKAGE_NAME + "/drawable/");
                //Obtiene la uri de la imagen.
                Uri uriImagen = Uri.parse(img);
                //Agrega imagen al ImageView.
                img_Contacto.setImageURI(uriImagen);
                alertDialog.dismiss();

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        localBuilder.setView(v);
        return localBuilder.create();
    }
}
