package geektime.springbucks.waiter.controller;


import geektime.springbucks.waiter.controller.request.NewCoffeeRequest;
import geektime.springbucks.waiter.controller.request.NewOrderRequest;
import geektime.springbucks.waiter.model.Coffee;
import geektime.springbucks.waiter.model.CoffeeOrder;
import geektime.springbucks.waiter.service.CoffeeOrderService;
import geektime.springbucks.waiter.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


@Controller
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {

    @Autowired
    private CoffeeOrderService coffeeOrderService;

    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/{id}")
    public CoffeeOrder getOrder(@PathVariable Long id) {
        return coffeeOrderService.getOrder(id);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest newOrder) {
        log.info("request new order {}", newOrder);
        Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems()).toArray(new Coffee[]{});
        return coffeeOrderService.createOrder(newOrder.getCustomer(), coffeeList);
    }

    @ModelAttribute
    public List<Coffee> coffeeList() {
        List<Coffee> coffees = coffeeService.getAllCoffee();
        return coffeeService.getAllCoffee();
    }

    @GetMapping(path = "/")
    public ModelAndView showCreateForm(){
        return new ModelAndView("create-order-form");
    }


    @PostMapping(path = "/",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String CreateOrder(@Valid NewOrderRequest newOrderRequest,
                              BindingResult bindingResult, ModelMap modelMap){
        if(bindingResult.hasErrors()){
            log.warn("Binding Result: {}", bindingResult);
            modelMap.addAttribute("message",bindingResult.toString());
            return "create-order-form";
        }
        log.info("Receive new Order {}", newOrderRequest);
        Coffee[] coffees = coffeeService.getCoffeeByName(newOrderRequest.getItems()).toArray(new Coffee[] {});
        CoffeeOrder order = coffeeOrderService.createOrder(newOrderRequest.getCustomer(),coffees);
        return "redirect:/order/" + order.getId();
    }



}
