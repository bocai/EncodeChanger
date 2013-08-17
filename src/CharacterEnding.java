import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import java.io.File;
import java.nio.charset.Charset;
// cpdetector 检测编码
public class CharacterEnding {

    public static String getFileCharacterEnding (String filePath) {

        File file = new File (filePath);

        return getFileCharacterEnding (file);
    }

    /*
        to get file character ending.

       * cpDetector to detect file's encoding.

       * @param file

       * @return
    */

    @SuppressWarnings("deprecation")
	public static String getFileCharacterEnding (File file) {

        String fileCharacterEnding = null;

        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance ();

        detector.add (JChardetFacade.getInstance ());

        Charset charset = null;

       // File f = new File(filePath);

        try {

            charset = detector.detectCodepage (file.toURL ());

        } catch (Exception e) {

            e.printStackTrace ();

        }

        if (charset != null) {

            fileCharacterEnding = charset.name ();

        }

        return fileCharacterEnding;
    }

//    public static void main (String[]args) {
//
//        String filePath = "G:/J_TEST/src/CharacterEnding.java";
//
//        String type = CharacterEnding.getFileCharacterEnding (filePath);
//
//        System.out.println (type);
//
//    }

}