package com.pluralsight.sneakerdrops;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StartUpRunner implements CommandLineRunner {
    private final BrandRepository brandRepository;
    private  final SneakerRepository sneakerRepository;
    @Autowired
    public StartUpRunner(BrandRepository brandRepository, SneakerRepository sneakerRepository) {
        this.brandRepository = brandRepository;
        this.sneakerRepository = sneakerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
    seedData();
        Scanner userScanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
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
                case 11 -> addBrand(userScanner);
                case 0 -> running = false;
                default -> System.out.println("Unknown option");
            }

        }

    }

    private void findByReleaseYear(Scanner scanner){
        System.out.println("Release Year:");
        int year = scanner.nextInt();

        for (Sneaker sneaker : sneakerRepository.findByReleaseYear(year)) {
            System.out.println(sneaker.getModel() + "\n Release Year :( " + sneaker.getReleaseYear() +" ) ");

        }
    }
    private void findByModel(Scanner scanner){
        scanner.nextLine();
        System.out.println("model:");
        String model = scanner.nextLine();

        for (Sneaker sneaker : sneakerRepository.findByModelContaining(model)) {
            System.out.println("\n model : " + sneaker.getModel());

        }
    }
    private void findByPrice(Scanner scanner){

        System.out.println("Price:");
        double price = scanner.nextDouble();

        for (Sneaker sneaker : sneakerRepository.findByPriceLessThan(price)) {
            System.out.println(sneaker.getModel() + "\n Price :( " + sneaker.getPrice() +" ) ");

        }
    }
    private void listSneakers() {
        System.out.println(" You have " + sneakerRepository.count() + "Sneakers");
        for (Sneaker s : sneakerRepository.findAll()) {
            System.out.println(s.getBrand() + " - "  + s.getModel() +  " _ " + s.getReleaseYear() +  " - " + s.getPrice());

        }
    }
    private void listBrand(){
        System.out.println(" You have " + brandRepository.count() + "Brands");
        for (Brand brand : brandRepository.findAll()) {
            System.out.println(brand.getId() + ") " +  brand.getName());
        }
    }
    private void advancedSearch(Scanner scanner){
        scanner.nextLine();
        System.out.println("Max price: ");
        double maxPrice = scanner.nextDouble();
        System.out.println("Release on or after Year: ");
        int minYear = scanner.nextInt();
        for (Sneaker s : sneakerRepository.search(maxPrice, minYear)) {
            System.out.println(s.getModel() + "|" + s.getPrice() + "|" + s.getReleaseYear());
        }

    }
    private void viewById(Scanner scanner){
        System.out.println("Sneaker ID: ");
        long id = scanner.nextLong();
        Sneaker sneaker = sneakerRepository.findById(id).orElse(null);
        if (sneaker == null){
            System.out.println("No Sneakers with that ID");
        }else {
            System.out.println(sneaker.getId() +  "|" + sneaker.getModel()
                    + "|" + sneaker.getPrice() + "|" + sneaker.getBrand());
        }
    }
    private void addBrand(Scanner scanner){
        scanner.nextLine();
        System.out.println("Enter Brand Name: ");
        String brandName = scanner.nextLine();
        brandRepository.save(new Brand(brandName));
        System.out.println(brandName + " Was Added!");

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
        listBrand();
        long brandId = scanner.nextLong();

        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new RuntimeException("No Brand with id " + brandId));

        sneakerRepository.save(new Sneaker(name,price,year,brand));
        System.out.println("Sneakers Added!");
    }
    private void deleteSneaker(Scanner scanner) {
        System.out.print("Sneaker id: ");
        long id = scanner.nextLong();
        if (sneakerRepository.existsById(id)) {
            sneakerRepository.deleteById(id);
            System.out.println("Deleted.");
        } else {
            System.out.println("No Sneaker with that id.");
        }
    }
    private void updatePrice(Scanner scanner){
        System.out.println("Sneaker ID: ");
        long id = scanner.nextLong();
        Sneaker sneaker = sneakerRepository.findById(id).orElseThrow(() -> new RuntimeException("No Sneaker with id " + id));
        System.out.print("New Price: ");
        sneaker.setPrice(scanner.nextDouble());
        sneakerRepository.save(sneaker);
        System.out.println("Updated!");
    }
    private void listByBrand(Scanner scanner){
        scanner.nextLine();
        System.out.println("Brand name: ");
        String brandName = scanner.nextLine();
        for (Sneaker sneaker : sneakerRepository.findByBrand_Name(brandName)) {
            System.out.println(sneaker.getId() + "|" + sneaker.getModel() + "|"
                    + sneaker.getPrice() + "|"  + sneaker.getReleaseYear());
        }
    }
    private void seedData(){

        if (sneakerRepository.count() > 0) {
            return;
        }
        Brand nike = brandRepository.save(new Brand("Nike"));
        Brand jordan = brandRepository.save(new Brand("Jordan"));
        Brand adidas = brandRepository.save(new Brand("Adidas"));
        Brand newBalance = brandRepository.save(new Brand("New Balance"));
        Brand converse = brandRepository.save(new Brand("Converse"));


            sneakerRepository.save(new Sneaker("Nike Air Force 1",89.99,1982 , nike));
            sneakerRepository.save(new Sneaker("Air Jordan 1", 64.99, 1985, jordan));
            sneakerRepository.save(new Sneaker( "Samba", 100.00, 1950, adidas));
            sneakerRepository.save(new Sneaker("Men's 574", 74.99, 1988, newBalance ));
            sneakerRepository.save(new Sneaker(" Chuck Taylor All-Star", 65.00, 1917,converse));

    }
}
