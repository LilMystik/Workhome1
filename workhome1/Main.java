package homework;
import java.util.*;

class Workspace{
  private int id;
  private String details;
  private int price;
  private boolean availability;
  public int getId() {
    return id;
  }
  public String getDetails() {
    return details;
  }
  public int getPrice() {
    return price;
  }
  public boolean isAvailability() {
    return availability;
  }
  public void setId(int id) {
    this.id = id;

  }
  public void setDetails(String details) {
    this.details = details;
  }
  public void setPrice(int price) {
    this.price = price;
  }
  public void setAvailability(boolean availability) {
    this.availability = availability;
  }
  public Workspace(int id, String details, int price){
    this.id=id;
    this.details=details;
    this.price=price;
    this.availability=true;
  }
}

class Reservation{
  int reservationID;
  int workspaceId;
  String name;
  String date;
  String startTime;
  String endTime;
  public int getReservationID() {
    return reservationID;
  }

  public void setReservationID(int reservationID) {
    this.reservationID = reservationID;
  }

  public int getWorkspaceId() {
    return workspaceId;
  }

  public void setWorkspaceId(int workspaceId) {
    this.workspaceId = workspaceId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  Reservation(int reservationID, int workspaceId, String name, String date, String startTime, String endTime){
    this.reservationID=reservationID;
    this.workspaceId=workspaceId;
    this.name=name;
    this.date=date;
    this.startTime=startTime;
    this.endTime=endTime;
  }
}


public class Main {
  private static final List<Workspace> workspaces = new ArrayList<>();
  private static final List<Reservation> reservations = new ArrayList<>();
  private static int reservationId = 1;
  private static int workspaceId = 4;
  private static final String WRONG = "Wrong choice";
  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    testWorkspaces();
    while (true) {
      System.out.println("Select Option:\n1 - Admin\n2 - Customer\n3 - Exit");
      int option = scanner.nextInt();
      scanner.nextLine();
      switch (option) {
        case 1: {
          adminMenu();
          break;
        }
        case 2: {
          customerMenu();
          break;
        }
        case 3: {
          return;
        }
        default: {
          System.out.println(WRONG);
          break;
        }
      }
    }
  }

  private static void testWorkspaces() {
    workspaces.add(new Workspace(1, "Open Space", 10));
    workspaces.add(new Workspace(2, "Private Room", 25));
    workspaces.add(new Workspace(3, "Conference Roo2m", 50));
  }

  private static void adminMenu() {
    while (true) {
      System.out.println("Workspace management:\n1 - View workspaces\n2 - Add workspace\n3 - Remove workspace\n4 - View reservations\n5 - Return");
      int option = scanner.nextInt();
      scanner.nextLine();
      switch (option) {
        case 1: {
          showWorkspaces();
          break;
        }
        case 2:{
          addWorkspace();
          break;
        }
        case 3:{
          removeWorkspace();
          break;
        }
        case 4:{
          showReservations();
          break;
        }
        case 5:{
          return;
        }
        default: {
          System.out.println(WRONG);
          break;
        }
      }
    }
  }

  private static void customerMenu() {
    while (true) {
      System.out.println("Customer menu:\n1 - View workspaces\n2 - Make reservation\n3 - See reservations\n4 - Cancel reservation\n5 - Return");
      int option = scanner.nextInt();
      scanner.nextLine();
      switch (option) {
        case 1: {
          showWorkspaces();
          break;
        }
        case 2:{
          addReservation();
          break;
        }
        case 3:{
          showReservations();
          break;
        }
        case 4:{
          cancelReservation();
          break;
        }
        case 5:{
          return;
        }
        default: {
          System.out.println(WRONG);
          break;
        }
      }
    }
  }

  private static void showWorkspaces(){
    System.out.println("Workspaces:");
    for(Workspace ws:workspaces){
      System.out.println("ID: " + ws.getId() + " | Details: " + ws.getDetails() + " | Price: " + ws.getPrice() + "| Available: " + ws.isAvailability() + " |" );
    }
  }

  private static  void  addWorkspace() {
    System.out.println("Enter workspace description:");
    String description = scanner.nextLine();
    System.out.println("Enter price:");
    int price = scanner.nextInt();
    scanner.nextLine();
    workspaces.add(new Workspace(workspaceId++,description,price));
  }

  private static void removeWorkspace(){
    showWorkspaces();
    System.out.println("Enter workspace ID to remove:");
    int id = scanner.nextInt();
    scanner.nextLine();
    workspaces.removeIf(ws -> ws.getId()==id);
  }

  private static void addReservation() {
    System.out.println("Enter the name");
    String name = scanner.nextLine();
    showWorkspaces();
    System.out.println("Choose workspace ID");
    int wsId = scanner.nextInt();
    scanner.nextLine();
    Workspace selectedWorkspace = workspaces.stream()
             .filter(ws -> ws.getId()==wsId && ws.isAvailability())
            .findFirst()
            .orElse(null);
    if (selectedWorkspace ==null){
      System.out.println("Unavailable workspace");
      return;
    }

    System.out.println("Enter date");
    String date = scanner.nextLine();
    System.out.println("Enter the start time");
    String startTime = scanner.nextLine();
    System.out.println("Enter the end time");
    String endTime = scanner.nextLine();
    selectedWorkspace.setAvailability(false);
    reservations.add(new Reservation(reservationId++,wsId,name,date,startTime,endTime));


  }

  private static void showReservations(){
    if(reservations.isEmpty()){
      System.out.println("No reservations");
      return;
    }
    for (Reservation res:reservations){
      System.out.println("ID: " + res.reservationID + " | Workspace ID: " + res.workspaceId + " | Name: " + res.name + " | date: " + res.date + " | start time: " + res.startTime + " | end time: " + res.endTime + " |");
    }
  }

  private static void cancelReservation() {
    showReservations();
    System.out.println("Enter reservation ID to cancel");
    int resId = scanner.nextInt();
    scanner.nextLine();
    Reservation cancelledReservation = reservations.stream()
            .filter(res->res.getReservationID()==resId)
            .findFirst()
            .orElse(null);
    if (cancelledReservation == null) {
      System.out.println("Reservation not found");
      return;
    }
    reservations.remove(cancelledReservation);
    workspaces.stream()
            .filter(ws ->ws.getId()==cancelledReservation.getWorkspaceId())
            .findFirst()
            .ifPresent(ws->ws.setAvailability(true));
    System.out.println("Workspace cancelled");

  }
}
