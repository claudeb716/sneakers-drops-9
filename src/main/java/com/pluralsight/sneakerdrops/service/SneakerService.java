package com.pluralsight.sneakerdrops.service;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


// (A Bean that holds business logic)
@Service
public class SneakerService {

    private  final SneakerRepository sneakerRepository;
    private  final BrandRepository brandRepository;
    // Constructor Injection
@Autowired
    public SneakerService(SneakerRepository sneakerRepository, BrandRepository brandRepository) {
        this.sneakerRepository = sneakerRepository;
        this.brandRepository = brandRepository;
    }

    public  long count(){
    return sneakerRepository.count();
    }
    public List<Sneaker> allSneakers(){
    return sneakerRepository.findAll();
    }
    public List<Brand> allBrands(){
    return brandRepository.findAll();
    }
    public List<Sneaker> byModel(String model){
    return sneakerRepository.findByModelContaining(model);
    }
    public List<Sneaker> byMaxPrice(double price){
    return sneakerRepository.findByPriceLessThan(price);
    }
    public List<Sneaker> byYear(int year){
    return sneakerRepository.findByReleaseYear(year);
    }
    public List<Sneaker> customSearch(double maxPrice, int minYear){
    return sneakerRepository.search(maxPrice, minYear);
    }
    public List<Sneaker> byBrand(String brandname){
    return sneakerRepository.findByBrand_Name(brandname);
    }

    public Sneaker byId(long id){
    return sneakerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No Sneakers with id " + id));
    }

    public Sneaker addSneaker(String model, double price, int year, long brandId) {
    Brand brand = brandRepository.findById(brandId)
            .orElseThrow(() -> new NotFoundException("No Brand with id " + brandId));
    return sneakerRepository.save(new Sneaker(model,price,year,brand));
}
    public Sneaker updatePrice(long id, double price){
    Sneaker sneaker = byId(id);
    sneaker.setPrice(price);
    return sneakerRepository.save(sneaker);
    }
    public void deleteSneaker(long id){
    if (!sneakerRepository.existsById(id)){
        throw new NotFoundException("No Sneaker with ID " + id);
    }
    sneakerRepository.deleteById(id);
    }
    public void seedIfEmpty(){
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
