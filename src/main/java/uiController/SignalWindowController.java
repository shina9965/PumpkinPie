package uiController;

import javafx.event.ActionEvent;

public class SignalWindowController extends WindowController{


    public void initState() {
    System.out.println("SignalWindowController: initState");
  }

  public void endState() {
    System.out.println("SignalWindowController: endState");
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    System.out.println("SignalWindowController: actionPerformed");
  }
}
