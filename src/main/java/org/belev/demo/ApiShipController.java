package org.belev.demo;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://127.0.0.1:8080"})
@RequestMapping("/api/ship")
public class ApiShipController {
    private ShipRepository shipRepository;
    private TouristRepository touristRepository;

    public ApiShipController(ShipRepository shipRepository, TouristRepository touristRepository) {
        this.shipRepository = shipRepository;
        this.touristRepository = touristRepository;
    }

    // Find ship
    @GetMapping(value = "/search")
    public JSONArray search(@RequestParam String direction, String departure) {

        List<Ship> ships = new ArrayList<Ship>();
        Iterable<Ship> shipsIterable;
        JSONArray jsonArray = new JSONArray();
        LocalDateTime departureDateTime = LocalDateTime.now();

        if (departure != null && departure.length() != 0) {
            System.out.println("Firt date formatter --->");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            departureDateTime = LocalDateTime.parse(departure, dateTimeFormatter);
        }

        if (direction.length() != 0 && departure.length() != 0) {
            System.out.println("Second --->");
            ships = shipRepository.findShipsByDirectionAndDeparture(direction, departureDateTime);
        } else if (direction.length() == 0 && departure.length() == 0) {
            System.out.println("Third --->");
            shipsIterable = shipRepository.findAll();
            for (Ship ship : shipsIterable) {
                jsonArray.appendElement(ship);
            }
        } else if (direction.length() == 0) {
            System.out.println("The forth --->");
            ships = shipRepository.findShipsByDeparture(departureDateTime);
        } else if (departure.length() == 0) {
            System.out.println("The fifth --->");
            ships = shipRepository.findShipsByDirection(direction);
        }

        for (Ship ship : ships) {
            jsonArray.appendElement(ship);
            System.out.println(ship.getDirection());
        }

        return jsonArray;
    }

    // Single ship
    @GetMapping(value = "/{id}")
    public Optional<Ship> single(@PathVariable Long id) {
        Optional<Ship> ship = shipRepository.findById(id);

        return ship;
    }

    // Add new ship
    @PostMapping(value = "/add")
    public JSONObject add(@RequestBody Ship ship) {
        List<Tourist> touristNotFull = ship.getTouristsAdded();

        // Init available seats
        ship.setSeatsAvailable(ship.getSeats());

        // New available seats
        int newSeats = ship.getSeatsAvailable() - touristNotFull.size();
        ship.setSeatsAvailable(newSeats);

        // Set tourists
        for (int i = 0; i < touristNotFull.size(); i++) {
            Long touristId = touristNotFull.get(i).getId();
            Tourist tourist = touristRepository.findById(touristId).get();
            ship.addTourist(tourist);
        }

        Ship shipSaved = shipRepository.save(ship);
        Long shipSavedId = shipSaved.getId();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("redirect", "/ship/edit/" + shipSavedId);

        return jsonObject;
    }

    // Update ship
    @PutMapping(value = "/update/{id}")
    public JSONObject update(@RequestBody Ship updatedShip, @PathVariable Long id) {

        Ship currentShip = shipRepository.findById(id).get();
        List<Tourist> currentTourists = currentShip.getTourists();
        List<Tourist> addedTourists = updatedShip.getTouristsAdded();

        // Filter
        List<Tourist> touristsToRemove = new ArrayList<Tourist>();
        for (int i = 0; i < currentTourists.size(); i++) {
            boolean isExist = false;
            for (int k = 0; k < addedTourists.size(); k++) {
                if (currentTourists.get(i).getId() == addedTourists.get(k).getId()) {
                    isExist = true;
                }
            }
            if (! isExist) {
                touristsToRemove.add(currentTourists.get(i));
            }
        }

        for (int i = 0; i < touristsToRemove.size(); i++) {
            updatedShip.removeTourist(touristsToRemove.get(i));
        }

        for (int i = 0; i < addedTourists.size(); i++) {
            updatedShip.addTourist(addedTourists.get(i));
        }

        shipRepository.save(updatedShip);

        /*
        List<Tourist> updatedTourists = updatedShip.getTourists();
        for (Tourist tourist : updatedTourists) {
            System.out.println(tourist.getFirstName());
        }
        */

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "1");
        jsonObject.put("message", "Success! Ship updated!");

        return jsonObject;
    }

    // Get all
    @GetMapping(value = "/all")
    public Iterable<Ship> all() {
        return shipRepository.findAll();
    }

    // Delete
    @DeleteMapping(value = "/{id}")
    public JSONObject delete(@PathVariable Long id) {
        Ship ship = shipRepository.findById(id).get();
        List<Tourist> tourists = ship.getTourists();

        for (Tourist tourist : tourists) {
            tourist.setShip(null);
        }

        shipRepository.deleteById(id);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "1");
        jsonObject.put("message", "Success! Ship deleted!");

        return jsonObject;
    }
}
