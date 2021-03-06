import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
	String inputFileName;
	String outputFileName;
	//コンストラクタ
	public DetectFaceDemo(String inputFileName, String outputFileName) {
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
	}

  public void run() {
    CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/haarcascade_frontalface_alt.xml").getPath());
    Mat image = null;
    try {
        image = Imgcodecs.imread(getClass().getResource(inputFileName).getPath());
	} catch (NullPointerException e) {
		System.out.println("ファイル: " + inputFileName + " が見つかりません。やり直してください。");
		System.exit(1);
	}
    // ファイルから顔を認識する
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);
    int size = faceDetections.toArray().length;

    //x座標順に並び替え
    if(size == 0) {
    	System.out.println("顔を検出できませんでした。他のファイルで試してみてください。");
    	System.exit(1);
    }else {
        System.out.println(String.format("%sつの顔を検出しました。", faceDetections.toArray().length));
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

//        for(int i=0 ; i<size ; i++) {
//        	System.out.println(facesArray[i].x);
//        }

        Scanner sc = new Scanner(System.in);
        boolean continueFlag = true;
        Set<Integer> pienRegisterSet = new HashSet<Integer>();

        while(continueFlag) {
            String n;
            String YorN;
            System.out.print("左から何番目の人にぴえんをつけますか？: ");
            //何番目にぴえんをつけるか
            n = sc.next();
            if(isInt(n)) {
            	int nn = Integer.parseInt(n);
            	if(nn > size || nn < 1) {
            		System.out.println("【エラー】数値 " + n + " は指定できません。1から" + size + "の間で指定してください。");
            		continue;
            	}
            	pienRegisterSet.add(nn);
            	boolean YorNFlag  =true;
            	while(YorNFlag) {
            		System.out.print("他の人にもぴえんをつけますか？ [yes/no]: ");
            		YorN = sc.next();
            		if(YorN.equals("yes")) {
            			YorNFlag = false;
            		}else if(YorN.equals("no")) {
            			YorNFlag = false;
            			continueFlag = false;
            		}else {
            			System.out.println("【エラー】yes か no で入力してください。");
            		}
            	}
            }else {
            	System.out.println("【エラー】int型の数値を入力してください。");
            }
        }

        sc.close();

        //setに登録された顔にぴえんをつける
        for(Integer n : pienRegisterSet) {
            Imgproc.rectangle(image, new Point(facesArray[n-1].x, facesArray[n-1].y), new Point(facesArray[n-1].x + facesArray[n-1].width, facesArray[n-1].y +facesArray[n-1].height), new Scalar(0, 255, 0));
        }

        // ファイルの生成
        System.out.println(String.format("ファイル %s を生成しました。", outputFileName));
        Imgcodecs.imwrite(outputFileName, image);

      }
    }


  //入力された値がintかどうかを判定するメソッド
  private boolean isInt(String n) {
	  boolean intFlag = true;
      for(int i=0 ; i<n.length() ; i++) {
      	if(Character.isDigit(n.charAt(i))) {
      		continue;
      	}else {
      		intFlag = false;
      		break;
      	}
      }
      return intFlag;
  }

}
public class HelloOpenCV {
  public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    System.out.print("顔を検出したいファイル名を入力してください: ");
    String inputFileName = sc.next();
    System.out.print("出力するファイル名を入力してください: ");
    String outputFileName = sc.next();
    DetectFaceDemo dfd = new DetectFaceDemo(inputFileName, outputFileName);
    dfd.run();
    sc.close();
  }
}