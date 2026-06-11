package com.pluralsight.sneakerdrops;



import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.NotFoundException;
import com.pluralsight.sneakerdrops.service.SneakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StartUpRunner implements CommandLineRunner {

    private final SneakerService sneakerService;

    @Autowired
    public StartUpRunner(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @Override
    public void run(String... args) throws Exception {
    sneakerService.seedIfEmpty();
        Scanner userScanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            try {
                switch(userScanner.nextInt()){
                    case 1 -> listSneakers();
                    case 2 -> findByReleaseYear(userScanner);
                    case 3 -> findByModel(userScanner);
                    case 4 -> findByPrice(userScanner);
                    case 5 -> advancedSearch(userScanner);
                    case 6 -> viewById(userScanner);
                    case 7 -> addSneaker(userScanner);
                    case 8 -> updatePrice(userScanner);
                    case 9 -> deleteSneaker(userScanner);
                    case 10 -> listByBrand(userScanner);
                    case 0 -> running = false;
                    default -> System.out.println("Unknown option");
                }
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void printMenu(){
        System.out.println("""
                        === Sneaker Library ===
                        1) List all Sneakers
                        2) Find by Year
                        3) Find by Model
                        4) Find by Price
                        5) Advanced Search (price + year)
                        6) View Sneakers by ID
                        7) Add new Sneakers
                        8) Update Price
                        9) Delete Sneakers
                        10) List Sneaker By Brand
                        11) Add Brand
                        0) Quit
                        =======================
                """);
        System.out.println("Choose:");
    }
    private void listSneakers() {
        System.out.println(" You have " + sneakerService.count() + "Sneakers");
        for (Sneaker s : sneakerService.allSneakers()) {
            System.out.println(s.getId() + "|"  + s.getModel() +  "|" + s.getReleaseYear() +  "|" + s.getBrand().getName() + "(" + s.getPrice() + ")" );

        }
    }
    private void findByModel(Scanner scanner){
        scanner.nextLine();
        System.out.println("model:");
        String model = scanner.nextLine();
        for (Sneaker sneaker : sneakerService.byModel(model)) {
            System.out.println(sneaker.getModel());

        }
    }
    private void findByPrice(Scanner scanner){

        System.out.println("Price:");
        double price = scanner.nextDouble();

        for (Sneaker sneaker : sneakerService.byMaxPrice(price)) {
            System.out.println(sneaker.getModel() + "\n Price :( " + sneaker.getPrice() +" ) ");

        }
    }
    private void findByReleaseYear(Scanner scanner){
        System.out.println("Release Year:");
        int year = scanner.nextInt();

        for (Sneaker sneaker : sneakerService.byYear(year)) {
            System.out.println(sneaker.getModel() + "\n Release Year :(" + sneaker.getReleaseYear() +")");

        }
    }

    private void advancedSearch(Scanner scanner){
        System.out.println("Max price: ");
        double maxPrice = scanner.nextDouble();
        System.out.println("Release on or after Year: ");
        int minYear = scanner.nextInt();

        for (Sneaker s : sneakerService.customSearch(maxPrice, minYear)) {
            System.out.println(s.getModel() + "|" + s.getPrice() + "|" + s.getReleaseYear());
        }

    }
    private void listByBrand(Scanner scanner){
        scanner.nextLine();
        System.out.println("Brand name: ");
        String brandName = scanner.nextLine();
        for (Sneaker sneaker : sneakerService.byBrand(brandName)) {
            System.out.println(sneaker.getId() + "|" + sneaker.getModel() + "|"
                    + sneaker.getPrice() + "|"  + sneaker.getReleaseYear());
        }
    }
    private void viewById(Scanner scanner){
        System.out.println("Sneaker ID: ");
        long id = scanner.nextLong();
        Sneaker sneaker = sneakerService.byId(id);
        System.out.println(sneaker.getId() +  "|" + sneaker.getModel() + "|" + sneaker.getPrice() + "|" + sneaker.getBrand());

    }
    private void addSneaker(Scanner scanner){
        scanner.nextLine();
        System.out.println("Enter Model(name): ");
        String name = scanner.nextLine();
        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        System.out.println("Enter Year Released: ");
        int year = scanner.nextInt();

        System.out.println("Choose a Brand: ");
        for (Sneaker sneaker : sneakerService.allBrands()) {
            System.out.println(sneaker.getId() + ") " +  sneaker.getBrand());
        }
        System.out.println("Brand ID: ");
        long brandId = scanner.nextLong();
        sneakerService.addSneaker(name,price,year,brandId);

        System.out.println("Sneakers Added!");
    }
    private void updatePrice(Scanner scanner){
        System.out.println("Sneaker ID: ");
        long id = scanner.nextLong();
        System.out.print("New Price: ");
        int updatedPrice = scanner.nextInt();
        sneakerService.updatePrice(id,updatedPrice);
        System.out.println("Updated!");
    }
    private void deleteSneaker(Scanner scanner) {
        System.out.print("Sneaker id: ");
        long id = scanner.nextLong();
        sneakerService.deleteSneaker(id);
    }





}
