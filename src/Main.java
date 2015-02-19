import com.mortennobel.imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    final static int DIV_WIDTH = 280;
    final static int DIV_HEIGHT = 180;

    public static void main(String[] args) throws IOException {
        String[] files = new String[]{
                "scr01.jpg", "scr02.jpg", "scr03.jpg", "scr04.jpg"
        };
        final String directory = "/home/contragent/Documents/JavaProjects/fervency/images";

        createSmallImages(directory, directory + "/small", files);

        System.out.println("New images were successfully created!");
    }

    private static void createSmallImages(String dirSource, String dirDest, String[] files) throws IOException {
        createDirectory(dirDest);

        for (String file : files) {
            BufferedImage image = ImageIO.read(new File(dirSource + "/" + file));
            image = resizeImage(image);

            ImageIO.write(image, "jpg", new File(dirDest + "/" + file));
        }
    }

    private static void createDirectory(String dirDest) {
        File newDir = new File(dirDest);

        if (!newDir.exists()) {
            newDir.mkdir();
        }
    }

    private static BufferedImage resizeImage(BufferedImage image) {
        float scaleWidth = (float) DIV_WIDTH / image.getWidth();
        float scaleHeight = (float) DIV_HEIGHT / image.getHeight();
        float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;

        int newWidth = (int) (scale * image.getWidth());
        int newHeight = (int) (scale * image.getHeight());

        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();

        return resizeWithStandardLib(image, newWidth, newHeight, type);
    }

    private static BufferedImage resizeWithStandardLib(BufferedImage originalImage, int newWidth, int newHeight, int type) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }

    private static BufferedImage resize(BufferedImage originalImage, int newWidth, int newHeight, int type) {
        ResampleOp resampleOp = new ResampleOp(newWidth, newHeight);
        BufferedImage resizedImage = resampleOp.filter(originalImage, null);

        return resizedImage;
    }
}
