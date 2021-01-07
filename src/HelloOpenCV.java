import java.util.ArrayList;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
class DetectFaceDemo {
  public void run() {
//    System.out.println("\nRunning DetectFaceDemo");
    // Create a face detector from the cascade file in the resources
    // directory.
    CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/haarcascade_frontalface_alt.xml").getPath());
    Mat image = Imgcodecs.imread(getClass().getResource("/lena2.jpeg").getPath());
    // Detect faces in the image.
    // MatOfRect is a special container class for Rect.
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);
    System.out.println(String.format("%sつの顔を取得しました。", faceDetections.toArray().length));

    //x座標順に並び替え
    int size = faceDetections.toArray().length;
    Rect[] facesArray = new Rect[size];
    for(int i=0 ; i<size ; i++) {
    	facesArray[i] = faceDetections.toArray()[i];
    }


    for(int i=0 ; i<size ; i++) {
    	int j = i;
    	while(j>0 && facesArray[j-1].x > facesArray[j].x) {
            Rect tmp = facesArray[j];
            facesArray[j] = facesArray[j-1];
            facesArray[j-1] = tmp;
            j--;
    	}
    }

//    for(int i=0 ; i<size ; i++) {
//    	System.out.println(facesArray[i].x);
//    }

    //どれにぴえんをつけるか入力を待つ
    Scanner sc = new Scanner(System.in);
    boolean continueFlag = true;
    ArrayList<Integer> pienRegisterList = new ArrayList<Integer>();

    while(continueFlag) {
        System.out.print("左から何番目の人にぴえんをつけますか？: ");
        String n;
        String yOrN;

        n = sc.next();
        //入力された値がintかどうかを判定
        boolean intFlag = true;
        for(int i=0 ; i<n.length() ; i++) {
        	if(Character.isDigit(n.charAt(i))) {
        		continue;
        	}else {
        		intFlag = false;
        		break;
        	}
        }
        if(intFlag) {
        	int nn = Integer.parseInt(n);
        	if(nn > size) {
        		System.out.println("【エラー】数値 " + n + " は指定できません。0から" + size + "の間で指定してください。");
        		continue;
        	}
        	pienRegisterList.add(nn);
        	boolean yOrNFlag  =true;
        	while(yOrNFlag) {
        		System.out.print("他の人にもぴえんをつけますか？ [yes/no]: ");
        		yOrN = sc.next();
        		if(yOrN.equals("yes")) {
        			yOrNFlag = false;
        		}else if(yOrN.equals("no")) {
        			yOrNFlag = false;
        			continueFlag = false;
        		}else {
        			System.out.println("【エラー】yes か no で入力してください。");
        		}
        	}

        }else {
        	System.out.println("【エラー】int型の数値を入力してください");
        }


    }


    sc.close();

    for (Rect rect : faceDetections.toArray()) {
        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
    }





    // Save the visualized detection.
    String filename = "faceDetection.png";
    System.out.println(String.format("ファイル %s を生成しました。", filename));
    Imgcodecs.imwrite(filename, image);
  }
}
public class HelloOpenCV {
  public static void main(String[] args) {
    // System.out.println("Hello, OpenCV");
    // Load the native library.
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    new DetectFaceDemo().run();
  }
}