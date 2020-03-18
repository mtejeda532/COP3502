//change for GitHub lab
import com.miguel.test.ConsoleGfx;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class RleProgram {
    public static void main(String[] args) {
//Displays and  welcome message and spectrum image
        int option = 1;
        String userData = "", dataType = "";
        byte[] imageData = null;
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the RLE image encoder!");
        System.out.println("Displaying Spectrum Image: ");
        ConsoleGfx.displayImage(ConsoleGfx.testRainbow);
//Displays menu
        while (option != 0) {
            System.out.print("\nRLE Menu\n" +
                    "--------\n" +
                    "0. Exit\n" +
                    "1. Load File\n" +
                    "2. Load Test Image\n" +
                    "3. Read RLE String\n" +
                    "4. Read RLE Hex String\n" +
                    "5. Read Data Hex String\n" +
                    "6. Display Image\n" +
                    "7. Display RLE String\n" +
                    "8. Display Hex RLE Data\n" +
                    "9. Display Hex Flat Data\n" +
                    "\nSelect a Menu Option: ");
            option = input.nextInt();
            //Options user can choose
            switch (option) {
                case 0:
                    break;
                    //Data byte array is converted into a string
                    //dataType is used to determine what methods are ran first in each case
                case 1:
                    System.out.println("Enter name of file to load: ");
                    String filename = input.next();
                    imageData = ConsoleGfx.loadFile(filename);
                    for(int i = 0; i < imageData.length; i++){
                        userData = userData + String.valueOf(imageData[i]);
                    }
                    dataType = "raw";
                    break;
                case 2:
                    imageData = ConsoleGfx.testImage;
                    System.out.println("Test image data loaded.");
                    for(int i = 0; i < imageData.length; i++){
                        userData = userData + String.valueOf(imageData[i]);
                    }
                    dataType = "raw";
                    break;
                case 3:
                    //WHAT DO I DO WITH THIS DATA???
                    System.out.println("Enter an RLE string to be decoded: ");
                    userData = input.next();
                    dataType = "RleString";
                    break;
                case 4:
                    System.out.println("Enter the hex string holding RLE data: ");
                    userData = input.next();
                    dataType = "RleHex";
                    break;
                case 5:
                    System.out.println("Enter the hex string holding flat data: ");
                    userData = input.next();
                    dataType = "raw";
                    break;
                case 6:
                    System.out.println("Displaying image...");
                    if (imageData == null) {
                        System.out.println("(no data)");
                    } else {
                        ConsoleGfx.displayImage(imageData);
                    }
                    break;
                case 7:
                    if (userData != "") {
                        if (dataType == "raw")
                            System.out.println("RLE representation: " + toRleString(encodeRle(stringToData(userData))));
                        else if (dataType == "RleHex") {
                            System.out.println("RLE representation: " + toRleString(stringToData(userData)));
                        }
                        else if(dataType == "RleString"){
                            System.out.println(userData);
                        }
                    }
                    else
                        System.out.println("(no data)");
                    break;
                case 8:
                    if (userData != "") {
                        if (dataType == "RleString")
                            System.out.println("RLE hex values: " + toHexString(stringToRle(userData)));
                        else if (dataType == "raw") {
                            System.out.println("RLE hex values: " + toHexString(encodeRle(stringToData(userData))));
                        }
                        else if(dataType == "RleHex"){
                            System.out.println(userData);
                        }
                    }
                    else
                        System.out.println("(no data)");
                    break;
                case 9:
                    if (userData != "") {
                        if (dataType == "RleHex")
                            System.out.println("Flat hex values:  " + toHexString(stringToData(userData)));

                        else if (dataType == "RleString") {
                            System.out.println("Flat hex values: " + toHexString(decodeRle(stringToRle(userData))));
                        }
                        else if(dataType == "raw"){
                            System.out.println(userData);
                        }
                    }
                    else
                        System.out.println("(no data)");
                    break;
                default:
                    System.out.println("Error! Invalid input.");
            }

        }

    }
//converts byte array of raw data into a string
    public static String toHexString(byte[] data) {

        int[] arr = new int[data.length];
        String dataString = "";

        for (int i = 0; i < data.length; i++) {
            arr[i] = (int) data[i];

            dataString = dataString + Integer.toHexString(arr[i]);

        }
        return dataString;
    }
//counts how many runs of a same number is repeated and accounts for cases in which a number is repeated more than 15 times
    public static int countRuns(byte[] flatData) {
        int count = 1, added = 0, countRun = 1;
        for (int i = 0; i < flatData.length - 1; i++) {
            count = 1;
            while (flatData[i] == flatData[i + 1]){
                //Add if f happens more than 15 times
                if(count == 15){
                    added++;
                    break;
                }
                count++;
                i++;
                if (i == flatData.length - 1)
                    break;
            }
        }

        for (int i = 0; i < flatData.length - 1; i++) {
            if (flatData[i] != flatData[i + 1]) {
                countRun++;
            }
        }
        countRun = countRun + added;
        return countRun;
    }
//takes byte array of data and converts it into rle data
    public static byte[] encodeRle(byte[] flatData) {

        int size = countRuns(flatData);
        byte[] encoded = new byte[size * 2];
        int count, i = 0, j = 0;
        for (i = 0; i < flatData.length - 1; i++) {
            count = 1;

            while (flatData[i] == flatData[i + 1]) {
                if (count > 14) {
                    break;
                }
                count++;
                i++;
                if (i == flatData.length - 1)
                    break;
            }

                if (i == flatData.length - 2 && flatData[i] != flatData[i + 1]) {
                    encoded[j++] = (byte) count;
                    encoded[j++] = flatData[i];
                    encoded[j++] = 1;
                    encoded[j++] = flatData[i + 1];
                } else {
                    encoded[j++] = (byte) count;
                    encoded[j++] = flatData[i];

                }
            }
            return encoded;
        }
//returns length of array to be decoded
        public static int getDecodedLength(byte[] rleData) {
        int length = 0;

        for (int i = 0; i < rleData.length; i++) {
            if (i % 2 == 0) {
                length = length + rleData[i];
            }
        }
        return length;
    }
//decodes rle hex data into raw data
    public static byte[] decodeRle(byte[] rleData) {
        byte[] decoded = new byte[getDecodedLength(rleData)];
        int length = 0, k = 0;

        for (int i = 0; i < rleData.length; i++) {
            if (i % 2 == 0) {
                for(int j = 0; j < rleData[i]; j++) {
                    decoded[k] = rleData[i+1];
                    k++;
                }
            }
        }
        return decoded;
    }
    //converts data into decoded byte array
    public static byte[] stringToData(String dataString){

        byte[] rleArr = new byte[dataString.length()];

        for(int i = 0; i < dataString.length(); i++) {
                rleArr[i] = (byte) Integer.parseInt((String.valueOf(dataString.charAt(i))), 16);
        }
        return rleArr;
    }
    //converts byte array to an encoded rle string with delimeters
    public static String toRleString(byte[] rleData){
        String rleString = "";
        if(rleData != null) {
            String[] arr = new String[rleData.length];

            for (int i = 0; i < arr.length; i++) {
                arr[i] = Integer.toString((int) rleData[i]);
                if (i % 2 != 0) {
                    rleString = rleString + Integer.toHexString(rleData[i]);
                    if (i % 2 != 0 && i != arr.length - 1) {
                        rleString = rleString + ":";
                    }
                } else {
                    rleString = rleString + arr[i];
                }
            }
        }

        return rleString;
    }
    //takes an rle string, strips its delimeters and returns a byte array of data
    public static byte[] stringToRle(String rleString){

        String[] elements = rleString.split(":");
        byte[] rleArrD = new byte[elements.length*2];
        int j = 0;

        for(int i = 0; i < elements.length; i++){
            if(elements[i].length() > 2){
                rleArrD[j++] = (byte)Integer.parseInt((String.valueOf(elements[i].charAt(0)) + String.valueOf(elements[i].charAt(1))));
                rleArrD[j++] = (byte)Integer.parseInt(String.valueOf(elements[i].charAt(2)),16);
            }
            else{
                rleArrD[j++] = (byte)Integer.parseInt(String.valueOf(elements[i].charAt(0)));
                rleArrD[j++] = (byte)Integer.parseInt(String.valueOf(elements[i].charAt(1)),16);
            }
        }
        return rleArrD;
    }
}




















