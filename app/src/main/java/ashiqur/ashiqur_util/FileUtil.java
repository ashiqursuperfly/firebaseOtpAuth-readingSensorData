package ashiqur.ashiqur_util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


public class FileUtil {

    /**
     * TODO: 1. add apache POI lib jar files inside app->libs folder
     * Download From : https://github.com/andruhon/android5xlsx
     * In build.gradle's app module :
     * TODO: 2.Inside android{} tag:
     *  packagingOptions {
     *         exclude 'META-INF/DEPENDENCIES'
     *         exclude 'META-INF/NOTICE'
     *         exclude 'META-INF/NOTICE.txt'
     *         exclude 'META-INF/LICENSE'
     *         exclude 'META-INF/LICENSE.txt'
     *     }
     *  TODO: 3.Inside defaultconfig{} Tag
     *       multiDexEnabled true
     *  TODO: 4.Inside dependencies{} Tag :
     *     implementation fileTree(dir: 'libs', include: ['*.jar'])
     *     implementation 'com.android.support:multidex:1.0.3'
     * */

    final static String TAG = "ASHIQUR-FILEUTIL";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String projectName;
    private String path ;

    /**@param projectName creates a directory with the project's name in the device's
     * local storage, writes files that this app needs to store, in this directory*/
    public FileUtil(String projectName) {
        this.projectName = projectName;
        this.path =Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ projectName;
    }

    //= Environment.getExternalStorageDirectory().getAbsolutePath() +"/Lady";

    public String readTxtFile(String fileName){
        String line = null;

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(path+"/"+fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line).append(System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        } catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return line;
    }

    public boolean appendToTxtFile(String data, String fileName){
        try {
            new File(path ).mkdir();
            File file = new File(path+"/"+fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

            return true;
        } catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return  false;


    }

    @TargetApi(27)
    protected static void askPermissions(Activity activity) {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        activity.requestPermissions(permissions, requestCode);
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permissionWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionRead != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public boolean delete(File file) {
        try{
            if(file.delete())
            {
                System.out.println("File deleted successfully");
                return true;
            }
            else
            {
                System.out.println("Failed to delete the file");
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    public HashMap<String,String> readAllDataFromXls(String fileName) throws IOException
    {
        InputStream ExcelFileToRead = new FileInputStream(path+"/"+fileName);
        HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

        HSSFSheet sheet=wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;
        HashMap<String,String> data=new HashMap<>();
        Iterator rows = sheet.rowIterator();

        int r=0,c=0;
        while (rows.hasNext())
        {
            row=(HSSFRow) rows.next();
            Iterator cells = row.cellIterator();

            while (cells.hasNext())
            {
                cell=(HSSFCell) cells.next();

                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
                {
                    String keyString="["+r+"]["+c+"]";
                    data.put(keyString,cell.getStringCellValue()+" ");
                    Log.wtf(TAG,"XLS KEY,Data READ:"+keyString+","+cell.getStringCellValue());
                }
                else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
                {
                    String keyString="["+r+"]["+c+"]";
                    data.put(keyString,cell.getNumericCellValue()+" ");
                    Log.wtf(TAG,"XLS KEY,Data READ:"+keyString+","+cell.getStringCellValue());
                }
                else
                {
                    //U Can Handel Boolean, Formula, Errors
                    Log.wtf(TAG,"Un-Identified Type or Null Data Read From xls");
                }
                c++;
            }
            c=0;
            r++;
            System.out.println();
        }
        return data;

    }

    public void appendToXls(String fileName,String [] data){

        File file=new File(path+"/"+fileName);
        if(!file.exists())
        {
            Log.wtf(TAG,fileName+"File Doesnot Exist");
            try {
                createXlsFile(fileName);
                appendToXlsUtil(fileName,data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            {
                Log.wtf(TAG,"File Exists ,preparing to Append");
                try {
                    appendToXlsUtil(fileName,data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    private void appendToXlsUtil(String filename, String[] data) throws IOException {

        String excelFileName = path+"/"+filename;//name of excel file

            FileInputStream myxls = new FileInputStream(excelFileName);
            HSSFWorkbook studentsSheet = new HSSFWorkbook(myxls);
            HSSFSheet worksheet = studentsSheet.getSheetAt(0);
            int lastRow=worksheet.getLastRowNum();

            //HSSFRow lastRow=worksheet.getRow(lastRowIdx);
            //lastRow.getCell(1).toString().equals("null");
            HSSFRow row = worksheet.createRow(++lastRow);
            Log.wtf(TAG,"Appending at row"+lastRow);
            for (int i=0;i<data.length;i++)
            {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(data[i]);

            }
            myxls.close();
            FileOutputStream output_file =new FileOutputStream(new File(excelFileName));
                    //write changes
            studentsSheet.write(output_file);
            output_file.close();
            //Log.wtf(TAG,"File is successfully Appended");



//        HSSFSheet sheet = workbook.getSheet(filename) ;
//
//        HSSFRow row = sheet.createRow(r);
//        for (int i=0;i<data.length;i++)
//        {
//            HSSFCell cell = row.createCell(i);
//            cell.setCellValue(data[i]);
//
//        }
//
//        FileOutputStream fileOut = new FileOutputStream(excelFileName);
//
//        //write this workbook to an Outputstream.
//        workbook.write(fileOut);
//        fileOut.flush();
//        fileOut.close();
        Log.wtf(TAG,"Append SuccessFul");
    }


    public void createXlsFile(String fileName) throws IOException{
        createDirIfNotExists();
        File file=new File(path+"/"+fileName);
        if(!file.exists())
        {   Log.wtf(TAG,"Creating File"+fileName);


           HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(fileName);
            //Create a new row in current sheet
            Row row = sheet.createRow(0);
            //Create a new cell in current row
            Cell cell = row.createCell(0);
            //Set value to new value

            try {
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.wtf(TAG,"File Creation Successful");

        }
        else
            {
                Log.wtf(TAG,path+"/"+fileName + "Cannot Create File, Already Exists!");
            }
    }
    private boolean createDirIfNotExists()
    {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), projectName);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.wtf(TAG, "Problem creating folder");
                ret = false;
            }
        }
        return ret;
    }
    public HashMap<String,String> readExcelFileFromAssets(Activity activity,String fileName) {
        HashMap<String,String> data=new HashMap<>();
        try {
            // Creating Input Stream
            /*
             * File file = new File( filename); FileInputStream myInput = new
             * FileInputStream(file);
             */
            Log.wtf(TAG,"Opening Assets File "+fileName);

            InputStream myInput;

            //  Don't forget to Change to your assets folder excel sheet
            myInput = activity.getAssets().open(fileName);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells. **/
            Iterator<Row> rowIter = mySheet.rowIterator();

            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    String keyString="["+myCell.getRowIndex()+"]["+myCell.getColumnIndex()+"]";
                    data.put(keyString,myCell.toString());
                    Log.e("FileUtils", "Cell Value: " + myCell.toString()+ " Index :" +myCell.getColumnIndex() );

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}


