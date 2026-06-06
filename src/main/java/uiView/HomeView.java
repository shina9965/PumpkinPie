package uiView;

import listener.ActionListener;

public class HomeView {

  private ActionListener actionListener;

  public HomeView(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  public void createScene() {
    System.out.println("HomeView: createScene");
  }

  public void CreateButton() {
    System.out.println("HomeView: CreateButton");
  }

  public void createTitle() {
    System.out.println("HomeView: createTitle");
  }

}
