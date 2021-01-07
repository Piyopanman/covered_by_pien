import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
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
    Rect[] test = new Rect[size];
    for(int i=0 ; i<size ; i++) {
    	test[i] = faceDetections.toArray()[i];
    }

    for(int i=0 ; i<size ; i++) {
    	int j = i;
    	while(j>0 && test[j-1].x > test[j].x) {
            Rect tmp = test[j];
            test[j] = test[j-1];
            test[j-1] = tmp;
            j--;
    	}
    }

//    for(int i=0 ; i<size ; i++) {
//    	System.out.println(test[i].x);
//    }

    Scanner sc = new Scanner(System.in);
    boolean flag = true;
    ArrayList<Integer> pienRegisterList = new ArrayList<Integer>();

    while(flag) {
        System.out.print("左から何番目の人にぴえんをつけますか？: ");
        int n;
        try {
            n = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("数値を入力してください");
			continue;
		}
//        Integer N = new Integer(n);
        pienRegisterList.add(n);

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