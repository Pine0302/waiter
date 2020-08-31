package geektime.springbucks.waiter.controller;

import geektime.springbucks.waiter.model.Coffee;
import geektime.springbucks.waiter.repository.CoffeeRepository;
import geektime.springbucks.waiter.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/coffee")
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping(path = "/",params = "!name")
    @ResponseBody
    public List<Coffee> getAll(){
        return coffeeService.getAllCoffee();
    }

    @RequestMapping(path = "/{id}",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Coffee getById(@PathVariable Long id){
        Coffee coffee = coffeeService.getCoffee(id);
        return coffee;
    }

    @RequestMapping(path = "/",params = "name")
    @ResponseBody
    public Coffee getByName(@RequestParam String name){
        Coffee coffee = coffeeService.getCoffee(name);
        return coffee;
    }
}
