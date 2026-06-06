package uiController;

public class ImageWindowController extends WindowController{


    public void initState() {
    System.out.println("ImageWindowController: initState");
  }

  public void endState() {
    System.out.println("ImageWindowController: endState");
  }

  public void actionPerformed(javafx.event.ActionEvent event) {
    System.out.println("ImageWindowController: actionPerformed");
  }
}
