package homework;

import homework.exception.WorkspaceUnavailableException;
import homework.models.Reservation;
import homework.models.Workspace;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Storage {

  private static final String WORKSPACES_FILE = "workspaces";
  private static final String RESERVATIONS_FILE = "reservations";
  private final List<Workspace> workspaces = new ArrayList<>();
  private final List<Reservation> reservations = new ArrayList<>();
  private int reservationId = 1;
  private int workspaceId = 1;

  public List<Workspace> getWorkspaces() {
    return workspaces;
  }

  public List<Reservation> getReservations() {
    return reservations;
  }

  public void addWorkspace(String details, int price) {
    workspaces.add(new Workspace(workspaceId++, details, price));
  }

  public void removeWorkspace(int id) {
    workspaces.removeIf(ws -> ws.getId() == id);
  }

  public Optional<Workspace> findAvailableWorkspace(int id) {
    return workspaces.stream()
            .filter(ws -> ws.getId() == id && ws.isAvailable())
            .findFirst();
  }

  public void addReservation(int workspaceId, String name, String date, String startTime, String endTime) throws WorkspaceUnavailableException {
    Optional<Workspace> workspaceOpt = findAvailableWorkspace(workspaceId);
    if (workspaceOpt.isEmpty() || !workspaceOpt.get().isAvailable()) {
      throw new WorkspaceUnavailableException("Workspace is unavailable.");
    }
    Workspace workspace = workspaceOpt.get();
    workspace.setAvailability(false);
    reservations.add(new Reservation(reservationId++, workspaceId, name, date, startTime, endTime));
  }

  public void cancelReservation(int reservationId) {
    Optional<Reservation> reservationOpt = reservations.stream()
            .filter(res -> res.getReservationID() == reservationId)
            .findFirst();

    if (reservationOpt.isPresent()) {
      Reservation reservation = reservationOpt.get();
      reservations.remove(reservation);
      workspaces.stream()
              .filter(ws -> ws.getId() == reservation.getWorkspaceId())
              .findFirst()
              .ifPresent(ws -> ws.setAvailability(true));
      System.out.println("Reservation cancelled.");
    } else {
      System.out.println("Reservation not found.");
    }
  }

  public void saveState() {
    saveToFile(WORKSPACES_FILE, workspaces);
    saveToFile(RESERVATIONS_FILE, reservations);
  }

  private <T> void saveToFile(String filename, List<T> list) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
      oos.writeObject(list);
    } catch (IOException e) {
      System.out.println("Error saving data: " + e.getMessage());
    }
  }
public void loadState() {
    List<Workspace> loadedWorkspaces = loadFromFile(WORKSPACES_FILE);
    if (loadedWorkspaces != null) workspaces.addAll(loadedWorkspaces);
    workspaceId = loadedWorkspaces.size()+1;

    List<Reservation> loadedReservations = loadFromFile(RESERVATIONS_FILE);
    if (loadedReservations != null) reservations.addAll(loadedReservations);
    reservationId = loadedReservations.size()+1;
  }

  private <T> List<T> loadFromFile(String filename) {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
      return (List<T>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      return null;
    }
  }
}
