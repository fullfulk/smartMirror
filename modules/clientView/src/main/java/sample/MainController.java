package sample;

import engine.OpenCvEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Paths;
import utils.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainController {

    @FXML
    private ImageView originalFrame;

    @FXML
    private CheckBox haarClassifier;

    @FXML
    private CheckBox lbpClassifier;

    @FXML
    private Button cameraButton;

    private OpenCvEngine engine;

    protected void init() {
        this.engine = new OpenCvEngine();
        this.originalFrame.setFitWidth(600);
        this.originalFrame.setPreserveRatio(true);
    }

    @FXML
    void startCamera(ActionEvent event) {
        if (!this.engine.isCameraActive()) {
            this.haarClassifier.setDisable(true);
            this.lbpClassifier.setDisable(true);
            this.engine.getCapture().open(0);
            if (this.engine.getCapture().isOpened()) {
                this.engine.setCameraActive(true);
                Runnable frameGrabber = new FrameGrabber() {
                    @Override
                    public void run() {
                        updateImageView(originalFrame, Utils.mat2Image(engine.grabFrame()));
                    }
                };
                this.engine.setTimer(Executors.newSingleThreadScheduledExecutor());
                this.engine.getTimer().scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
                this.cameraButton.setText("Stop Camera");
            } else {
                System.err.println("Failed to open the camera connection...");
            }
        } else {
            this.engine.setCameraActive(false);
            this.cameraButton.setText("Start Camera");
            this.haarClassifier.setDisable(false);
            this.lbpClassifier.setDisable(false);
            this.engine.stopAcquisition();
        }
    }

    @FXML
    public void haarSelected(ActionEvent event) {
        if (this.lbpClassifier.isSelected()) {
            this.lbpClassifier.setSelected(false);
        }
        this.checkboxSelection(Paths.haarSources);
    }

    @FXML
    public void lbpSelected(ActionEvent event) {
        if (this.haarClassifier.isSelected()) {
            this.haarClassifier.setSelected(false);
        }
        this.checkboxSelection(Paths.lbpSources);
    }

    private void checkboxSelection(String... classifierPath) {

        for (String xmlClassifier : classifierPath) {
            this.engine.getFaceCascade().load(xmlClassifier);
        }
        this.cameraButton.setDisable(false);

    }

    protected void setClosed() {
        this.engine.stopAcquisition();
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }
}
